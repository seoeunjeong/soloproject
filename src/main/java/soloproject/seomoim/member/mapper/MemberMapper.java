package soloproject.seomoim.mapper;

import org.mapstruct.Mapper;
import soloproject.seomoim.domain.Member;
import soloproject.seomoim.dto.MemberDto;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberDto.Post signupRequest);
    Member memberUpdateDtoToMember(MemberDto.Update updateRequest);
    MemberDto.ResponseDto memberToMemberResponseDto(Member member);
}
