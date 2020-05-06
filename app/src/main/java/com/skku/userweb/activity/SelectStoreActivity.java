package com.skku.userweb.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import android.widget.Toast;

import com.skku.userweb.R;
import com.skku.userweb.adapter.StoreListAdapter;
import com.skku.userweb.util.Store;


public class SelectStoreActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    StoreListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StoreListAdapter(getApplicationContext());
        adapter.addItem(new Store(R.drawable.seoul_skku,"Seoul-sungkyunkwan-library","25-2 Sungkyunkwan-ro, Jongno-gu, Seoul","25/46"));
        adapter.addItem(new Store(R.drawable.suwon_skku,"Suwon-sungkyunkwan-library","2066, Seobu-ro, Jangan-gu, Suwon-si, Gyeonggi-do","31/53"));

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new StoreListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, View view, int position) {
                Store item = adapter.getItem(position);
                Toast.makeText(getApplicationContext(),item.getStoreName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
