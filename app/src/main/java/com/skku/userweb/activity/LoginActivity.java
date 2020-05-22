package com.skku.userweb.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skku.userweb.R;
import com.skku.userweb.fragment.ContactFragment;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText editTextEmail, editTextPassword;
    private Bundle bundle;
    private FirebaseAuth firebaseAuth;
    private ContactFragment ContactFragment;
    //activity 부분
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.activity_login_ID);
        editTextPassword = findViewById(R.id.activity_login_Password);
        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.activity_login_Login).setOnClickListener(onClickListener);

        //register activity로 이동
        TextView textView = (TextView) findViewById(R.id.activity_login_register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.activity_login_Login:
                    SignIn(view);
                    break;


            }
        }
    };



    private void  SignIn(View view) {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }


        if (email.isEmpty()) {
            editTextEmail.setError("Email requried");
            editTextEmail.requestFocus();
            return;
        }

        if ((!Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
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

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                             FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                            ContactFragment = new ContactFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("userId", email);
                            ContactFragment.setArguments(bundle);
                            Log.d("test",email);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {

                          //  Log.w(TAG, "signInWithEmail:failure", task.getException());
                            if(task.getException() !=null){
                                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                            }

                        }

                    }
                });



    }

}