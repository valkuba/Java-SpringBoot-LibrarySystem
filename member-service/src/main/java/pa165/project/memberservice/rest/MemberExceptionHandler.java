package pa165.project.memberservice.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pa165.project.memberservice.model.exception.MemberUpdateException;
import pa165.project.memberservice.service.exception.MemberIdAlreadyExistsException;
import pa165.project.memberservice.service.exception.MemberServiceException;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberIdAlreadyExistsException.class)
    public ResponseEntity<String> handleMemberIdAlreadyExists(MemberIdAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("could not create member");
    }

    @ExceptionHandler(MemberServiceException.class)
    public ResponseEntity<String> handleMemberRepository(MemberServiceException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(MemberUpdateException.class)
    public ResponseEntity<String> handleMemberUpdate(MemberUpdateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
