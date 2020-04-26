package com.example.guessthelogo;

import java.util.HashMap;
import java.util.Map;

public class Quiz {
    private String question;
    private String answer;

    public Quiz(String question, String answer){
        this.question=question;
        this.answer=answer;
    }
    public boolean checkAnswer(String logo,String answer){
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
}
