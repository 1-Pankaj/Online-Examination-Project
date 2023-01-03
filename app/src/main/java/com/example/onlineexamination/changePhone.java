package com.example.onlineexamination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class changePhone extends AppCompatActivity {


    EditText phoneText, newPhoneText;
    MaterialButton btnCheck, btnChange;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);

        Intent intent = getIntent();
        String phoneData = getIntent().getExtras().getString("phone");


        phoneText = findViewById(R.id.phoneText);
        newPhoneText = findViewById(R.id.newPhoneText);
        btnCheck = findViewById(R.id.btnPrevious);
        btnChange = findViewById(R.id.btnChange);

        phoneText.setText(phoneData);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        newPhoneText.setVisibility(View.GONE);
        newPhoneText.setEnabled(false);
        btnChange.setVisibility(View.GONE);
        btnChange.setEnabled(false);



        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneCheckText = phoneText.getText().toString();
                if(phoneCheckText.isEmpty()){
                    phoneText.setError("Required Field");
                }else if(phoneCheckText.length()<9 || phoneCheckText.length()<10){
                    phoneText.setError("Invalid phone number");
                }else if(!phoneCheckText.isEmpty() && (phoneCheckText.length()<11 || phoneCheckText.length()>9)){
                    if(phoneCheckText.equals(phoneData)){
                        newPhoneText.setVisibility(View.VISIBLE);
                        newPhoneText.setEnabled(true);
                        btnChange.setVisibility(View.VISIBLE);
                        btnChange.setEnabled(true);

                        phoneText.setVisibility(View.GONE);
                        phoneText.setEnabled(false);
                        btnCheck.setVisibility(View.GONE);
                        btnCheck.setEnabled(false);
                        btnChange.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String newPhoneTextData = newPhoneText.getText().toString();
                                if(newPhoneTextData.isEmpty()){
                                    newPhoneText.setError("Required field");
                                }
                                else if(newPhoneTextData.equals(phoneData)){
                                    newPhoneText.setError("New phone number shouldn't be same as previous one");
                                }
                                else if(!newPhoneTextData.isEmpty() && (newPhoneTextData.length()>9 || newPhoneTextData.length()<11)){
                                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            databaseReference.child("users").child(phoneCheckText).child("phone").setValue(newPhoneTextData);
                                            Toast.makeText(getApplicationContext(), "Phone number changed!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), Home.class);
                                            finish();
                                            finishAffinity();
                                            startActivity(intent);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(getApplicationContext(), "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else{
                        phoneText.setError("Phone number doesn't matches");
                    }
                }
            }
        });

    }
}