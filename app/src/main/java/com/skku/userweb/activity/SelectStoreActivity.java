package com.skku.userweb.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skku.userweb.R;
import com.skku.userweb.adapter.StoreListAdapter;
import com.skku.userweb.util.Store;


public class SelectStoreActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    StoreListAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StoreListAdapter(getApplicationContext());
        adapter.addItem(new Store(R.drawable.seoul_skku,"Seoul-sungkyunkwan-library","25-2 Sungkyunkwan-ro, Jongno-gu, Seoul","25/46"));
        adapter.addItem(new Store(R.drawable.suwon_skku,"Suwon-sungkyunkwan-library","2066, Seobu-ro, Jangan-gu, Suwon-si, Gyeonggi-do","31/53"));
        adapter.addItem(new Store(R.drawable.modumoim,"Modumoim","102-603, Samseong-ro 292beon-gil, Yeongtong-gu, Suwon-si, Gyeonggi-do","15/42"));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new StoreListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, View view, int position) {
                Store item = adapter.getItem(position);
                Toast.makeText(getApplicationContext(),item.getStoreName(), Toast.LENGTH_SHORT).show();
            }
        });

        getData();
    }
    private void getData() {
        //get data from firestore "stores" collection and log the result data

        db.collection("stores").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d("store data", document.getId() + " => " + document.getData());
                    }

                }
            }
        });
    }

}
