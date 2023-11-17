package request;

import com.study.sociallogin.dto.TripMemberDto;
import lombok.Getter;

import java.util.List;
@Getter
public class TripRequest {

    private String title;
    private String content;
    private int type;
    private String startDate;
    private String endDate;
    private List<TripMemberDto> users;
    private String tripDetails;
}
