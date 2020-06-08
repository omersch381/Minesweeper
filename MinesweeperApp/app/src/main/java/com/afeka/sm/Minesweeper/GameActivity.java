package com.afeka.sm.Minesweeper;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.content.Intent;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import com.example.mineswipper.R;

public class GameActivity extends AppCompatActivity implements Finals, SensorServiceListener {
    Game game;
    GridView gridView;
    TileAdapter tileAdapter;
    TextView numOfFlagsView;
    TextView levelView;
    TextView timerView;
    Timer timer;
    boolean haveTheUserClickedForTheFirstTime = false;
    int timeSoFar;
    int currentTime;
    int level;
    SharedPreferences sharedPref;
    SensorsService.SensorServiceBinder mBinder;
    boolean isBound = false;
    boolean isFirstTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        isFirstTime = true;
        sharedPref = GameActivity.this.getSharedPreferences(APP_CHOSEN_NAME, Context.MODE_PRIVATE);
        level = Objects.requireNonNull(getIntent().getExtras()).getInt(LEVEL_ACTIVITY_KEY);
        game = new Game(level);
        handleUpperLayout(level);
        handleGridView();
    }

    private void handleUpperLayout(int level) {
        handleNumOfFlagsView();
        handleLevelView(level);
    }

    private void handleNumOfFlagsView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String numOfFlags = Integer.toString(game.getBoard().getNumberOfFlags());
                String numOfFlagsLabel = getResources().getString(R.string.NumOfFlags);
                numOfFlagsView = findViewById(R.id.NumOfFlags);
                numOfFlagsView.setText(String.format("%s %s", numOfFlagsLabel, numOfFlags));
            }
        });
    }

    private void handleLevelView(int level) {
        levelView = findViewById(R.id.Level);
        switch (level) {
            case EASY_LEVEL:
                levelView.setText(R.string.EasyLevel);
                break;
            case MEDIUM_LEVEL:
                levelView.setText(R.string.MediumLevel);
                break;
            default:
                levelView.setText(R.string.HardLevel);
                break;
        }
    }

    private void handleGridView() {
        timerView = findViewById(R.id.Timer);
        gridView = findViewById(R.id.GridView);
        tileAdapter = new TileAdapter(this, game.getBoard());
        gridView.setAdapter(tileAdapter);
        gridView.setNumColumns(game.getBoard().getSize());
        handleShortClick();
        handleLongClick();
    }

    private void handleShortClick() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!haveTheUserClickedForTheFirstTime) {
                    haveTheUserClickedForTheFirstTime = true;
                    timeSoFar = 0;
                    runTimer();
                }
                playMove(position);
            }
        });
    }

    private void playMove(int position) {
        String result = game.playTile(position);
        if (!result.equals(GAME_STATUS_PLAY)) // Which means the user has won or lost
            initiateGameOverActivity(result);
        updateNumOfFlagsView(game.getBoard().getNumberOfFlags());
        tileAdapter.notifyDataSetChanged();
    }

    private void handleLongClick() { // put\remove a flag
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                game.getBoard().updateNumberOfFlags(position);
                tileAdapter.notifyDataSetChanged();
                updateNumOfFlagsView(game.getBoard().getNumberOfFlags());
                return true;
            }
        });
    }

    private void runTimer() {
        timer = new Timer();
        timer.schedule(new MineSweeperTimerTask(), 0, 1000);
    }

    class MineSweeperTimerTask extends TimerTask {
        private long firstClickTime = System.currentTimeMillis();

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    currentTime = (int) ((System.currentTimeMillis() - firstClickTime) / 1000 + timeSoFar);
                    timerView.setText(String.format("Timer:\n  %03d", currentTime));
                }
            });
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (SensorsService.SensorServiceBinder) service;
            mBinder.registerListener(GameActivity.this);
            isBound = true;
            mBinder.startSensors(isFirstTime);
            isFirstTime = false;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            mBinder.unregisterListener(GameActivity.this);
            mBinder.stopSensors();
        }
    };

    private void updateNumOfFlagsView(int numberOfFlags) {
        numOfFlagsView.setText("Num Of Flags: " + Integer.toString(numberOfFlags));
    }

    public void initiateGameOverActivity(String status) {
        Intent intent = new Intent(this, GameOverActivity.class);
        boolean hasWon = status.equals(GAME_STATUS_WIN);
        intent.putExtra(GAME_RESULT, hasWon);
        intent.putExtra(TIME_PASSED, currentTime);
        intent.putExtra(LEVEL_ACTIVITY_KEY, level);
        finishAffinity();
        this.startActivity(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();
        timeSoFar = currentTime;
        if (timer != null)
            timer.cancel();
        if (isBound) {
            mBinder.stopSensors();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (haveTheUserClickedForTheFirstTime) // so the timer will run only after the user clicks
            runTimer();
        if (isBound) {
            mBinder.startSensors(isFirstTime);
            isFirstTime = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, SensorsService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void alarmStateChanged(ALARM_STATE state, int timeSinceLastPositionChanged) {
        if (!game.getBoard().getGameStatus().equals(GAME_STATUS_PLAY))
            return;
        if (state == ALARM_STATE.ON_POSITION) { // which means the user reverted the phone to initial position
            setGridViewBackgroundColor(gridView, R.color.BackgroundColor);
        } else { // NOT_ON_POSITION
            setGridViewBackgroundColor(gridView, R.color.alarmedGridBackground);
            if (currentTime % INSERT_A_MINE_THRESHOLD == 0) { // 10, 20, 30, ...
                game.insertARandomMine();
                handleNumOfFlagsView();
            } else if (currentTime % COVER_A_TILE_THRESHOLD == 0) // 5, 15, 25, ...
                game.coverARandomTile();
        }
        handleAlarmMessage(state);
        runOnUINotifyDataSetChanged();
        if (game.getBoard().hasLost()) {
            game.getBoard().finishGame();
            timer.cancel();
        }
    }

    private void handleAlarmMessage(final ALARM_STATE state) {
        final TextView warning = findViewById(R.id.AlarmMessage);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (state == ALARM_STATE.ON_POSITION)
                    warning.setVisibility(View.GONE);
                else
                    warning.setVisibility(View.VISIBLE);
            }
        });
    }

    private void runOnUINotifyDataSetChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tileAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setGridViewBackgroundColor(final GridView gridView, final int color) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridView.setBackgroundResource(color);
            }
        });
    }
}
