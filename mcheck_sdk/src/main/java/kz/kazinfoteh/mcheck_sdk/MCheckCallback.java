package kz.kazinfoteh.mcheck_sdk;

/**
 * Created by Yelnar Shopanov
 * on 14.03.2018.
 */

public interface MCheckCallback<T> {
    void onResponse(T response, Exception error);
}