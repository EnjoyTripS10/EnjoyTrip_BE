package com.study.sociallogin.controller;

import com.study.sociallogin.dto.BoardDto;
import com.study.sociallogin.dto.BoardResponse;
import com.study.sociallogin.model.Boards;
import com.study.sociallogin.model.Locations;
import com.study.sociallogin.request.BoardLikeDto;
import com.study.sociallogin.service.BoardLikeService;
import com.study.sociallogin.service.BoardService;
import com.study.sociallogin.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import java.nio.file.DirectoryStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

;

@CrossOrigin
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    @Value("${imageFile.uploadPath}")
    private String filePath;

    private final BoardService boardService;
    private final LocationService locationService;
    private final BoardLikeService boardLikeService;


    @PostMapping
    public ResponseEntity<HttpStatus> registerBoard(@RequestParam("title") String title,
                                                    @RequestParam("content") String content,
                                                    @RequestParam("inLocation") String inLocation,
                                                    @RequestParam("files") List<MultipartFile> files) throws ParseException, IOException {
        System.out.println("create");

        //login check 해야함....
        String userEmail = "1";

        //location 정보 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(inLocation);

        Locations location = Locations.builder()
                .locationAddr(jsonObject.get("locationAddr").toString())
                .locationLat(jsonObject.get("locationLat").toString())
                .locationType(jsonObject.get("locationType").toString())
                .locationLon(jsonObject.get("locationLon").toString())
                .locationName(jsonObject.get("locationName").toString())
                .build();

        location = locationService.createLocation(location);

        Boards board = Boards.builder()
                .boardTitle(title)
                .boardContent(content)
                .locationId(location.getLocationId())
                .userEmail(userEmail)
                .build();

        board = boardService.createBoard(board);

        // 파일 처리
        System.out.println("파일 존재함?" + files.size());
        String nanoname = String.valueOf(System.nanoTime());
        String directoryPath = filePath + File.separator + userEmail + File.separator + board.getBoardId();
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }

        int index = 1;
        // 다중 파일 처리
        for(MultipartFile multipartFile : files) {

            // 파일의 확장자 추출
            String originalFileExtension;
            String contentType = multipartFile.getContentType();

            // 확장자명이 존재하지 않을 경우 처리 x
            if (ObjectUtils.isEmpty(contentType)) {
                break;
            } else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
                if (contentType.contains("image/jpeg"))
                    originalFileExtension = ".jpg";
                else if (contentType.contains("image/png"))
                    originalFileExtension = ".png";
                else  // 다른 확장자일 경우 처리 x
                    break;
            }

            // 파일명 중복 피하고자 나노초까지 얻어와 지정
            String new_file_name = nanoname+ (index++)  + originalFileExtension;

            File file = new File(directory, new_file_name);
            // 업로드 한 파일 데이터를 지정한 파일에 저장 로컬저장

            multipartFile.transferTo(file);
            System.out.println(directory);
            //FileCopyUtils.copy(file.getBytes(), new File(new_file_name));

            // 파일 권한 설정(쓰기, 읽기)
            file.setWritable(true);
            file.setReadable(true);
        }
        System.out.println("파일 폴더, DB 저장 완료");
        //

        return ResponseEntity.ok(HttpStatus.CREATED);

    }
    @GetMapping
    public ResponseEntity<List<BoardResponse>> getBoardList() {
        System.out.println("get board list");

        List<Boards> boards = boardService.getBoardList();
        List<BoardResponse> responses = new ArrayList<>();
        for (Boards board: boards) {
            BoardResponse boardRespose = BoardResponse.builder()
                    .boardId(board.getBoardId())
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .createdAt(board.getCreatedAt().toString())
                    .boardHit(board.getBoardHit())
                    .userEmail(board.getUserEmail())
                    .locationId(board.getLocationId())
                    .build();
            try {
                String folderPath = filePath +"/" + board.getUserEmail() + "/" + board.getBoardId();

                Path dirPath = Paths.get(folderPath);
                DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath);
                for (Path filePath : stream) {
                    if (Files.isRegularFile(filePath)) {
                            // 첫 번째 이미지 파일을 찾았으므로 읽어서 반환
                        byte[] imageBytes = Files.readAllBytes(filePath);
                        boardRespose.setImage(imageBytes);
                        responses.add(boardRespose);
                        break;
                    }
                }

//                Path path = Paths.get("/Users/kimbokyeong/Desktop/develop/ssafy/image/1/1/1.png"); // 이미지 경로
//                boardRespose.setImage(Files.readAllBytes(path)); // 이미지 데이터 로드 및 설정
//                responses.add(boardRespose);
            } catch (IOException e) {
                e.printStackTrace();
                // 오류 처리
            }
        }
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable("id") Long id) {
        System.out.println("get board one");
        String userEmail = "1";
        Boards board = boardService.getBoardId(id);
        BoardDto boardDto = BoardDto.builder()
                    .boardId(board.getBoardId())
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .createdAt(board.getCreatedAt().toString())
                    .boardHit(board.getBoardHit()+1)
                    .userEmail(board.getUserEmail())
                    .locationId(board.getLocationId())
                    .build();
        try {
            String folderPath = filePath + "/" + board.getUserEmail() + "/" + board.getBoardId();

            Path dirPath = Paths.get(folderPath);
            DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath);
            List<byte[]> imageList = new ArrayList<>();
            for (Path filePath : stream) {
                if (Files.isRegularFile(filePath)) {
                    // 첫 번째 이미지 파일을 찾았으므로 읽어서 반환
                    byte[] imageBytes = Files.readAllBytes(filePath);
                    imageList.add(imageBytes);

                }
            }
            boardDto.setImage(imageList);
            //장소 가져오기
            Locations loc = locationService.getLocationId(board.getLocationId());
            boardDto.setLocationName(loc.getLocationName());
            boardDto.setLocationAddr(loc.getLocationAddr());
            boardDto.setLocationLat(loc.getLocationLat());
            boardDto.setLocationLon(loc.getLocationLon());
            boardDto.setLocationType(loc.getLocationType());

            //좋아요 가져오기
            boardDto.setBoardLikes(boardLikeService.getBoardLikes(board.getBoardId()));
            //조회수 상승
            boardService.updateBoardHit(board.getBoardId());

            //내가 좋아요를 눌렀는지?
            boardDto.setLike(boardLikeService.getLikeCheck(board.getBoardId(), userEmail));

        } catch (IOException e) {
            e.printStackTrace();
            // 오류 처리
        }
        return ResponseEntity.ok(boardDto);
    }

    //좋아요누르기 controller
    @PostMapping("/like")
    public ResponseEntity<HttpStatus> likeBoard(@RequestBody BoardLikeDto boardLikeDto) {
        System.out.println("like board");
        String userEmail = "1";
        System.out.println(boardLikeDto.getBoardId() + " " + boardLikeDto.getIsLiked());
        if(boardLikeDto.getIsLiked())
            boardLikeService.createBoardLike(boardLikeDto.getBoardId(), userEmail);
        else
            boardLikeService.removeBoardLike(boardLikeDto.getBoardId(), userEmail);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<BoardResponse>> getLikeSearchList(@PathVariable("keyword") String keyword) {
        System.out.println("get board list");
        String userEmail = "1";

        List<Boards> boards = boardService.findSearchBoards(keyword);
        List<BoardResponse> responses = new ArrayList<>();
        for (Boards board: boards) {
            BoardResponse boardRespose = BoardResponse.builder()
                    .boardId(board.getBoardId())
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .createdAt(board.getCreatedAt().toString())
                    .boardHit(board.getBoardHit())
                    .userEmail(board.getUserEmail())
                    .locationId(board.getLocationId())
                    .build();
            try {
                String folderPath = filePath +"/" + board.getUserEmail() + "/" + board.getBoardId();

                Path dirPath = Paths.get(folderPath);
                DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath);
                for (Path filePath : stream) {
                    if (Files.isRegularFile(filePath)) {
                        // 첫 번째 이미지 파일을 찾았으므로 읽어서 반환
                        byte[] imageBytes = Files.readAllBytes(filePath);
                        boardRespose.setImage(imageBytes);
                        responses.add(boardRespose);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<HttpStatus> deleteBoard(@PathVariable("id") Long boardId){
        System.out.println("delete board");
        String userEmail = "1";
        if(boardService.getBoardId(boardId).getUserEmail().equals(userEmail)){
            boardLikeService.deleteBoardLike(boardId);
            //댓글 삭제
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.ok(HttpStatus.FORBIDDEN);

    }

    @PostMapping("/update")
    public ResponseEntity<HttpStatus> updateBoard(@RequestParam("boardId") Long boardId,
                                                    @RequestParam("title") String title,
                                                    @RequestParam("content") String content,
                                                    @RequestParam("inLocation") String inLocation,
                                                    @RequestParam("files") List<MultipartFile> files) throws ParseException, IOException {
        System.out.println("update");

        //login check 해야함....
        String userEmail = "1";

        //location 정보 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(inLocation);

        Locations location = Locations.builder()
                .locationAddr(jsonObject.get("locationAddr").toString())
                .locationLat(jsonObject.get("locationLat").toString())
                .locationType(jsonObject.get("locationType").toString())
                .locationLon(jsonObject.get("locationLon").toString())
                .locationName(jsonObject.get("locationName").toString())
                .build();

        location = locationService.createLocation(location);

        Boards board = Boards.builder()
                .boardId(boardId)
                .createdAt(boardService.getBoardId(boardId).getCreatedAt())
                .boardTitle(title)
                .boardContent(content)
                .locationId(location.getLocationId())
                .userEmail(userEmail)
                .build();

        board = boardService.update(board);

        // 파일 처리
        System.out.println("파일 존재함?" + files.size());
        String nanoname = String.valueOf(System.nanoTime());
        String directoryPath = filePath + File.separator + userEmail + File.separator + board.getBoardId();
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }

        //기존 파일 삭제 하는 로직
        String folderPath = filePath+"/"+userEmail+"/"+boardId; // 여기에 실제 폴더 경로를 입력하세요.

        // File 객체 생성
        File folder = new File(folderPath);

        // 폴더가 존재하고, 실제로 폴더인지 확인
        if (folder.exists() && folder.isDirectory()) {
            // 폴더 내의 모든 파일 리스트를 얻음
            File[] deltefiles = folder.listFiles();

            // 파일이 하나도 없을 경우
            if (deltefiles == null || deltefiles.length == 0) {
                System.out.println("폴더 내에 삭제할 파일이 없습니다.");
                //return;
            }
            else {
                // 파일을 순회하며 삭제
                for (File file : deltefiles) {
                    if (file.isFile()) { // 현재 항목이 파일인 경우에만 삭제
                        boolean isDeleted = file.delete();
                        if (isDeleted) {
                            System.out.println(file.getName() + " 파일이 삭제되었습니다.");
                        } else {
                            System.out.println(file.getName() + " 파일 삭제에 실패했습니다.");
                        }
                    }
                }
            }
        } else {
            System.out.println("지정된 경로가 폴더가 아니거나 존재하지 않습니다.");
        }

        int index = 1;
        // 다중 파일 처리
        for(MultipartFile multipartFile : files) {

            // 파일의 확장자 추출
            String originalFileExtension;
            String contentType = multipartFile.getContentType();

            // 확장자명이 존재하지 않을 경우 처리 x
            if (ObjectUtils.isEmpty(contentType)) {
                break;
            } else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
                if (contentType.contains("image/jpeg"))
                    originalFileExtension = ".jpg";
                else if (contentType.contains("image/png"))
                    originalFileExtension = ".png";
                else  // 다른 확장자일 경우 처리 x
                    break;
            }

            // 파일명 중복 피하고자 나노초까지 얻어와 지정
            String new_file_name = nanoname+ (index++)  + originalFileExtension;

            File file = new File(directory, new_file_name);
            // 업로드 한 파일 데이터를 지정한 파일에 저장 로컬저장

            multipartFile.transferTo(file);
            System.out.println(directory);
            //FileCopyUtils.copy(file.getBytes(), new File(new_file_name));

            // 파일 권한 설정(쓰기, 읽기)
            file.setWritable(true);
            file.setReadable(true);
        }
        System.out.println("파일 폴더, DB 저장 완료");
        //

        return ResponseEntity.ok(HttpStatus.CREATED);

    }
}
