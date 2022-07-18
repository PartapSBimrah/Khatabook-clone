package com.likwiik.lekha.activity;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

//import com.likwiik.lekha.BuildConfig;
import com.jaredrummler.materialspinner.BuildConfig;
import com.likwiik.lekha.db.DatabaseHelper;
import com.likwiik.lekha.R;
import com.likwiik.lekha.fragment.CustomerFragment;
import com.likwiik.lekha.fragment.HomeFragment;
import com.likwiik.lekha.fragment.TransactionFragment;
import com.likwiik.lekha.model.LanguageManager;
//import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormatSymbols;

public class MainActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    private InterstitialAd mInterstitialAd;
   AdRequest adRequest;
  //  DrawerLayout drawerLayout;
  //  NavigationView side_navigation;
    ImageView sidebar_icon,customer_image;
    TextView status_bar_text,customer_business_name,customer_name,transaction_date;
    EditText contact_number;
    String user_id,user_business_name,user_name;
    MaterialCardView edit_button;
    byte[] user_image;

    private static final String[] paths = {"Light", "Dark"};
    private static final String[] languages = {"English", "French", "Hindi", "Russian", "Spanish", "Arabic"};
    private String DARK_MODE = "DARK_MODE";
    

    public void transaction_chat(View view){
        MaterialCardView card = (MaterialCardView) view;
        String a = card.getTag().toString();
        Intent intent = new Intent(getApplicationContext(), TransactionCActivity.class);
        intent.putExtra("Friend_id",a);
        startActivity(intent);
    }
    public void transaction_summary(View view){
        String transaction_id,transaction_sender_id = null,transaction_receiver_id = null,transaction_amount_text = null,transaction_remarks_text = null,transaction_date_text = null,sender_id,customer_phone_number_alert_text = null;
        Bitmap customer_image_alert_text = null;
        final ImageView close_alert,transactionamountsymbol,customer_image_alert,share_icon;
        TextView transaction_amount,transaction_remarks,transaction_time,customer_phone_number_alert;
        final MaterialCardView alert_dialog;
        final LinearLayout share_layout;

        MaterialCardView card = (MaterialCardView) view;
        transaction_id = card.getTag().toString();
        DatabaseHelper myDB = new DatabaseHelper(this);
        Cursor cursor = myDB.get_transaction_details(transaction_id);

        while (cursor.moveToNext()){
            transaction_sender_id = cursor.getString(1);
            transaction_receiver_id = cursor.getString(2);
            transaction_amount_text = cursor.getString(3);
            transaction_remarks_text = cursor.getString(5);
            transaction_date_text = cursor.getString(7);
        }

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_transaction_alert);

        alert_dialog = dialog.findViewById(R.id.alert_dialog);
        close_alert = dialog.findViewById(R.id.close_alert);
        customer_image_alert = dialog.findViewById(R.id.customer_image);
        customer_phone_number_alert = dialog.findViewById(R.id.customer_contact_number);
        transactionamountsymbol = dialog.findViewById(R.id.transactionamountsymbol);
        transaction_amount = dialog.findViewById(R.id.transaction_amount);
        transaction_remarks = dialog.findViewById(R.id.transaction_remarks);
        transaction_time = dialog.findViewById(R.id.transaction_time);
        share_icon = dialog.findViewById(R.id.share_icon);
        share_layout = dialog.findViewById(R.id.share_layout);

        if(transaction_sender_id.compareTo(user_id)==0){
            sender_id = transaction_receiver_id;
            transaction_amount.setText("- " + getResources().getString(R.string.currency_sign) + " " + transaction_amount_text);
            transaction_amount.setTextColor(getApplicationContext().getResources().getColor(R.color.warning));
        //    transactionamountsymbol.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.debit_rs_symbol));
            close_alert.setImageResource(R.drawable.alertbox_cross_icon_debit);
        }
        else{
            String hello = String.valueOf(R.string.currency_sign);
            sender_id = transaction_sender_id;
            transaction_amount.setText("+ " + getResources().getString(R.string.currency_sign) + " " + transaction_amount_text);
           // transaction_amount.setText("+ "+transaction_amount_text);
            transaction_amount.setTextColor(getApplicationContext().getResources().getColor(R.color.sucess));
            //transactionamountsymbol.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.credit_rs_symbol));
            close_alert.setImageResource(R.drawable.alertbox_cross_icon_credit);
            share_icon.setImageResource(R.drawable.credit_share_icon);
        }

        Cursor cursor1 = myDB.get_user_details(sender_id);

        while (cursor1.moveToNext()){
            customer_phone_number_alert_text = cursor1.getString(0);
            customer_image_alert_text = BitmapFactory.decodeByteArray(cursor1.getBlob(4), 0 , cursor1.getBlob(4).length);
        }

        transaction_remarks.setText(transaction_remarks_text);
        transaction_time.setText(transaction_date_text);
        customer_phone_number_alert.setText(customer_phone_number_alert_text);
        customer_image_alert.setImageBitmap(customer_image_alert_text);

        close_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                share_layout.setVisibility(View.INVISIBLE);
                close_alert.setVisibility(View.INVISIBLE);
                Bitmap bitmap = Bitmap.createBitmap(alert_dialog.getWidth(),alert_dialog.getHeight(),Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                alert_dialog.draw(canvas);

                try {
                    File file = new File(getApplicationContext().getExternalCacheDir(), File.separator + user_name +"_"+user_business_name+".jpg");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);
                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/jpg");
                    startActivity(Intent.createChooser(intent, "Share image via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        boolean strPref = false;
        SharedPreferences shf = this.getSharedPreferences("config", MODE_PRIVATE);
        strPref = shf.getBoolean(DARK_MODE, false);

        if(strPref)
        {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
            setContentView(R.layout.activity_main);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //getSupportActionBar().hide();


            final AdView mAdView = findViewById(R.id.adView);
            adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
            user_id = sharedPreferences.getString("Id", "");

            final DatabaseHelper myDB = new DatabaseHelper(getApplicationContext());
            Cursor cursor = myDB.get_user_details(user_id);
            while (cursor.moveToNext()){
                user_name = cursor.getString(1);
                user_business_name = cursor.getString(2);
                user_image = cursor.getBlob(4);
            }

            final Bitmap bitmap = BitmapFactory.decodeByteArray(user_image, 0 , user_image.length);

            /* Side Navigation */

        //    drawerLayout = findViewById(R.id.side_drawer);
          //  side_navigation = findViewById(R.id.side_navigation);
          //  sidebar_icon = findViewById(R.id.sidebar_icon);
           // navigationDrawer();



            /* Status Bar */
            status_bar_text = findViewById(R.id.status_bar_text);

            /* Bottom Navigation Fragment */

            final BottomNavigationView bottom_nav = findViewById(R.id.bottom_navigation);
            bottom_nav.setOnNavigationItemSelectedListener(navListener);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();

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
                            LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_ENGLISH);

                            recreate();
                            break;
                        case 1:
                            LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_FRENCH);
                            recreate();
                            break;
                        case 2:
                            LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_HINDI);
                            recreate();
                            break;
                        case 3:
                            LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_RUSSIA);
                            recreate();
                            break;
                        case 4:
                            LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_SPANISH);
                            recreate();
                            break;
                        case 5:
                            LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_ARABIC);
                            recreate();
                            break;
                    }


                    AdRequest adRequest2 = new AdRequest.Builder().build();
                    InterstitialAd.load(MainActivity.this,getString(R.string.interstitial_ad_unit), adRequest2,
                            new InterstitialAdLoadCallback() {
                                @Override
                                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                    // The mInterstitialAd reference will be null until
                                    // an ad is loaded.
                                    mInterstitialAd = interstitialAd;

                                    Log.i("TAG", "onAdLoaded");
                                    if (mInterstitialAd != null) {
                                        mInterstitialAd.show(MainActivity.this);
                                    } else {
                                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                    }

                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    // Handle the error
                                    Log.i("TAG", loadAdError.getMessage());
                                    mInterstitialAd = null;
                                }
                            });

                }
            });
            spinner2.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
                @Override public void onNothingSelected(MaterialSpinner spinner) {
                }
            });

        }
        else {
            setTheme(R.style.AppTheme_Light_NoActionBar);
            setContentView(R.layout.activity_main);
            //getSupportActionBar().hide();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            final AdView mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);


          //  AdRequest adRequest = new AdRequest.Builder().build();



            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
            user_id = sharedPreferences.getString("Id", "");

            final DatabaseHelper myDB = new DatabaseHelper(getApplicationContext());
            Cursor cursor = myDB.get_user_details(user_id);
            while (cursor.moveToNext()){
                user_name = cursor.getString(1);
                user_business_name = cursor.getString(2);
                user_image = cursor.getBlob(4);
            }

            final Bitmap bitmap = BitmapFactory.decodeByteArray(user_image, 0 , user_image.length);

            /* Side Navigation */




            /* Status Bar */
            status_bar_text = findViewById(R.id.status_bar_text);

            /* Bottom Navigation Fragment */

            final BottomNavigationView bottom_nav = findViewById(R.id.bottom_navigation);
            bottom_nav.setOnNavigationItemSelectedListener(navListener);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();



            MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
            spinner.setItems(paths);
            spinner.setSelectedIndex(0);
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
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
                @Override
                public void onNothingSelected(MaterialSpinner spinner) {
                }
            });

            MaterialSpinner spinner2 = (MaterialSpinner) findViewById(R.id.spinner2);
            spinner2.setItems(languages);
            //  spinner2.setSelectedIndex(1);
            spinner2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                    switch (position) {
                        case 0:
                            LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_ENGLISH);
                            recreate();
                            break;
                        case 1:
                            LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_FRENCH);
                            recreate();
                            break;
                        case 2:
                            LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_HINDI);
                            recreate();
                            break;
                        case 3:
                            LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_RUSSIA);
                            recreate();
                            break;
                        case 4:
                            LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_SPANISH);
                            recreate();
                            break;
                        case 5:
                            LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_ARABIC);
                            recreate();
                            break;

                    }

                }
            });
            spinner2.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
                @Override
                public void onNothingSelected(MaterialSpinner spinner) {
                }
            });


        }

    }

    @Override
    public void onBackPressed() {


            super.onBackPressed();

    }



    /* Bottom Navigation Fragment */

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFragment = null;
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    status_bar_text.setText(R.string.homedashboardstatusbar);
                    break;
                case R.id.nav_user:
                    selectedFragment = new CustomerFragment();
                    status_bar_text.setText(R.string.selectcustomerdashboardstatusbar);
                    break;
                case R.id.nav_book:
                    selectedFragment = new TransactionFragment();
                    status_bar_text.setText(R.string.transactionlistdashboardstatusbar);
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            EditText user_location = findViewById(R.id.user_location);
            user_location.setText(place.getName());
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String month_name = new DateFormatSymbols().getMonths()[month];
        String temp_date = String.valueOf(day) + " - " + month_name.substring(0, 3) + " - " + String.valueOf(year);
        transaction_date.setText(temp_date);

    }
}