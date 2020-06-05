package com.skku.userweb.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.sqrt;


public class SelectStoreActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    StoreListAdapter adapter;
    static private String[][] arr;
    static private String temp;
    private Integer count;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);
        recyclerView = findViewById(R.id.recyclerView);
        pd = ProgressDialog.show(SelectStoreActivity.this, "로딩중", "Page Loading...");

        arr = new String[100][7];
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StoreListAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

        count=-1;
        adapter.setOnItemClickListener(new StoreListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, View view, int position) {
                Store item = adapter.getItem(position);
                GlobalVar storeId = (GlobalVar) getApplication();
                storeId.setStoreId(item.getStoreId());
                GlobalVar ownerEmail = (GlobalVar) getApplication();
                ownerEmail.setOwnerEmail(item.getOwnerEmail());
                GlobalVar storeName = (GlobalVar) getApplication();
                storeName.setStoreName(item.getStoreName());
                GlobalVar totalSeat = (GlobalVar) getApplication();
                totalSeat.setTotalSeat(item.getRemainedSeat());
                GlobalVar imgUrl = (GlobalVar) getApplication();
                imgUrl.setImgUrl(item.getStoreImage());
                GlobalVar goTime = (GlobalVar) getApplication();
                goTime.setGotime(item.getGotime());
                GlobalVar breakTime = (GlobalVar) getApplication();
                breakTime.setBreaktime(item.getBreaktime());
                Intent intent = new Intent(SelectStoreActivity.this,MainActivity.class);
                startActivity(intent);
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
                        String storeid = (String) document.getId();
                        String address= (String) document.getData().get("address");
                        String imgurl= (String) document.getData().get("img_url");
                        Double latitude= (Double) document.getData().get("latitude");
                        Double longtitude= (Double) document.getData().get("longtitude");
                        String storeName= (String) document.getData().get("name");
                        String ownerEmail= (String) document.getData().get("owner_email");
                        Long goTime = (Long) document.getData().get("go_time");
                        Long breakTime = (Long) document.getData().get("break_time");
                        Long num_users= (Long) document.getData().get("num_users");
                        Long num_seats= (Long) document.getData().get("num_seats");
                        String remained = num_users+"/"+num_seats;
                        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if ( Build.VERSION.SDK_INT >= 23 &&
                                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                            ActivityCompat.requestPermissions( SelectStoreActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },0 );
                        }
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        double userlongitude = location.getLongitude();
                        double userlatitude = location.getLatitude();
                        double res=sqrt((userlongitude-longtitude)*(userlongitude-longtitude)-(userlatitude-latitude)*(userlatitude-latitude));
                        count++;
                        arr[count]= new String[]{imgurl, storeName, address, remained, storeid, Double.toString(res),ownerEmail, goTime.toString(), breakTime.toString()};

                    }

                    for (int j=0;j<=count;j++){
                        Log.d("test", arr[j][0]+"\\"+arr[j][1]+"\\"+arr[j][2]+"\\"+arr[j][3]+"\\"+arr[j][4]+"\\"+arr[j][5]+"\\"+arr[j][6]);
                    }
                    for (int j =0;j<count;j++){
                        for (int k =j+1;k<=count;k++){
                            if(Double.parseDouble(arr[j][5])>Double.parseDouble(arr[k][5])){
                                for(int n=0;n<7;n++){
                                    temp=arr[j][n];
                                    arr[j][n]=arr[k][n];
                                    arr[k][n]=temp;
                                }
                            }
                        }
                    }
                    for(int i =0;i<=count;i++){
                        adapter.addItem(new Store(arr[i][0],arr[i][1],arr[i][2],arr[i][3],arr[i][4],arr[i][6],arr[i][7],arr[i][8]));
                        Log.d("test res", arr[i][5]);
                        recyclerView.setAdapter(adapter);
                    }
                    pd.dismiss();
                }
            }
        });
    }
}

