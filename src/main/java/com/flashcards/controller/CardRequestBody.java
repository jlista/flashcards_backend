package com.flashcards.controller;

import java.io.Serializable;

public class CardRequestBody implements Serializable {
    private String hint;
    private String answer;

    public CardRequestBody(String hint, String answer) {
        this.hint = hint;
        this.answer = answer;
    }

    public String getHint() { return this.hint; }
    public String getAnswer() { return this.answer; }

    public void setHint(String hint) { this.hint = hint; }
    public void setAnswer(String answer) { this.answer = answer; }
}
