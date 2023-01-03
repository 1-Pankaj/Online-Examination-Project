package com.example.onlineexamination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(getApplicationContext());

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);


        if(isConnected()) {
            try {
                if ((UserPhoneKey == "" && UserPasswordKey == "") || (UserPasswordKey == null && UserPhoneKey == null)) {
                    Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                    startActivity(intent);
                    finish();
                } else if (!UserPhoneKey.isEmpty() && !UserPasswordKey.isEmpty()) {
                    progressDialog = ProgressDialog.show(this, "", "Logging in", true);
                    if(isConnected()) {
                        AllowAccess(UserPhoneKey, UserPasswordKey);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), NoConnection.class);
                        finish();
                        startActivity(intent);
                    }
                }
            } catch (Exception e) {
                Log.d("tag", e.getMessage());
            }
        }
        else{
            Intent intent = new Intent(getApplicationContext(), NoConnection.class);
            finish();
            startActivity(intent);
        }

    }

    private void AllowAccess(String userPhoneKey, String userPasswordKey) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(userPhoneKey)){

                    String usernameData = snapshot.child(userPhoneKey).child("username").getValue().toString();
                    String emailData = snapshot.child(userPhoneKey).child("email").getValue().toString();
                    String phoneData = snapshot.child(userPhoneKey).child("phone").getValue().toString();
                    String passwordData = snapshot.child(userPhoneKey).child("password").getValue().toString();

                    UserData userData = new UserData(usernameData, emailData, phoneData, passwordData);

                    if(passwordData.equals(userPasswordKey)){
                            String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
                            Paper.book().write(Prevalent.UserPhoneKey, userPhoneKey);
                            Paper.book().write(Prevalent.UserPasswordKey, userPasswordKey);
                            Intent intent = new Intent(getApplicationContext(), Home.class);

                            intent.putExtra("username", usernameData);
                            intent.putExtra("email", emailData);
                            intent.putExtra("phone", UserPhoneKey);
                            intent.putExtra("password", passwordData);

                            finish();
                            finishAffinity();
                            startActivity(intent);

                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                        finishAffinity();
                        finish();
                        startActivity(intent);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}