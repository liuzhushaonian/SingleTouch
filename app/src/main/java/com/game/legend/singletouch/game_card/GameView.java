package com.game.legend.singletouch.game_card;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.Square;
import com.game.legend.singletouch.utils.Difficulty;

/**
 * 整个游戏view
 * 可滑动标识
 * 用于识别标识所在位置，并固定其真正的位置，不允许同时xy轴一起移动
 */
public class GameView extends FrameLayout {


    private ViewDragHelper viewDragHelper;
    private View dragView;
    private Difficulty difficulty = Difficulty.EASY;//游戏难度 3x3 or 3x4
    private int unitWidth;//单元宽度，大致是宽度/3，但考虑到有space，所以会小于这个值

    private int viewSpace;

    boolean isHorizontal = false;//标记是否横向移动
    boolean isVertical = false;//标记是否纵向移动
    private int column;//列
    private int row;//行


    private Square[] [] squares;

    private SquareView[][] squareViews = new SquareView[3][3];//存放view，以二维数组的形式

    public Square[][] getSquares() {
        return squares;
    }

    public void setSquares(Square[][] squares) {
        this.squares = squares;

        initViews();
    }

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {



        viewDragHelper = ViewDragHelper.create(this, 1.0f, callback);

        initPaint();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        postInvalidate();

        this.dragView=findViewById(R.id.drag_view);

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

    private void resetFlag() {

        isHorizontal = false;
        isVertical = false;

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        resetFlag();

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


    /**
     * 测量
     * 获取宽度，默认为屏幕宽度，但是还是取这里测量到的数据，并获取一个单元所需的宽高，作为单元单位
     *
     * @param widthMeasureSpec  测量的宽度
     * @param heightMeasureSpec 测量的高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        this.viewSpace = getResources().getDimensionPixelSize(R.dimen.view_space);

        int w = getMeasuredWidth() - 4 * this.viewSpace;

        this.unitWidth = w / 3;


//        Log.d("unit-->>",viewSpace+"");

    }


    /**
     * 摆放方格 3x3 or 3x4
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        initViews();

    }

    /**
     * 滑动view相关操作
     */
    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        boolean ctrlHorizontal = false;
        boolean ctrlVertical = false;

        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {

            Log.d("view--->>>",dragView.toString());

            return view==dragView;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {



            if (ctrlHorizontal) {//控制水平方向

//                Log.d("ll--->>>",""+left+child.getMeasuredWidth());

                switch (column) {

                    case 0:


                        if (dx > 0 && left + child.getMeasuredWidth() >= unitWidth&&!ctrlVertical) {

                            return unitWidth - child.getMeasuredWidth();

                        }

                        break;


                    case 1://在中间

                        //左边
                        if (dx<0&&left<=unitWidth&&!ctrlVertical){


                            return unitWidth;
                        }

                        //右边
                        if (dx>0&&!ctrlVertical&&left>=2*unitWidth-child.getMeasuredWidth()){


                            return 2*unitWidth-child.getMeasuredWidth();

                        }



                        break;


                    case 2:

                        if (left<=2*unitWidth&&!ctrlVertical){
                            return 2*unitWidth;
                        }

                        break;


                }

            }


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

//            Log.d("top--->>",top+"");

//            if (isHorizontal){
//                return child.getTop();
//            }
//
//            if (Math.abs(dy)>10){
//                isVertical=true;
//            }


            //在控制范围内

//            Log.d("dy--->>>",dy+"");//向上是负，向下是正

            if (ctrlVertical){

                switch (row){

                    case 0:

                        if (dy>0&&top+child.getMeasuredHeight()>=unitWidth&&!ctrlHorizontal){

                            return unitWidth-child.getMeasuredHeight();

                        }

                        break;
                    case 1:

                        if (dy<0&&top<=unitWidth&&!ctrlHorizontal){


                            return unitWidth;

                        }

                        if (dy>0&&2*unitWidth<top+child.getMeasuredHeight()&&!ctrlHorizontal){

                            return 2*unitWidth-child.getMeasuredHeight();

                        }


                        break;
                    case 2:

                        if (dy<0&&top<=2*unitWidth&&!ctrlHorizontal){
                            return 2*unitWidth;
                        }

                        break;


                }


            }




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
             * 计算是否在两个中间
             * 判断unitSpace-left<left<unitSpace、2*unitSpace-left<left<2*unitSpace
             * unitSpace-top<top<unitSpace、2*unitSpace-top<top<2*unitSpace
             */

            /**
             * 计算当前位置，用于判断是否进入某个格子
             */

            column = left / unitWidth;
            row = top / unitWidth;


            switch (column){

                case 0:



                    ctrlVertical = unitWidth - left < changedView.getMeasuredWidth();

                    break;

                case 1:

                    ctrlVertical = 2 * unitWidth - left < changedView.getMeasuredWidth();


                    break;

                case 2:
                    ctrlVertical=false;

                    break;

            }


            switch (row){

                case 0:


                    ctrlHorizontal = unitWidth - top < changedView.getMeasuredHeight();

                    break;

                case 1:

                    ctrlHorizontal = 2 * unitWidth - top < changedView.getMeasuredHeight();



                    break;

                case 2:

                    ctrlHorizontal=false;

                    break;


            }

//            Log.d("ctrlHorizontal-->>>", "" + ctrlHorizontal);
//            Log.d("ctrlVertical-->>>", "" + ctrlVertical);



//
//            Log.d("column--->>",""+column);
//            Log.d("row--->>",""+row);

        }
    };


    Paint paint;


    private void initPaint() {

        paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStrokeWidth(5);


    }



    private void initViews(){

        if (this.squares==null){
            return;
        }

        for (int a=0;a<this.squares.length;a++){


            for (int t=0;t<this.squares[a].length;t++){

                SquareView view=new SquareView(getContext());

                FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(unitWidth,unitWidth);

                view.setLayoutParams(layoutParams);

                addView(view,0);

                view.setSquare(squares[a][t]);


                //设置view的具体放置位置

                switch (a){

                    case 0://第一行

                        int l=2*viewSpace+viewSpace+t*unitWidth;

                        int top1=viewSpace+viewSpace+a*unitWidth;

                        view.layout(l,top1,t*unitWidth+unitWidth,a*unitWidth+unitWidth);

                        break;

                    case 1:

                        int l2=2*viewSpace+viewSpace+t*unitWidth;

                        int top=viewSpace+a*viewSpace+a*unitWidth;

                        view.layout(l2,top,t*unitWidth+unitWidth,a*unitWidth+unitWidth);


                        break;


                    case 2:

                        int l3=2*viewSpace+viewSpace+t*unitWidth;

                        int top2=a*viewSpace+a*unitWidth;

                        view.layout(l3,top2,t*unitWidth+unitWidth,a*unitWidth+unitWidth);

                        break;

                }
            }
        }
    }





}
