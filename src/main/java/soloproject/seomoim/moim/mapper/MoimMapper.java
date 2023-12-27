package soloproject.seomoim.moim.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimMember;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MoimMapper {

    @Mapping(source = "memberId",target = "member.id")
    Moim moimPostDtoToMoim(MoimDto.Post postDto);

    Moim moimUpdateDtoToMoim(MoimDto.Update updateDto);

    @Mapping(source ="id",target ="moimId")
    MoimDto.Update moimToMoimUpdateDto(Moim moim);

    @Mapping(source ="member.id",target ="memberId")
    @Mapping(source = "member.profileImage.profileImageUrl",target ="memberProfileImageUrl")
    @Mapping(source = "id",target = "moimId")
    @Mapping(target = "participants", qualifiedByName = "filterParticipants")
    MoimDto.Response moimToResponseDto(Moim moim);

    @Mapping(source = "member.id",target = "memberId")
    @Mapping(source = "member.name",target = "name")
    @Mapping(source = "member.profileImage.profileImageUrl",target ="profileImageUrl")
    @Mapping(source = "member.age",target ="age")
    @Mapping(source = "member.gender",target ="gender")
    @Mapping(source = "moim.totalParticipantCount",target = "totalParticipantCount")
    @Mapping(source = "moim.participantCount",target = "participantCount")
    MoimDto.MoimMemberDto moimMemberToMoimMemberDto(MoimMember moimMember);

    @Named("filterParticipants")
    default List<MoimDto.MoimMemberDto> filterParticipants(List<MoimMember> moimMembers){
        return moimMembers.stream()
                .filter(MoimMember::isStatus)
                .map(this::moimMemberToMoimMemberDto)
                .collect(Collectors.toList());
    }



    @Mapping(source ="member.id",target ="memberId")
    @Mapping(source = "member.profileImage.profileImageUrl",target ="memberProfileImageUrl")
    @Mapping(source = "id",target = "moimId")
    @Mapping(target = "participants", qualifiedByName = "filterParticipants")
    List<MoimDto.Response> moimsToResponseDtos(List<Moim> moims);
}
