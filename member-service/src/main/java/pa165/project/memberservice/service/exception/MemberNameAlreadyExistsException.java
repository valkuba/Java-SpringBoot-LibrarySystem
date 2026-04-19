package pa165.project.memberservice.service.exception;

public class MemberNameAlreadyExistsException extends MemberServiceException {
    public MemberNameAlreadyExistsException(String username) {
        super("Member with username " + username + " already exists");
    }
}
