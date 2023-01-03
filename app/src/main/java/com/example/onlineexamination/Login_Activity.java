package com.example.onlineexamination;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login_Activity extends AppCompatActivity {

    MaterialButton login, close, btnContinue;
    CardView cardView;
    TextView forgot, signupText;
    EditText phone, password;
    TextView signup;
    CheckBox rememberMe;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

            rememberMe = findViewById(R.id.rememberMe);
            phone = findViewById(R.id.phone);
            cardView = findViewById(R.id.loginCard);
            cardView.setVisibility(GONE);
            signupText = findViewById(R.id.textSignUp);
            cardView.setEnabled(false);
            btnContinue = findViewById(R.id.btnContinue);
            signup = findViewById(R.id.textSignUpHere);
            login = findViewById(R.id.login);
            close = findViewById(R.id.close);
            password = findViewById(R.id.pass);
            forgot = findViewById(R.id.forgot);

            forgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                    finish();
                    startActivity(intent);
                }
            });


            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Signup.class);
                    finish();
                    startActivity(intent);
                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    cardView.setEnabled(true);
                    cardView.setVisibility(View.VISIBLE);
                    signup.setEnabled(false);
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    cardView.setVisibility(GONE);
                    cardView.setEnabled(false);
                    signup.setEnabled(true);
                }
            });
            signupText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Signup.class);
                    finish();
                    startActivity(intent);
                }
            });
            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        String phoneText = phone.getText().toString();
                        String passwordText = password.getText().toString();
                        if (phoneText.isEmpty() || passwordText.isEmpty()) {
                            phone.setError("Required Field");
                            password.setError("Required Field");
                        } else if (passwordText.length() < 6) {
                            password.setError("Invalid password");
                        } else if (phoneText.length() > 10 || phoneText.length() < 10) {
                            phone.setError("Invalid Email address");
                        } else {
                            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.hasChild(phoneText)) {

                                        String usernameData = snapshot.child(phoneText).child("username").getValue().toString();
                                        String emailData = snapshot.child(phoneText).child("email").getValue().toString();
                                        String phoneData = snapshot.child(phoneText).child("phone").getValue().toString();
                                        String passwordData = snapshot.child(phoneText).child("password").getValue().toString();

                                        UserData userData = new UserData(usernameData, emailData, phoneData, passwordData);

                                        if (passwordData.equals(passwordText)) {
                                            if (rememberMe.isChecked()) {

                                                Paper.book().write(Prevalent.UserPhoneKey, phoneText);
                                                Paper.book().write(Prevalent.UserPasswordKey, passwordText);
                                                Intent intent = new Intent(getApplicationContext(), Home.class);

                                                intent.putExtra("username", usernameData);
                                                intent.putExtra("email", emailData);
                                                intent.putExtra("phone", phoneText);
                                                intent.putExtra("password", passwordData);


                                                finish();
                                                finishAffinity();
                                                startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(getApplicationContext(), Home.class);

                                                intent.putExtra("username", usernameData);
                                                intent.putExtra("email", emailData);
                                                intent.putExtra("phone", phoneData);
                                                intent.putExtra("password", passwordData);

                                                finish();
                                                finishAffinity();
                                                startActivity(intent);
                                            }
                                        } else {
                                            password.setError("Error in password");
                                        }
                                    } else {
                                        phone.setError("Phone number not registered");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        Log.d("connection", e.getMessage());
                    }
                }
            });
        }
        catch (Exception e){
            Log.d("tag", e.getMessage());
        }
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
        builder.setTitle("Alert!");
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

