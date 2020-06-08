package com.afeka.sm.Minesweeper;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mineswipper.R;

public class AnimationFragment extends Fragment implements Finals {
    boolean hasWon = true;

    public AnimationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.animation_fragment, container, false);
    }

    @Override
    public void onStart() {
        hasWon = getArguments().getBoolean(GAME_RESULT);
        super.onStart();

        ImageView ivLoader = getActivity().findViewById(R.id.gameOverAnimation);
        int resource = hasWon ? R.drawable.win : R.drawable.lose;
        ivLoader.setBackgroundResource(resource);
        AnimationDrawable frameAnimation = (AnimationDrawable) ivLoader.getBackground();
        frameAnimation.start();
    }
}