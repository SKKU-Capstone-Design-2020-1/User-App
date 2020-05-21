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
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.skku.userweb.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;


public class StatusFragment extends Fragment implements BeaconConsumer {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView tv_time;
    private TextView ab_time;
    private MyCountDownTimer myCountDownTimer;
    private AbsentCountDownTimer absentCountDownTimer;
    private String[] descriptionData = {"Ready", "Go", "Using", "timeout"};  //추후 예약전 상태도 입력 예정
    private StateProgressBar stateProgressBar;
    private int gotime = 15;
    private int usingtime = 10;
    private int absenttime = 5;
    private boolean isAbsent = false;
    private ConstraintLayout absentLayout;

    public static final String BeaconsEverywhere = "BeaconsEverywhere";
    private BeaconManager beaconManager;
    public static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 999;

    private Boolean realbeacon =false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();

        /*Beacon permission*/
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            Log.i(BeaconsEverywhere,"location!!!!!!!!");
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION
            );
        } else{
            beaconManager = BeaconManager.getInstanceForApplication(getActivity());
            beaconManager.getBeaconParsers().add(new BeaconParser()
                    .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
            //이건 알트비콘의 layout 입니다
//            //2-3/4-19이런 것들은 다 byte position 을 의미합니다

            beaconManager.bind(this);
            Log.i(BeaconsEverywhere,"beacons bind success");

        }
//                }
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
//        } else {
//            beaconManager=BeaconManager.getInstanceForApplication(this);
//            beaconManager.getBeaconParsers().add(new BeaconParser()
//                    .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
//            //이건 알트비콘의 layout 입니다
//            //2-3/4-19이런 것들은 다 byte position 을 의미합니
//
//            beaconManager.bind(this);
//        }

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
        TextView seatnum = (TextView) rootView.findViewById(R.id.fragment_status_seatNum_textView);
        absentLayout = (ConstraintLayout) rootView.findViewById(R.id.fragment_status_absent_constraintlayout);
        
        Button stepSwitchButton = (Button) rootView.findViewById(R.id.fragment_status_stepSwitch_button);
        Button returnButton = (Button) rootView.findViewById(R.id.fragment_status_return_button);



        //Log.d(lololo, "timer start!");

        stepSwitchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (stateProgressBar.getCurrentStateNumber()){
                    case 1:
                        myCountDownTimer = new MyCountDownTimer(gotime*1000, 1000);
                        myCountDownTimer.start();
                        seatnum.setVisibility(View.VISIBLE);
                        returnButton.setVisibility(View.VISIBLE);
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                        break;

                    case 2:
                        myCountDownTimer.cancel();
                        myCountDownTimer = new MyCountDownTimer(usingtime*1000, 1000);
                        myCountDownTimer.start();
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        break;

                    case 3:
                        if (!isAbsent){
                            absentLayout.setVisibility(View.VISIBLE);
                            absentCountDownTimer = new AbsentCountDownTimer(absenttime*1000, 1000);
                            absentCountDownTimer.start();
                            isAbsent = true;
                        }
                        else{
                            absentCountDownTimer.cancel();
                            absentLayout.setVisibility(View.INVISIBLE);
                            isAbsent = false;
                        }
                        break;

                    case 4:
                        stateProgressBar.setAllStatesCompleted(true);
                        break;
                }

            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {

        beaconManager.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onBeaconServiceConnect() {
        final Region region = new Region("myBeacons", Identifier.parse("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), null, null);

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try{
                    Log.i(BeaconsEverywhere, "I just saw an beacon for the first time! Id1->"+region.getId1()
                            +" id 2:"+region.getId2()+" id 3:"+region.getId3());

                    //첫번째 아이디는 UUID
                    //두번째 아이디는 major
                    //세번째 아이디는 minor

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
                        if(beacon.getDistance() < 5.0){
                            realbeacon = true;
                            Log.d(BeaconsEverywhere, "I see a beacon that in inside the 2.0 range");
                        }
                        else{
                            realbeacon = false;
                            Log.d(BeaconsEverywhere, "I see a beacon that in outside the 2.0 range");
                        }
                    }
                }else{
                    realbeacon = false;
                    Log.d(BeaconsEverywhere, "Where is beacon...");
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

    public class MyCountDownTimer extends CountDownTimer{
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {


            int progress = (int) (millisUntilFinished/1000);
            //progress = 시간이 지난 정도

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

        }

        @Override
        public void onFinish() {
            switch (stateProgressBar.getCurrentStateNumber()){

                case 2:
                    // 1. go 상황이였을때 타이머 종료
                    // 유저상태 예약전으로 하고 map화면으로 전환
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                    // map화면으로 전환
                    break;

                case 3:
                    // 2. using 상황에서 타이머 종료
                    // 유저상태 예약전으로 하기
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);

                    if (isAbsent){
                        absentCountDownTimer.cancel();
                        absentLayout.setVisibility(View.INVISIBLE);
                        isAbsent = false;
                    }
                    // 3. 자리비움 상황에서 자리비움꺼 타이머 종료
                    // 유저상태 예약전으로 하기
                    //
                    tv_time.setText("Time out");
                    break;
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


            int progress = (int) (millisUntilFinished/1000);
            //progress = 시간이 지난 정도

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
            // 유저상태 예약전으로 하고 map화면으로 전환
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
            myCountDownTimer.cancel();
            absentLayout.setVisibility(View.INVISIBLE);
            isAbsent = false;
            tv_time.setText("Time out");
            absentCountDownTimer.cancel();

        }
    }
    private void getData(){

        db.document("stores/eOc1iI2gvDUWy9LeA4hw").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                Object dd = document.getData().get("limit_time");
                Log.d("status", "limit_time: " + dd);
                gotime = Integer.parseInt(dd.toString());
                // usingtime
                // seatnum
            }
        });

    }



//    private void getData(){
//        DocumentReference docRef = db.collection("stores").document("eOc1iI2gvDUWy9LeA4hw");
//
//    }

//    private void getData(){
//        db.collection("stores").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//
//                }
//            }
//        });
//    }

}
