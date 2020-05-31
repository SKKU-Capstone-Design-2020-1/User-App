package com.skku.userweb.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.skku.userweb.R;
import com.skku.userweb.adapter.MainFragmentAdapter;
import com.skku.userweb.util.GlobalVar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "Temp";
    public static String token;
    private String store_Name;


    @BindView(R.id.main_view_pager)
    ViewPager2 viewPager;
    @BindView(R.id.main_tab_layout)
    TabLayout tabLayout;
    MainFragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // todo: change toolbar title to store name when login page give select store Activity as next page
//        GlobalVar storeName = (GlobalVar) getApplication();
//        store_Name = storeName.getStoreName();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        // todo: change toolbar title to store name
        mToolbar.setTitle("Suwon-sungkyunkwan-library");    // store name
//        mToolbar.setTitle(store_Name);    // store name
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);


        //토큰 부분
        SharedPreferences sharedPreferences = getSharedPreferences("sFile1",MODE_PRIVATE); //저장된 토큰을 불러오기 위한 셋팅
        token = sharedPreferences.getString("Token1", token); //key값과 value값으로 구분된 저장된 토큰값을 불러옴





        fragmentAdapter = new MainFragmentAdapter(this);
        viewPager.setAdapter(fragmentAdapter);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Spanned imageText = Html.fromHtml("<img src=\"icon_drag\" width=\"50\" height=\"50\">", new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String source) {
                            Drawable draw;
                            // TODO Auto-generated method stub
                            if (source.equals("icon_drag")) {
                                if(position==0){
                                    draw = getResources().getDrawable(R.drawable.seat_icon);
                                }else if(position==1){
                                    draw = getResources().getDrawable(R.drawable.timer_icon);
                                }else if(position==2){
                                    draw = getResources().getDrawable(R.drawable.suggestion_icon);
                                }else{
                                    draw = getResources().getDrawable(R.drawable.store_icon);
                                }
                                draw.setBounds(0, 0, 100, 100);
                                return draw;
                            }
                            return null;
                        }
                    }, null);
                    tab.setText(imageText);
                }
            }
        }).attach();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_action, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_account:
                Intent intent = new Intent(getApplicationContext(), EditUserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void getToken(){
        //토큰값을 받아옴
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        SharedPreferences sharedPreferences = getSharedPreferences("sFile1", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        token = task.getResult().getToken(); // 사용자가 입력한 저장할 데이터
                        editor.putString("Token1",token); // key, value를 이용하여 저장하는 형태
                        editor.commit();

                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getSharedPreferences("sFile1",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Token1",token); // key, value를 이용하여 저장하는 형태
        editor.commit();
    }


}
