# mCheck SDK 

mCheck SDK for Android let you integrate the mobile phone number validation API in mobile devices.

In order to test the sample you need to change the secret key.

## Setup

#### Gradle
`compile 'com.github.kazinfoteh:mcheck_sdk:0.0.1'`

#### Maven
```
<dependency>
    <groupId>com.github.kazinfoteh</groupId>
    <artifactId>mcheck_sdk</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Sample Usage

**Init SDK**
```java
//automatically generated token from https://isms.center
final String token = "YOUR_TOKEN";
final MCheck mcheck = new MCheck(token);
```
**Request validation**
```java
// [:phone] phone number
// [:pin] validation code
// smsBody is optional param, and maybe is null
final String smsBody = "Your validation code: [:pin]"; 

mcheck.requestValidation(token, ValidationType.SMS, smsBody, new MCheckCallback<RequestValidationResult>() {
    @Override
    public void onResponse(RequestValidationResult response, Exception error) {
        if (error != null) {
            if (error instanceof MCheckException) {
                final MCheckException serverError = (MCheckException) error;
                Log.e(TAG, "error code: " + serverError.getCode() + " error message: " + serverError.getMessage());
            } else {
                Log.e(TAG, " error message: " + error.getMessage());
            }

            return;
        }

        Log.d(TAG, "Success, request ID: " + response.getId());
    }
});
```
**Verify Pin**
```java
final String requestID = ""; //request id received from mcheck.requestValidation - response.getId()
final String pin = ""; //pin code to check

mcheck.verifyValidation(requestID, pin, new MCheckCallback<VerifyValidationResult>() {
    @Override
    public void onResponse(VerifyValidationResult response, Exception error) {
        if (error != null) {
            if (error instanceof MCheckException) {
                final MCheckException serverError = (MCheckException) error;
                Log.e(TAG, "error code: " + serverError.getCode() + " error message: " + serverError.getMessage());
            } else {
                Log.e(TAG, " error message: " + error.getMessage());
            }

            return;
        }

        Log.d(TAG, "Validated: " + String.valueOf(response.isValidated()));
    }
});
```
**Validation Status**
```java
final String requestID = ""; //request id received from mcheck.requestValidation - response.getId()

mcheck.checkValidationStatus(requestID, new MCheckCallback<ValidationStatusResult>() {
    @Override
    public void onResponse(ValidationStatusResult response, Exception error) {
        if (error != null) {
            if (error instanceof MCheckException) {
                final MCheckException serverError = (MCheckException) error;
                Log.e(TAG, "error code: " + serverError.getCode() + " error message: " + serverError.getMessage());
            } else {
                Log.e(TAG, " error message: " + error.getMessage());
            }

            return;
        }
        
        Log.d(TAG, "Validation status: " + String.valueOf(response.isValidated()));
    }
});
```