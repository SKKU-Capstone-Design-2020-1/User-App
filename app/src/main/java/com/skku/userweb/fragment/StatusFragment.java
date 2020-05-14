package com.skku.userweb.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
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





public class StatusFragment extends Fragment {

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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();

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
