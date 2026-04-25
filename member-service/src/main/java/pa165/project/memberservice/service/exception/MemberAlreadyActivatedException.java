package pa165.project.memberservice.service.exception;

public class MemberAlreadyActivatedException extends MemberServiceException {
    public MemberAlreadyActivatedException(Long id) {
        super("User with id " + id + " is already activated");
    }
}
