package com.skku.userweb.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.skku.userweb.R;
import com.skku.userweb.activity.EditUserActivity;


public class UserFragment extends Fragment {
//public class UserFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user, container, false);

//        Button editbutton = (Button) rootView.findViewById(R.id.fragment_user_editButton);
//        Button logoutbutton = (Button) rootView.findViewById(R.id.fragment_user_logoutButton);
//
//        editbutton.setOnClickListener(this);
//        logoutbutton.setOnClickListener(this);

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
