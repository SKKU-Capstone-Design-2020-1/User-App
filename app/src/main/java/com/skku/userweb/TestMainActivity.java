package com.skku.userweb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.skku.userweb.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestMainActivity extends AppCompatActivity {
    @BindView(R.id.test_main_list_view)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        ButterKnife.bind(this);
        ArrayList<String> activities = new ArrayList<>();
        for (Class c : Constants.activities ) activities.add(c.getSimpleName());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activities);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(TestMainActivity.this, Constants.activities[i]);
            startActivity(intent);
        });

    }
}
