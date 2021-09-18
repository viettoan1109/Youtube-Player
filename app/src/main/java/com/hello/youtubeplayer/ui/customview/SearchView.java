package com.hello.youtubeplayer.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.data.local.SearchDatabase;
import com.hello.youtubeplayer.interfaces.ICallBack;

public class SearchView extends LinearLayout {

    private Context context;

    private EditText edSearch;
    private LinearLayout searchBlock;
    public LinearLayout linearRecent;
    private ImageView imgVoice;

    private SearchListView listView;
    private BaseAdapter adapter;

    private SearchDatabase searchDatabase;
    private SQLiteDatabase db;

    private ICallBack iCallBack;

    private Float textSize;
    private int textColor;
    private String textHint;

    private int searchBlockHeight;
    private int searchBlockColor;


    public SearchView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttributes(context, attrs);
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttributes(context, attrs);
        init();
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.Search_View);

        textSize = typedArray.getDimension(R.styleable.Search_View_textSizeSearch, 14);

        int defaultColor = context.getResources().getColor(R.color.black);
        textColor = typedArray.getColor(R.styleable.Search_View_textColorSearch, defaultColor);

        textHint = typedArray.getString(R.styleable.Search_View_textHintSearch);

        searchBlockHeight = typedArray.getInteger(R.styleable.Search_View_searchBlockHeight, 120);

        int defaultColor2 = context.getResources().getColor(R.color.gray);
        searchBlockColor = typedArray.getColor(R.styleable.Search_View_searchBlockColor, defaultColor2);

        typedArray.recycle();
    }

    private void init() {
        initView();
        searchDatabase = new SearchDatabase(context);
        queryData("");

        edSearch.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (iCallBack != null) {
                        iCallBack.SearchAciton(edSearch.getText().toString());
                        linearRecent.setVisibility(View.GONE);
                    }
                    linearRecent.setVisibility(View.GONE);
                    boolean hasData = hasData(edSearch.getText().toString().trim());

                    if (!hasData) {
                        insertData(edSearch.getText().toString().trim());
                        queryData("");
                    }
                    //hide key broad
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                return false;
            }
        });

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                linearRecent.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imgVoice.setVisibility(GONE);
                linearRecent.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = edSearch.getText().toString();
                queryData(name);
                linearRecent.setVisibility(View.VISIBLE);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.textAutoComplete);
                String name = textView.getText().toString();
                edSearch.setText(name);
                iCallBack.SearchAciton(edSearch.getText().toString());
                linearRecent.setVisibility(View.GONE);

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });

    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.search_layout, this);

        edSearch = findViewById(R.id.edit_search);
        edSearch.setTextSize(textSize);
        edSearch.setTextColor(textColor);
        edSearch.setHint(textHint);

        searchBlock = findViewById(R.id.search_block);
        LinearLayout.LayoutParams params = (LayoutParams) searchBlock.getLayoutParams();
        params.height = searchBlockHeight;
        searchBlock.setLayoutParams(params);

        listView = findViewById(R.id.listView);

        linearRecent = findViewById(R.id.linear_recent);


        imgVoice = findViewById(R.id.img_voice);


    }

    private void queryData(String name) {
        Cursor cursor = searchDatabase.getReadableDatabase().rawQuery(
                "select id as _id, name from recents " +
                        "where name like '%" + name + "%' order by id desc  ", null);

        adapter = new SimpleCursorAdapter(context, R.layout.history_search, cursor, new String[]{"name"},
                new int[]{R.id.textAutoComplete}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (name.equals("") && cursor.getCount() != 0) {
            imgVoice.setVisibility(VISIBLE);
            //linearRecent.setVisibility(View.VISIBLE);
        }

    }

    private boolean hasData(String name) {
        Cursor cursor = searchDatabase.getReadableDatabase().rawQuery(
                "select id as _id,name from recents where name =?", new String[]{name});

        return cursor.moveToNext();
    }

    private void insertData(String name) {
        db = searchDatabase.getReadableDatabase();
        db.execSQL("insert into recents(name) values ('" + name + "')");
        db.close();
    }

    public void setOnclickSearch(ICallBack iCallBack) {
        this.iCallBack = iCallBack;
    }

}
