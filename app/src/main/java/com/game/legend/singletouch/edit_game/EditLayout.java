package com.game.legend.singletouch.edit_game;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.bean.Game;
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

    private int br,bc,positionX,positionY;//底部被移动view的行和列

    private EditCard bottomCard;

    private int unitWidth;

    private int viewSpace;

    private int row,column;

    private EditCardClickCallback editCardClickCallback;

    private EditCard editCard;

    private EditCard card;

    private Game game;

    private boolean isLayout=false;

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

    public void setEditCardClickCallback(EditCardClickCallback editCardClickCallback) {
        this.editCardClickCallback = editCardClickCallback;
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
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

//        if (!isLayout) {

            initCards();
//        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
//        initCards();
//        invalidate();

        requestLayout();
    }

//    public Square[][] getSquares() {
//        return squares;
//    }
//
//
//    public void setSquares(Square[][] squares) {
//        this.squares = squares;
//
//        initCards();
//    }

    /**
     * 初始化卡片
     * 赋值x和y
     * 赋值坐标
     */
    private void initCards(){

        if (this.game==null||this.game.getSquares()==null){
            return;
        }

//        Log.d("length--->>>",squares.length+"");

        for (int a=0;a<this.game.getSquares().length;a++){


            for (int t=0;t<this.game.getSquares()[a].length;t++){

                EditCard view=new EditCard(getContext());

                FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(unitWidth,unitWidth);

                view.setLayoutParams(layoutParams);

                addView(view,0);

                Square square=this.game.getSquares()[a][t];

                square.setRow(a);

                square.setColumn(t);

                view.setSquare(square);

//                Log.d("view--->>",view.toString());

                view.setOnClickListener(v -> {

                    if (!view.isScale()){

                        if (editCardClickCallback!=null){
                            editCardClickCallback.addFragment(view.getSquare());
                            this.editCard=view;//保存状态

                        }

                    }
//                    else {
//
//                        view.changeType();
//                        view.setScale(false);
//
//                    }

                });

                view.setOnLongClickListener(v -> {

                    //设置开始与结束的位置
                    //如果已经设置，那么取消
                    //如果在设置好之后把方块设置为禁止，则取消

                    showDialog(view.getSquare(),view);

//                    relayout();

                    return true;
                });


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

        isLayout=true;

    }

    /**
     * 修改完成，放回原位
     * @param square
     */
    public void applyCard(Square square){

        if (this.editCard!=null&&square!=null){
            square.setScale(false);
            this.editCard.setSquare(square);
//            this.editCard.secard_bottomtScale(false);
        }

    }

    private void showDialog(Square square,EditCard editCard){

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        String[] strings=new String[]{"设为起点","设为终点"};

        if (square.getRow()+square.getColumn()==game.getStart()){//已经被设为起点
            strings[0]="取消起点";
        }

        if (square.getRow()+square.getColumn()==game.getEnd()){//已经被设为终点
            strings[1]="取消终点";
        }



        builder.setTitle("要设置一下起点或终点吗？").setItems(strings,(dialog, which) -> {

           switch (strings[which]){

               case "设为起点":

                   if (this.card!=null) {

                       this.card.setStart(false);
                   }

                   editCard.setStart(true);

                   this.card=editCard;

                   game.setStart(square.getRow()+square.getColumn());

                   break;
               case "设为终点":

                   if (this.card!=null) {

                       this.card.setEnd(false);
                   }

                   editCard.setEnd(true);

                   this.card=editCard;

                   game.setEnd(square.getRow()+square.getColumn());

                   break;
               case "取消起点":

                   editCard.setStart(false);

                   game.setStart(-1);
                   break;
               case "取消终点":

                   editCard.setEnd(false);

                   game.setEnd(-1);
                   break;


           }

        }).show();

    }

    public void relayout(){

        requestLayout();
    }



}
