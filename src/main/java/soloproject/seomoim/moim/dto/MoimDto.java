package soloproject.seomoim.moim.dto;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.moim.entitiy.MoimCategory;

public class MoimDto {

    @Getter
    @Setter
    public static class Post{
        private Long memberId;
        private String title;
        private String content;
        private int participantCount;
        private String region;
        private MoimCategory moimCategory;
    }

    @Getter
    @Setter
    public static class Update{
        private String title;
        private String content;
        private int participantCount;
        private String region;
        private MoimCategory moimCategory;

    }

    @Getter
    @Setter
    public static class Response{
        private Long memberId;
        private String title;
        private String content;
        private int participantCount;
        private String region;
        private MoimCategory moimCategory;

    }
}
