package com.example.onlineexamination;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Test extends Fragment {
    public Test() {
        super(R.layout.fragment_test);
    }
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Items> list;
    Adapter adapter;
    CardView playCard;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FloatingActionButton floatingBtn;
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

        list = new ArrayList<>();

        floatingBtn = view.findViewById(R.id.floatingBtn);
        floatingBtn.setVisibility(View.GONE);
        floatingBtn.setEnabled(false);
        floatingBtn.hide();

        Bundle bundle = getArguments();

        String phoneText = bundle.getString("phone");
        Bundle newBundle = new Bundle();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(phoneText)){

                    if(phoneText.equals("9024075337")){
                        floatingBtn.setVisibility(View.VISIBLE);
                        floatingBtn.setEnabled(true);
                        floatingBtn.show();
                    }
                    else{
                        floatingBtn.setEnabled(false);
                        floatingBtn.hide();
                        floatingBtn.setVisibility(View.GONE);
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
            }
        });

        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTests.class);
                startActivity(intent);
            }
        });
        playCard = view.findViewById(R.id.playCard);

        databaseReference.child("tests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.getKey();
                    int questNo = (int) ds.getChildrenCount();
                    list.add(new Items(name, "Number of questions : "+questNo, R.drawable.ic_baseline_play_arrow_24));
                }
                recyclerView = view.findViewById(R.id.recyclerview);
                linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new Adapter(list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
            }
        });



    }

}