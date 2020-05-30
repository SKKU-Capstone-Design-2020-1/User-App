package com.skku.userweb.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skku.userweb.R;
import com.skku.userweb.util.GlobalVar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class UserFragment extends Fragment {
//public class UserFragment extends Fragment implements View.OnClickListener {
    private TextView store_name;
    private TextView totalseat;
    private TextView currentuser;
    private String store_Name;
    private String remained_seat;
    private String img_url;
    private ImageView imgView;
    private Bitmap bitmap;
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
        GlobalVar storeName = (GlobalVar) getActivity().getApplication();
        store_Name = storeName.getStoreName();
        GlobalVar storeSeat = (GlobalVar) getActivity().getApplication();
        remained_seat = storeSeat.getTotalSeat();
        GlobalVar imgUrl = (GlobalVar) getActivity().getApplication();
        img_url = imgUrl.getImgUrl();

        String seat[]=remained_seat.split("/");
        store_name.setText(store_Name);
        totalseat.setText(seat[1]);
        currentuser.setText(seat[0]);
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

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rootView;
    }
}
