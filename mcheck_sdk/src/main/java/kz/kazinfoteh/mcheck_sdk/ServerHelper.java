package kz.kazinfoteh.mcheck_sdk;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Yelnar Shopanov
 * on 13.03.2018.
 */

final class ServerHelper {
    private final static String URL_HOST = "http://isms.center";
    private final static String URL_V1 = "/v1";
    private final static String URL_VALIDATION = URL_V1 + "/validation";
    private final static String URL_VALIDATION_REQUEST = URL_VALIDATION + "/request";
    private final static String URL_VALIDATION_VERIFY = URL_VALIDATION + "/verify";
    private final static String URL_VALIDATION_STATUS = URL_VALIDATION + "/status";

    private final static String TOKEN = "token";
    private final static String ERROR_CODE = "error_code";
    private final static String ERROR_MESSAGE = "error_message";

    interface ServiceApi {
        @POST(URL_VALIDATION_REQUEST)
        Call<RequestValidationResult> requestValidation(@Header(TOKEN) String token, @Body RequestValidationBody body);

        @POST(URL_VALIDATION_VERIFY)
        Call<VerifyValidationResult> verifyValidation(@Header(TOKEN) String token, @Body VerifyValidationBody body);

        @POST(URL_VALIDATION_STATUS)
        Call<ValidationStatusResult> statusValidation(@Header(TOKEN) String token, @Body StatusValidationBody body);
    }

    private final ServiceApi mService;
    private static ServerHelper mHelper = null;

    static ServerHelper getInstance() {
        synchronized (ServerHelper.class) {
            if (mHelper == null) {
                mHelper = new ServerHelper();
            }

            return mHelper;
        }
    }

    private ServerHelper() {
        final Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mService = retrofit.create(ServiceApi.class);
    }

    void requestValidation(final String token, final String phone,
                           final ValidationType type, final String msg,
                           final MCheckCallback<RequestValidationResult> callback) {
        final RequestValidationBody body = new RequestValidationBody();
        body.setPhone(phone);
        body.setType(type.getValue());
        body.setMsg(msg);

        final Call<RequestValidationResult> call = mService.requestValidation(token, body);
        call.enqueue(new Callback<RequestValidationResult>() {
            @Override
            public void onResponse(Call<RequestValidationResult> call,
                                   Response<RequestValidationResult> response) {
                if (callback == null) {
                    return;
                }

                try {
                    checkResponse(response);
                    callback.onResponse(response.body(), null);
                } catch (Exception e) {
                    callback.onResponse(null, e);
                }
            }

            @Override
            public void onFailure(Call<RequestValidationResult> call, Throwable t) {
                if (callback != null) {
                    callback.onResponse(null, new MCheckException(500, t.getMessage()));
                }
            }
        });
    }

    void verifyValidation(final String token, final String id,
                          final String pin,
                          final MCheckCallback<VerifyValidationResult> callback) {
        final VerifyValidationBody body = new VerifyValidationBody();
        body.setId(id);
        body.setPin(pin);

        final Call<VerifyValidationResult> call = mService.verifyValidation(token, body);
        call.enqueue(new Callback<VerifyValidationResult>() {
            @Override
            public void onResponse(Call<VerifyValidationResult> call,
                                   Response<VerifyValidationResult> response) {
                if (callback == null) {
                    return;
                }

                try {
                    checkResponse(response);
                    callback.onResponse(response.body(), null);
                } catch (Exception e) {
                    callback.onResponse(null, e);
                }
            }

            @Override
            public void onFailure(Call<VerifyValidationResult> call, Throwable t) {
                if (callback != null) {
                    callback.onResponse(null, new MCheckException(500, t.getMessage()));
                }
            }
        });
    }

    void statusValidation(final String token, final String id, final MCheckCallback<ValidationStatusResult> callback) {
        final StatusValidationBody body = new StatusValidationBody();
        body.setId(id);

        final Call<ValidationStatusResult> call = mService.statusValidation(token, body);
        call.enqueue(new Callback<ValidationStatusResult>() {
            @Override
            public void onResponse(Call<ValidationStatusResult> call, Response<ValidationStatusResult> response) {
                if (callback == null) {
                    return;
                }

                try {
                    checkResponse(response);
                    callback.onResponse(response.body(), null);
                } catch (Exception e) {
                    callback.onResponse(null, e);
                }
            }

            @Override
            public void onFailure(Call<ValidationStatusResult> call, Throwable t) {
                if (callback != null) {
                    callback.onResponse(null, new MCheckException(500, t.getMessage()));
                }
            }
        });
    }

    private class BaseResponse {
        @SerializedName(ERROR_CODE)
        private int errorCode;
        @SerializedName(ERROR_MESSAGE)
        private String errorMessage;

        private int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        private String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    private void checkResponse(final Response response) throws Exception {
        if (!response.isSuccessful()) {
            if (response.errorBody() != null) {
                final String errorBody = response.errorBody().string();

                if (TextUtils.isEmpty(errorBody)) {
                    checkBase(errorBody);
                }
            }

            throw new MCheckException(response.code(), response.message());
        } else {
            if (response.body() == null) {
                throw new MCheckException(response.code(), "Server error, empty body");
            }
        }
    }

    private void checkBase(final String errorBody) throws Exception {
        BaseResponse error = null;
        try {
            error = new Gson().fromJson(errorBody, BaseResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (error != null && !TextUtils.isEmpty(error.getErrorMessage())) {
            throw new MCheckException(error.getErrorCode(), error.getErrorMessage());
        }
    }

    private class RequestValidationBody {
        private String phone;
        private String type;
        private String msg;

        private String getPhone() {
            return phone;
        }

        private void setPhone(String phone) {
            this.phone = phone;
        }

        private String getType() {
            return type;
        }

        private void setType(String type) {
            this.type = type;
        }

        private String getMsg() {
            return msg;
        }

        private void setMsg(String msg) {
            this.msg = msg;
        }
    }

    private class VerifyValidationBody {
        private String id;
        private String pin;

        private String getId() {
            return id;
        }

        private void setId(String id) {
            this.id = id;
        }

        private String getPin() {
            return pin;
        }

        private void setPin(String pin) {
            this.pin = pin;
        }
    }

    private class StatusValidationBody {
        private String id;

        private String getId() {
            return id;
        }

        private void setId(String id) {
            this.id = id;
        }
    }
}