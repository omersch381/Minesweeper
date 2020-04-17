package com.afeka.sm.Minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.example.mineswipper.R;


public class ThirdActivity extends AppCompatActivity {
    final String GAME_RESULT = "GameResult";
    TextView TextResult;
    boolean Win;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);
        Intent activity = getIntent();
        Win = activity.getExtras().getBoolean(GAME_RESULT);
        if (Win) {
            TextResult = findViewById(R.id.GameResult);
            TextResult.setText(R.string.Win);
        } else {
            TextResult = findViewById(R.id.GameResult);
            TextResult.setText(R.string.Lose);
        }
    }

    public void StartNewGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}
