package kr.ac.jalee.mycypherssupporter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import kr.ac.jalee.mycypherssupporter.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    public static SearchHistoryDatabase noteDatabase = null;

    Context context;
    MainRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 안 되게

        Log.d("life cycle", "onCreate");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // 액션바 없앰
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding.mainBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 검색 기록 DB에 삽입
                String nickname = binding.mainEditTextName.getText().toString();

                String sqlSave = "insert into " + SearchHistoryDatabase.TABLE_SEARCHHISTORY + " (NICKNAME) values (" +
                        "'" + nickname + "')";

                SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);
                database.execSQL(sqlSave);

                //검색한 값 전달
            }
        });

        openDatabase();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setStackFromEnd(true);
        binding.mainRecycler.setLayoutManager(layoutManager);
        adapter = new MainRecyclerViewAdapter(loadSHListData());
        binding.mainRecycler.setAdapter(adapter);
    }

    public void openDatabase() {
        // open database
        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }

        noteDatabase = SearchHistoryDatabase.getInstance(this);
        boolean isOpen = noteDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("life cycle", "onDestroy");

        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("life cycler", "onResume");
    }

    public ArrayList<SearchHistory> loadSHListData(){

        //데이터를 가져오는 sql문 select... (id의 역순으로 정렬)
        String loadSql = "select _id, NICKNAME from " + SearchHistoryDatabase.TABLE_SEARCHHISTORY + " order by _id desc";

        int recordCount = -1;
        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);

        //_id, NICKNAME이 담겨질 배열 생성
        ArrayList<SearchHistory> items = new ArrayList<>();

        if(database != null){
            //cursor를 객체화하여 rawQuery문 저장
            Cursor outCursor = database.rawQuery(loadSql);

            recordCount = outCursor.getCount();

            //for문을 통해 하나하나 추가
            for(int i = 0; i < recordCount; i++){
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String nickname = outCursor.getString(1);
                items.add(new SearchHistory(_id, nickname));
            }
            outCursor.close();
        }

        return items;
    }

    public void resetAdapter() {
        binding.mainRecycler.setAdapter(adapter);
    }
}