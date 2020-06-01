package com.skku.userweb.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.skku.userweb.R;
import com.skku.userweb.activity.MainActivity;

import java.util.zip.Inflater;

import static com.skku.userweb.activity.MainActivity.token;

public class MapFragment extends Fragment {

    private String map_url_value;
    private String[] after_map_url_value;


    //private String token;

    public MapFragment() {
    }

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view=inflater.inflate(R.layout.fragment_map,container,false);
        WebView webview=(WebView)view.findViewById(R.id.fragment_map_webview);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // webview.loadUrl("https://reserveseats.site/reserve?sid=6j46BJioYNQS0TEYCRoY");

        webview.loadUrl("https://reserveseats.site/reserve?sid=6j46BJioYNQS0TEYCRoY&user_token="+ MainActivity.idToken);

        Log.d("WebView", "Before" + webview.getUrl());
        webview.loadUrl( "javascript:window.location.reload( true )" );


        //페이지가 바뀐 후 링크 받아오기
        new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                // counttime.setText("Finished");
                Log.d("WebView", "After" + webview.getUrl());
                //받아온 url
                map_url_value=webview.getUrl();

                //받은 url 필요 값 추출하기
                if(map_url_value!=null){
                    //url 분리
                    after_map_url_value=map_url_value.split("&");
                    for(int i=0;i<after_map_url_value.length;i++) {
                        //System.out.println(after_map_url_value[i]);
                        Log.d("WebView", "!!url" + after_map_url_value[i]);
                    }
                }

            }
        }.start();


        return view;

    }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }



}
