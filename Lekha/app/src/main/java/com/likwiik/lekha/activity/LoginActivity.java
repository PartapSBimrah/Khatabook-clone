package com.likwiik.lekha.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.likwiik.lekha.db.DatabaseHelper;
import com.likwiik.lekha.R;
import com.likwiik.lekha.model.LanguageManager;
import com.hbb20.CountryCodePicker;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class LoginActivity extends BaseActivity {
    TextView error_msg;
    EditText phone_number;
    Button redirectpasscode;
    Button redirectlink;
    Intent intent;
    String database_passcode,database_id;
    CountryCodePicker ccp;
    public static final String MOBILE_NUMBER = "mobile_number";

    private static final String[] paths = {"Light", "Dark"};
    private static final String[] languages = {"English", "French", "Hindi", "Russian", "Spanish", "Arabic"};
    private String DARK_MODE = "DARK_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean strPref = false;
        SharedPreferences shf = this.getSharedPreferences("config", MODE_PRIVATE);
        strPref = shf.getBoolean(DARK_MODE, false);

        if (strPref) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
            setContentView(R.layout.activity_login);
            // getSupportActionBar().hide();
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

            ccp = (CountryCodePicker) findViewById(R.id.ccp);

            redirectlink = findViewById(R.id.redirectlink);
            redirectpasscode = findViewById(R.id.redirectpasscode);
            phone_number = findViewById(R.id.phone_number);
            error_msg = findViewById(R.id.error_msg);

            redirectlink.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            redirectpasscode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone_no = phone_number.getText().toString();
                    if (!phone_no.isEmpty() && phone_no.length() == 10 && phone_no.matches("^[0-9]{10}")) {
                        final String complete_phone_no = ccp.getSelectedCountryCodeWithPlus() + phone_no;
                        DatabaseHelper myDB = new DatabaseHelper(LoginActivity.this);
                        Cursor cursor = myDB.check_usernumber_exist(complete_phone_no, 1);
                        if (cursor.getCount() == 0) {
                            // Toast.makeText(login.this, "No such user exist", Toast.LENGTH_SHORT).show();
                            intent = new Intent(LoginActivity.this, OtpActivity.class);
                            intent.putExtra(MOBILE_NUMBER, complete_phone_no);
                        } else {
                            while (cursor.moveToNext()) {
                                database_id = cursor.getString(0);
                                database_passcode = cursor.getString(3);
                            }
                            intent = new Intent(LoginActivity.this, PasscodeActivity.class);
                            intent.putExtra("Id", database_id);
                            intent.putExtra("Passcode", database_passcode);
                        }
                        intent.putExtra("User_number", complete_phone_no);
                        startActivity(intent);
                        finish();


                    } else {
                        phone_number.requestFocus();
                        if (phone_no.isEmpty()) {
                            error_msg.setText("Mobile Number is required");
                        } else if (phone_no.length() != 10) {
                            error_msg.setText("Mobile Number is not valid");
                        } else {
                            phone_number.setError("Require only 10 digit");
                        }
                    }

                }
            });

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
                            LanguageManager.setNewLocale(LoginActivity.this, LanguageManager.LANGUAGE_KEY_ENGLISH);
                            recreate();
                            break;
                        case 1:
                            LanguageManager.setNewLocale(LoginActivity.this, LanguageManager.LANGUAGE_KEY_FRENCH);
                            recreate();
                            break;
                        case 2:
                            LanguageManager.setNewLocale(LoginActivity.this, LanguageManager.LANGUAGE_KEY_HINDI);
                            recreate();
                            break;
                        case 3:
                            LanguageManager.setNewLocale(LoginActivity.this, LanguageManager.LANGUAGE_KEY_RUSSIA);
                            recreate();
                            break;
                        case 4:
                            LanguageManager.setNewLocale(LoginActivity.this, LanguageManager.LANGUAGE_KEY_SPANISH);
                            recreate();
                            break;
                        case 5:
                            LanguageManager.setNewLocale(LoginActivity.this, LanguageManager.LANGUAGE_KEY_ARABIC);
                            recreate();
                            break;
                    }

                }
            });
            spinner2.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
                @Override public void onNothingSelected(MaterialSpinner spinner) {
                }
            });

        } else
        {
            setTheme(R.style.AppTheme_Light_NoActionBar);

            setContentView(R.layout.activity_login);
            // getSupportActionBar().hide();
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

            ccp = (CountryCodePicker) findViewById(R.id.ccp);

            redirectlink = findViewById(R.id.redirectlink);
            redirectpasscode = findViewById(R.id.redirectpasscode);
            phone_number = findViewById(R.id.phone_number);
            error_msg = findViewById(R.id.error_msg);

            redirectlink.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            redirectpasscode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone_no = phone_number.getText().toString();
                    if (!phone_no.isEmpty() && phone_no.length() == 10 && phone_no.matches("^[0-9]{10}")) {
                        final String complete_phone_no = ccp.getSelectedCountryCodeWithPlus() + phone_no;
                        DatabaseHelper myDB = new DatabaseHelper(LoginActivity.this);
                        Cursor cursor = myDB.check_usernumber_exist(complete_phone_no, 1);
                        if (cursor.getCount() == 0) {
                            // Toast.makeText(login.this, "No such user exist", Toast.LENGTH_SHORT).show();
                            intent = new Intent(LoginActivity.this, OtpActivity.class);
                            intent.putExtra(MOBILE_NUMBER, complete_phone_no);
                        } else {
                            while (cursor.moveToNext()) {
                                database_id = cursor.getString(0);
                                database_passcode = cursor.getString(3);
                            }
                            intent = new Intent(LoginActivity.this, PasscodeActivity.class);
                            intent.putExtra("Id", database_id);
                            intent.putExtra("Passcode", database_passcode);
                        }
                        intent.putExtra("User_number", complete_phone_no);
                        startActivity(intent);
                        finish();


                    } else {
                        phone_number.requestFocus();
                        if (phone_no.isEmpty()) {
                            error_msg.setText("Mobile Number is required");
                        } else if (phone_no.length() != 10) {
                            error_msg.setText("Mobile Number is not valid");
                        } else {
                            phone_number.setError("Require only 10 digit");
                        }
                    }

                }
            });

            
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
                            LanguageManager.setNewLocale(LoginActivity.this, LanguageManager.LANGUAGE_KEY_ENGLISH);
                            recreate();
                            break;
                        case 1:
                            LanguageManager.setNewLocale(LoginActivity.this, LanguageManager.LANGUAGE_KEY_FRENCH);
                            recreate();
                            break;
                        case 2:
                            LanguageManager.setNewLocale(LoginActivity.this, LanguageManager.LANGUAGE_KEY_HINDI);
                            recreate();
                            break;
                        case 3:
                            LanguageManager.setNewLocale(LoginActivity.this, LanguageManager.LANGUAGE_KEY_RUSSIA);
                            recreate();
                            break;
                        case 4:
                            LanguageManager.setNewLocale(LoginActivity.this, LanguageManager.LANGUAGE_KEY_SPANISH);
                            recreate();
                            break;
                        case 5:
                            LanguageManager.setNewLocale(LoginActivity.this, LanguageManager.LANGUAGE_KEY_ARABIC);
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

}