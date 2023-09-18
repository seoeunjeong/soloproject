package soloproject.seomoim.moim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class MoimMemberDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response{
        private Long memberId;
        private Long moimId;
        private String moimName;
    }
}
