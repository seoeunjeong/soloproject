package soloproject.seomoim.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class CommentDto {
    @Getter @Setter
    public static class Post{
        @NotBlank(message = "댓글을 등록하는 회원Id는 필수값입니다.")
        private Long memberId;
        @NotBlank(message = "댓글을 등록하는 모임Id는 필수값입니다.")
        private Long moimId;
        @NotBlank(message = "댓글 내용은 필수값입니다.")
        private String content;
    }
    @Getter
    @Setter
    public static class Update{
        private String content;
    }

    @Getter
    @Setter
    public static class Response{
        private Long moimId;
        private Long memberId;
        private List<String> commentList;

    }
}
