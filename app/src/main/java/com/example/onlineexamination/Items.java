package com.example.onlineexamination;

public class Items {
    private String test, questions;
    private int play;

    Items(String test, String questions, int play){
        this.test = test;
        this.questions = questions;
        this.play = play;
    }

    public String getTest() {
        return test;
    }

    public String getQuestions() {
        return questions;
    }

    public int getPlay() {
        return play;
    }
}
