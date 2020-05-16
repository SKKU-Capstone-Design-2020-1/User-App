package com.skku.userweb.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skku.userweb.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.view.View.*;

public class EditUserActivity extends AppCompatActivity {




    private EditText  editTextPassword, editTextPasswordConfirm, editTextUsername, editTextPhone;
    private FirebaseAuth firebaseAuth;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_user);
        Toolbar mToolbar = (Toolbar)  findViewById(R.id.activity_EditUser_toolbar);
        mToolbar.setTitle("Edit profile");
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        editTextPassword = findViewById(R.id.activity_EditUser_editPassword);
        editTextPasswordConfirm = findViewById(R.id.activity_EditUser_editconfirm);
        editTextUsername = findViewById(R.id.activity_EditUser_editName);
        editTextPhone = findViewById(R.id.activity_EditUser_editPhone);
        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.activity_EditUser_save_change_button).setOnClickListener(onClickListener);   //save button
        findViewById(R.id.activity_EditUser_logout_button).setOnClickListener(onClickListener);   //logout button

    }

    OnClickListener onClickListener= new OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                //logout button
                case R.id.activity_EditUser_logout_button:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    break;
                    //save button
                case R.id.activity_EditUser_save_change_button:
                    editdata(view);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;
            }
        }
    };


   public void  editdata(View view) {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password_confirm = editTextPasswordConfirm.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

     if (username.isEmpty()) {
            editTextUsername.setError("Name requried");
            editTextUsername.requestFocus();
            return;
        }


        if (password.isEmpty()) {
            editTextPassword.setError("Enter password");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password should be atleast 6 characters long");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(password_confirm)) {

            editTextPasswordConfirm.setError("Password does not matched.");
            editTextPasswordConfirm.requestFocus();
            return;
        }


        if (phone.isEmpty()) {
            editTextPhone.setError("Phone requried");
            editTextPhone.requestFocus();
            return;
        }

        if (phone.length() != 11) {
            editTextPhone.setError("Phone number should be atleast 11 characters long");
            editTextPhone.requestFocus();
            return;
        }




       userID = firebaseAuth.getCurrentUser().getUid();
        DocumentReference userinfo=FirebaseFirestore.getInstance()
                .collection("users")
                .document( userID);

        Map<String, Object> user = new HashMap<>();
        user.put("username",username);

        user.put("phone", phone);
        user.put("password", password);

        userinfo.update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditUserActivity.this, "Updated", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditUserActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
                });



   }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
