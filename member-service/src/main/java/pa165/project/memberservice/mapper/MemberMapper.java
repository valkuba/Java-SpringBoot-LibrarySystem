package pa165.project.memberservice.mapper;

import org.mapstruct.Mapper;
import pa165.project.memberservice.dto.MemberDto;
import pa165.project.memberservice.dto.NewMemberRegisteringDto;
import pa165.project.memberservice.model.Member;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberDto toDto(Member Member);
    Member toModel(MemberDto MemberDto);
    Member toModel(NewMemberRegisteringDto newMemberRegisteringDto);
}