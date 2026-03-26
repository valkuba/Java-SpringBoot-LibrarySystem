package pa165.project.memberservice.repository;

public class MemberDoesntExistsException extends MemberRepositoryException {
    public MemberDoesntExistsException(Long id) {
        super("Member with id " + id + " does not exist");
    }
}
