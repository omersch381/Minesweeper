package com.afeka.sm.Minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.mineswipper.R;


public class GameOverActivity extends AppCompatActivity implements Finals {
    TextView TextResult;
    boolean Win;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_layout);
        Intent activity = getIntent();
        Win = activity.getExtras().getBoolean(GAME_RESULT);
        if (Win) {
            TextResult = findViewById(R.id.GameResult);
            TextResult.setText(R.string.Win);
        } else {
            TextResult = findViewById(R.id.GameResult);
            TextResult.setText(R.string.Lose);
        }

        Button exitButton = findViewById(R.id.ExitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void StartNewGame(View view) {
        Intent intent = new Intent(this, DifficultyChooserActivity.class);
        this.startActivity(intent);
    }
}
