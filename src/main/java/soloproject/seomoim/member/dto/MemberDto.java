package soloproject.seomoim.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

public class MemberDto {
    @Getter @Setter
    @AllArgsConstructor
    public static class Post {
        @Email
        @NotNull
        private String email;

        @NotNull//패스워드 유효성검증

        private String password;

        //패스워드 입려확인필드 추가로존재하는게 좋다 두 패스워드가 일치하는지 검증하는 로직까지/
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class Update{
        private String name;
        private int age;
        private String gender;
        private String region;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class ResponseDto{
        private String email;
        private String name;
        private int age;
        private String gender;
        private String region;
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


