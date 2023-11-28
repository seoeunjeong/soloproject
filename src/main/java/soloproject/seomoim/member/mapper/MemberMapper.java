package soloproject.seomoim.member.mapper;

import org.mapstruct.Mapper;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.dto.MemberDto;
import soloproject.seomoim.security.CustomUserDetails;


@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberSignUpDtoToMember(MemberDto.Signup request);

    Member memberUpdateDtoToMember(MemberDto.Update updateRequest);

    MemberDto.Dto memberToMemberDto(CustomUserDetails userDetails);

}

//api 만들때 고려
//    MemberDto.ResponseDto memberToMemberResponseDto(Member member);
//
////    @Mapping(source = "moim.id", target = "moimId")
////    @Mapping(source = "moim.title",target = "moimTitle")
////    MemberDto.LikeMoimDto LikeMoimToLikeMoimResponseDto(LikeMoim likeMoim);
//
//    default List<MemberDto.LikeMoimDto> likeMoimsToLikeMoimsResponseDtos(List<LikeMoim> likeMoims) {
//    return likeMoims
//            .stream()
//            .map(likeMoim -> {
//                return MemberDto.LikeMoimDto
//                        .builder()
//                        .moimId(likeMoim.getMoim().getId())
//                        .moimTitle(likeMoim.getMoim().getTitle())
//                        .build();
//            })
//            .collect(Collectors.toList());
//    }
//
//    default List<MemberDto.MoimMemberDto> MoimMemberToMoimMemberResponseDtos(List<MoimMember> moimMembers) {
//        return moimMembers
//                .stream()
//                .map(moimMember -> {
//                    return MemberDto.MoimMemberDto
//                            .builder()
//                            .moimId(moimMember.getMoim().getId())
//                            .moimTitle(moimMember.getMoim().getTitle())
//                            .build();
//                })
//                .collect(Collectors.toList());
//    }
//
//    default List<MemberDto.CreateMoimsDto> MoimToCreateMoimsResponseDtos(List<Moim> moims) {
//        return moims
//                .stream()
//                .map(moim -> {
//                    return MemberDto.CreateMoimsDto
//                            .builder()
//                            .moimId(moim.getId())
//                            .moimTitle(moim.getTitle())
//                            .build();
//                })
//                .collect(Collectors.toList());
//    }
