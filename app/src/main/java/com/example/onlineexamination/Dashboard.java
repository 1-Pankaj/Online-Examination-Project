package com.example.onlineexamination;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Dashboard extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Test test = new Test();
    Profile profile = new Profile();
    Info info = new Info();

    Bundle bundle;

    CardView testCard, feedCard, profileCard, progressionCard, aboutCard, moreOptionsCard;

    public Dashboard() {
        super(R.layout.fragment_dashboard);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {


        bundle = getArguments();

        String phoneData = bundle.getString("phone");
        String emailData = bundle.getString("email");
        String usernameData = bundle.getString("username");
        String passwordData = bundle.getString("password");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Bundle newBundle = new Bundle();
        newBundle.putString("phone", phoneData);
        newBundle.putString("email", emailData);
        newBundle.putString("username", usernameData);
        newBundle.putString("password", passwordData);
        profile.setArguments(newBundle);
        test.setArguments(newBundle);



        testCard = view.findViewById(R.id.testCard);
        feedCard = view.findViewById(R.id.feedCard);
        profileCard = view.findViewById(R.id.profileCard);
        progressionCard = view.findViewById(R.id.progressionCard);
        aboutCard = view.findViewById(R.id.aboutCard);
        moreOptionsCard = view.findViewById(R.id.moreOptionsCard);


        testCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(test);
                ((Home) getActivity()  ).setActionBarTitle("Tests");
            }
        });
        feedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "example@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Enter your feedback");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                   openFragment(profile);
                    ((Home) getActivity()  ).setActionBarTitle("Profile");
                }
                catch (Exception e){
                    Log.d("tag", e.getMessage());
                }
            }
        });
        aboutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(info);
                ((Home) getActivity()  ).setActionBarTitle("About Us");
            }
        });
    }
    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}