package kz.kazinfoteh.mcheck_demo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kz.kazinfoteh.mcheck_sdk.MCheck;
import kz.kazinfoteh.mcheck_sdk.MCheckCallback;
import kz.kazinfoteh.mcheck_sdk.MCheckException;
import kz.kazinfoteh.mcheck_sdk.RequestValidationResult;
import kz.kazinfoteh.mcheck_sdk.ValidationStatusResult;
import kz.kazinfoteh.mcheck_sdk.ValidationType;
import kz.kazinfoteh.mcheck_sdk.VerifyValidationResult;
import mehdi.sakout.dynamicbox.DynamicBox;

public class MainActivity extends AppCompatActivity {
    private DynamicBox mDynamicBox;
    private MCheck mMCheck;
    private EditText mPhoneNumberInput;
    private TextView mRequestIdLabel;
    private EditText mPinInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(kz.kazinfoteh.mcheck_demo.R.layout.activity_main);

        final Toolbar toolbar = findViewById(kz.kazinfoteh.mcheck_demo.R.id.toolbar);
        setSupportActionBar(toolbar);

        mPhoneNumberInput = findViewById(kz.kazinfoteh.mcheck_demo.R.id.phoneNumber);
        mRequestIdLabel = findViewById(kz.kazinfoteh.mcheck_demo.R.id.requestID);
        mPinInput = findViewById(kz.kazinfoteh.mcheck_demo.R.id.pin);

        // validate button
        final View requestValidation = findViewById(kz.kazinfoteh.mcheck_demo.R.id.requestValidation);
        requestValidation.setOnClickListener(mRequestValidation);

        // verify button
        final View verifyValidation = findViewById(kz.kazinfoteh.mcheck_demo.R.id.verifyValidation);
        verifyValidation.setOnClickListener(mVerifyValidation);

        // check status button
        final View validationStatus = findViewById(kz.kazinfoteh.mcheck_demo.R.id.validationStatus);
        validationStatus.setOnClickListener(mCheckValidationStatus);

        mDynamicBox = new DynamicBox(this, findViewById(kz.kazinfoteh.mcheck_demo.R.id.root));

        final String token = "qxfmvtyhljpsoyukxdtoejky"; //automatically generated token from https://isms.center

        mMCheck = new MCheck(token);
    }

    private View.OnClickListener mRequestValidation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismissKeyboard();

            if (TextUtils.isEmpty(mPhoneNumberInput.getText())) {
                Toast.makeText(MainActivity.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            // [:phone] phone number
            // [:pin] validation code

            final String smsBody = "Your validation code: [:pin]"; // smsBody is optional param, and maybe is null

            mDynamicBox.showLoadingLayout(); //show loading view

            mMCheck.requestValidation(mPhoneNumberInput.getText().toString(), ValidationType.SMS,
                    smsBody, new MCheckCallback<RequestValidationResult>() {
                        @Override
                        public void onResponse(RequestValidationResult response, Exception error) {
                            mDynamicBox.hideAll(); //hide loading view

                            if (error != null) {
                                if (error instanceof MCheckException) {
                                    final MCheckException serverError = (MCheckException) error;

                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("Error")
                                            .setMessage("code: " + serverError.getCode() + "\nmessage: " + serverError.getMessage())
                                            .setCancelable(true)
                                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            }).create().show();
                                } else {
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("Error")
                                            .setMessage(error.getMessage())
                                            .setCancelable(true)
                                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            }).create().show();
                                }

                                return;
                            }

                            mRequestIdLabel.setText(response.getId());

                            Toast.makeText(MainActivity.this, "Success, request ID: " + response.getId(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    };

    private View.OnClickListener mVerifyValidation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismissKeyboard();

            if (TextUtils.isEmpty(mPinInput.getText())) {
                Toast.makeText(MainActivity.this, "Enter pin code", Toast.LENGTH_SHORT).show();
                return;
            }

            mDynamicBox.showLoadingLayout(); //show loading view

            final String requestID = mRequestIdLabel.getText().toString(); // it's coming from request validation
            final String pinCode = mPinInput.getText().toString();

            mMCheck.verifyValidation(requestID, pinCode, new MCheckCallback<VerifyValidationResult>() {
                @Override
                public void onResponse(VerifyValidationResult response, Exception error) {
                    mDynamicBox.hideAll(); //hide loading view

                    if (error != null) {
                        if (error instanceof MCheckException) {
                            final MCheckException serverError = (MCheckException) error;

                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Error")
                                    .setMessage("code: " + serverError.getCode() + "\nmessage: " + serverError.getMessage())
                                    .setCancelable(true)
                                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).create().show();
                        } else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Error")
                                    .setMessage(error.getMessage())
                                    .setCancelable(true)
                                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).create().show();
                        }

                        return;
                    }

                    Toast.makeText(MainActivity.this, "Validated: "
                            + String.valueOf(response.isValidated()), Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private View.OnClickListener mCheckValidationStatus = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismissKeyboard();

            mDynamicBox.showLoadingLayout(); //show loading view

            final String requestID = mRequestIdLabel.getText().toString(); // it's coming from request validation

            mMCheck.checkValidationStatus(requestID, new MCheckCallback<ValidationStatusResult>() {
                @Override
                public void onResponse(ValidationStatusResult response, Exception error) {
                    mDynamicBox.hideAll(); //hide loading view

                    if (error != null) {
                        if (error instanceof MCheckException) {
                            final MCheckException serverError = (MCheckException) error;

                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Error")
                                    .setMessage("code: " + serverError.getCode() + "\nmessage: " + serverError.getMessage())
                                    .setCancelable(true)
                                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).create().show();
                        } else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Error")
                                    .setMessage(error.getMessage())
                                    .setCancelable(true)
                                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).create().show();
                        }

                        return;
                    }

                    Toast.makeText(MainActivity.this, "Validation status: "
                            + String.valueOf(response.isValidated()), Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    // close keyboard
    private void dismissKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(mPhoneNumberInput.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mPinInput.getWindowToken(), 0);
        }
    }
}