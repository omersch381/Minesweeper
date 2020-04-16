package com.afeka.sm.Minesweeper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.example.mineswipper.R;


public class TileAdapter extends BaseAdapter {
    final char MINE = 'X';
    final char FLAG = 'F';
    final char RED_MINE = 'R';
    private Board board;
    private Context context;

    public TileAdapter(Context context, Board board) {
        this.board = board;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tile tile = board.getTile(position);
        TileView tileView = getASquaredTile(convertView);
        tileView = handleImage(tileView, tile);
        tileView.setFontSize(board.getLevel());
        return tileView;
    }

    private TileView getASquaredTile(View convertView) {
        final TileView tileView;
        if (convertView == null)
            tileView = new TileView(context);
        else
            tileView = (TileView) convertView;

        tileView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        tileView.getViewTreeObserver().removeOnPreDrawListener(this);
                        tileView.setLayoutParams(new GridView.LayoutParams(tileView.getWidth(), tileView.getWidth()));
                        return false;
                    }
                }
        );
        return tileView;
    }

    private TileView handleImage(TileView tileView, Tile tile) {
        char currentImageToShowToUser = tile.getShowToUser();
        switch (currentImageToShowToUser) {
            case FLAG:
                tileView.setBackgroundResource(R.drawable.flag);
                break;
            case MINE:
                tileView.setBackgroundResource(R.drawable.mine);
                break;
            case RED_MINE: //which means Red - the mine which was clicked
                tileView.setBackgroundResource(R.drawable.red_mine);
                break;
            default: // Digit
                tileView.setBackgroundColor(tile.getBackgroundColor());
                tileView.setText(tile.getShowToUser());
                break;
        }
        return tileView;
    }

    @Override
    public Object getItem(int position) {
        return board.getTile(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        int boardSize = board.getBoardSize();
        return boardSize;
    }
}
