package com.skku.userweb.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.skku.userweb.R;
import com.skku.userweb.activity.MainActivity;
import com.skku.userweb.util.GlobalVar;

import java.util.ArrayList;
import java.util.zip.Inflater;

import static com.skku.userweb.activity.MainActivity.idToken;

public class MapFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String map_url_value;
    private String[] after_map_url_value;
    private String store_id;
    private String seat_id;
    private Long num_users;
    private Long num_seats;
   // private WebView webview;

    //edit 
    public static View view;
    public static String Url;
    public static WebView webview;
    
    
    private boolean is_success = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalVar storeId = (GlobalVar) getActivity().getApplication();
        store_id = storeId.getStoreId();
    }

    //private String token;

    public MapFragment() {
    }

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view=inflater.inflate(R.layout.fragment_map,container,false);
        webview=(WebView)view.findViewById(R.id.fragment_map_webview);
//        webview.s
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // webview.loadUrl("https://reserveseats.site/reserve?sid=6j46BJioYNQS0TEYCRoY");

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            Url= "https://reserveseats.site/reserve?sid="+store_id+"&user_token="+ idToken;
                            webview.loadUrl(Url);
                                                                             
                        } else {
                            task.getException();
                        }
                    }
                });


        //Log.d("WebView", "Before" + webview.getUrl());
        webview.loadUrl( "javascript:window.location.reload( true )" );




        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!((MainActivity)getActivity()).is_reserve){
            //페이지가 바뀐 후 링크 받아오기
            new CountDownTimer(20000,2000) {
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
                        String[] get_value;
                        // success
                        get_value = after_map_url_value[0].split("=");
                        is_success = get_value[1].equals("success");
                        if(is_success){
                            // map_id
                            get_value = after_map_url_value[1].split("=");
                            GlobalVar mapId = (GlobalVar) getActivity().getApplication();
                            mapId.setMap_id(get_value[1]);
                            // seat_id
                            get_value = after_map_url_value[2].split("=");
                            GlobalVar seatId = (GlobalVar) getActivity().getApplication();
                            seatId.setSeat_id(get_value[1]);
                            seat_id = get_value[1];
                            // secected_seat
                            get_value = after_map_url_value[3].split("=");
                            GlobalVar secectedSeat = (GlobalVar) getActivity().getApplication();
                            secectedSeat.setSelected_seat(get_value[1]);
                            getDatas();

                        }
                        else{

                            Log.e("Error", "fail to reserve");
                        }

                    }

                }
            }.start();
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void getDatas(){
        //todo: find seatGroupid, beaconid,

        GlobalVar SeatGroupId = (GlobalVar) getActivity().getApplication();
        GlobalVar BeaconId = (GlobalVar) getActivity().getApplication();

        CollectionReference SeatGroupRef = db.collection("stores")
                .document(store_id)
                .collection("seatGroups");

        SeatGroupRef
                .whereEqualTo("seat_id", seat_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("Taaaaaag", document.getId() + " => " + document.getData());
                                SeatGroupId.setSeatGroupId(document.getId());
                                Object go = document.getData().get("go_time");

                                ArrayList<String> beaconlist = (ArrayList<String>) document.getData().get("beacon_ids");
                                Log.d("Taaaaaag", "beacon ids: " + beaconlist);
                                Log.d("Taaaaaag", "beacon id 0: " + beaconlist.get(0));
                                BeaconId.setBeaconId(beaconlist.get(0));

                            }

                            ((MainActivity)getActivity()).is_reserve = true;
                            ((MainActivity)getActivity()).viewPager.setCurrentItem(1);
                            Log.d("sssssss", "success to change");
                        }
                        else{
                           
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



}
