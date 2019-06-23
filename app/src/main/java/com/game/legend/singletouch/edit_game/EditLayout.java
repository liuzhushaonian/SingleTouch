package com.game.legend.singletouch.edit_game;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.bean.Square;
import com.game.legend.singletouch.game.GameView;
import com.game.legend.singletouch.game.SquareView;

/**
 * 编辑区域
 * 游戏方块编辑，自由移动（两块方格自由切换位置），设置边界（上下左右），改变类型（空白、普通、不可走）
 *
 *
 */
public class EditLayout extends FrameLayout {

    private EditCard dragCard;//需要移动的卡

    private ViewDragHelper viewDragHelper;

    private EditCard[][] editCards=new EditCard[3][3];

    private Square[][] squares;

    private int br,bc,positionX,positionY;//底部被移动view的行和列

    private EditCard bottomCard;

    private int unitWidth;

    private int viewSpace;

    private int row,column;


    public EditLayout(Context context) {
        super(context);
        init();
    }

    public EditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public EditLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        this.viewSpace = getResources().getDimensionPixelSize(R.dimen.view_space);

        int w = getMeasuredWidth() - 4 * this.viewSpace;

        this.unitWidth = w / 3;
    }

    protected void init() {
//        super.init();

        viewDragHelper = ViewDragHelper.create(this, 1.0f, callback1);



    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        initCards();
    }

    public Square[][] getSquares() {
        return squares;
    }


    public void setSquares(Square[][] squares) {
        this.squares = squares;

        initCards();
    }

    /**
     * 初始化卡片
     * 赋值x和y
     * 赋值坐标
     */
    private void initCards(){

        if (this.squares==null){
            return;
        }

//        Log.d("length--->>>",squares.length+"");

        for (int a=0;a<this.squares.length;a++){


            for (int t=0;t<this.squares[a].length;t++){

                EditCard view=new EditCard(getContext());

                FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(unitWidth,unitWidth);

                view.setLayoutParams(layoutParams);

                addView(view,0);

                Square square=squares[a][t];

                square.setRow(a);

                square.setColumn(t);

                view.setSquare(square);

//                Log.d("view--->>",view.toString());

                view.setOnClickListener(v -> {

                    view.click(getMeasuredWidth()/2,getMeasuredHeight()/2);

                });

                view.setOnLongClickListener(v -> {



                    return true;
                });




                //设置view的具体放置位置

                switch (a){

                    case 0://第一行

                        int l=2*viewSpace+viewSpace+t*unitWidth;

                        int top1=viewSpace+viewSpace+a*unitWidth;

                        view.layout(l,top1,t*unitWidth+unitWidth,a*unitWidth+unitWidth);

                        view.setPositionX(l+unitWidth/2);

                        view.setPositionY(t+unitWidth/2);

                        break;

                    case 1:

                        int l2=2*viewSpace+viewSpace+t*unitWidth;

                        int top=viewSpace+a*viewSpace+a*unitWidth;

                        view.layout(l2,top,t*unitWidth+unitWidth,a*unitWidth+unitWidth);

                        view.setPositionX(l2+unitWidth/2);

                        view.setPositionY(top+unitWidth/2);

                        break;


                    case 2:

                        int l3=2*viewSpace+viewSpace+t*unitWidth;

                        int top2=a*viewSpace+a*unitWidth;

                        view.layout(l3,top2,t*unitWidth+unitWidth,a*unitWidth+unitWidth);

                        view.setPositionX(l3+unitWidth/2);

                        view.setPositionY(top2+unitWidth/2);

                        break;

                }
            }
        }

    }


    ViewDragHelper.Callback callback1=new ViewDragHelper.Callback() {
        boolean ctrlHorizontal = false;
        boolean ctrlVertical = false;

        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {

//            Log.d("view--->>>",view.toString());

            return view==dragCard;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {


            if (left <= 0) {//左边边界
                return 0;
            }

//            Log.d("right--->>>",getRight()+"");

            if (left + child.getMeasuredWidth() >= getMeasuredWidth()) {//避免越过右边边界

                left = getRight() - child.getMeasuredWidth();

                return left;
            }

            return left;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {

            if (top <= 0) {//上边界
                return 0;
            }

            if (top + child.getMeasuredHeight() >= getMeasuredHeight()) {//下边界

                top = getMeasuredHeight() - child.getMeasuredHeight();

                return top;

            }


            return top;
        }


        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            /**
             * 计算当前位置，用于判断是否进入某个格子
             */

            int xx=left+unitWidth/2;//获取中心位置

            int yy=top+unitWidth/2;//获取中心位置

            //只有中心位置到了该card上方才能激活该card，激活后如何松手，将会交换二者的位置

            column = xx / unitWidth;
            row = yy / unitWidth;

            activeView(row,column);


        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            //松手后判断当前位置，以及是否有对应的bottomView，有则交换，没有就回归原位

            if (bottomCard!=null){//可以交换

                //将该view的行和列赋给底部view，同时先滑动底部view的位置到该view的原先位置，最后再将备份的底部view行和列给该view，
                // 同时给该view赋上位置，滑动到底部view原先的位置，移动用动画，麻蛋其他移动方式贼特么恶心，直接上动画完事！

                bottomCard.setRow(dragCard.getRow());
                bottomCard.setColumn(dragCard.getColumn());
                bottomCard.setPositionX(dragCard.getPositionX());
                bottomCard.setPositionY(dragCard.getPositionY());

                //计算被滑动view的原本位置

                startMove(dragCard.getPositionX(),dragCard.getPositionY());

                //滑动完成后还要把dragCard放到下面

                dragCard.setRow(br);
                dragCard.setColumn(bc);

                dragCard.setPositionX(positionX);
                dragCard.setPositionY(positionY);

                viewDragHelper.settleCapturedViewAt(positionX-unitWidth/2,positionY-unitWidth/2);

                resetValues();


            }else {//不可交换,回归原位


                viewDragHelper.settleCapturedViewAt(dragCard.getPositionX()-unitWidth/2,dragCard.getPositionY()-unitWidth/2);

                resetValues();

            }

            dragCard=null;//取消标记可移动


        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (viewDragHelper.continueSettling(true)){

            invalidate();

        }

    }

    /**
     * 激活底部的view
     * @param row 行
     * @param column 列
     */
    private void activeView(int row,int column){

        if (row<0||column<0){

            if (this.bottomCard!=null){
                this.bottomCard.inactive();
                this.bottomCard=null;
                resetValues();
            }
            return;
        }

        if (editCards==null){
            if (this.bottomCard!=null){
                this.bottomCard.inactive();
                this.bottomCard=null;
                resetValues();
            }
            return;
        }

        if (row>editCards[0].length){
            if (this.bottomCard!=null){
                this.bottomCard.inactive();
                this.bottomCard=null;
                resetValues();
            }
            return;
        }

        if (column>editCards[1].length){
            if (this.bottomCard!=null){
                this.bottomCard.inactive();
                this.bottomCard=null;
                resetValues();
            }
            return;
        }

        EditCard editCard=editCards[row][column];

        if (editCard==null){
            if (this.bottomCard!=null){
                this.bottomCard.inactive();
                this.bottomCard=null;
                resetValues();
            }
            return;
        }

        //如果已存在的底部view不为null且不等于这个获得的view，则取消当前底部view的激活状态，并将其化为新的值
        if (this.bottomCard!=null&&this.bottomCard!=editCard){

            this.bottomCard.inactive();

            this.bottomCard=editCard;

            bottomCard.active();

        }else if (bottomCard==null){

            this.bottomCard=editCard;

            bottomCard.active();

        }

        //获取行和列
        int r=editCard.getRow();
        int c=editCard.getColumn();

        setPosition(editCard.getPositionX(),editCard.getPositionY());


        setBottomMoveValue(r,c);//保存

    }

    /**
     * 保存底部被激活view的行和列
     * @param r 行
     * @param c 列
     */
    private void setBottomMoveValue(int r,int c){

        this.br=r;
        this.bc=c;

    }

    private void setPosition(int x,int y){

        this.positionX=x;
        this.positionY=y;

    }


    //重置，避免之后数据错误
    private void resetValues(){

        this.bc=0;
        this.br=0;

        this.positionY=0;

        this.positionX=0;
    }

    private void startMove(final int finalX, final int finalY){

        ObjectAnimator animator=ObjectAnimator.ofFloat(bottomCard,"translationX",bottomCard.getPositionX(),finalX);

        ObjectAnimator animator1=ObjectAnimator.ofFloat(bottomCard,"translationY",bottomCard.getPositionY(),finalY);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(animator,animator1);

        animatorSet.setDuration(250);

        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                //完成后把真正的给放过去
//                bottomCard.layout();
                int l=finalX-unitWidth/2;
                int t=finalY-unitWidth/2;
                int r=finalX+unitWidth/2;
                int b=finalY+unitWidth/2;

                bottomCard.layout(l,t,r,b);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        viewDragHelper.processTouchEvent(event);

        performClick();
//        return super.onTouchEvent(event);

        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }



}
