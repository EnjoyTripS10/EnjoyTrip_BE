package com.study.sociallogin.service;

import com.study.sociallogin.NotFoundException;
import com.study.sociallogin.dto.*;
import com.study.sociallogin.model.User;
import com.study.sociallogin.repository.UserRepository;
import com.study.sociallogin.type.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final List<SocialLoginService> loginServices;
    private final UserRepository userRepository;

    public LoginResponse doSocialLogin(SocialLoginRequest request) {

        SocialLoginService loginService = this.getLoginService(request.getUserType());
        SocialAuthResponse socialAuthResponse = loginService.getAccessToken(request.getCode());

        SocialUserResponse socialUserResponse = loginService.getUserInfo(socialAuthResponse.getAccess_token());
        log.info("socialUserResponse {} ", socialUserResponse.toString());

        if (userRepository.findByUserId(socialUserResponse.getId()).isEmpty()) {
            this.joinUser(
                    UserJoinRequest.builder()
                            .userId(socialUserResponse.getId())
                            .userEmail(socialUserResponse.getEmail())
                            .userName(socialUserResponse.getName())
                            .userType(request.getUserType())
                            .picture(socialUserResponse.getPicture())
                            .token(socialAuthResponse.getAccess_token())
                            .build()
            );
        }

        User user = userRepository.findByUserId(socialUserResponse.getId())
                .orElseThrow(() -> new NotFoundException("ERROR_001", "유저 정보를 찾을 수 없습니다."));

        System.out.println("user info" + user.toString());

        return LoginResponse.builder()
                .id(user.getId())
                .accessToken(socialAuthResponse.getAccess_token())
                .build();
    }

    private UserJoinResponse joinUser(UserJoinRequest userJoinRequest) {
        User user = userRepository.save(
                User.builder()
                        .userId(userJoinRequest.getUserId())
                        .userType(userJoinRequest.getUserType())
                        .userEmail(userJoinRequest.getUserEmail())
                        .userName(userJoinRequest.getUserName())
                        .picture(userJoinRequest.getPicture())
                        .token(userJoinRequest.getToken())
                        .build()
        );

        return UserJoinResponse.builder()
                .id(user.getId())
                .build();
    }

    private SocialLoginService getLoginService(UserType userType) {
        for (SocialLoginService loginService : loginServices) {
            if (userType.equals(loginService.getServiceName())) {
                log.info("login service name: {}", loginService.getServiceName());
                return loginService;
            }
        }
        return new LoginServiceImpl();
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ERROR_001", "유저 정보를 찾을 수 없습니다."));
        System.out.println("user" + user.toString());
        return UserResponse.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .userName(user.getUserName())
                .picture(user.getPicture()) // TODO: picture 추가
                .build();
    }

    public UserResponse getUserEmail(String email) {
        User user = userRepository.findByUserEmail(email);
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .userName(user.getUserName())
                .picture(user.getPicture())
                .build();
    }

    public List<UserResponse> getUsers() {
        List<User> userList =  userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : userList) {
            userResponseList.add(
                    UserResponse.builder()
                            .userEmail(user.getUserEmail())
                            .userName(user.getUserName())
                            .picture(user.getPicture())
                            .build()
            );
        }
        return userResponseList;
    }

    public String getUserEmailFromToken(String token) {
        token = token.substring(7);
        User user = userRepository.findByToken(token);
        if (user == null) {
            return null;
        }
        return user.getUserEmail();
    }
}
