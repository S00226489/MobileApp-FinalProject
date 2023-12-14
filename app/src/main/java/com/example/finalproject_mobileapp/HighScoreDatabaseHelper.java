package com.example.finalproject_mobileapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class HighScoreDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HighScores.db";
    private static final int DATABASE_VERSION = 1;

    public HighScoreDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE high_scores (_id INTEGER PRIMARY KEY AUTOINCREMENT, score INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement database upgrade policy here
    }

    public void addHighScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("score", score);
        db.insert("high_scores", null, values);
        db.close();
    }

    public List<Integer> getHighScores() {
        List<Integer> highScores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT score FROM high_scores ORDER BY score DESC LIMIT 10", null);

        if (cursor != null && cursor.moveToFirst()) {
            int scoreColumnIndex = cursor.getColumnIndex("score");
            if (scoreColumnIndex != -1) {
                do {
                    int score = cursor.getInt(scoreColumnIndex);
                    highScores.add(score);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return highScores;
    }
}
