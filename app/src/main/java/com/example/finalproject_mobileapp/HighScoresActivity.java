package com.example.finalproject_mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.Collections;

public class HighScoresActivity extends AppCompatActivity {

    private HighScoreDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        ListView highScoresList = findViewById(R.id.highScoresList);
        Button backToMainButton = findViewById(R.id.btnBackToMain);

        databaseHelper = new HighScoreDatabaseHelper(this);

        List<Integer> highScores = databaseHelper.getHighScores();

        // Sort the high scores in descending order
        Collections.sort(highScores, Collections.reverseOrder());

        // Get the top 5 high scores or all available scores if there are fewer than 5
        int numberOfScoresToShow = Math.min(highScores.size(), 5);
        List<Integer> top5HighScores = highScores.subList(0, numberOfScoresToShow);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, top5HighScores);
        highScoresList.setAdapter(adapter);

        backToMainButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
