package pa165.project.memberservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Date registrationDate;
    private boolean isActivated = false;

    public void validateChanges(Member changed) throws MemberUpdateException {
        if(!id.equals(changed.getId()))
            throw new MemberUpdateException("Cannot change member's id");
        if(!registrationDate.equals(changed.registrationDate))
            throw new MemberUpdateException("Cannot change member's registration date");
        if(isActivated != changed.isActivated){
            throw new MemberUpdateException("Cannot change member's activation status");
        }
    }
}
