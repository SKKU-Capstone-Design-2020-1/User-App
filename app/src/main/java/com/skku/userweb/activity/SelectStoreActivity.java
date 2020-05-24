package com.skku.userweb.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skku.userweb.R;
import com.skku.userweb.adapter.StoreListAdapter;
import com.skku.userweb.fragment.ContactFragment;
import com.skku.userweb.util.GlobalVar;
import com.skku.userweb.util.Store;

import java.util.List;


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
        recyclerView.setAdapter(adapter);
        GlobalVar userId = (GlobalVar) getApplication();
        Toast.makeText(getApplicationContext(),userId.getUserId(), Toast.LENGTH_SHORT).show();

        adapter.setOnItemClickListener(new StoreListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, View view, int position) {
                Store item = adapter.getItem(position);
                //Toast.makeText(getApplicationContext(),item.getStoreId(), Toast.LENGTH_SHORT).show();
                GlobalVar storeId = (GlobalVar) getApplication();
                storeId.setStoreId(item.getStoreId());
                ContactFragment fragment = new ContactFragment();
                Bundle bundle = new Bundle();
                bundle.putString("storeId",item.getStoreId());
                fragment.setArguments(bundle);
                Toast.makeText(getApplicationContext(),storeId.getStoreId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SelectStoreActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( SelectStoreActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },0 );
        }
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Toast.makeText(getApplicationContext(),"위도 : " + longitude + "\n" +
                    "경도 : " + latitude, Toast.LENGTH_LONG).show();
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
                        String storeid = (String) document.getId();
                        String address= (String) document.getData().get("address");
                        String imgurl= (String) document.getData().get("img_url");
                        Double latitude= (Double) document.getData().get("latitude");
                        Double longtitude= (Double) document.getData().get("longtitude");
                        String storeName= (String) document.getData().get("name");
                        Long num_users= (Long) document.getData().get("num_users");
                        Long num_seats= (Long) document.getData().get("num_seats");
                        String remained=num_users+"/"+num_seats;
                        adapter.addItem(new Store(imgurl,storeName,address,remained,storeid));
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
        });
    }

}
