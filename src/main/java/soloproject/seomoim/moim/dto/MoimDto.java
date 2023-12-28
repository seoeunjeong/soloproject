package soloproject.seomoim.moim.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import soloproject.seomoim.moim.dto.validator.PastDate;
import soloproject.seomoim.moim.entitiy.MoimCategory;
import soloproject.seomoim.moim.entitiy.MoimStatus;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class MoimDto {

    @Getter @Setter
    public static class Post{

        @NotNull
        private Long memberId;

        @NotBlank(message = "모임 제목은 필수값입니다.")
        private String title;

        @Length(min=5,max =300)
        private String content;

        @NotNull(message = "모임인원을 작성해주세요")
        @Min(value = 2, message = "모임참여자수는 2명이상만 가능합니다.")
        @Max(value = 10, message = "모임참여자수는 10명 이하만 가능합니다.")
        private Integer totalParticipantCount;

        @NotNull(message = "모임일 지정은 필수입니다.")
        @PastDate
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime startedAt;

        @NotEmpty(message = "주소는 필수 입력 값 입니다.")
        private String placeName;

        private String placeAddress;

        @NotNull(message = "카테고리 선택은 필수입니다.")
        private MoimCategory moimCategory;

    }

    @Getter @Setter
    public static class Update{

        @NotNull
        private Long moimId;

        @NotBlank(message = "모임 제목은 필수값입니다.")
        private String title;

        @Length(min=5,max =300)
        private String content;

        @NotNull(message = "모임인원을 작성해주세요")
        @Min(value = 2, message = "모임참여자수는 2명이상만 가능합니다.")
        @Max(value = 15, message = "모임참여자수는 15명까지만 늘릴수있습니다.")
        private Integer totalParticipantCount;

        @NotNull(message = "모임일 지정은 필수입니다.")
        @PastDate
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime startedAt;

        @NotEmpty(message = "주소는 필수 입력 값 입니다.")
        private String placeName;

        private String placeAddress;

        @NotNull(message = "카테고리 선택은 필수입니다.")
        private MoimCategory moimCategory;

        private MoimStatus moimStatus;

    }

    @Getter @Setter
    @EqualsAndHashCode(of = {"moimId"})
    public static class Response implements Serializable {
        private Long moimId;
        private Long memberId;
        private String memberProfileImageUrl;
        private String title;
        private String content;
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startedAt;
        private int dDay;
        private String placeName;
        private String placeAddress;
        private int totalParticipantCount;
        private int participantCount;
        private MoimCategory moimCategory;
        private int likeCount;
        private List<MoimMemberDto> participants;
        private boolean open;
        private MoimStatus moimStatus;
    }
    @Getter @Setter
    public static class MoimMemberDto implements Serializable{
        private Long memberId;
        private String name;
        private String profileImageUrl;
        private Integer age;
        private char gender;
        private Boolean status;
        private int totalParticipantCount;
        private int participantCount;
    }

    @Setter @Getter
    public static class joinDto {
        private Long memberId;
        private boolean Status;
        private MoimDto.Response moimResponseDto;

    }

    @Getter @Setter
    public static class CookieDto{

        private Long moimId;
        private Long memberId;
        private String title;
        private String content;
        private Integer totalParticipantCount;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime startedAt;
        private String placeName;
        private String placeAddress;
        private MoimCategory moimCategory;
        private MoimStatus moimStatus;

    }
}
