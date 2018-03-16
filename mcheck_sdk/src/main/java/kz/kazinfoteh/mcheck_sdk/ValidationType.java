package kz.kazinfoteh.mcheck_sdk;

/**
 * Created by Yelnar Shopanov
 * on 13.03.2018.
 */

public enum ValidationType {
    SMS("sms");

    private final String value;

    public String getValue() {
        return value;
    }

    ValidationType(final String value) {
        this.value = value;
    }
}