package soloproject.seomoim.moim.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import soloproject.seomoim.moim.entitiy.MoimCategory;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MoimSearchDto {

    private MoimCategory moimCategory;

    private String keyword;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startedAt;

}
