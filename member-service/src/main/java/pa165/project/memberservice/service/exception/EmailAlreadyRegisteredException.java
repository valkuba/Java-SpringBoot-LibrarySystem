package pa165.project.memberservice.service.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String email) {
        super("Email " + email + " is already registered");
    }
}
