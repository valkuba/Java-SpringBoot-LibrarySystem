package pa165.project.memberservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pa165.project.memberservice.model.exception.MemberUpdateException;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="Member")
public class Member implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(updatable = false)
    private Date registrationDate;
    private boolean activated = false;

    @PrePersist
    private void prePersist() {
        if (this.registrationDate == null) {
            this.registrationDate = new Date();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Member member)) return false;
        return activated == member.activated && Objects.equals(username, member.username) && Objects.equals(email, member.email) && Objects.equals(password, member.password) && Objects.equals(registrationDate, member.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password, registrationDate, activated);
    }

    public void validateChanges(Member changed) throws MemberUpdateException {
        if(!id.equals(changed.getId()))
            throw new MemberUpdateException("Cannot change member's id");
        if(!registrationDate.equals(changed.registrationDate))
            throw new MemberUpdateException("Cannot change member's registration date");
        if(activated != changed.activated){
            throw new MemberUpdateException("Cannot change member's activation status");
        }
    }
}
