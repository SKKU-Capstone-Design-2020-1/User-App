package com.skku.userweb.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.skku.userweb.R;
import com.skku.userweb.adapter.StoreListAdapter;
import com.skku.userweb.util.Store;

import java.util.ArrayList;

public class SelectStoreActivity extends AppCompatActivity {

    ArrayList<Store> storeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);
        this.InitializeStore();

        ListView listView = findViewById(R.id.listView);
        final StoreListAdapter storeListAdapter = new StoreListAdapter(this,storeList);

        listView.setAdapter(storeListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //String name=storeListAdapter.getItem(position).getStoreName();              //store name 변수로 저장해서 보낼준비
                Toast.makeText(getApplicationContext(),storeListAdapter.getItem(position).getStoreName(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SelectStoreActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void InitializeStore() {
        storeList = new ArrayList<Store>();
        storeList.add(new Store(R.drawable.seoul_skku,"Seoul-sungkyunkwan-library","25-2 Sungkyunkwan-ro, Jongno-gu, Seoul","25/46"));
        storeList.add(new Store(R.drawable.suwon_skku,"Suwon-sungkyunkwan-library","2066, Seobu-ro, Jangan-gu, Suwon-si, Gyeonggi-do","31/53"));
    }
}
