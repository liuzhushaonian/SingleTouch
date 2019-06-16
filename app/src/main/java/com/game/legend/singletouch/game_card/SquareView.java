package com.game.legend.singletouch.game_card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.Square;
import com.game.legend.singletouch.utils.Status;

/**
 * 方格view
 * 滑动进入其时改变颜色
 * 提供自身颜色获取
 * 该view不做事件判断
 */
public class SquareView extends CardView {

    private Square square;

    private Status status= Status.INACTIVE;//标记自身是否被滑过

    private int inactiveColor=getResources().getColor(R.color.colorCha);//未激活颜色
    private int activationColor=getResources().getColor(R.color.colorPrimary);//激活颜色

    private Paint paint;

    public SquareView(Context context) {
        super(context);
//        setBackgroundColor(Color.BLUE);
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getInactiveColor() {
        return inactiveColor;
    }

    public void setInactiveColor(int inactiveColor) {
        this.inactiveColor = inactiveColor;
    }

    public int getActivationColor() {
        return activationColor;
    }

    public void setActivationColor(int activationColor) {
        this.activationColor = activationColor;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;

        init();

    }


    private void initPaint(){

        paint=new Paint();

        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

    }


    /**
     * 滑动并进入方法
     * 如果是已激活，则改为未激活
     * 如果是未激活，则改为已激活
     */
    public void enter(){

        if (square==null){
            return;
        }

        this.square.setActive(!square.isActive());

        //刷新

        refresh();

    }

    private void refresh(){

        if (square.isBlank()){
            return;
        }

        if (this.square.isActive()) {

            setElevation(10);

            setBackgroundTintList(ColorStateList.valueOf(activationColor));
//            setBackgroundColor(activationColor);
        }else {

            setElevation(0);
            setBackgroundTintList(ColorStateList.valueOf(inactiveColor));
//            setBackgroundColor(inactiveColor);
        }

    }

    /**
     * 初始化
     */
    private void init(){

        initPaint();

        setRadius(10f);

        if (square.isBlank()){
            return;
        }

        refresh();


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                postInvalidate();

                getViewTreeObserver().removeOnGlobalLayoutListener(this);



            }
        });

//        postInvalidate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (square==null){
            return;
        }

//        Log.d("draw--->>>","画画");

        paint.setColor(getResources().getColor(R.color.colorYan));

        paint.setStrokeWidth(20);

//        canvas.drawLine(20,0,20,50,paint);


        if (square.getLeft()<0){

            canvas.drawLine(0,0,0,getHeight(),paint);

        }

        if (square.getRight()<0){

            canvas.drawLine(getWidth(),0,getWidth(),getHeight(),paint);
        }

        if (square.getTop()<0){

            canvas.drawLine(0,0,getWidth(),0,paint);

        }

        if (square.getBottom()<0){

            canvas.drawLine(0,getHeight(),getWidth(),getHeight(),paint);

        }

    }

    public boolean isLeft(){

        return square.getLeft()>0;
    }

    public boolean isRight(){

        return square.getRight()>0;
    }

    public boolean isTop(){

        return square.getTop()>0;
    }

    public boolean isBottom(){

        return square.getBottom()>0;
    }






}
