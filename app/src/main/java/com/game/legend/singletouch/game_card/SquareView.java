package com.game.legend.singletouch.game_card;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.utils.Status;

/**
 * 方格view
 * 滑动进入其时改变颜色
 * 提供自身颜色获取
 * 该view不做事件判断
 */
public class SquareView extends FrameLayout {

    private Status status= Status.INACTIVE;//标记自身是否被滑过

    private int inactiveColor=getResources().getColor(R.color.colorAccent);//未激活颜色
    private int activationColor=getResources().getColor(R.color.colorPrimary);//激活颜色


    public SquareView(Context context) {
        super(context);
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquareView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

    /**
     * 滑动并进入方法
     * 如果是已激活，则改为未激活
     * 如果是未激活，则改为已激活
     */
    public void enter(){

        if (Status.ACTIVATION==this.status){//激活状态


            this.status=Status.INACTIVE;


        }else {

            this.status=Status.ACTIVATION;

        }

        //刷新

        refresh();

    }

    public void refresh(){

        if (this.status==Status.ACTIVATION) {

            setBackgroundColor(activationColor);
        }else {

            setBackgroundColor(inactiveColor);
        }

    }


}
