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
       // webview.loadUrl("https://reserveseats.site/reserve?sid=6j46BJioYNQS0TEYCRoY&user_token=asd");
         webview.loadUrl("https://reserveseats.site/reserve?sid=6j46BJioYNQS0TEYCRoY&user_token="+ MainActivity.token);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

  /*  
    Task<GetTokenResult> mUser = FirebaseAuth.getInstance()
            .getCurrentUser().getIdToken(true)
            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        // Send token to your backend via HTTPS
                        // ...
                        Log.d("FragmentCreate", "Token found from thread1 after expiry " + task.getResult().getToken());
                        //  Toast.makeText(MainActivity.this, " successful", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(MainActivity.this, " ful", Toast.LENGTH_LONG).show();
                    Log.d("FragmentCreate","Token failed from main thread single "+e.toString());
                }
            });
*/




}
