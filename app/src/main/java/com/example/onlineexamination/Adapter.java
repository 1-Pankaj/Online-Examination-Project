package com.example.onlineexamination;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Items> list;
    public Adapter (List<Items>list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        String test = list.get(position).getTest();
        String questions = list.get(position).getQuestions();
        int play = list.get(position).getPlay();
        holder.setData(test, questions, play);
        holder.nameTest.setText(test);
        holder.hidden_layout.setVisibility(View.GONE);
        holder.hidden_layout.setEnabled(false);
        holder.imageRules.setImageResource(R.drawable.rules_text_img);
        holder.cardTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.hidden_layout.getVisibility() == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(holder.cardTest, new AutoTransition());
                    holder.hidden_layout.setVisibility(View.GONE);
                    holder.hidden_layout.setEnabled(false);
                }
                else{
                    TransitionManager.beginDelayedTransition(holder.cardTest, new AutoTransition());
                    holder.hidden_layout.setVisibility(View.VISIBLE);
                    holder.hidden_layout.setEnabled(true);
                }
            }
        });
        holder.start_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String testHeader = String.valueOf(holder.nameTest.getText());
                bundle.putString("testHead", testHeader);
                Intent intent = new Intent(v.getContext(), ExamActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView test, questions;
        private ImageView play;
        private CardView cardTest;
        private LinearLayout hidden_layout;
        private ImageView imageRules;
        private TextView nameTest;
        private MaterialButton start_exam;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            test = itemView.findViewById(R.id.test);
            questions= itemView.findViewById(R.id.questions);
            play = itemView.findViewById(R.id.play);
            cardTest = itemView.findViewById(R.id.cardTest);
            hidden_layout = itemView.findViewById(R.id.hidden_layout);
            imageRules = itemView.findViewById(R.id.imageRules);
            nameTest = itemView.findViewById(R.id.nameTest);
            start_exam = itemView.findViewById(R.id.start_exam);

        }

        public void setData(String test, String questions, int play) {
            this.test.setText(test);
            this.questions.setText(questions);
            this.play.setImageResource(play);
        }
    }
}
