package com.afeka.sm.Minesweeper;

import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mineswipper.R;

import java.util.Objects;


public class GameOverActivity extends AppCompatActivity implements Finals, InputFragment.OnDataPass {
    TextView TextResult;
    boolean win;
    SharedPreferences sharedPref;
    int timePassed;
    int level;
    String userName;
    MediaPlayer sound;
    Fragment fragmentAnimation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_layout);
        int soundToPlay;
        Intent activity = getIntent();
        sharedPref = GameOverActivity.this.getSharedPreferences(APP_CHOSEN_NAME, Context.MODE_PRIVATE);
        TextResult = findViewById(R.id.GameResult);
        win = Objects.requireNonNull(activity.getExtras()).getBoolean(GAME_RESULT);
        if (win) {
            soundToPlay = R.raw.victory;
            TextResult.setText(R.string.Win);
            level = activity.getExtras().getInt(LEVEL_ACTIVITY_KEY);
            timePassed = activity.getExtras().getInt(TIME_PASSED);
            if (hasTheUserBrokenARecord(level, timePassed))
                StartInputFragment();
        } else { // lose
            soundToPlay = R.raw.bomb;
            TextResult.setText(R.string.Lose);
            Button exitButton = findViewById(R.id.ExitButton);
            exitButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        StartAnimationFragment();
        sound = MediaPlayer.create(this, soundToPlay);
        sound.start();
    }


    public void StartNewGame(View view) {
        if (hasTheUserBrokenARecord(level, timePassed))
            updateRecords(new MineSweeperRecord(userName, timePassed), level);
        startActivity(new Intent(this, DifficultyChooserActivity.class));
    }

    private void updateRecords(MineSweeperRecord newRecord, int level) {
        MineSweeperRecord[] currentLevelRecords = getCurrentLevelRecords(level);
        if (newRecord.getTime() < currentLevelRecords[0].getTime()) { // if first place - move all the elements one index away from the first place
            System.arraycopy(currentLevelRecords, 0, currentLevelRecords, 1, NUM_OF_RECORDS_TO_SAVE - 1);
            currentLevelRecords[0] = newRecord;
        } else if (newRecord.getTime() < currentLevelRecords[1].getTime()) { // same for second place
            System.arraycopy(currentLevelRecords, 1, currentLevelRecords, 2, NUM_OF_RECORDS_TO_SAVE - 2);
            currentLevelRecords[1] = newRecord;
        } else
            currentLevelRecords[2] = newRecord;
        setCurrentLevelRecords(level, currentLevelRecords);
    }

    private MineSweeperRecord[] getCurrentLevelRecords(int level) {
        MineSweeperRecord[] currentLevelRecords = createEmptyRecords();
        switch (level) {
            case EASY_LEVEL:
                currentLevelRecords[0].setTime(sharedPref.getInt(String.valueOf(R.id.EasyFirstPlaceTime), 0));
                currentLevelRecords[0].setName(sharedPref.getString(String.valueOf(R.id.EasyFirstPlaceName), ""));
                currentLevelRecords[1].setTime(sharedPref.getInt(String.valueOf(R.id.EasySecondPlaceTime), 0));
                currentLevelRecords[1].setName(sharedPref.getString(String.valueOf(R.id.EasySecondPlaceName), ""));
                currentLevelRecords[2].setTime(sharedPref.getInt(String.valueOf(R.id.EasyThirdPlaceTime), 0));
                currentLevelRecords[2].setName(sharedPref.getString(String.valueOf(R.id.EasyThirdPlaceName), ""));
                break;
            case MEDIUM_LEVEL:
                currentLevelRecords[0].setTime(sharedPref.getInt(String.valueOf(R.id.MediumFirstPlaceTime), 0));
                currentLevelRecords[0].setName(sharedPref.getString(String.valueOf(R.id.MediumFirstPlaceName), ""));
                currentLevelRecords[1].setTime(sharedPref.getInt(String.valueOf(R.id.MediumSecondPlaceTime), 0));
                currentLevelRecords[1].setName(sharedPref.getString(String.valueOf(R.id.MediumSecondPlaceName), ""));
                currentLevelRecords[2].setTime(sharedPref.getInt(String.valueOf(R.id.MediumThirdPlaceTime), 0));
                currentLevelRecords[2].setName(sharedPref.getString(String.valueOf(R.id.MediumThirdPlaceName), ""));
                break;
            default: // which is HARD_LEVEL
                currentLevelRecords[0].setTime(sharedPref.getInt(String.valueOf(R.id.HardFirstPlaceTime), 0));
                currentLevelRecords[0].setName(sharedPref.getString(String.valueOf(R.id.HardFirstPlaceName), ""));
                currentLevelRecords[1].setTime(sharedPref.getInt(String.valueOf(R.id.HardSecondPlaceTime), 0));
                currentLevelRecords[1].setName(sharedPref.getString(String.valueOf(R.id.HardSecondPlaceName), ""));
                currentLevelRecords[2].setTime(sharedPref.getInt(String.valueOf(R.id.HardThirdPlaceTime), 0));
                currentLevelRecords[2].setName(sharedPref.getString(String.valueOf(R.id.HardThirdPlaceName), ""));
                break;
        }
        return currentLevelRecords;
    }

    private MineSweeperRecord[] createEmptyRecords() {
        MineSweeperRecord[] currentLevelRecords = new MineSweeperRecord[NUM_OF_RECORDS_TO_SAVE];
        for (int i = 0; i < NUM_OF_RECORDS_TO_SAVE; i++)
            currentLevelRecords[i] = new MineSweeperRecord();
        return currentLevelRecords;
    }

    private boolean hasTheUserBrokenARecord(int level, int time) {
        if (!win)
            return false;
        MineSweeperRecord[] currentLevelRecords = getCurrentLevelRecords(level);
        return time < currentLevelRecords[NUM_OF_RECORDS_TO_SAVE - 1].getTime(); // if smaller than last place
    }

    private void setCurrentLevelRecords(int level, MineSweeperRecord[] currentLevelRecords) {
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (level) {
            case EASY_LEVEL:
                editor.putInt(String.valueOf(R.id.EasyFirstPlaceTime), currentLevelRecords[0].getTime());
                editor.putString(String.valueOf(R.id.EasyFirstPlaceName), currentLevelRecords[0].getName());
                editor.putInt(String.valueOf(R.id.EasySecondPlaceTime), currentLevelRecords[1].getTime());
                editor.putString(String.valueOf(R.id.EasySecondPlaceName), currentLevelRecords[1].getName());
                editor.putInt(String.valueOf(R.id.EasyThirdPlaceTime), currentLevelRecords[2].getTime());
                editor.putString(String.valueOf(R.id.EasyThirdPlaceName), currentLevelRecords[2].getName());
                break;
            case MEDIUM_LEVEL:
                editor.putInt(String.valueOf(R.id.MediumFirstPlaceTime), currentLevelRecords[0].getTime());
                editor.putString(String.valueOf(R.id.MediumFirstPlaceName), currentLevelRecords[0].getName());
                editor.putInt(String.valueOf(R.id.MediumSecondPlaceTime), currentLevelRecords[1].getTime());
                editor.putString(String.valueOf(R.id.MediumSecondPlaceName), currentLevelRecords[1].getName());
                editor.putInt(String.valueOf(R.id.MediumThirdPlaceTime), currentLevelRecords[2].getTime());
                editor.putString(String.valueOf(R.id.MediumThirdPlaceName), currentLevelRecords[2].getName());
                break;
            default:
                editor.putInt(String.valueOf(R.id.HardFirstPlaceTime), currentLevelRecords[0].getTime());
                editor.putString(String.valueOf(R.id.HardFirstPlaceName), currentLevelRecords[0].getName());
                editor.putInt(String.valueOf(R.id.HardSecondPlaceTime), currentLevelRecords[1].getTime());
                editor.putString(String.valueOf(R.id.HardSecondPlaceName), currentLevelRecords[1].getName());
                editor.putInt(String.valueOf(R.id.HardThirdPlaceTime), currentLevelRecords[2].getTime());
                editor.putString(String.valueOf(R.id.HardThirdPlaceName), currentLevelRecords[2].getName());
                break;
        }
        editor.apply();
    }

    public void StartInputFragment() {
        InputFragment alertDialog = InputFragment.newInstance();
        alertDialog.show(getSupportFragmentManager(), FRAGMENT_ALERT);
    }

    public void StartAnimationFragment() {
        fragmentAnimation = new AnimationFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GAME_RESULT, win);
        fragmentAnimation.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.Animation_container, fragmentAnimation);
        transaction.commit();
    }

    @Override
    public void onDataPass(String data) {
        userName = data;
    }
}