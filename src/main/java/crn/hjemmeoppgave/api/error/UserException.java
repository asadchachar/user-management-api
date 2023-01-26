package crn.hjemmeoppgave.api.error;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ResponseCode responseCode;
    private HttpStatus httpStatus;

    public UserException() {
    }

    public UserException(ResponseCode responseCode) {
        super();
        this.responseCode = responseCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public UserException(ResponseCode responseCode, HttpStatus httpStatus) {
        super();
        this.responseCode = responseCode;
        this.httpStatus = httpStatus;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "CustomException [responseCode=" + responseCode + "]";
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
