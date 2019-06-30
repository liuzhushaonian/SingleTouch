package com.game.legend.singletouch.game;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.bean.Square;
import com.game.legend.singletouch.utils.Conf;
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

    private int inactiveColor=getResources().getColor(R.color.colorYingCao);//未激活颜色
    private int activationColor=getResources().getColor(R.color.colorLan);//激活颜色

    private Paint paint,circlePaint;

    private boolean isEnd=false;

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


    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
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


        circlePaint=new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(8f);

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

        //刷新

        refresh();

    }

    private void refresh(){

        if (square.getType()==Conf.NORMAL) {

            square.setType(Conf.ACTIVE);

            setElevation(6f);
            setBackgroundTintList(ColorStateList.valueOf(activationColor));


        }else if (square.getType()==Conf.ACTIVE){

            square.setType(Conf.NORMAL);
            setElevation(0);
            setBackgroundTintList(ColorStateList.valueOf(inactiveColor));


        }

    }

    /**
     * 初始化
     */
    private void init(){

        initPaint();

        setRadius(10f);

//        if (square.getType() == Conf.NORMAL) {
//            refresh();
//        }


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                postInvalidate();

                getViewTreeObserver().removeOnGlobalLayoutListener(this);



            }
        });

        initType();

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

        paint.setStrokeWidth(20);

//        canvas.drawLine(20,0,20,50,paint);


        if (square.getLeft()<0){
            paint.setColor(getResources().getColor(R.color.colorYan));

            canvas.drawLine(0,0,0,getHeight(),paint);

        }else {

            paint.setColor(Color.TRANSPARENT);

            canvas.drawLine(0,0,0,getHeight(),paint);

        }

        if (square.getRight()<0){
            paint.setColor(getResources().getColor(R.color.colorYan));

            canvas.drawLine(getWidth(),0,getWidth(),getHeight(),paint);
        }else {


            paint.setColor(Color.TRANSPARENT);
            canvas.drawLine(getWidth(),0,getWidth(),getHeight(),paint);

        }

        if (square.getTop()<0){
            paint.setColor(getResources().getColor(R.color.colorYan));

            canvas.drawLine(0,0,getWidth(),0,paint);

        }else {

            paint.setColor(Color.TRANSPARENT);
            canvas.drawLine(0,0,getWidth(),0,paint);

        }

        if (square.getBottom()<0){
            paint.setColor(getResources().getColor(R.color.colorYan));

            canvas.drawLine(0,getHeight(),getWidth(),getHeight(),paint);

        }else {

            paint.setColor(Color.TRANSPARENT);
            canvas.drawLine(0,getHeight(),getWidth(),getHeight(),paint);

        }


        //如果是结束地点，画一个圈，比开始的圈大一点
        if (isEnd){
            drawCircle(canvas,getResources().getColor(R.color.colorFei),2);

        }else {

            drawCircle(canvas,Color.TRANSPARENT,2);

        }

    }

    private void initType(){

        if (this.square==null){
            return;
        }

        switch (square.getType()){

            case Conf.NORMAL:

                setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorYingCao)));
                setElevation(0);

                break;

            case Conf.BLANK:

                setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorXiangYaBai)));
                setElevation(0);

                break;

            case Conf.UNAVAILABLE:

                setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorYa)));
                setElevation(0);

                break;

            case Conf.ACTIVE:

                setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLan)));
                setElevation(5f);

                break;

        }





    }


    /**
     * 画环
     * @param canvas 画布，在ondraw方法内调用
     */
    private void drawCircle(Canvas canvas,int color,int t){

        circlePaint.setColor(color);

        RectF oval=new RectF();

        switch (t){

            case 1:

                int s=getWidth()/4;

                oval.left=getWidth()/2f-s;
                oval.top=getHeight()/2f-s;
                oval.right=2*s+oval.left;
                oval.bottom=2*s+oval.top;

                break;


            case 2:

                int d=getWidth()/12;

                oval.left=getWidth()/2f-d;
                oval.top=getHeight()/2f-d;
                oval.right=2*d+oval.left;
                oval.bottom=2*d+oval.top;

                break;
        }

        canvas.drawArc(oval,0,360,false,circlePaint);


    }


}
