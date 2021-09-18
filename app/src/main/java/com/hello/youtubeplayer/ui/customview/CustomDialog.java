package com.hello.youtubeplayer.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.ui.main.MainActivity;

public class CustomDialog extends Dialog {
    public RadioButton onPlay;
    public RadioButton offPlay;
    public RadioGroup radioGroup;
    private Context context;
    private SharedPreferences sharedPreferences;


    public CustomDialog(@NonNull Context context) {
        super(context);

    }

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    private void initAttributes() {
        ((MainActivity) getContext()).onPlayBackground();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_play_background);

        radioGroup = findViewById(R.id.radios);
        onPlay = findViewById(R.id.first);
        offPlay = findViewById(R.id.second);

        onPlay.setChecked(update("one"));
        offPlay.setChecked(update("two"));

        onPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ffff", Toast.LENGTH_SHORT).show();

            }
        });

        offPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "fffsdf", Toast.LENGTH_SHORT).show();

            }
        });

        onPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveRadioButtons("one", isChecked);

            }
        });

        offPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveRadioButtons("two", isChecked);

            }
        });


    }


    public void saveRadioButtons(String key, boolean value) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private boolean update(String key) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("save", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    private void setOnPlay() {
        ((MainActivity) getContext()).onPlayBackground();
    }

}
