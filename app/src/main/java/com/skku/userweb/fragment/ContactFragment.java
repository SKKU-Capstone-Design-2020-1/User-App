package com.skku.userweb.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.skku.userweb.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ContactFragment extends Fragment {
      //TextView userId = (TextView)getView().findViewById(R.id.fagment_contact_id);        edit를 text로 바꿀 것
        private TextView test;
        private Spinner spinner;
        private Button button;
        private String selItem;
        private EditText edittext;
        private String edit;
        private String user_id;
//    Bundle bundle = new Bundle();
// bundle.putString("userId", editText.getText().toString());           로그인 액티비티에 넣을 코드
// ContactFragment.setArguments(bundle);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle extra = getArguments();
//        if(extra != null){
//            user_id = extra.getString("userId");              로그인 액티비티에서 보낸 userId를 변수에 저장
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_contact, container, false);
        test = rootView.findViewById(R.id.test);
        spinner = rootView.findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        button=rootView.findViewById(R.id.button3);
        edittext = rootView.findViewById(R.id.fagment_contact_inquire);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edittext.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Error!!");
                    builder.setMessage("Please input text");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }else{
                    edit = edittext.getText().toString();
                    selItem= (String)spinner.getSelectedItem();
                    //test.setText(selItem);
                }
            }
        });
        return rootView;

    }


}
