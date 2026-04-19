package pa165.project.memberservice.service.exception;

public class MemberIdAlreadyExistsException extends MemberServiceException {
    public MemberIdAlreadyExistsException(Long id) {
        super("User with id " + id + " is already registered");
    }
}
