package kz.kazinfoteh.mcheck_sdk;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yelnar Shopanov
 * on 16.03.2018.
 */

public class ValidationStatusResult {
    private String phone;
    private boolean validated;
    @SerializedName("validation_date")
    private long validationDate;

    /**
     * @return the number that needs to be validated
     */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return true if the pin was correct. false Otherwise.
     */
    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    /**
     * @return the date time as UNIX timestamp when the validation was completed (the pin was matched first time).
     * In case the number is not validated the value is null
     */
    public long getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(long validationDate) {
        this.validationDate = validationDate;
    }
}