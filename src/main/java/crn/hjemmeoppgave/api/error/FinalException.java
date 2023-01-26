package crn.hjemmeoppgave.api.error;

public class FinalException {
    private String message;
    private int responseCode;

    public FinalException() {
    }

    public FinalException(String message, int responseCode) {
        super();
        this.message = message;
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

}
