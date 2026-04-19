package pa165.project.memberservice.repository.exception;

public class MemberNameAlreadyExistsException extends MemberRepositoryException {
    public MemberNameAlreadyExistsException(String username) {
        super("Member with username " + username + " already exists");
    }
}
