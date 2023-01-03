package com.example.onlineexamination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity {

    Profile profile = new Profile();
    Info info = new Info();
    Dashboard dashboard = new Dashboard();
    Test test = new Test();
    String usernameData, emailData, phoneData, passwordData;
    Bundle bundle;
    int count = 0;
    boolean doubleBackToExitPressedOnce = false;

    CardView searchBtn, searchBox;
    ImageView searchBtnImage;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
        toolbar = findViewById(R.id.appBar);
        drawerLayout = findViewById(R.id.nav_layout);
        navigationView = findViewById(R.id.nav);

        searchBtn = findViewById(R.id.searchBtn);
        searchBox = findViewById(R.id.searchBox);
        searchBox.setVisibility(View.GONE);
        searchBox.setEnabled(false);
        searchBtnImage = findViewById(R.id.searchBtnImage);

        bundle = new Bundle();
        Intent intent = getIntent();
        usernameData = intent.getStringExtra("username");
        emailData = intent.getStringExtra("email");
        phoneData = intent.getStringExtra("phone");
        passwordData = intent.getStringExtra("password");
        bundle.putString("username", usernameData);
        bundle.putString("email", emailData);
        bundle.putString("phone", phoneData);
        bundle.putString("password", passwordData);
        profile.setArguments(bundle);
        info.setArguments(bundle);
        dashboard.setArguments(bundle);
        test.setArguments(bundle);

        toolbar.setTitle("Dashboard");
        openFragment(dashboard);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchBox.getVisibility() == View.GONE) {
                    searchBox.setVisibility(View.VISIBLE);
                    searchBox.setEnabled(true);
                    searchBtnImage.setImageResource(R.drawable.ic_baseline_close_24);
                }
                else{
                    searchBox.setVisibility(View.GONE);
                    searchBox.setEnabled(false);
                    searchBtnImage.setImageResource(R.drawable.ic_baseline_search_24);
                }


            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        }
        catch (Exception e){
            Log.d("tag", e.getMessage());
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                try {
                    switch (item.getItemId()) {
                        case R.id.profile:
                            toolbar.setTitle("Profile");
                            drawerLayout.close();
                            openFragment(profile);
                            break;
                        case R.id.dashboard:
                            toolbar.setTitle("Dashbaord");
                            drawerLayout.close();
                            openFragment(dashboard);
                            break;
                        case R.id.info:
                            toolbar.setTitle("About Us");
                            drawerLayout.close();
                            openFragment(info);
                            break;
                        case R.id.test:
                            toolbar.setTitle("Tests");
                            drawerLayout.close();
                            openFragment(test);
                            break;
                        default:
                            drawerLayout.close();
                            Toast.makeText(getApplicationContext(), "Error in navigation", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            finishAffinity();
        }

        openFragment(dashboard);

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 3000);

    }

    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void setActionBarTitle(String text){
        toolbar.setTitle(text);
    }
}