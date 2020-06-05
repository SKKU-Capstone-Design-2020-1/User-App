package com.skku.userweb.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Objects;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.firestore.Source;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.skku.userweb.R;
import com.skku.userweb.activity.MainActivity;
import com.skku.userweb.util.GlobalVar;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class StatusFragment extends Fragment{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private BeaconConsumer beaconConsumer;

    private TextView tv_time;
    private TextView ab_time;
    private MyCountDownTimer myCountDownTimer;
    private AbsentCountDownTimer absentCountDownTimer;
    private String[] descriptionData = {"Ready", "Go", "Using"};  //추후 예약전 상태도 입력 예정
    private StateProgressBar stateProgressBar;

    private boolean isAbsent = false;
    private ConstraintLayout absentLayout;

    private int gotime = 15;
    private int breaktime = 5;
    private String Store_key;
    private String SeatGroup_key;
    private String Selected_seat;
    private String beaconnumber;

    private static final String BeaconsEverywhere = "BeaconsEverywhere";
    private BeaconManager beaconManager;
    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 999;


    private Boolean realbeacon =false;

    private TextView seatnum;
    private Button returnButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalVar storeId = (GlobalVar) getActivity().getApplication();
        Store_key = storeId.getStoreId();
        GlobalVar goTime = (GlobalVar) getActivity().getApplication();
        gotime = goTime.getGotime();
        GlobalVar breakTime = (GlobalVar) getActivity().getApplication();
        breaktime = breakTime.getBreaktime();


        /*Beacon permission*/
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(BeaconsEverywhere, "location permission is needed");
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION
            );
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_status, container, false);

        stateProgressBar = (StateProgressBar) rootView.findViewById(R.id.fragment_status_state_progress_bar);
        stateProgressBar.setStateDescriptionData(descriptionData);

        tv_time = (TextView) rootView.findViewById(R.id.fragment_status_remaintime_textView);
        ab_time = (TextView) rootView.findViewById(R.id.fragment_status_absenttime_textView);
        seatnum = (TextView) rootView.findViewById(R.id.fragment_status_seatNum_textView);
        absentLayout = (ConstraintLayout) rootView.findViewById(R.id.fragment_status_absent_constraintlayout);
        returnButton = (Button) rootView.findViewById(R.id.fragment_status_return_button);


        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // return seat
                switch (stateProgressBar.getCurrentStateNumber()){

                    case 2:
                        myCountDownTimer.cancel();
                        returnSeat(1);
                        break;

                    case 3:
                        if (isAbsent){
                            absentCountDownTimer.cancel();
                            absentLayout.setVisibility(View.INVISIBLE);
                            isAbsent = false;
                        }
                        returnSeat(2);
                        Log.i("Return Seat","return seat by button");
                        break;
                }

            }
        });



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(stateProgressBar.getCurrentStateNumber() == 1){
            if(((MainActivity)getActivity()).is_reserve){
                GlobalVar BeaconNumber = (GlobalVar) getActivity().getApplication();
                beaconnumber = BeaconNumber.getBeaconId();
                GlobalVar SelectedSeat = (GlobalVar) getActivity().getApplication();
                Selected_seat = SelectedSeat.getSelected_seat();
                GlobalVar SeatGroupKey = (GlobalVar) getActivity().getApplication();
                SeatGroup_key = SeatGroupKey.getSeatGroupId();


                bindBeaconConsumer();
                myCountDownTimer = new MyCountDownTimer(gotime*1000, 1000);
                myCountDownTimer.start();
                seatnum.setText("Seat Number: "+ Selected_seat);
                seatnum.setVisibility(View.VISIBLE);
                returnButton.setVisibility(View.VISIBLE);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
            }
        }
    }

    public class MyCountDownTimer extends CountDownTimer{
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            if(realbeacon){
                usingSeat();
            }

            /*텍스트로 시간 보여주는 부분*/
            int seconds = (int) (millisUntilFinished / 1000) % 60;
            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
            int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
            String newtime = hours + ":" + minutes + ":" + seconds;

            if (newtime.equals("0:0:0")) {
                tv_time.setText("00:00:00");
                onFinish();   //원래는 onFinish에 써야하는건데 이상하게 중간에 계속 나가져서 일단 여기에 적어둠. 자리반납하는 함수
            } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                tv_time.setText("0" + hours + ":0" + minutes + ":0" + seconds);
            } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                tv_time.setText("0" + hours + ":0" + minutes + ":" + seconds);
            } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                tv_time.setText("0" + hours + ":" + minutes + ":0" + seconds);
            } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                tv_time.setText(hours + ":0" + minutes + ":0" + seconds);
            } else if (String.valueOf(hours).length() == 1) {
                tv_time.setText("0" + hours + ":" + minutes + ":" + seconds);
            } else if (String.valueOf(minutes).length() == 1) {
                tv_time.setText(hours + ":0" + minutes + ":" + seconds);
            } else if (String.valueOf(seconds).length() == 1) {
                tv_time.setText(hours + ":" + minutes + ":0" + seconds);
            } else {
                tv_time.setText(hours + ":" + minutes + ":" + seconds);
            }
            if(stateProgressBar.getCurrentStateNumber() == 3){
                tv_time.setText("Using time");
            }

        }

        @Override
        public void onFinish() {
            if(stateProgressBar.getCurrentStateNumber() == 2){
                returnSeat(1);
                Log.i("Return Seat","return seat by go timeer");
            }

            myCountDownTimer.cancel();

        }
    }

    public class AbsentCountDownTimer extends CountDownTimer{
        public AbsentCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(realbeacon){
                isAbsent = false;
                absentCountDownTimer.cancel();
                absentLayout.setVisibility(View.INVISIBLE);
                tv_time.setText("Using time");
            }

            /*텍스트로 시간 보여주는 부분*/
            int seconds = (int) (millisUntilFinished / 1000) % 60;
            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
            int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
            String newtime = hours + ":" + minutes + ":" + seconds;

            if (newtime.equals("0:0:0")) {
                ab_time.setText("00:00:00");
                onFinish();   //원래는 onFinish에 써야하는건데 이상하게 중간에 계속 나가져서 일단 여기에 적어둠. 자리반납하는 함수
            } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                ab_time.setText("0" + hours + ":0" + minutes + ":0" + seconds);
            } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                ab_time.setText("0" + hours + ":0" + minutes + ":" + seconds);
            } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                ab_time.setText("0" + hours + ":" + minutes + ":0" + seconds);
            } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                ab_time.setText(hours + ":0" + minutes + ":0" + seconds);
            } else if (String.valueOf(hours).length() == 1) {
                ab_time.setText("0" + hours + ":" + minutes + ":" + seconds);
            } else if (String.valueOf(minutes).length() == 1) {
                ab_time.setText(hours + ":0" + minutes + ":" + seconds);
            } else if (String.valueOf(seconds).length() == 1) {
                ab_time.setText(hours + ":" + minutes + ":0" + seconds);
            } else {
                ab_time.setText(hours + ":" + minutes + ":" + seconds);
            }



        }

        @Override
        public void onFinish() {
            absentLayout.setVisibility(View.INVISIBLE);
            isAbsent = false;
            absentCountDownTimer.cancel();

            returnSeat(2);
            Log.i("Return Seat","return seat by absent timer");
        }
    }

    private void usingSeat(){
        myCountDownTimer.cancel();

        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        //tv_time.setVisibility(View.INVISIBLE);
        tv_time.setText("Using time");
        isAbsent = false;
        changeSeatStatus(1,2);
        Log.i("using","using start");
    }

    private void absentStart(){
        absentLayout.setVisibility(View.VISIBLE);
        tv_time.setText("Absent");
        absentCountDownTimer = new AbsentCountDownTimer(breaktime*1000, 1000);
        absentCountDownTimer.start();
        Log.d("absent", "absent start");


    }
    private void returnSeat(int previous_Status){
        seatnum.setVisibility(View.INVISIBLE);
        returnButton.setVisibility(View.INVISIBLE);
        tv_time.setText("Reserve First");
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);

        changeSeatStatus(previous_Status,0);
        ((MainActivity)getActivity()).is_reserve = false;
         
        //예약 실패시 map 다시 로드
        MapFragment.webview.loadUrl(MapFragment.Url);
        
        beaconManager.unbind(beaconConsumer);
    }

    private void changeSeatStatus(int preStatus, int newStatus){

        DocumentReference docRef = db.collection("stores")
                .document(Store_key)
                .collection("seatGroups")
                .document(SeatGroup_key);


        DocumentReference StoredocRef = db.collection("stores")
                .document(Store_key);


        Map<String, Object> findData = new HashMap<>();
        findData.put("id", Selected_seat);
        findData.put("status", preStatus);

        docRef.update("seats", FieldValue.arrayRemove(findData));

        Map<String, Object> newData = new HashMap<>();
        newData.put("id", Selected_seat);
        newData.put("status", newStatus);

        docRef.update("seats", FieldValue.arrayUnion(newData));

        if(preStatus == 0){
            StoredocRef.update("num_users", FieldValue.increment(1));
        }
        else if(newStatus == 0){
            StoredocRef.update("num_users", FieldValue.increment(-1));
        }

    }

    private void bindBeaconConsumer(){
        beaconManager = BeaconManager.getInstanceForApplication(getActivity());
        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        //This is layout of AltBeacon
        //2-3/4-19 means byte position

        beaconConsumer = new BeaconConsumer() {
            @Override
            public void onBeaconServiceConnect() {
                final Region region = new Region("myBeacons", Identifier.parse(beaconnumber), null, null);

                beaconManager.addMonitorNotifier(new MonitorNotifier() {
                    @Override
                    public void didEnterRegion(Region region) {
                        try{
                            Log.i(BeaconsEverywhere, "I just saw an beacon for the first time! Id1->"+region.getId1()
                                    +" id 2:"+region.getId2()+" id 3:"+region.getId3());

                            //id 1: UUID
                            //id 2: major
                            //id 3: minor

                            beaconManager.startRangingBeaconsInRegion(region);
                        } catch(RemoteException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void didExitRegion(Region region) {
                        try {
                            Log.d(BeaconsEverywhere, "did exit region");
                            beaconManager.stopRangingBeaconsInRegion(region);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void didDetermineStateForRegion(int i, Region region) {

                    }
                });

                beaconManager.addRangeNotifier(new RangeNotifier() {
                    @Override
                    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                        Log.i(BeaconsEverywhere,"beacons.size less then 0");
                        if(collection.size() > 0){
                            Log.i(BeaconsEverywhere, "The first beacon I see is about " + collection.iterator().next().getDistance() + " meters away.");
                            for(Beacon beacon: collection){
                                if(beacon.getDistance() < 1.0){
                                    realbeacon = true;
                                }
                                else{
                                    realbeacon = false;
                                    Log.d(BeaconsEverywhere, "I see a beacon that in outside the 5.0 range");
                                    if(stateProgressBar.getCurrentStateNumber() == 3 && !isAbsent){
                                        isAbsent = true;
                                        absentStart();
                                    }
                                }
                            }
                        }else{
                            realbeacon = false;
                            Log.d(BeaconsEverywhere, "Where is beacon...");
                            if(stateProgressBar.getCurrentStateNumber() == 3 && !isAbsent){
                                isAbsent = true;
                                absentStart();
                            }
                        }
                    }
                });

                try{
                    beaconManager.startMonitoringBeaconsInRegion(region);
                } catch (RemoteException e){
                    e.printStackTrace();
                }
            }

            @Override
            public Context getApplicationContext() {
                return null;
            }

            @Override
            public void unbindService(ServiceConnection serviceConnection) {

            }

            @Override
            public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
                return false;
            }
        };

        beaconManager.bind(beaconConsumer);
        Log.i(BeaconsEverywhere,"beacons bind success");
    }

}
