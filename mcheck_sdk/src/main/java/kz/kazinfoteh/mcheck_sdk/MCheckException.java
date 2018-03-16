package kz.kazinfoteh.mcheck_sdk;

/**
 * Created by Yelnar Shopanov
 * on 14.03.2018.
 */

public class MCheckException extends Exception {
    private int code;

    public MCheckException(final int code, final String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}