package kz.kazinfoteh.mcheck_sdk;

/**
 * Created by Yelnar Shopanov
 * on 14.03.2018.
 */

public class RequestValidationResult {
    private String id;
    private boolean type;
    private String phone;

    /**
     * @return unique identifier of the validation request
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return validation type as requested
     */
    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}