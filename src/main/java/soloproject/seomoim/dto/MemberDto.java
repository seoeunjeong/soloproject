package soloproject.seomoim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class MemberDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Post {
        @Email
        @NotNull
        private String email;
        @NotNull
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Update{
        private String name;
        private int age;
        private String gender;
        private String region;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ResponseDto{
        private String email;
        private String name;
        private int age;
        private String gender;
        private String region;
    }

}
