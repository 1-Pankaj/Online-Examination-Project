package com.example.onlineexamination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class ForgotPassword extends AppCompatActivity {

    MaterialButton sendOTP, verifyOTP, confirm;
    EditText mobileNumber, otp, newPass;
    TextView loginHere;

    private FirebaseAuth mAuth;
    private String verificationId;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        loginHere = findViewById(R.id.loginText);
        sendOTP = findViewById(R.id.sendOTP);
        verifyOTP = findViewById(R.id.verifyOTP);
        confirm = findViewById(R.id.confirm);
        mobileNumber = findViewById(R.id.mobileNumber);
        otp = findViewById(R.id.otp);
        newPass = findViewById(R.id.newPass);

        verifyOTP.setEnabled(false);
        verifyOTP.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        confirm.setEnabled(false);
        otp.setEnabled(false);
        otp.setVisibility(View.GONE);
        newPass.setVisibility(View.GONE);
        newPass.setEnabled(false);

        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                finish();
                finishAffinity();
                startActivity(intent);
            }
        });

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mobileNumber.getText().toString();
                if(phone.isEmpty()){
                    mobileNumber.setError("Required Field");
                }
                else if(phone.length()<9 || phone.length()>10){
                    mobileNumber.setError("Invalid Phone number");
                }
                else{

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phone)) {
                                sendVerificationCode("+91" + phone);

                                verifyOTP.setEnabled(true);
                                verifyOTP.setVisibility(View.VISIBLE);
                                confirm.setVisibility(View.GONE);
                                confirm.setEnabled(false);
                                otp.setEnabled(true);
                                otp.setVisibility(View.VISIBLE);
                                newPass.setVisibility(View.GONE);
                                newPass.setEnabled(false);
                                sendOTP.setEnabled(false);
                                sendOTP.setVisibility(View.GONE);
                                mobileNumber.setVisibility(View.GONE);
                                mobileNumber.setEnabled(false);
                            }
                            else{
                                mobileNumber.setError("Phone number not registered");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    verifyOTP.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String otpText = otp.getText().toString();

                            if(otpText.isEmpty()){
                                otp.setError("Required Field");
                            }
                            else{
                                verifyCode(otpText);
                                confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String newPassText = newPass.getText().toString();

                                        if(newPassText.isEmpty()){
                                            newPass.setError("Required Field");
                                        }
                                        else{

                                            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.hasChild(phone)){
                                                        String getPass = snapshot.child(phone).child("password").getValue().toString();

                                                        if(getPass.equals(newPassText)){
                                                            newPass.setError("New password must not be same as previous one");
                                                        }
                                                        else{
                                                            databaseReference.child("users").child(phone).child("password").setValue(newPassText);
                                                            Paper.book().write(Prevalent.UserPasswordKey, "");
                                                            Paper.book().write(Prevalent.UserPhoneKey, "");
                                                            Toast.makeText(getApplicationContext(), "Password Changed, Login Again!", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                                                            finish();
                                                            startActivity(intent);
                                                        }
                                                    }
                                                    else{
                                                        Toast.makeText(getApplicationContext(), "Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(getApplicationContext(), "Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });







    }
    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            verifyOTP.setEnabled(false);
                            verifyOTP.setVisibility(View.GONE);
                            confirm.setVisibility(View.VISIBLE);
                            confirm.setEnabled(true);
                            otp.setEnabled(false);
                            otp.setVisibility(View.GONE);
                            newPass.setVisibility(View.VISIBLE);
                            newPass.setEnabled(true);
                            sendOTP.setEnabled(false);
                            sendOTP.setVisibility(View.GONE);

                        } else {
                            otp.setError("Error in OTP");
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
        }


        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            final String code = phoneAuthCredential.getSmsCode();


            if (code != null) {

                otp.setText(code);


                verifyCode(code);
            }
        }


        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithCredential(credential);

    }
}