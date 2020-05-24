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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skku.userweb.R;
import com.skku.userweb.util.GlobalVar;

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
        private String store;
        private GlobalVar store_id;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db.collection("contacts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                    }
                }
            }
        });

//        GlobalVar storeId = (GlobalVar) getActivity().getApplication();
//        store = storeId.getStoreId();
//        Log.d("test",store);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_contact, container, false);
        test = rootView.findViewById(R.id.test);
        spinner = rootView.findViewById(R.id.spinner2);
        userId = rootView.findViewById(R.id.fagment_contact_id);
        if (getArguments()!=null){
                        store=getArguments().getString("storeId");
                        Log.d("test",store);
        }else{
            Log.d("test","null");
        }
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

//                    Bundle bundle = getArguments();
//                    if(bundle != null){
//                        store=bundle.getString("storeId");
//                        Log.d("test",store);
//                    }else{
//                        Log.d("test","null");
//                    }
                    test.setText(store);
                    Map<String, Object> contact = new HashMap<>();
                    contact.put("contents",edit);
                    contact.put("user_id",user_id);
                    contact.put("store_id",store_id);
                    contact.put("option",selItem);
                    contact.put("created_at", FieldValue.serverTimestamp());
                    db.collection("contacts").document().set(contact).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("test", "DocumentSnapshot successfully written!");
                            //Log.d("test", user_id);
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
