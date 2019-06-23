package com.game.legend.singletouch.edit_game;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.bean.Square;
import com.game.legend.singletouch.game.SquareView;
import com.game.legend.singletouch.utils.Conf;

/**
 * 设置边界
 * 改变形态
 *
 */
public class EditCard extends CardView {


    private int positionX;//记录该view的X值，用于改变位置

    private int positionY;//记录该view的Y值，用于改变位置

    private int activeColor;

    private int color;

    private Square square;

    private boolean canHorizontal = false;

    private boolean canVertical = false;
    int position=0;
    int p;
    int x = 0, y = 0, dx, dy;
    int oneThird;//获取三分之一的宽度、高度

    int limitLeft=0,limitRight=0,limitTop=0,limitBottom=0;

    private Paint paint;

    boolean flag=false;

    boolean isScale=false;


    private int px,py;


    public EditCard(Context context) {
        super(context);
        init();
    }

    public EditCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.oneThird=getWidth()/3;
    }


    public Square getSquare() {
        return square;
    }


    public void setSquare(Square square) {
        this.square = square;

        if (square.getLeft()<0){
            threadShow(10);
        }

        if (square.getTop()<0){
            threadShow(20);
        }

        if (square.getRight()<0){
            threadShow(30);
        }

        if (square.getBottom()<0){
            threadShow(40);
        }

        initType();

    }

    //用于外部改变位置后改变顺序
    public int getRow() {

        return square.getRow();

    }

    public int getColumn() {

        return square.getColumn();
    }

    public void setRow(int row) {

        square.setRow(row);

    }

    public void setColumn(int column) {

        square.setColumn(column);

    }

    private void init() {

        paint=new Paint();

        activeColor = getResources().getColor(R.color.colorBiLv);

        color = getResources().getColor(R.color.colorSu);

        inactive();

    }


    /**
     * 移动激活
     * 颜色变为激活色
     * 抬高6f
     */
    public void active() {

        setBackgroundTintList(ColorStateList.valueOf(activeColor));

        setElevation(6f);

    }

    /**
     * 取消激活
     * 颜色恢复默认
     * 将抬高值设为0
     **/
    public void inactive() {

        setBackgroundTintList(ColorStateList.valueOf(color));
        setElevation(0);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        oneThird=getWidth()/3;




        /**
         * 触摸设置颜色
         * 1/3处
         */
        switch (action) {

            case MotionEvent.ACTION_DOWN:


                x = (int) event.getX();

                y = (int) event.getY();

                canHorizontal = y <= oneThird || y >= 2 * oneThird;

                canVertical = x <= oneThird || x >= 2 * oneThird;



                if (x < oneThird && y < oneThird) {//左上角

                    position=10;

                    flag=true;

//                    Log.d("position-->>",position+"左上角");

                } else if (x < oneThird && y > 2 * oneThird) {//左下角

                    position=20;

                    flag=true;

//                    Log.d("position-->>",position+"左下角");

                } else if (x > 2 * oneThird && y < oneThird) {//右上角
                    position=30;

                    flag=true;
//                    Log.d("position-->>",position+"右上角");

                } else if (x > 2 * oneThird && y > 2 * oneThird) {//右下角
                    position=40;

                    flag=true;
//                    Log.d("position-->>",position+"右下角");

                }

                if (flag){
                    return true;
                }

                break;


            case MotionEvent.ACTION_MOVE:

                dx = (int) event.getX() - x;

//                Log.d("dx--->>",dx+"");

                dy = (int) (event.getY() - y);

//                Log.d("dy--->>",dy+"");


                getTheP();

                if (flag){
                    return true;
                }

                break;


            case MotionEvent.ACTION_UP:


                goStart();

                resetFlag();

                if (flag){
                    return true;
                }


        }

        return super.onTouchEvent(event);
    }


    private void resetFlag(){

        position=0;
        p=0;
        canHorizontal=false;
        canVertical=false;
        x=0;
        y=0;
        dx=0;
        dy=0;
        flag=false;

    }

    private void getTheP(){

        oneThird=getWidth()/3;

        //共同区域
        if (canHorizontal && canVertical) {

            switch (position){

                case 10://左上角


                    if (Math.abs(dx)>50){

                        flag=true;

                        p=20;

                    }else if (Math.abs(dy)>50){


                        flag=true;

                        p=10;

                    }


                    break;

                case 20://左下角

                    if (Math.abs(dx)>50){


                        flag=true;


                        p=40;

                    }else if (Math.abs(dy)>50){


                        flag=true;

                        p=10;

                    }

                    break;

                case 30://右上角

                    if (Math.abs(dx)>50){


                        flag=true;

                        p=20;

                    }else if (Math.abs(dy)>50){


                        flag=true;

                        p=30;

                    }


                    break;
                case 40://右下角

                    if (Math.abs(dx)>50){


                        flag=true;

                        p=40;

                    }else if (Math.abs(dy)>50){

                        flag=true;


                        p=30;

                    }


                    break;

            }

        } else if (canHorizontal) {//可以水平滑动

            if (y<oneThird){//上面


                if (Math.abs(dx)>50) {

                    p = 20;
                    flag=true;
                }


            }else if (y>2*oneThird){//下面

                if (Math.abs(dx)>50) {
                    p = 40;
                    flag=true;
                }

            }




        } else if (canVertical) {

            if (x<oneThird){//左边

                if (Math.abs(dy)>50){
                    p=10;
                    flag=true;
                }

            }else if (x>2*oneThird){//右边

                if (Math.abs(dy)>50){
                    p=30;
                    flag=true;
                }

            }


        }


    }

    /**
     * 根据滑动的位置设置上不可越过的区域
     */
    private void goStart(){

        switch (p){

            case 10://左边上下滑

                limitLeft=0;
                square.setLeft(-square.getLeft());
                threadShow(10);

                break;

            case 20://上边左右滑
                limitTop=0;

                square.setTop(-square.getTop());

                threadShow(20);

                break;

            case 30://右边上下滑

                limitRight=0;
                square.setRight(-square.getRight());
                threadShow(30);
                break;

            case 40://下边左右滑

                limitBottom=0;
                square.setBottom(-square.getBottom());
                threadShow(40);
                break;

        }

    }

    /**
     * 以线程方式画出禁止线或取消禁止线
     * @param p 方向
     */
    private void threadShow(int p){

        if (!isScale){
            return;
        }

        if (getRow()==0&&p==20){//最上面一排顶部不用动

            return;
        }

        if (getRow()==2&&p==40){//最下面一排底部不用动


            return;
        }

        if (getColumn()==0&&p==10){


            return;
        }

        if (getColumn()==2&&p==30){

            return;
        }

        new Thread(){

            @Override
            public void run() {
                super.run();

                int t=10;

                while (t>=0){
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    t--;
                    Message message=new Message();
                    message.what=p;
                    handler.sendMessage(message);


                }

            }
        }.start();

    }

    Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int step=getWidth()/10;//每次的进度

            switch (msg.what){

                case 10://左边
                    limitLeft+=step;

                    postInvalidate();

                    break;

                case 20://上面

                    limitTop+=step;
                    postInvalidate();

                    break;

                case 30://右边
                    limitRight+=step;

                    postInvalidate();
                    break;

                case 40://下边
                    limitBottom+=step;

                    postInvalidate();
                    break;

            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(getResources().getColor(R.color.colorYan));

        paint.setStrokeWidth(20);

//        canvas.drawLine(20,0,20,50,paint);


        if (square.getLeft()<0){

            paint.setColor(getResources().getColor(R.color.colorYan));

            canvas.drawLine(0,0,0,limitLeft,paint);

        }else {

            paint.setColor(Color.TRANSPARENT);

            canvas.drawLine(0,0,0,limitLeft,paint);

        }

        if (square.getRight()<0){

            paint.setColor(getResources().getColor(R.color.colorYan));

            canvas.drawLine(getWidth(),0,getWidth(),limitRight,paint);
        }else {


            paint.setColor(Color.TRANSPARENT);
            canvas.drawLine(getWidth(),0,getWidth(),limitRight,paint);

        }

        if (square.getTop()<0){

            paint.setColor(getResources().getColor(R.color.colorYan));

            canvas.drawLine(0,0,limitTop,0,paint);

        }else {

            paint.setColor(Color.TRANSPARENT);
            canvas.drawLine(0,0,limitTop,0,paint);

        }

        if (square.getBottom()<0){

            paint.setColor(getResources().getColor(R.color.colorYan));

            canvas.drawLine(0,getHeight(),limitBottom,getHeight(),paint);



        }else {

            paint.setColor(Color.TRANSPARENT);
            canvas.drawLine(0,getHeight(),limitBottom,getHeight(),paint);


        }

    }


    public void click(int x,int y){

        if (!isScale){//未放大状态

            scale(10,2f,x,y);

            isScale=true;

        }else {
            scale(20,1f,x,y);
            isScale=false;
        }

    }


    /**
     * 外部改变view的类型
     */
    public void changeType(){

        if (square==null){
            return;
        }




        switch (square.getType()){

            case Conf.NORMAL:

                square.setType(Conf.ACTIVE);

                break;

            case Conf.BLANK:

                square.setType(Conf.UNAVAILABLE);

                break;

            case Conf.UNAVAILABLE:

                square.setType(Conf.NORMAL);

                break;

            case Conf.ACTIVE:

                square.setType(Conf.BLANK);

                break;


        }

        initType();


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
     * 缩放view
     * @param sc 倍数
     */
    private void scale(int type,float sc,float x,float y){






        switch (type){

            case 10://放大

//                setPivotX(x);
//                setPivotY(y);

                setScaleX(sc);
                setScaleY(sc);

                setElevation(6f);

                break;

            case 20://恢复

//                setPivotX(px);
//                setPivotY(py);
                setScaleX(sc);
                setScaleY(sc);

                if (square.isActive()){

                    setElevation(6f);

                }else {

                    setElevation(0f);
                }

                break;


        }


    }

    /**
     * 保存当前view的中心，便于恢复
     */
    private void savePivot(){

        px=getLeft()+getWidth()/2;

        py=getTop()+getHeight()/2;


    }


}
