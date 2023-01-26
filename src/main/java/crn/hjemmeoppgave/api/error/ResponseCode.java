package crn.hjemmeoppgave.api.error;

public enum ResponseCode {

    USER_ID_REQUIRED(400, "User ID is required"),
    UNIT_ID_REQUIRED(400, "Unit ID is required"),
    ROLE_ID_REQUIRED(400, "Role ID is required"),
    TIMESTAMP_REQUIRED(400, "Timestamp is required"),
    MISSING_DATA(400, "mandatory Data Missing"),

    ROLES_ALREADY_EXISTS(400, "User Role Already Exists"),
    ROLE_DOES_NOT_EXIST(400, "Role does not exist"),
    USER_DOES_NOT_EXIST(400, "User Does not exists"),
    VALIDTO_IS_BEFORE_VALIDFROM(400, "Valid_From date must be before Valid_To date"),
    USER_ROLE_ALREADY_EXISTS(400, "User Role already exists for the specified Unit ID");

    private int code;
    private String description;

    private ResponseCode() {
    }

    private ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}