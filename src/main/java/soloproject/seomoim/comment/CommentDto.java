package soloproject.seomoim.comment;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CommentDto {
    @Getter
    @Setter

    public static class Post{
        private Long memberId;
        private Long moimId;
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
