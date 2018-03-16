package kz.kazinfoteh.mcheck_sdk;

/**
 * Created by Yelnar Shopanov
 * on 12.03.2018.
 */

public class MCheck {
    private final String mToken;

    /**
     * Init constructor.
     *
     * @param token automatically generated token from https://isms.center
     */
    public MCheck(final String token) {
        this.mToken = token;
    }

    /**
     * The Request Validation API lets you requesting a validation using one of the available methods:
     * SMS - Send a message to the user with a validation code that he has to enter to validate his mobile number.
     *
     * @param phone    the number that has to be validated
     * @param type     one of the following value: sms
     * @param message  sms message body
     * @param callback result
     */
    public void requestValidation(final String phone, final ValidationType type, final String message,
                                  final MCheckCallback<RequestValidationResult> callback) {
        ServerHelper.getInstance().requestValidation(mToken, phone, type, message, callback);
    }

    /**
     * The Verify Pin API lets you to match a validation request with a validation pin inserted by a user.
     * SMS validation process.
     *
     * @param requestId validation request id
     * @param pin       the pin number inserted by user
     * @param callback  result
     */
    public void verifyValidation(final String requestId, final String pin,
                                 final MCheckCallback<VerifyValidationResult> callback) {
        ServerHelper.getInstance().verifyValidation(mToken, requestId, pin, callback);
    }

    /**
     * The Validation status API let you to check the validation status of a request.
     *
     * @param requestId validation request id
     * @param callback  result
     */
    public void checkValidationStatus(final String requestId,
                                      final MCheckCallback<ValidationStatusResult> callback) {
        ServerHelper.getInstance().statusValidation(mToken, requestId, callback);
    }
}