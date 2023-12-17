package soloproject.seomoim.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import soloproject.seomoim.like.LikeMoim;
import soloproject.seomoim.member.dto.MemberDto.CreateMoimsDto;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.dto.MemberDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimMember;

import java.util.List;
import java.util.stream.Collectors;

import static soloproject.seomoim.member.dto.MemberDto.*;


@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberSignUpDtoToMember(Signup request);

    Member memberUpdateDtoToMember(Update updateRequest);

    @Mapping(source = "profileImage.profileImageUrl", target = "profileImageUrl")
    @Mapping(target = "joinMoims", qualifiedByName = "filterJoins")
    ResponseDto memberToMemberResponseDto(Member member);

    List<CreateMoimsDto> createMoimsToCreateMoimsDto(List<Moim> createMoims);

    @Mapping(source = "moim.id", target = "id")
    @Mapping(source = "moim.title", target = "title")
    List<MoimMemberDto> joinMoimsTojoinMoimDtos(List<MoimMember> joinMoims);


    @Mapping(source = "moim.id", target = "id")
    @Mapping(source = "moim.title", target = "title")
    @Mapping(source = "moim.startedAt", target = "startedAt")
    List<LikeMoimDto> likeMoimsTolikeMoimsDtos(List<LikeMoim> likeMoims);

    @Mapping(source = "moim.id", target = "id")
    @Mapping(source = "moim.title", target = "title")
    @Mapping(source = "moim.startedAt", target = "startedAt")
    MemberDto.MoimMemberDto moimMemberToMoimMemberDto(MoimMember moimMember);


    @Named("filterJoins")
    default List<MoimMemberDto> filterParticipants(List<MoimMember> moimMembers) {
        return moimMembers.stream()
                .filter(MoimMember::isStatus)
                .map(this::moimMemberToMoimMemberDto)
                .collect(Collectors.toList());
    }

    default MemberDto.LikeMoimDto likeMoimToLikeMoimDto(LikeMoim likeMoim) {
        if (likeMoim == null) {
            return null;
        } else {
            MemberDto.LikeMoimDto likeMoimDto = new MemberDto.LikeMoimDto();
            likeMoimDto.setId(likeMoim.getMoim().getId());
            likeMoimDto.setTitle(likeMoim.getMoim().getTitle());
            likeMoimDto.setStartedAt(likeMoim.getMoim().getStartedAt());
            return likeMoimDto;
        }
    }

}




