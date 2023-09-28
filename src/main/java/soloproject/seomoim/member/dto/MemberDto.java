package soloproject.seomoim.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;


public class MemberDto {
    @Getter @Setter
    public static class Signup {


        @Email(message = "이메일의 형식이 올바르지 않습니다.")
        @NotBlank(message = "아이디는 필수 입력 값 입니다.")
        private String email;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",
                message = "비밀번호는 영문,특수문자,숫자를 포함하여 8자리 이상이여야합니다.")
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        private String password;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",
                message = "비밀번호는 영문,특수문자,숫자를 포함하여 8자리 이상이여야합니다.")
        @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
        private String confirmPassword;
    }

    @Getter @Setter
    public static class Update{


        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",
                message = "비밀번호는 영문,특수문자,숫자를 포함하여 8자리 이상이여야합니다.")
        private String password;
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{3,4}$", message = "이름은 특수문자를 제외한 3~4자리여야 합니다.")
        private String name;

        @Min(12) @Max(100)
        private int age;

        private String gender;

        private String city;
        private String gu;
        private String dong;
    }

    @Getter @Setter
    public static class ResponseDto{
        private String email;
        private String name;
        private Integer age;
        private String gender;
        private String city;
        private String gu;
        private String dong;
        private List<CreateMoimsDto> createMoims;
        private List<MoimMemberDto> participationMoims;
        private List<LikeMoimDto> likeMoims;


    }

    @Getter @Setter
    @AllArgsConstructor
    @Builder
    public static class LikeMoimDto{
        private Long moimId;
        private String moimTitle;
    }

    @Getter @Setter
    @AllArgsConstructor
    @Builder
    public static class MoimMemberDto{
        private Long moimId;
        private String moimTitle;
    }
    @Getter @Setter
    @AllArgsConstructor
    @Builder
    public static class CreateMoimsDto{
        private Long moimId;
        private String moimTitle;
    }
}


