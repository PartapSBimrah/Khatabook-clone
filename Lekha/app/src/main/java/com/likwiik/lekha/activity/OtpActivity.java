package com.likwiik.lekha.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.likwiik.lekha.R;
import com.likwiik.lekha.model.LanguageManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends BaseActivity {

    ImageView redirectback;
    TextView number1, number2, number3, number4, number5, number6, user_number, resend_timer;
    String otp, phone_number,msg;
    int randomNumber, Min = 100000, Max = 999999;
    private String verificationId;
    private TextView tvOtpInstruction;
    private ProgressBar loadingProgressBar;

    private Button btnVerify;

    private static final String[] paths = {"Light", "Dark"};
    private static final String[] languages = {"English", "French", "Hindi", "Russian", "Spanish", "Arabic"};
    private String DARK_MODE = "DARK_MODE";
    

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                setCodeToEditTexts(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean strPref = false;
        SharedPreferences shf = this.getSharedPreferences("config", MODE_PRIVATE);
        strPref = shf.getBoolean(DARK_MODE, false);

        if(strPref)
        {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
            setContentView(R.layout.activity_otp);

            redirectback = findViewById(R.id.redirectback);
            user_number = findViewById(R.id.user_number);
            resend_timer = findViewById(R.id.resend_timer);

            phone_number = getIntent().getStringExtra("User_number");
            user_number.setText(phone_number);
            mAuth = FirebaseAuth.getInstance();
            tvOtpInstruction = findViewById(R.id.otp);
            number1 = findViewById(R.id.number1);
            number2 = findViewById(R.id.number2);
            number3 = findViewById(R.id.number3);
            number4 = findViewById(R.id.number4);
            number5 = findViewById(R.id.number5);
            number6 = findViewById(R.id.number6);
            loadingProgressBar = findViewById(R.id.pbLoading);
            btnVerify = findViewById(R.id.verify);
            String promptMessage = getResources().getString(R.string.otp1);
            tvOtpInstruction.setText(Html.fromHtml(promptMessage));
            btnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String code = "";

                    if (TextUtils.isEmpty(number1.getText().toString())) {
                        Toast.makeText(OtpActivity.this, "Please enter full code ", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else {
                        code += number1.getText().toString();
                    }

                    if (TextUtils.isEmpty(number2.getText().toString())) {
                        Toast.makeText(OtpActivity.this, "Please enter full code ", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else {
                        code += number2.getText().toString();
                    }


                    if (TextUtils.isEmpty(number3.getText().toString())) {
                        Toast.makeText(OtpActivity.this, "Please enter full code ", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else {
                        code += number3.getText().toString();
                    }


                    if (TextUtils.isEmpty(number4.getText().toString())) {
                        Toast.makeText(OtpActivity.this, "Please enter full code ", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else {
                        code += number4.getText().toString();
                    }


                    if (TextUtils.isEmpty(number5.getText().toString())) {
                        Toast.makeText(OtpActivity.this, "Please enter full code ", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else {
                        code += number5.getText().toString();
                    }


                    if (TextUtils.isEmpty(number6.getText().toString())) {
                        Toast.makeText(OtpActivity.this, "Please enter full code ", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else {
                        code += number6.getText().toString();
                    }


                    verifyCode(code);

                }
            });


            number1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if (editable != null) {
                        if (editable.length() == 1)
                            number2.requestFocus();
                    }
                }
            });


            number2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable != null) {
                        if (editable.length() == 1)
                            number3.requestFocus();
                    }
                }
            });


            number3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable != null) {
                        if (editable.length() == 1)
                            number4.requestFocus();
                    }
                }
            });


            number4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable != null) {
                        if (editable.length() == 1)
                            number5.requestFocus();
                    }
                }
            });


            number5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable != null) {
                        if (editable.length() == 1)
                            number6.requestFocus();
                    }
                }
            });



            redirectback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent new_intent = new Intent(OtpActivity.this, RegisterActivity.class);
                    startActivity(new_intent);
                    finish();
                }
            });

            sendVerificationCode(phone_number);
            MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
            spinner.setItems(paths);
            spinner.setSelectedIndex(1);
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    switch (position) {
                        case 0:
                            SharedPreferences saveVerified = getSharedPreferences("config", MODE_PRIVATE);
                            saveVerified.edit().putBoolean(DARK_MODE, false).apply();
                            recreate();
                            break;
                        case 1:
                            SharedPreferences saveVerified2 = getSharedPreferences("config", MODE_PRIVATE);
                            saveVerified2.edit().putBoolean(DARK_MODE, true).apply();
                            recreate();
                            break;
                    }

                }
            });
            spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
                @Override public void onNothingSelected(MaterialSpinner spinner) {
                }
            });
            MaterialSpinner spinner2 = (MaterialSpinner) findViewById(R.id.spinner2);
            spinner2.setItems(languages);
            spinner2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                    switch (position) {
                        case 0:
                            LanguageManager.setNewLocale(OtpActivity.this, LanguageManager.LANGUAGE_KEY_ENGLISH);
                            recreate();
                            break;
                        case 1:
                            LanguageManager.setNewLocale(OtpActivity.this, LanguageManager.LANGUAGE_KEY_FRENCH);
                            recreate();
                            break;
                        case 2:
                            LanguageManager.setNewLocale(OtpActivity.this, LanguageManager.LANGUAGE_KEY_HINDI);
                            recreate();
                            break;
                        case 3:
                            LanguageManager.setNewLocale(OtpActivity.this, LanguageManager.LANGUAGE_KEY_RUSSIA);
                            recreate();
                            break;
                        case 4:
                            LanguageManager.setNewLocale(OtpActivity.this, LanguageManager.LANGUAGE_KEY_SPANISH);
                            recreate();
                            break;
                        case 5:
                            LanguageManager.setNewLocale(OtpActivity.this, LanguageManager.LANGUAGE_KEY_ARABIC);
                            recreate();
                            break;
                    }

                }
            });
            spinner2.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
                @Override public void onNothingSelected(MaterialSpinner spinner) {
                }
            });

        }
        else {
            setTheme(R.style.AppTheme_Light_NoActionBar);
            setContentView(R.layout.activity_otp);

            redirectback = findViewById(R.id.redirectback);
            user_number = findViewById(R.id.user_number);
            resend_timer = findViewById(R.id.resend_timer);

            phone_number = getIntent().getStringExtra("User_number");
            user_number.setText(phone_number);
            mAuth = FirebaseAuth.getInstance();
            tvOtpInstruction = findViewById(R.id.otp);
            number1 = findViewById(R.id.number1);
            number2 = findViewById(R.id.number2);
            number3 = findViewById(R.id.number3);
            number4 = findViewById(R.id.number4);
            number5 = findViewById(R.id.number5);
            number6 = findViewById(R.id.number6);
            loadingProgressBar = findViewById(R.id.pbLoading);
            btnVerify = findViewById(R.id.verify);
            String promptMessage = getResources().getString(R.string.otp1);
            tvOtpInstruction.setText(Html.fromHtml(promptMessage));
            btnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String code = "";

                    if (TextUtils.isEmpty(number1.getText().toString())) {
                        Toast.makeText(OtpActivity.this, "Please enter full code ", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else {
                        code += number1.getText().toString();
                    }

                    if (TextUtils.isEmpty(number2.getText().toString())) {
                        Toast.makeText(OtpActivity.this, "Please enter full code ", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else {
                        code += number2.getText().toString();
                    }


                    if (TextUtils.isEmpty(number3.getText().toString())) {
                        Toast.makeText(OtpActivity.this, "Please enter full code ", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else {
                        code += number3.getText().toString();
                    }


                    if (TextUtils.isEmpty(number4.getText().toString())) {
                        Toast.makeText(OtpActivity.this, "Please enter full code ", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else {
                        code += number4.getText().toString();
                    }


                    if (TextUtils.isEmpty(number5.getText().toString())) {
                        Toast.makeText(OtpActivity.this, "Please enter full code ", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else {
                        code += number5.getText().toString();
                    }


                    if (TextUtils.isEmpty(number6.getText().toString())) {
                        Toast.makeText(OtpActivity.this, "Please enter full code ", Toast.LENGTH_LONG)
                                .show();
                        return;
                    } else {
                        code += number6.getText().toString();
                    }


                    verifyCode(code);

                }
            });


            number1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if (editable != null) {
                        if (editable.length() == 1)
                            number2.requestFocus();
                    }
                }
            });


            number2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable != null) {
                        if (editable.length() == 1)
                            number3.requestFocus();
                    }
                }
            });


            number3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable != null) {
                        if (editable.length() == 1)
                            number4.requestFocus();
                    }
                }
            });


            number4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable != null) {
                        if (editable.length() == 1)
                            number5.requestFocus();
                    }
                }
            });


            number5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable != null) {
                        if (editable.length() == 1)
                            number6.requestFocus();
                    }
                }
            });



            redirectback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent new_intent = new Intent(OtpActivity.this, RegisterActivity.class);
                    startActivity(new_intent);
                    finish();
                }
            });

            sendVerificationCode(phone_number);
            MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
            spinner.setItems(paths);
            spinner.setSelectedIndex(0);
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    switch (position) {
                        case 0:
                            SharedPreferences saveVerified = getSharedPreferences("config", MODE_PRIVATE);
                            saveVerified.edit().putBoolean(DARK_MODE, false).apply();
                            recreate();
                            break;
                        case 1:
                            SharedPreferences saveVerified2 = getSharedPreferences("config", MODE_PRIVATE);
                            saveVerified2.edit().putBoolean(DARK_MODE, true).apply();
                            recreate();
                            break;

                    }

                }
            });
            spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
                @Override public void onNothingSelected(MaterialSpinner spinner) {
                }
            });

            MaterialSpinner spinner2 = (MaterialSpinner) findViewById(R.id.spinner2);
            spinner2.setItems(languages);
            //  spinner2.setSelectedIndex(1);
            spinner2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                    switch (position) {
                        case 0:
                            LanguageManager.setNewLocale(OtpActivity.this, LanguageManager.LANGUAGE_KEY_ENGLISH);
                            recreate();
                            break;
                        case 1:
                            LanguageManager.setNewLocale(OtpActivity.this, LanguageManager.LANGUAGE_KEY_FRENCH);
                            recreate();
                            break;
                        case 2:
                            LanguageManager.setNewLocale(OtpActivity.this, LanguageManager.LANGUAGE_KEY_HINDI);
                            recreate();
                            break;
                        case 3:
                            LanguageManager.setNewLocale(OtpActivity.this, LanguageManager.LANGUAGE_KEY_RUSSIA);
                            recreate();
                            break;
                        case 4:
                            LanguageManager.setNewLocale(OtpActivity.this, LanguageManager.LANGUAGE_KEY_SPANISH);
                            recreate();
                            break;
                        case 5:
                            LanguageManager.setNewLocale(OtpActivity.this, LanguageManager.LANGUAGE_KEY_ARABIC);
                            recreate();
                            break;

                    }

                }
            });
            spinner2.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
                @Override public void onNothingSelected(MaterialSpinner spinner) {
                }
            });


        }


    }


    /**
     * Initiates the process of sending the verification code on the mobile number entered by the user in previous activity
     */
    private void sendVerificationCode(String number) {
        loadingProgressBar.setVisibility(View.VISIBLE);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


      //  PhoneAuthProvider.verifyPhoneNumber(
           //     number,
          //      60,
         //       TimeUnit.SECONDS,
        //        TaskExecutors.MAIN_THREAD,
       //         mCallBack
      //  );

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(OtpActivity.this, PasscodeRegActivity.class);
                            intent.putExtra("User_number",phone_number);
                            startActivity(intent);
                            finish();
                           // Toast.makeText(getApplicationContext(), "Phone Number Verify", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(OtpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                        loadingProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }


    private void setCodeToEditTexts(String code) {

        //Assuming code length is 6
        if (code.length() == 6) {

            number1.setText(code.substring(0, 1));
            number2.setText(code.substring(1, 2));
            number3.setText(code.substring(2, 3));
            number4.setText(code.substring(3, 4));
            number5.setText(code.substring(4, 5));
            number6.setText(code.substring(5, 6));

        }

    }
    


}