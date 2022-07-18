package com.likwiik.lekha.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.likwiik.lekha.db.DatabaseHelper;
import com.likwiik.lekha.R;
import com.likwiik.lekha.model.LanguageManager;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileActivity extends BaseActivity {

    TextView user_name, user_businessname, user_location, user_number;
    String phone_number, name, businessname, location, passcode;
    ImageView user_image;
    Button redirect;
    final int REQUEST_CODE_GALLERY = 999;
    private static final String[] paths = {"Light", "Dark"};
    private static final String[] languages = {"English", "French", "Hindi", "Russian", "Spanish", "Arabic"};
    private String DARK_MODE = "DARK_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean strPref = false;
        SharedPreferences shf = this.getSharedPreferences("config", MODE_PRIVATE);
        strPref = shf.getBoolean(DARK_MODE, false);

        if(strPref)
        {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
            setContentView(R.layout.activity_profile);

            user_name = findViewById(R.id.user_name);
            user_businessname = findViewById(R.id.user_businessname);
            user_location = findViewById(R.id.user_location);
            user_number = findViewById(R.id.user_number);
            redirect = findViewById(R.id.redirect);
            user_image = findViewById(R.id.user_image);

            phone_number = getIntent().getStringExtra("User_number");
            passcode = getIntent().getStringExtra("Passcode");
            user_number.setText(phone_number);

            user_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    name = user_name.getText().toString();
                    if (name.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                        Resources res = getResources();
                        String uri = "@drawable/" + name.substring(0, 1).toLowerCase();
                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        Drawable drawable = res.getDrawable(imageResource);
                          user_image.setImageDrawable(drawable);
                    }
                }
            });

            redirect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    name = user_name.getText().toString();
                    businessname = user_businessname.getText().toString();
                    location = user_location.getText().toString();
                    if (!name.isEmpty() && !businessname.isEmpty() && !location.isEmpty() && name.matches("^[a-zA-Z]{1}[a-zA-Z ]*$") && businessname.matches("^[a-zA-Z]{1}[a-zA-Z ]*$") && location.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                        user_name.setError(null);
                        user_businessname.setError(null);
                        user_location.setError(null);
                        Resources res = getResources();
                        String uri = "@drawable/" + name.substring(0, 1).toLowerCase();
                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        Drawable drawable = res.getDrawable(imageResource);
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] image = stream.toByteArray();
                        DatabaseHelper myDB = new DatabaseHelper(ProfileActivity.this);
                        if (myDB.storeNewUserData(phone_number, name, passcode, businessname, location, image)) {
                            SharedPreferences SharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = SharedPreferences.edit();
                            myEdit.putBoolean("first_time_login", false);
                            myEdit.putBoolean("is_logged_in", false);
                            myEdit.commit();
                             Toast.makeText(ProfileActivity.this, "Account Created, Login it !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Toast.makeText(formdashboard.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (name.isEmpty()) {
                            user_name.setError("Field cannot be empty");
                        } else if (!name.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                            user_name.setError("Require Character and Whitespace Only");
                        } else {
                            user_name.setError(null);
                        }
                        if (businessname.isEmpty()) {
                            user_businessname.setError("Field cannot be empty");
                        } else if (!businessname.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                            user_businessname.setError("Require Character and Whitespace Only");
                        } else {
                            user_businessname.setError(null);
                        }
                        if (location.isEmpty()) {
                            user_location.setError("Field cannot be empty");
                        } else if (!location.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                            user_location.setError("Require Character and Whitespace Only");
                        } else {
                            user_location.setError(null);
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
                            LanguageManager.setNewLocale(ProfileActivity.this, LanguageManager.LANGUAGE_KEY_ENGLISH);
                            recreate();
                            break;
                        case 1:
                            LanguageManager.setNewLocale(ProfileActivity.this, LanguageManager.LANGUAGE_KEY_FRENCH);
                            recreate();
                            break;
                        case 2:
                            LanguageManager.setNewLocale(ProfileActivity.this, LanguageManager.LANGUAGE_KEY_HINDI);
                            recreate();
                            break;
                        case 3:
                            LanguageManager.setNewLocale(ProfileActivity.this, LanguageManager.LANGUAGE_KEY_RUSSIA);
                            recreate();
                            break;
                        case 4:
                            LanguageManager.setNewLocale(ProfileActivity.this, LanguageManager.LANGUAGE_KEY_SPANISH);
                            recreate();
                            break;
                        case 5:
                            LanguageManager.setNewLocale(ProfileActivity.this, LanguageManager.LANGUAGE_KEY_ARABIC);
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
            setContentView(R.layout.activity_profile);

            user_name = findViewById(R.id.user_name);
            user_businessname = findViewById(R.id.user_businessname);
            user_location = findViewById(R.id.user_location);
            user_number = findViewById(R.id.user_number);
            redirect = findViewById(R.id.redirect);
            user_image = findViewById(R.id.user_image);
            phone_number = getIntent().getStringExtra("User_number");
            passcode = getIntent().getStringExtra("Passcode");
            user_number.setText(phone_number);

            user_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    name = user_name.getText().toString();
                    if (name.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                        Resources res = getResources();
                        String uri = "@drawable/" + name.substring(0, 1).toLowerCase();
                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        Drawable drawable = res.getDrawable(imageResource);
                          user_image.setImageDrawable(drawable);
                    }
                }
            });

            redirect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    name = user_name.getText().toString();
                    businessname = user_businessname.getText().toString();
                    location = user_location.getText().toString();
                    if (!name.isEmpty() && !businessname.isEmpty() && !location.isEmpty() && name.matches("^[a-zA-Z]{1}[a-zA-Z ]*$") && businessname.matches("^[a-zA-Z]{1}[a-zA-Z ]*$") && location.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                        user_name.setError(null);
                        user_businessname.setError(null);
                        user_location.setError(null);
                        Resources res = getResources();
                        String uri = "@drawable/" + name.substring(0, 1).toLowerCase();
                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        Drawable drawable = res.getDrawable(imageResource);
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] image = stream.toByteArray();
                        DatabaseHelper myDB = new DatabaseHelper(ProfileActivity.this);
                        if (myDB.storeNewUserData(phone_number, name, passcode, businessname, location, image)) {
                            SharedPreferences SharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = SharedPreferences.edit();
                            myEdit.putBoolean("first_time_login", false);
                            myEdit.putBoolean("is_logged_in", false);
                            myEdit.commit();
                            // Toast.makeText(formdashboard.this, "Account Created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Toast.makeText(formdashboard.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (name.isEmpty()) {
                            user_name.setError("Field cannot be empty");
                        } else if (!name.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                            user_name.setError("Require Character and Whitespace Only");
                        } else {
                            user_name.setError(null);
                        }
                        if (businessname.isEmpty()) {
                            user_businessname.setError("Field cannot be empty");
                        } else if (!businessname.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                            user_businessname.setError("Require Character and Whitespace Only");
                        } else {
                            user_businessname.setError(null);
                        }
                        if (location.isEmpty()) {
                            user_location.setError("Field cannot be empty");
                        } else if (!location.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                            user_location.setError("Require Character and Whitespace Only");
                        } else {
                            user_location.setError(null);
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
                            LanguageManager.setNewLocale(ProfileActivity.this, LanguageManager.LANGUAGE_KEY_ENGLISH);
                            recreate();
                            break;
                        case 1:
                            LanguageManager.setNewLocale(ProfileActivity.this, LanguageManager.LANGUAGE_KEY_FRENCH);
                            recreate();
                            break;
                        case 2:
                            LanguageManager.setNewLocale(ProfileActivity.this, LanguageManager.LANGUAGE_KEY_HINDI);
                            recreate();
                            break;
                        case 3:
                            LanguageManager.setNewLocale(ProfileActivity.this, LanguageManager.LANGUAGE_KEY_RUSSIA);
                            recreate();
                            break;
                        case 4:
                            LanguageManager.setNewLocale(ProfileActivity.this, LanguageManager.LANGUAGE_KEY_SPANISH);
                            recreate();
                            break;
                        case 5:
                            LanguageManager.setNewLocale(ProfileActivity.this, LanguageManager.LANGUAGE_KEY_ARABIC);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
               // Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                user_image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            user_location.setText(place.getName());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}