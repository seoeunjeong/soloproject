package soloproject.seomoim.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import soloproject.seomoim.moim.like.LikeMoim;
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

    MemberDto.Update memberToMemberUpdateDto(Member member);

    @Mapping(source = "profileImage.profileImageUrl", target = "profileImageUrl")
    @Mapping(target = "joinMoimList", qualifiedByName = "filterJoins")
    @Mapping(target = "likeMoimList", qualifiedByName = "filterLikeMoims")
    ResponseDto memberToMemberResponseDto(Member member);

    List<CreateMoimsDto> createMoimsToCreateMoimsDto(List<Moim> createMoims);

    @Mapping(source = "moim.id", target = "id")
    @Mapping(source = "moim.title", target = "title")
    @Mapping(source = "moim.startedAt", target = "startedAt")
    MemberDto.MoimMemberDto moimMemberToMoimMemberDto(MoimMember moimMember);

    @Mapping(source = "moim.id", target = "id")
    @Mapping(source = "moim.title", target = "title")
    @Mapping(source = "moim.startedAt", target = "startedAt")
    MemberDto.LikeMoimDto likeMoimToLikeMoimDto(LikeMoim likeMoim);


    @Named("filterJoins")
    default List<MemberDto.MoimMemberDto> filterJoinMoims(List<MoimMember> moimMembers){
        return moimMembers.stream()
                .filter(MoimMember::isStatus)
                .map(this::moimMemberToMoimMemberDto)
                .collect(Collectors.toList());
    }
    @Named("filterLikeMoims")
    default List<LikeMoimDto> filterLikeMoims(List<LikeMoim> likeMoims) {
        return likeMoims.stream()
                .filter(LikeMoim::isStatus)
                .map(this::likeMoimToLikeMoimDto)
                .collect(Collectors.toList());
    }

}




