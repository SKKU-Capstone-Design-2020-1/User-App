package com.skku.userweb.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.skku.userweb.R;
import com.skku.userweb.activity.SelectStoreActivity;
import com.skku.userweb.util.GlobalVar;
import com.skku.userweb.util.Store;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static java.lang.Math.sqrt;


public class UserFragment extends Fragment {
//public class UserFragment extends Fragment implements View.OnClickListener {
    private TextView store_name;
    private TextView totalseat;
    private TextView currentuser;
    private TextView email_text;
    private String store_id;
    private String store_Name;
    private String owner_email;
    private String remained_seat;
    private String img_url;
    private ImageView imgView;
    private Bitmap bitmap;
    private Long[] arr;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user, container, false);
        store_name = rootView.findViewById(R.id.fragment_user_storename);
        totalseat = rootView.findViewById(R.id.fragment_user_num_totalSeat);
        currentuser = rootView.findViewById(R.id.fragment_user_num_dailyUsers);
        email_text = rootView.findViewById(R.id.fragment_user_email);
        GlobalVar storeId = (GlobalVar) getActivity().getApplication();
        store_id = storeId.getStoreId();
        GlobalVar storeName = (GlobalVar) getActivity().getApplication();
        store_Name = storeName.getStoreName();
        GlobalVar ownerEmail = (GlobalVar) getActivity().getApplication();
        owner_email = ownerEmail.getOwnerEmail();

        GlobalVar imgUrl = (GlobalVar) getActivity().getApplication();
        img_url = imgUrl.getImgUrl();
        store_name.setText(store_Name);
        imgView = rootView.findViewById(R.id.fragment_user_profileImage);
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(img_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // 서버로 부터 응답 수신
                    conn.connect();
                    InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start(); // Thread 실행
        try {
            mThread.join();
            imgView.setImageBitmap(bitmap);

//        Button editbutton = (Button) rootView.findViewById(R.id.fragment_user_editButton);
//        Button logoutbutton = (Button) rootView.findViewById(R.id.fragment_user_logoutButton);
//
//        editbutton.setOnClickListener(this);
//        logoutbutton.setOnClickListener(this);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DocumentReference docRef = db.collection("stores").document(store_id);
        Source source = Source.CACHE;
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    Long num_users= (Long) document.getData().get("num_users");
                    Long num_seats= (Long) document.getData().get("num_seats");
                    totalseat.setText(String.valueOf(num_seats));
                    currentuser.setText(String.valueOf(num_users));
                } else {
                    Log.d("test", "Cached get failed: ", task.getException());
                }
            }
        });
        email_text.setText(owner_email);



        return rootView;
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.fragment_user_editButton:
//                //go to edit user activity
//            case R.id.fragment_user_logoutButton:
//                //logout
//
//        }
//    }
}
