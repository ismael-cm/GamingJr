package com.example.gamingjr.model;

public class Card {

    private int id;
    private int imageResId;
    private int audioResId;
    private boolean isRevealed;

    public Card(int id, int imageResId, int audioResId) {
        this.id = id;
        this.imageResId = imageResId;
        this.audioResId = audioResId;
        this.isRevealed = false;
    }

    public int getId() {
        return id;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getAudioResId() {
        return audioResId;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }
}

