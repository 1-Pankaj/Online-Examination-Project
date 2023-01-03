package com.example.onlineexamination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddTests extends AppCompatActivity {

    MaterialButton add;
    EditText questionHead, questionId,
            question, option1, option2,
            option3, option4, answer;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tests);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        add = findViewById(R.id.btnAdd);
        questionHead = findViewById(R.id.questionHead);
        questionId = findViewById(R.id.questionId);
        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        answer = findViewById(R.id.answerText);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question_head = questionHead.getText().toString();
                String Qid = questionId.getText().toString();
                String ques = question.getText().toString();
                String op1 = option1.getText().toString();
                String op2 = option2.getText().toString();
                String op3 = option3.getText().toString();
                String op4 = option4.getText().toString();
                String ans = answer.getText().toString();

                if(question_head.isEmpty() || ques.isEmpty() || Qid.isEmpty() || op1.isEmpty() || op2.isEmpty() || op3.isEmpty() || op4.isEmpty() || ans.isEmpty()){
                    questionHead.setError("Required Field");
                    questionId.setError("Required Field");
                    question.setError("Required Field");
                    option1.setError("Required Field");
                    option2.setError("Required Field");
                    option3.setError("Required Field");
                    option4.setError("Required Field");
                    answer.setError("Required Field");
                }else {

                    databaseReference.child("tests").child(question_head).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Qid)) {
                                questionId.setError("Question id already exists");
                            } else {
                                databaseReference.child("tests").child(question_head).child(Qid).child("question").setValue(ques);
                                databaseReference.child("tests").child(question_head).child(Qid).child("option 1").setValue(op1);
                                databaseReference.child("tests").child(question_head).child(Qid).child("option 2").setValue(op2);
                                databaseReference.child("tests").child(question_head).child(Qid).child("option 3").setValue(op3);
                                databaseReference.child("tests").child(question_head).child(Qid).child("option 4").setValue(op4);
                                databaseReference.child("tests").child(question_head).child(Qid).child("answer").setValue(ans);
                                Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                                int id = Integer.parseInt(Qid);
                                id = id + 1;
                                String newQid = Integer.toString(id);
                                questionId.setText(newQid);
                                question.setText("");
                                option1.setText("");
                                option2.setText("");
                                option3.setText("");
                                option4.setText("");
                                answer.setText("");
                            }
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