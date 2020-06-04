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
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "Temp";
    public static String idToken;
    public boolean is_reserve = false;
    String store_Name;


    @BindView(R.id.main_view_pager)
    public ViewPager2 viewPager;
    @BindView(R.id.main_tab_layout)
    TabLayout tabLayout;
    MainFragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // todo: change toolbar title to store name when login page give select store Activity as next page
        GlobalVar storeName = (GlobalVar) getApplication();
        store_Name = storeName.getStoreName();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        // todo: change toolbar title to store name
//        mToolbar.setTitle("Suwon-sungkyunkwan-library");    // store name
        mToolbar.setTitle(store_Name);    // store name
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);





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


}
