package soloproject.seomoim.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;


public class MemberDto {
    @Getter
    @Setter
    public static class Signup {

        @NotBlank(message = "이름은 필수입력 값 입니다.")
        @Pattern(regexp = "^[가-힣]{2,4}$", message = "숫자 와 특수문자를 제외한 2~4자리여야 합니다.")
        private String name;

        @NotBlank(message = "아이디는 필수 입력 값 입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$", message = "올바른 이메일 형식이 아닙니다.")
        private String email;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",
                message = "비밀번호는 영문,특수문자,숫자를 포함하여 8자리 이상이여야합니다.")
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        private String password;

        @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
        private String confirmPassword;

    }

    @Getter
    @Setter
    public static class Update {

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",
                message = "비밀번호는 영문,특수문자,숫자를 포함하여 8자리 이상이여야합니다.")
        private String password;

        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,4}$", message = "이름은 특수문자를 제외한 2~4자리여야 합니다.")
        private String name;

        private Integer age;

        private char gender;

        private String address;

        private MultipartFile profileImage;

    }

    @Getter
    @Setter
    public static class Dto {
        private Long id;
        private String email;
        private String name;
        private Integer age;
        private char gender;
        private String address;
    }
}



//@Getter
//@Setter
//@AllArgsConstructor
//@Builder
//public static class LikeMoimDto {
//    private Long moimId;
//    private String moimTitle;
//}
//
//@Getter
//@Setter
//@AllArgsConstructor
//@Builder
//public static class MoimMemberDto {
//    private Long moimId;
//    private String moimTitle;
//}
//
//@Getter
//@Setter
//@AllArgsConstructor
//@Builder
//public static class CreateMoimsDto {
//    private Long moimId;
//    private String moimTitle;
//}


