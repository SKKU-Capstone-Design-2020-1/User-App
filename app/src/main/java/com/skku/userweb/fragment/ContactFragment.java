package com.skku.userweb.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skku.userweb.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ContactFragment extends Fragment {
        private TextView userId;
        private TextView test;
        private Spinner spinner;
        private Button button;
        private String selItem;
        private EditText edittext;
        private String edit;
        private String user_id;
        private Integer count=1;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle extra = getArguments();
//        if(extra != null){
//            user_id = extra.getString("userId");      //로그인 액티비티에서 보낸 userId를 변수에 저장
//        }

        db.collection("contacts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        count+=1;
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_contact, container, false);
        test = rootView.findViewById(R.id.test);
        spinner = rootView.findViewById(R.id.spinner2);
        userId = rootView.findViewById(R.id.fagment_contact_id);
//        Bundle bundle = getArguments();
//        String text = bundle.getString("userId");
//        test.setText(text);
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
                    //test.setText(edit);
                    Map<String, Object> contact = new HashMap<>();
                    contact.put("contents",edit);
                    contact.put("id",user_id);
                    contact.put("option",selItem);
                    db.collection("contacts").document(Integer.toString(count)).set(contact).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("test", "DocumentSnapshot successfully written!");
                            //Log.d("test", user_id);
                            count+=1;
                            userId.setText(user_id);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("test", "Error writing document", e);
                        }
                    });
                }
            }
        });
        return rootView;

    }


}
