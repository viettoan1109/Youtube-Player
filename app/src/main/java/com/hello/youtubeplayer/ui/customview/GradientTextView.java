package com.hello.youtubeplayer.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hello.youtubeplayer.R;

public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {

    int color1, color2, color3;
    Shader shader;

    public GradientTextView(@NonNull Context context) {
        super(context);
    }

    public GradientTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode())
            createTextView(context, attrs, 0);

    }

    public GradientTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode())
            createTextView(context, attrs, defStyleAttr);

    }



    public void createTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView, defStyleAttr, 0);
        try {
            color1 = a.getInt(R.styleable.GradientTextView_gradientStart, 0);
            color2 = a.getInt(R.styleable.GradientTextView_gradientEnd, 0);
            color3 = a.getInt(R.styleable.GradientTextView_gradientCenter, 0);
            shader = new LinearGradient(0, 0, 0, getTextSize(),
                    new int[]{color1, color3, color2},
                    new float[]{0, 1, 2}, Shader.TileMode.CLAMP);
            getPaint().setShader(shader);
        } finally {
            a.recycle();
        }
    }

}
