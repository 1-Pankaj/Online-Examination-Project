package com.example.onlineexamination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class OTP_Verification extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String verificationId;
    EditText PhoneText, OTP;
    MaterialButton verifyBtn, continueBtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        Intent intent = getIntent();
        String phoneData = getIntent().getExtras().getString("phone");


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();

        PhoneText = findViewById(R.id.PhoneText);
        PhoneText.setText(phoneData);
        verifyBtn = findViewById(R.id.verifyBtn);
        continueBtn = findViewById(R.id.continueBtn);
        OTP = findViewById(R.id.OTP);

        OTP.setVisibility(View.GONE);
        OTP.setEnabled(false);

        continueBtn.setEnabled(false);
        continueBtn.setVisibility(View.GONE);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = PhoneText.getText().toString();
                if(TextUtils.isEmpty(phoneNo)){
                    PhoneText.setError("Required Field");
                }
                else if(phoneNo.length()<9 || phoneNo.length()>13){
                    PhoneText.setError("Invalid Phone Number");
                }
                else if(!phoneNo.isEmpty() && phoneNo.length()>9 && phoneNo.length()<13){
                    continueBtn.setEnabled(true);
                    continueBtn.setVisibility(View.VISIBLE);
                    OTP.setEnabled(true);
                    OTP.setVisibility(View.VISIBLE);

                    PhoneText.setVisibility(View.GONE);
                    PhoneText.setEnabled(false);
                    verifyBtn.setEnabled(false);
                    verifyBtn.setVisibility(View.GONE);

                    String phone = "+91" + phoneNo;
                    sendVerificationCode(phone);

                }
                else{
                    Toast.makeText(getApplicationContext(), "Unknown Parse Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = OTP.getText().toString();
                if (TextUtils.isEmpty(otp)) {
                    OTP.setError("Please Enter OTP");
                } else {
                    verifyCode(otp);
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

                            Intent i = new Intent(getApplicationContext(), Home.class);
                            startActivity(i);
                            finish();
                        } else {

                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
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

                OTP.setText(code);


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