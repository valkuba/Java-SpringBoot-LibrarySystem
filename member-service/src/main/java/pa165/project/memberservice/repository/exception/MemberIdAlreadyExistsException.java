package pa165.project.memberservice.repository.exception;

public class MemberIdAlreadyExistsException extends MemberRepositoryException {
    public MemberIdAlreadyExistsException(Long id) {
        super("User with id " + id + " is already registered");
    }
}
