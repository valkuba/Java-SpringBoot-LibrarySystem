package pa165.project.memberservice.repository.exception;

public class MemberDoesntExistsException extends MemberRepositoryException {
    public MemberDoesntExistsException(Long id) {
        super("Member with id " + id + " does not exist");
    }
}
