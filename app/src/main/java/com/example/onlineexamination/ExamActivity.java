package com.example.onlineexamination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class ExamActivity extends AppCompatActivity {

    MaterialButton start;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    CheckBox checkBoxExam;
    TextView testNametext, testNameExam;
    CardView rulesCard;


    CardView option1Card, option2Card, option3Card, option4Card;
    TextView option1Text, option2Text, option3Text, option4Text, ques_no, question;
    MaterialButton nextBtn;
    ImageView selection_circle_1, selection_circle_2, selection_circle_3, selection_circle_4;
    ImageView backExam;
    int testId = 0;
    String option1_db, option2_db, option3_db, option4_db, ques_db, answer_db;
    TextView textBool;
    int points = 0;
    int num_children = 0;

    TextView timerTextView;
    long startTime = 0;
    String answeredOptionText;



    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        option1Card = findViewById(R.id.option1Card);
        option2Card = findViewById(R.id.option2Card);
        option3Card = findViewById(R.id.option3Card);
        option4Card = findViewById(R.id.option4Card);

        option1Text = findViewById(R.id.option1Text);
        option2Text = findViewById(R.id.option2Text);
        option3Text = findViewById(R.id.option3Text);
        option4Text = findViewById(R.id.option4Text);
        ques_no = findViewById(R.id.ques_no);
        question = findViewById(R.id.question);
        textBool = findViewById(R.id.textBool);

        selection_circle_1 = findViewById(R.id.selection_circle_1);
        selection_circle_2 = findViewById(R.id.selection_circle_2);
        selection_circle_3 = findViewById(R.id.selection_circle_3);
        selection_circle_4 = findViewById(R.id.selection_circle_4);

        nextBtn = findViewById(R.id.nextBtn);



        backExam = findViewById(R.id.backExam);
        testNameExam = findViewById(R.id.testNameExam);
        timerTextView = findViewById(R.id.timerTextView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        testNametext = findViewById(R.id.testNameText);
        rulesCard = findViewById(R.id.rulesCard);

        Intent intent = getIntent();
        String testHead = intent.getStringExtra("testHead");
        start = findViewById(R.id.startExamBtn);
        checkBoxExam = findViewById(R.id.checkBoxRules);
        String phoneText = Paper.book().read(Prevalent.UserPhoneKey);
        start.setEnabled(false);
        rulesCard.setVisibility(View.VISIBLE);
        rulesCard.setEnabled(true);
        start.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.disable));
        testNametext.setText(testHead);
        testNameExam.setText(testHead);


        databaseReference.child("tests").child(testHead).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                num_children = (int) snapshot.getChildrenCount();
                testId = testId+1;
                ques_no.setText("Question "+testId+"/"+num_children);
                ques_db = snapshot.child(String.valueOf(testId)).child("question").getValue().toString();
                question.setText(ques_db);
                option1_db = snapshot.child(String.valueOf(testId)).child("option 1").getValue().toString();
                option2_db = snapshot.child(String.valueOf(testId)).child("option 2").getValue().toString();
                option3_db = snapshot.child(String.valueOf(testId)).child("option 3").getValue().toString();
                option4_db = snapshot.child(String.valueOf(testId)).child("option 4").getValue().toString();
                answer_db = snapshot.child(String.valueOf(testId)).child("answer").getValue().toString();

                option1Text.setText(option1_db);
                option2Text.setText(option2_db);
                option3Text.setText(option3_db);
                option4Text.setText(option4_db);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        option1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textBool.setText("op1");
                option1Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_newcolor));
                option2Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                option3Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                option4Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                selection_circle_1.setImageResource(R.drawable.selected_circle);
                selection_circle_2.setImageResource(R.drawable.non_select_circle);
                selection_circle_3.setImageResource(R.drawable.non_select_circle);
                selection_circle_4.setImageResource(R.drawable.non_select_circle);

                option1Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                option2Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                option3Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                option4Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));

            }
        });
        option2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textBool.setText("op2");
                option2Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_newcolor));
                option1Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                option3Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                option4Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                selection_circle_2.setImageResource(R.drawable.selected_circle);
                selection_circle_1.setImageResource(R.drawable.non_select_circle);
                selection_circle_3.setImageResource(R.drawable.non_select_circle);
                selection_circle_4.setImageResource(R.drawable.non_select_circle);

                option2Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                option1Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                option3Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                option4Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
            }
        });
        option3Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textBool.setText("op3");
                option3Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_newcolor));
                option1Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                option2Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                option4Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                selection_circle_3.setImageResource(R.drawable.selected_circle);
                selection_circle_1.setImageResource(R.drawable.non_select_circle);
                selection_circle_2.setImageResource(R.drawable.non_select_circle);
                selection_circle_4.setImageResource(R.drawable.non_select_circle);

                option3Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                option1Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                option2Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                option4Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));

            }
        });
        option4Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textBool.setText("op4");
                option4Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_newcolor));
                option1Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                option2Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                option3Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                selection_circle_4.setImageResource(R.drawable.selected_circle);
                selection_circle_2.setImageResource(R.drawable.non_select_circle);
                selection_circle_1.setImageResource(R.drawable.non_select_circle);
                selection_circle_3.setImageResource(R.drawable.non_select_circle);

                option4Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                option2Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                option1Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                option3Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(testId>=num_children){
                    databaseReference.child("tests").child(testHead).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String textOption = textBool.getText().toString();
                            if (textOption.equals("op1")) {
                                answeredOptionText = option1Text.getText().toString();
                                if (answer_db.equals(answeredOptionText)) {
                                    points = points + 1;

                                    option1Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                    selection_circle_1.setImageResource(R.drawable.non_select_circle);
                                    option1Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                                }
                            } else if (textOption.equals("op2")) {
                                answeredOptionText = option2Text.getText().toString();
                                if (answer_db.equals(answeredOptionText)) {
                                    points = points + 1;
                                    option2Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                    selection_circle_2.setImageResource(R.drawable.non_select_circle);
                                    option2Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                                }
                            } else if (textOption.equals("op3")) {
                                answeredOptionText = option3Text.getText().toString();
                                if (answer_db.equals(answeredOptionText)) {
                                    points = points + 1;
                                    option3Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                    selection_circle_3.setImageResource(R.drawable.non_select_circle);
                                    option3Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                                }
                            } else if (textOption.equals("op4")) {
                                answeredOptionText = option4Text.getText().toString();
                                if (answer_db.equals(answeredOptionText)) {
                                    points = points + 1;
                                    option4Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                    selection_circle_4.setImageResource(R.drawable.non_select_circle);
                                    option4Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Select an option first", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    databaseReference.child("tests").child(testHead).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            num_children = (int) snapshot.getChildrenCount();

                            String textOption = textBool.getText().toString();
                            if (textOption.equals("op1")) {
                                answeredOptionText = option1Text.getText().toString();
                                if (answer_db.equals(answeredOptionText)) {
                                    points = points + 1;

                                    option1Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                    selection_circle_1.setImageResource(R.drawable.non_select_circle);
                                    option1Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                                }
                            } else if (textOption.equals("op2")) {
                                answeredOptionText = option2Text.getText().toString();
                                if (answer_db.equals(answeredOptionText)) {
                                    points = points + 1;
                                    option2Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                    selection_circle_2.setImageResource(R.drawable.non_select_circle);
                                    option2Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                                }
                            } else if (textOption.equals("op3")) {
                                answeredOptionText = option3Text.getText().toString();
                                if (answer_db.equals(answeredOptionText)) {
                                    points = points + 1;
                                    option3Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                    selection_circle_3.setImageResource(R.drawable.non_select_circle);
                                    option3Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                                }
                            } else if (textOption.equals("op4")) {
                                answeredOptionText = option4Text.getText().toString();
                                if (answer_db.equals(answeredOptionText)) {
                                    points = points + 1;
                                    option4Card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                    selection_circle_4.setImageResource(R.drawable.non_select_circle);
                                    option4Text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_text));
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Select an option first", Toast.LENGTH_SHORT).show();
                            }

                            testId = testId + 1;
                            ques_no.setText("Question " + testId + "/" + num_children);
                            ques_db = snapshot.child(String.valueOf(testId)).child("question").getValue().toString();
                            question.setText(ques_db);
                            option1_db = snapshot.child(String.valueOf(testId)).child("option 1").getValue().toString();
                            option2_db = snapshot.child(String.valueOf(testId)).child("option 2").getValue().toString();
                            option3_db = snapshot.child(String.valueOf(testId)).child("option 3").getValue().toString();
                            option4_db = snapshot.child(String.valueOf(testId)).child("option 4").getValue().toString();
                            answer_db = snapshot.child(String.valueOf(testId)).child("answer").getValue().toString();

                            option1Text.setText(option1_db);
                            option2Text.setText(option2_db);
                            option3Text.setText(option3_db);
                            option4Text.setText(option4_db);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        backExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
                builder.setTitle("Alert!");
                builder.setMessage("Exiting will cancel current Exam, Are you sure?");
                builder.setCancelable(false);
                builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        checkBoxExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxExam.isChecked()){
                    start.setEnabled(true);
                    start.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.newmycolor));
                }else{
                    start.setEnabled(false);
                    start.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.disable));
                }
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (checkBoxExam.isChecked()) {
                        TransitionManager.beginDelayedTransition(rulesCard, new AutoTransition());
                        rulesCard.setVisibility(View.GONE);
                        rulesCard.setEnabled(false);
                        startTime = System.currentTimeMillis();
                        timerHandler.postDelayed(timerRunnable, 0);
                    } else {
                        Toast.makeText(getApplicationContext(), "Check the Box", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Log.d("tag", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
        builder.setTitle("Alert!");
        builder.setMessage("Exiting will cancel current Exam, Are you sure?");
        builder.setCancelable(false);
        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}