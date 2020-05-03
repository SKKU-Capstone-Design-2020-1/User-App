package com.skku.userweb.activity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skku.userweb.R;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button button=(Button) findViewById(R.id.activity_Signup_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class );
                startActivity(intent);
            }
        });

        TextView textView = (TextView) findViewById(R.id.activity_Signup_login);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), LoginActivity.class); //register activity로 바꿔야 함
                startActivity(intent);
            }
        });
    }
}
