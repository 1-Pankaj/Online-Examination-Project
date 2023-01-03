package com.example.onlineexamination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class changeEmail extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText previousEmail, newEmail;
    MaterialButton check, change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        Intent intent = getIntent();
        String phoneData = getIntent().getExtras().getString("phone");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String emailData = snapshot.child(phoneData).child("email").getValue().toString();
                previousEmail.setText(emailData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
            }
        });

        previousEmail = findViewById(R.id.emailText);
        newEmail = findViewById(R.id.newEmailText);
        check = findViewById(R.id.btnPrevious);
        change = findViewById(R.id.btnChange);



        newEmail.setVisibility(View.GONE);
        newEmail.setEnabled(false);
        change.setVisibility(View.GONE);
        change.setEnabled(false);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String previousEmailText = previousEmail.getText().toString();
                if(previousEmailText.isEmpty()){
                   previousEmail.setError("Required field");
                }
                else if(!isValidEmail(previousEmailText)){
                    previousEmail.setError("Not a valid Email address");
                }
                else if(!previousEmailText.isEmpty() && isValidEmail(previousEmailText)){

                    if(previousEmailText.equals(previousEmail.getText().toString())){
                        newEmail.setVisibility(View.VISIBLE);
                        newEmail.setEnabled(true);
                        change.setVisibility(View.VISIBLE);
                        change.setEnabled(true);

                        previousEmail.setVisibility(View.GONE);
                        previousEmail.setEnabled(false);
                        check.setVisibility(View.GONE);
                        check.setEnabled(false);
                    }
                    else{
                        previousEmail.setError("Email doesn't matches");
                    }
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmailText = newEmail.getText().toString();

                if(newEmailText.isEmpty()){
                    newEmail.setError("Required field");
                }
                else if(!isValidEmail(newEmailText)){
                    newEmail.setError("Not a valid Email address");
                }
                else if(newEmailText.equals(previousEmail.getText().toString())){
                    newEmail.setError("New email data shouldn't be same as previous one");
                }
                else if(!newEmailText.isEmpty() && isValidEmail(newEmailText)){
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child("users").child(phoneData).child("email").setValue(newEmailText);
                            Toast.makeText(getApplicationContext(), "Email changed successfully!", Toast.LENGTH_SHORT).show();
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
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}