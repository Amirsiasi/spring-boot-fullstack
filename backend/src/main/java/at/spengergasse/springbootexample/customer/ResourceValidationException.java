package at.spengergasse.springbootexample.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceValidationException extends Throwable {
    public ResourceValidationException(String noDataChangesFound) {
        super(noDataChangesFound);
    }
}
