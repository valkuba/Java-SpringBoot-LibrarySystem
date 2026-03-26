package pa165.project.memberservice.repository;

public class MemberIdAlreadyExistsException extends MemberRepositoryException {
    public MemberIdAlreadyExistsException(Long id) {
        super("User with id " + id + " is already registered");
    }
}
