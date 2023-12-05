package soloproject.seomoim.moim.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import soloproject.seomoim.moim.entitiy.MoimCategory;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class MoimDto {

    @Getter
    @Setter
    public static class Post{

        @NotBlank(message = "모임 제목은 필수값입니다.")
        private String title;

        @Length(min=10)
        private String content;

        @Min(value = 2, message = "모임참여자수는 2명이상만 가능합니다.")
        @Max(value = 10, message = "모임참여자수는 10명 이하만 가능합니다.")
        private int totalParticipantCount;

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime startedAt;

        private String region;

        @NotNull(message = "카테고리 선택은 필수입니다.")
        private MoimCategory moimCategory;

    }

    @Getter
    @Setter
    public static class Update{
        private String title;
        private String content;
        @Max(value = 15, message = "모임참여자수는 15명까지만 늘릴수있습니다.")
        private int totalParticipantCount;
        private String region;
        private MoimCategory moimCategory;

    }

    @Getter
    @Setter
    public static class Response{
        private Long memberId;
        private String title;
        private String content;
        private int totalParticipantCount;
        private int participantCount;
        private String region;
        private MoimCategory moimCategory;
        private int likeCount;

    }
}
