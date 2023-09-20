package soloproject.seomoim.member.mapper;

import org.mapstruct.Mapper;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.dto.MemberDto;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberDto.Post signupRequest);
    Member memberUpdateDtoToMember(MemberDto.Update updateRequest);

    MemberDto.ResponseDto
    memberToMemberResponseDto(Member member);
}
