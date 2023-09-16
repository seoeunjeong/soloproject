package soloproject.seomoim.moim.mapper;

import org.mapstruct.Mapper;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;

@Mapper(componentModel = "spring")
public interface MoimMapper {

    Moim moimPostDtoToMoim(MoimDto.Post createRequest);
}
