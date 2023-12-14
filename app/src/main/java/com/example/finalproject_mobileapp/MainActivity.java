package com.example.finalproject_mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final List<Integer> colorViews = Arrays.asList(
            R.id.redCircle, R.id.blueCircle, R.id.yellowCircle, R.id.greenCircle);
    private final List<Integer> colorSequence = new ArrayList<>();
    private final Random random = new Random();
    private final Handler handler = new Handler();
    private HighScoreDatabaseHelper databaseHelper;
    private int score = 0;

    private int expectedIndex = 0;
    private boolean isPlaying = false;
    private int round = 1; // Track the current round
    private int sequenceLength = 4; // Initial sequence length

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new HighScoreDatabaseHelper(this);

        Button playButton = findViewById(R.id.btnPlay);
        playButton.setOnClickListener(v -> startNewGame());

        for (int colorId : colorViews) {
            View colorView = findViewById(colorId);
            colorView.setOnClickListener(v -> {
                if (isPlaying) {
                    handleUserInput(colorId);
                }
            });
        }

        startNewGame();
    }

    private void generateSequence() {
        colorSequence.clear();
        for (int i = 0; i < sequenceLength; i++) { // Use the dynamic sequence length
            int randomIndex = random.nextInt(colorViews.size());
            colorSequence.add(colorViews.get(randomIndex));
        }
    }

    private void displaySequence() {
        expectedIndex = 0;
        handler.postDelayed(new Runnable() {
            int index = 0;

            @Override
            public void run() {
                if (index < colorSequence.size()) {
                    int colorId = colorSequence.get(index);
                    View colorView = findViewById(colorId);
                    colorView.setAlpha(0.5f);

                    handler.postDelayed(() -> {
                        colorView.setAlpha(1.0f);
                        index++;
                        if (index < colorSequence.size()) {
                            handler.postDelayed(this, 1500);
                        } else {
                            isPlaying = true;
                        }
                    }, 500);
                }
            }
        }, 1000);
    }

    private void handleUserInput(int colorId) {
        if (!isPlaying || colorSequence.size() == 0 || expectedIndex >= colorSequence.size()) {
            return;
        }

        if (colorSequence.get(expectedIndex) == colorId) {
            expectedIndex++;
            if (expectedIndex == colorSequence.size()) {
                score++;
                isPlaying = false;
                generateSequence();
                handler.postDelayed(this::displaySequence, 1500);
            }
        } else {
            endGame();
        }
    }

    private void endGame() {
        databaseHelper.addHighScore(score);
        Intent gameOverIntent = new Intent(MainActivity.this, GameOverActivity.class);
        gameOverIntent.putExtra("SCORE", score);
        startActivity(gameOverIntent);
        finish();
    }

    private void startNewGame() {
        score = 0;
        generateSequence();
        displaySequence();
        isPlaying = false;

        // Increase the sequence length by 2 for the next round
        sequenceLength += 2;
        round++; // Increase the round count
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}
