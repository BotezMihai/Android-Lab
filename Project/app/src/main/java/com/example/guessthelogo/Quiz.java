package com.example.guessthelogo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Quiz implements Parcelable {
    private String question;
    private String answer;

    public Quiz(String question, String answer){
        this.question=question;
        this.answer=answer;
    }

    protected Quiz(Parcel in) {
        question = in.readString();
        answer = in.readString();
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public boolean checkAnswer(String logo, String answer){
        return this.answer.equals(answer);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    static public String messageWrongAnswer(){
        return "Wrong!";
    }

    static public String messageCorrectAnswer(){
        return "Correct!";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer);
    }
}
