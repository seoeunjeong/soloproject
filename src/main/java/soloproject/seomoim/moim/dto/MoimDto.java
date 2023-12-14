package soloproject.seomoim.moim.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import soloproject.seomoim.moim.entitiy.MoimCategory;
import soloproject.seomoim.moim.entitiy.MoimMember;
import soloproject.seomoim.moim.entitiy.MoimStatus;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MoimDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post{

        @NotNull
        private Long memberId;

        @NotBlank(message = "모임 제목은 필수값입니다.")
        private String title;

        @Length(min=10)
        private String content;

        @Min(value = 2, message = "모임참여자수는 2명이상만 가능합니다.")
        @Max(value = 10, message = "모임참여자수는 10명 이하만 가능합니다.")
        private int totalParticipantCount;

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate startedAt;

        @NotEmpty(message = "주소는 필수 입력 값 입니다.")
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

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate startedAt;
        private String region;
        private MoimCategory moimCategory;
        private MoimStatus moimStatus;

    }

    @Getter
    @Setter
    public static class Response{
        private Long moimId;
        private Long memberId;
        private String memberProfileImageUrl;
        private String title;
        private String content;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate startedAt;
        private String region;
        private int totalParticipantCount;
        private int participantCount;
        private MoimCategory moimCategory;
        private int likeCount;
        private List<MoimMemberDto> participants;
        private boolean open;
        private MoimStatus moimStatus;


    }
    @Builder
    @Getter
    public static class MoimMemberDto{
        private Long memberId;
        private String name;
        private String profileImageUrl;
    }
}
