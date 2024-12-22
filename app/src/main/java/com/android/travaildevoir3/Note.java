package com.android.travaildevoir3;

public class Note {
    private String matiere;
    private int score;
    private boolean goodScore;
    private int imageResourceId; // Nouvel attribut pour l'identifiant de ressource de l'image

    public Note(String matiere, int score, boolean goodScore) {
        this.matiere = matiere;
        this.score = score;
        this.goodScore = goodScore;
        // Initialiser imageResourceId selon la valeur de goodScore
        this.imageResourceId = goodScore ? R.drawable.ic_like : R.drawable.ic_dislike;
    }

    public String getMatiere() {
        return matiere;
    }

    public int getScore() {
        return score;
    }

    public boolean isGoodScore() {
        return goodScore;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
