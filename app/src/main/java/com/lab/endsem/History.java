package com.lab.endsem;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.endsem.adapter.TimeAdapter;
import com.lab.endsem.db.TimerDatabase;

public class History extends AppCompatActivity {

    private TimerDatabase dbHelper;
    private RecyclerView recyclerViewHistory;
    private TimeAdapter adapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new TimerDatabase(this);

        cursor = dbHelper.getAllTimers();

        adapter = new TimeAdapter(cursor);
        recyclerViewHistory.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
