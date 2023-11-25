package soloproject.seomoim.moim.dto;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.moim.entitiy.MoimCategory;

@Getter
@Setter
public class MoimSearchDto {

    private String region;
    private MoimCategory moimCategory;
    private String keyword;

    public MoimSearchDto() {
    }
}
