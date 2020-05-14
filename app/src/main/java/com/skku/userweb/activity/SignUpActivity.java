package com.skku.userweb.activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skku.userweb.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignuPActivity";
    private EditText editTextEmail, editTextPassword, editTextPasswordConfirm, editTextUsername, editTextPhone;
    private ProgressBar progressBar;
    public Button validateButton;
    //  private boolean validate = false;

    //private DatabaseReference databaseReference;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    String userID;



    //activity 부분
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextEmail = findViewById(R.id.activity_Signup_Email);
        editTextPassword = findViewById(R.id.activity_Signup_Password);
        editTextPasswordConfirm = findViewById(R.id.activity_Signup_Confirm_Password);
        editTextUsername = findViewById(R.id.activity_Signup_Username);
        editTextPhone = findViewById(R.id.activity_Signup_Phone);
        firebaseAuth = FirebaseAuth.getInstance();


       /* if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            finish();
        }*/

        findViewById(R.id.activity_Signup_button).setOnClickListener(onClickListener);

        TextView textView = (TextView) findViewById(R.id.activity_Signup_login);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class); //register activity로 바꿔야 함
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() !=null){

        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.activity_Signup_button:
                    resigterUser();
                    break;

            }
        }
    };



    private void  resigterUser() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password_confirm = editTextPasswordConfirm.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (username.isEmpty()) {
            editTextUsername.setError("Name requried");
            editTextUsername.requestFocus();
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
            editTextEmail.setError("Enter password");
            editTextEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password should be atleast 6 characters long");
            editTextPassword.requestFocus();
            return;
        }

        if(!password.equals(password_confirm)){

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


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                Toast.makeText(SignUpActivity.this, "Sing Up successful", Toast.LENGTH_LONG).show();
                userID = firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = db.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("username", username);
                user.put("email", email);
                user.put("phone", phone);
                user.put("password", password);


                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + userID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });

                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));

            } else {

                Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

            }
        });
    }


}


