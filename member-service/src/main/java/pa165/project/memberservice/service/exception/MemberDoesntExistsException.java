package pa165.project.memberservice.service.exception;

public class MemberDoesntExistsException extends MemberServiceException {
    public MemberDoesntExistsException(Long id) {
        super("Member with id " + id + " does not exist");
    }
}
