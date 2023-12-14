package com.example.finalproject_mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView scoreTextView = findViewById(R.id.tvScore);
        Button playAgainButton = findViewById(R.id.btnPlayAgain);
        Button seeHighScoresButton = findViewById(R.id.btnSeeHighScores);

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreTextView.setText(getString(R.string.your_score_was, score));

        playAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        seeHighScoresButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, HighScoresActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
