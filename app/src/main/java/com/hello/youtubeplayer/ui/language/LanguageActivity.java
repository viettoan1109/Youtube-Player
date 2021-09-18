package com.hello.youtubeplayer.ui.language;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.ui.splash.SplashActivity;
import com.hello.youtubeplayer.utils.LocalLanguage;

public class LanguageActivity extends AppCompatActivity {
    private RelativeLayout rlLanguageEng;
    private RelativeLayout rlLanguageVie;
    private RelativeLayout rlLanguageJan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadLocale();
        setContentView(R.layout.activity_language);
        rlLanguageVie = findViewById(R.id.rl_lang_vie);
        rlLanguageVie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("vi");
            }
        });
        rlLanguageEng = findViewById(R.id.rl_lang_eng);
        rlLanguageEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
            }
        });
        rlLanguageJan = findViewById(R.id.rl_lang_jan);
        rlLanguageJan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("ja");
            }
        });

        //initView();

    }



    private void setLocale(String string) {
        LocalLanguage.setLocale(this, string);
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
/*
        Locale locale = new Locale(string);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
*/

        /*SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
        editor.putString("myLang", string);
        editor.apply();*/

    }

    public void loadLocale(){
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lang = sharedPreferences.getString("myLang", "");
        setLocale(lang);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private View.OnClickListener onEngSelected(){
        return view -> setLocale("en");
    }



    private View.OnClickListener onVieSelected(){
        return view -> setLocale("vi");
    }

    private View.OnClickListener onJanSelected(){
        return view -> setLocale("ja");
    }


}