package at.spengergasse.springbootexample.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateResourceNotFound extends RuntimeException{
    public DuplicateResourceNotFound(String message) {
        super(message);
    }
}
