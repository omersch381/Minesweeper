package com.afeka.sm.Minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mineswipper.R;


public class MainActivity extends AppCompatActivity {
    final String LEVEL_ACTIVITY_KEY = "level Activity";
    final int EASY = 1;
    final int MEDIUM = 2;
    final int HARD = 3;
    int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
    }

    public void StartGame(View view) {
        Intent getNameScreenIntent = new Intent(this, SecondActivity.class);
        switch (view.getId()) {
            case R.id.Easy:
                level = EASY;
                break;
            case R.id.Medium:
                level = MEDIUM;
                break;
            case R.id.Hard:
                level = HARD;
                break;
        }
        getNameScreenIntent.putExtra(LEVEL_ACTIVITY_KEY, level);
        startActivity(getNameScreenIntent);
    }
}