package com.skku.userweb.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import com.skku.userweb.R;

import java.util.zip.Inflater;

public class MapFragment extends Fragment {


    public MapFragment() {
    }

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_map,container,false);
        WebView webview=(WebView)view.findViewById(R.id.fragment_map_webview);
        webview.setWebViewClient(new WebViewClient());
        //webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.loadUrl("https://reserveseats.site/reserve?sid=6j46BJioYNQS0TEYCRoY&user_token=asd");
        return view;
      // return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
