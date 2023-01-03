package com.example.onlineexamination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    MaterialButton back, Create;
    TextView login;
    EditText fullname, email, phone, password, ConfirmPassword;


    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        login = findViewById(R.id.login);
        back = findViewById(R.id.back);
        Create = findViewById(R.id.create);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        ConfirmPassword = findViewById(R.id.ConfirmPassword);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                finish();
                startActivity(intent);
            }
        });
        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, emailText, phoneText, passwordText, ConfirmPasswordText;
                name = fullname.getText().toString();
                emailText = email.getText().toString();
                phoneText = phone.getText().toString();
                passwordText = password.getText().toString();
                ConfirmPasswordText = ConfirmPassword.getText().toString();

                if(name.isEmpty() || emailText.isEmpty() || phoneText.isEmpty() || passwordText.isEmpty() || ConfirmPasswordText.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Required Input", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(passwordText.length()<6 && ConfirmPasswordText.length()<6){
                        password.setError("Password must be 6 characters long");
                    }
                    else if (phoneText.length()>13 || phoneText.length()<10 || phoneText.contains(getString(R.string.CharacterResource))){
                        phone.setError("Not a valid phone number");
                    }
                    else if(!isValidEmail(emailText)){
                        email.setError("Not a valid Email Address");
                    }
                    else{
                        if(passwordText.equals(ConfirmPasswordText)){

                            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.hasChild(phoneText)){
                                        phone.setError("Phone number already registered");
                                    }
                                    else{

                                        UserData userData = new UserData(name, emailText, phoneText, passwordText);

                                        databaseReference.child("users").child(phoneText).setValue(userData);
                                        databaseReference.child("users").child(phoneText).child("image").setValue("");
                                        Toast.makeText(getApplicationContext(), "User registered, Please login!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                                        finish();
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            ConfirmPassword.setError("Password do not match");
                        }
                    }
                }
            }
        });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}