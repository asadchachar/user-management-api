package crn.hjemmeoppgave.api.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class, RuntimeException.class, Exception.class })
    public ResponseEntity<FinalException> handleException(UserException ex, WebRequest request) {
        logger.info("EXCEPTION.. {}", ex);

        FinalException finalException = new FinalException(ex.getResponseCode().getDescription(),
                ex.getResponseCode().getCode());

        return new ResponseEntity<>(finalException, ex.getHttpStatus());
    }
}

