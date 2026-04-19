package pa165.project.memberservice.repository.exception;

public class MemberAlreadyActivatedException extends MemberRepositoryException {
    public MemberAlreadyActivatedException(Long id) {
        super("User with id " + id + " is already activated");
    }
}
