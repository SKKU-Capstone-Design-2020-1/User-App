package com.skku.userweb.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.skku.userweb.R;


public class StatusFragment extends Fragment {

    String[] descriptionData = {"approach", "Using", "timeout"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_status, container, false);

        StateProgressBar stateProgressBar = (StateProgressBar) rootView.findViewById(R.id.fragment_status_state_progress_bar);
        stateProgressBar.setStateDescriptionData(descriptionData);

        Button stepSwitchButton = (Button) rootView.findViewById(R.id.fragment_status_stepSwitch_button);

        stepSwitchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (stateProgressBar.getCurrentStateNumber()){
                    case 1:
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                        break;
                    case 2:
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        break;
                    case 3:
                        stateProgressBar.setAllStatesCompleted(true);
                        break;
                }

            }
        });

        return rootView;
    }

}
