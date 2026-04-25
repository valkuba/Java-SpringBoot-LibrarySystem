package pa165.project.memberservice.model.exception;

public class MemberUpdateException extends RuntimeException {
    public MemberUpdateException(String message) {
        super(message);
    }
    public MemberUpdateException(String message, Throwable cause) {
        super(message,cause);
    }
}
