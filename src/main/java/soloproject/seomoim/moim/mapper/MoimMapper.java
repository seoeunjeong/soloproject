package soloproject.seomoim.moim.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;

@Mapper(componentModel = "spring")
public interface MoimMapper {

    Moim moimPostDtoToMoim(MoimDto.Post createDto);

    Moim moimUpdateDtoToMoim(MoimDto.Update updateDto);

    @Mapping(source ="member.id",target ="memberId")
    MoimDto.Response MoimToResponseDto(Moim moim);
}
