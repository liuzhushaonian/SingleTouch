package com.game.legend.singletouch.game_card;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.game.legend.singletouch.utils.Difficulty;

/**
 * 整个游戏view
 * 可滑动标识
 * 用于识别标识所在位置，并固定其真正的位置，不允许同时xy轴一起移动
 */
public class GameView extends FrameLayout {


    private ViewDragHelper viewDragHelper;
    private View dragView;
    private Difficulty difficulty=Difficulty.EASY;//游戏难度 3x3 or 3x4

    public GameView( Context context) {
        super(context);
        init();
    }

    public GameView( Context context,  AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView( Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GameView( Context context,AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }



    private void init(){
        viewDragHelper=ViewDragHelper.create(this,0.1f,callback);

    }

    public View getDragView() {
        return dragView;
    }

    public void setDragView(View dragView) {
        this.dragView = dragView;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        viewDragHelper.processTouchEvent(event);

        performClick();

        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }


    private ViewDragHelper.Callback callback=new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            return view==dragView;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return super.getViewHorizontalDragRange(child);
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return super.getViewVerticalDragRange(child);
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }
    };






}
