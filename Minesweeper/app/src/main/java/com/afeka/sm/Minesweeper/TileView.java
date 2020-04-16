package com.afeka.sm.Minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mineswipper.R;


public class TileView extends LinearLayout {
    final int TEXT_COLOR = Color.RED;
    TextView mTextView;

    public TileView(Context context) {
        super(context);
        generateTextViews(context);
    }

    private void generateTextViews(Context context) {
        mTextView = new TextView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTextView.setLayoutParams(layoutParams);
        mTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(TEXT_COLOR);
        addView(mTextView);
    }

    public void setText(char charSequence) {
        mTextView.setText(String.valueOf(charSequence));
    }

    public void setFontSize(int level) {
        switch (level) {
            case 1:
                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.easyLevelNumberSize));
                break;
            case 2:
                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.mediumLevelNumberSize));
                break;
            default:
                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hardLevelNumberSize));
                break;
        }
    }
}
