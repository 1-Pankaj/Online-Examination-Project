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

public class changePassword extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText oldPass, newPass;
    MaterialButton btnCheck, btnChange;
    TextView forgotText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Intent intent = getIntent();
        String phoneData = getIntent().getExtras().getString("phone");
        String passwordData = getIntent().getExtras().getString("password");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        oldPass = findViewById(R.id.passText);
        newPass = findViewById(R.id.newPassText);
        btnCheck = findViewById(R.id.btnPrevious);
        btnChange = findViewById(R.id.btnChange);
        forgotText = findViewById(R.id.forgotText);

        newPass.setVisibility(View.GONE);
        newPass.setEnabled(false);
        btnChange.setVisibility(View.GONE);
        btnChange.setEnabled(false);

        forgotText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = oldPass.getText().toString();

                if(oldPassword.isEmpty()){
                    oldPass.setError("Required Field");
                }
                else if(!oldPassword.equals(passwordData)){
                    oldPass.setError("Invalid password");
                }
                else if(oldPassword.equals(passwordData)){

                    newPass.setVisibility(View.VISIBLE);
                    newPass.setEnabled(true);
                    btnChange.setVisibility(View.VISIBLE);
                    btnChange.setEnabled(true);

                    oldPass.setVisibility(View.GONE);
                    oldPass.setEnabled(false);
                    btnCheck.setVisibility(View.GONE);
                    btnCheck.setEnabled(false);
                }
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPass.getText().toString();

                if(newPassword.isEmpty()){
                    newPass.setError("Required Field");
                }
                else if(newPassword.length()<6){
                    newPass.setError("Password must be 6 characters long");
                }
                else if(newPassword.equals(passwordData)){
                    newPass.setError("New password must be different from old password");
                }
                else if(!newPassword.equals(passwordData)){
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            databaseReference.child("users").child(phoneData).child("password").setValue(newPassword);
                            Toast.makeText(getApplicationContext(), "Password changed, Login again!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
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
}