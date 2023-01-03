package com.example.onlineexamination;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Profile extends Fragment {

    CardView logoutCard, verifyCard, emailCard,
            phoneCard, changePasswordCard,
            profileImage, cardImageSelect;
    TextView username, showInfoText, changeInfoText;
    CardView circleShow, circleChange;
    ImageView profileimg;
    LinearLayout layoutChange;
    ConstraintLayout layoutShow;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Bundle bundle;
    String usernameData, emailData, phoneData, passwordData;

    public Profile() {
        super(R.layout.fragment_profile);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

        try {
            if(isConnected()) {
                ProgressDialog progressDialog;
                progressDialog = ProgressDialog.show(getActivity(), "", "Fetching Data", true);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                Bundle bundle = getArguments();
                phoneData = bundle.getString("phone");
                username = view.findViewById(R.id.usernameCard);
                showInfoText = view.findViewById(R.id.showInfoText);
                changeInfoText = view.findViewById(R.id.changeInfoText);
                circleChange = view.findViewById(R.id.circleChange);
                circleShow = view.findViewById(R.id.circleShow);
                circleShow.setVisibility(View.GONE);
                showInfoText.setTextColor(ContextCompat.getColor(getActivity(), R.color.disable));
                layoutChange = view.findViewById(R.id.layoutChange);
                layoutChange.setVisibility(View.VISIBLE);
                layoutShow = view.findViewById(R.id.layoutShow);
                layoutShow.setVisibility(View.GONE);


                changeInfoText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(circleShow.getVisibility() == View.GONE){
                            showInfoText.setTextColor(ContextCompat.getColor(getActivity(), R.color.newmycolor));
                            circleShow.setVisibility(View.VISIBLE);
                            circleChange.setVisibility(View.GONE);
                            layoutChange.setVisibility(View.GONE);
                            layoutShow.setVisibility(View.VISIBLE);
                            changeInfoText.setTextColor(ContextCompat.getColor(getActivity(), R.color.disable));
                        }
                        else{
                            circleShow.setVisibility(View.GONE);
                            showInfoText.setTextColor(ContextCompat.getColor(getActivity(), R.color.disable));
                            changeInfoText.setTextColor(ContextCompat.getColor(getActivity(), R.color.newmycolor));
                            layoutChange.setVisibility(View.VISIBLE);
                            circleChange.setVisibility(View.VISIBLE);
                            layoutShow.setVisibility(View.GONE);
                        }
                    }
                });

                showInfoText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(circleShow.getVisibility() == View.GONE){
                            layoutShow.setVisibility(View.VISIBLE);
                            showInfoText.setTextColor(ContextCompat.getColor(getActivity(), R.color.newmycolor));
                            circleShow.setVisibility(View.VISIBLE);
                            circleChange.setVisibility(View.GONE);
                            layoutChange.setVisibility(View.GONE);
                            changeInfoText.setTextColor(ContextCompat.getColor(getActivity(), R.color.disable));
                        }
                        else{
                            circleShow.setVisibility(View.GONE);
                            layoutChange.setVisibility(View.VISIBLE);
                            showInfoText.setTextColor(ContextCompat.getColor(getActivity(), R.color.disable));
                            changeInfoText.setTextColor(ContextCompat.getColor(getActivity(), R.color.newmycolor));
                            circleChange.setVisibility(View.VISIBLE);
                            layoutShow.setVisibility(View.GONE);
                        }
                    }
                });
                databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(phoneData)) {
                            usernameData = snapshot.child(phoneData).child("username").getValue().toString();
                            emailData = snapshot.child(phoneData).child("email").getValue().toString();
                            String phoneDataText = snapshot.child(phoneData).child("phone").getValue().toString();
                            passwordData = snapshot.child(phoneData).child("password").getValue().toString();


                            username.setText(usernameData);
                            profileImage = view.findViewById(R.id.profileImage);
                            cardImageSelect = view.findViewById(R.id.cardImageSelect);
                            cardImageSelect.setVisibility(View.GONE);
                            cardImageSelect.setEnabled(false);
                            profileimg = view.findViewById(R.id.profileimg);
                            emailCard = view.findViewById(R.id.emailCard);
                            phoneCard = view.findViewById(R.id.phoneCard);
                            changePasswordCard = view.findViewById(R.id.changePasswordCard);
                            logoutCard = view.findViewById(R.id.logoutCard);
                            verifyCard = view.findViewById(R.id.verifyCard);
                            String imgString2 = snapshot.child(phoneData).child("image").getValue().toString();


                            if (imgString2 == "" || imgString2 == null) {
                                profileimg.setImageResource(R.drawable.profile);
                            } else {

                                if (imgString2.equals("img1"))
                                    profileimg.setImageResource(R.drawable.img1);
                                else if (imgString2.equals("img2"))
                                    profileimg.setImageResource(R.drawable.img2);
                                else if (imgString2.equals("img3"))
                                    profileimg.setImageResource(R.drawable.img3);
                                else if (imgString2.equals("img4"))
                                    profileimg.setImageResource(R.drawable.img4);
                                else if (imgString2.equals("img5"))
                                    profileimg.setImageResource(R.drawable.img5);
                                else if (imgString2.equals("img6"))
                                    profileimg.setImageResource(R.drawable.img6);
                                else if (imgString2.equals("img7"))
                                    profileimg.setImageResource(R.drawable.img7);
                                else if (imgString2.equals("img8"))
                                    profileimg.setImageResource(R.drawable.img8);
                                else if (imgString2.equals("img9"))
                                    profileimg.setImageResource(R.drawable.img9);
                                else
                                    profileimg.setImageResource(R.drawable.profile);
                            }

                            progressDialog.dismiss();
                            verifyCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), OTP_Verification.class);
                                    intent.putExtra("phone", phoneData);
                                    startActivity(intent);
                                }
                            });
                            profileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    PopupMenu popupMenu = new PopupMenu(getActivity(), profileImage);

                                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem menuItem) {

                                            int itemId = menuItem.getItemId();

                                            switch (itemId) {
                                                case R.id.opt1:
                                                    TransitionManager.beginDelayedTransition(cardImageSelect, new AutoTransition());
                                                    cardImageSelect.setVisibility(View.VISIBLE);
                                                    cardImageSelect.setEnabled(true);

                                                    ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9;
                                                    img1 = view.findViewById(R.id.img1);
                                                    img2 = view.findViewById(R.id.img2);
                                                    img3 = view.findViewById(R.id.img3);
                                                    img4 = view.findViewById(R.id.img4);
                                                    img5 = view.findViewById(R.id.img5);
                                                    img6 = view.findViewById(R.id.img6);
                                                    img7 = view.findViewById(R.id.img7);
                                                    img8 = view.findViewById(R.id.img8);
                                                    img9 = view.findViewById(R.id.img9);
                                                    MaterialButton close = view.findViewById(R.id.close);

                                                    img1.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            databaseReference.child("users").child(phoneData).child("image").setValue("img1");
                                                            profileimg.setImageResource(R.drawable.img1);
                                                            cardImageSelect.setVisibility(View.GONE);
                                                            cardImageSelect.setEnabled(false);
                                                        }
                                                    });
                                                    img2.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            databaseReference.child("users").child(phoneData).child("image").setValue("img2");
                                                            profileimg.setImageResource(R.drawable.img2);
                                                            cardImageSelect.setVisibility(View.GONE);
                                                            cardImageSelect.setEnabled(false);
                                                        }
                                                    });
                                                    img3.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            databaseReference.child("users").child(phoneData).child("image").setValue("img3");
                                                            profileimg.setImageResource(R.drawable.img3);
                                                            cardImageSelect.setVisibility(View.GONE);
                                                            cardImageSelect.setEnabled(false);
                                                        }
                                                    });
                                                    img4.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            databaseReference.child("users").child(phoneData).child("image").setValue("img4");
                                                            profileimg.setImageResource(R.drawable.img4);
                                                            cardImageSelect.setVisibility(View.GONE);
                                                            cardImageSelect.setEnabled(false);
                                                        }
                                                    });
                                                    img5.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            databaseReference.child("users").child(phoneData).child("image").setValue("img5");
                                                            profileimg.setImageResource(R.drawable.img5);
                                                            cardImageSelect.setVisibility(View.GONE);
                                                            cardImageSelect.setEnabled(false);
                                                        }
                                                    });
                                                    img6.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            databaseReference.child("users").child(phoneData).child("image").setValue("img6");
                                                            profileimg.setImageResource(R.drawable.img6);
                                                            cardImageSelect.setVisibility(View.GONE);
                                                            cardImageSelect.setEnabled(false);
                                                        }
                                                    });
                                                    img7.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            databaseReference.child("users").child(phoneData).child("image").setValue("img7");
                                                            profileimg.setImageResource(R.drawable.img7);
                                                            cardImageSelect.setVisibility(View.GONE);
                                                            cardImageSelect.setEnabled(false);
                                                        }
                                                    });
                                                    img8.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            databaseReference.child("users").child(phoneData).child("image").setValue("img8");
                                                            profileimg.setImageResource(R.drawable.img8);
                                                            cardImageSelect.setVisibility(View.GONE);
                                                            cardImageSelect.setEnabled(false);
                                                        }
                                                    });
                                                    img9.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            databaseReference.child("users").child(phoneData).child("image").setValue("img9");
                                                            profileimg.setImageResource(R.drawable.img9);
                                                            cardImageSelect.setVisibility(View.GONE);
                                                            cardImageSelect.setEnabled(false);
                                                        }
                                                    });
                                                    close.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            TransitionManager.beginDelayedTransition(cardImageSelect, new AutoTransition());
                                                            cardImageSelect.setVisibility(View.GONE);
                                                            cardImageSelect.setEnabled(false);
                                                        }
                                                    });
                                                    break;
                                                case R.id.opt2:
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                    builder.setTitle("Alert!");
                                                    builder.setMessage("Do you want to remove profile image?");
                                                    builder.setCancelable(false);
                                                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            databaseReference.child("users").child(phoneData).child("image").setValue("");
                                                            profileimg.setImageResource(R.drawable.profile);
                                                            cardImageSelect.setVisibility(View.GONE);
                                                            cardImageSelect.setEnabled(false);
                                                        }
                                                    });
                                                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();
                                                        }
                                                    });
                                                    AlertDialog alertDialog = builder.create();
                                                    alertDialog.show();
                                                    break;
                                                default:
                                                    Toast.makeText(getActivity(), "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
                                                    break;

                                            }
                                            return true;
                                        }
                                    });
                                    popupMenu.show();

                                }
                            });
                            logoutCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getActivity(), Login_Activity.class);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Alert!");
                                    builder.setMessage("Do you want to LogOut?");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            databaseReference.child("users").child(phoneData).child("image").setValue("");
                                            Paper.book().write(Prevalent.UserPhoneKey, "");
                                            Paper.book().write(Prevalent.UserPasswordKey, "");

                                            getActivity().finish();
                                            startActivity(intent);
                                        }
                                    });
                                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                }
                            });
                            emailCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), changeEmail.class);
                                    intent.putExtra("phone", phoneData);
                                    startActivity(intent);
                                }
                            });
                            phoneCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), changePhone.class);
                                    intent.putExtra("phone", phoneData);
                                    startActivity(intent);
                                }
                            });
                            changePasswordCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), changePassword.class);
                                    intent.putExtra("phone", phoneData);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Something went Wrong, Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Intent intent = new Intent(getActivity(), NoConnection.class);
                getActivity().finish();
                startActivity(intent);
            }
        }
        catch(Exception e){
            Log.d("exception", e.getMessage());
        }
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}