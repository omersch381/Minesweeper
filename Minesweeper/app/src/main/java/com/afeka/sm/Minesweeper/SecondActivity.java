package com.afeka.sm.Minesweeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.content.Intent;
import android.widget.GridView;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import com.example.mineswipper.R;

public class SecondActivity extends AppCompatActivity {
    final String WIN = "Win";
    final String LEVEL_ACTIVITY_KEY = "level Activity";
    final String GAME_RESULT = "GameResult";
    boolean isFirstClick = false;
    Game mGame;
    GridView mGridView;
    TileAdapter mTileAdapter;
    TextView numOfFlagsView;
    TextView levelView;
    TextView timerView;
    int level;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent activityCalled = getIntent();
        int level = activityCalled.getExtras().getInt(LEVEL_ACTIVITY_KEY);
        mGame = new Game(level);
        handleUpperLayout(level);
        mTileAdapter = new TileAdapter(this, mGame.getBoard());
        handleGridView();
    }

    private void handleUpperLayout(int level) {
        handleNumOfFlags();
        handleLevelView(level);
    }

    private void handleNumOfFlags() {
        numOfFlagsView = findViewById(R.id.NumOfFlags);
        String numOfFlags = Integer.toString(mGame.getBoard().getNumberOfFlags());
        numOfFlagsView.setText(numOfFlags);
    }

    private void handleLevelView(int level) {
        levelView = findViewById(R.id.Level);
        switch (level) {
            case 1:
                levelView.setText(R.string.EasyLevel);
                break;
            case 2:
                levelView.setText(R.string.MediumLevel);
                break;
            default:
                levelView.setText(R.string.HardLevel);
                break;
        }
    }


    private void handleGridView() {
        timerView = findViewById(R.id.Timer);
        mGridView = findViewById(R.id.gridView);
        mGridView.setAdapter(mTileAdapter);
        mGridView.setNumColumns(getNumOfColumnsByGivenLevel(level));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isFirstClick) {
                    isFirstClick = true;
                    runTimer();
                }
                String result = mGame.playTile(position);
                if (result == "Win")
                    gameOverActivity(result);
                else if (result == "Lose")
                    gameOverActivity(result);

                updateNumOfFlagsView(mGame.getBoard().getNumberOfFlags());
                mTileAdapter.notifyDataSetChanged();
            }

            private void runTimer() {
                class mineSweeperTimerTask extends TimerTask {
                    private long firstClickTime = System.currentTimeMillis();

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                int seconds = (int) ((System.currentTimeMillis() - firstClickTime) / 1000);
                                timerView.setText(String.format("%03d", seconds));
                            }
                        });
                    }
                }
                Timer timer = new Timer();
                timer.schedule(new mineSweeperTimerTask(), 0, 1000);
            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    mGame.getBoard().updateNumberOfFlags(position);
                    mTileAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateNumOfFlagsView(mGame.getBoard().getNumberOfFlags());
                return true;
            }
        });
    }

    private void updateNumOfFlagsView(int numberOfFlags) {
        numOfFlagsView.setText(Integer.toString(numberOfFlags));
    }

    public void gameOverActivity(String status) {
        if (status == WIN) {
            Intent intent = new Intent(this, ThirdActivity.class);
            intent.putExtra(GAME_RESULT, true);
            this.startActivity(intent);
        } else {
            Intent intent = new Intent(this, ThirdActivity.class);
            intent.putExtra(GAME_RESULT, false);
            this.startActivity(intent);
        }
    }

    private int getNumOfColumnsByGivenLevel(int level) {
        return mGame.getBoard().getSize();
    }
}
