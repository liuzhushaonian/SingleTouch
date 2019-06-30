package com.game.legend.singletouch.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.bean.Game;
import com.game.legend.singletouch.bean.Square;
import com.game.legend.singletouch.game.GameView;
import com.game.legend.singletouch.utils.Conf;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {


    GameView gameView;
    private TextView step,gameInfo;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getComponent();

        reDraw();

        init();

        initToolbar();

    }

    private void getComponent(){

        step=findViewById(R.id.step_info);
        gameInfo=findViewById(R.id.game_info);
        gameView=findViewById(R.id.game_view);
        toolbar=findViewById(R.id.toolbar);

    }


    private void reDraw(){

        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) gameView.getLayoutParams();

        layoutParams.height= getResources().getDisplayMetrics().widthPixels;

        gameView.setLayoutParams(layoutParams);


    }


    private void initToolbar(){

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void init(){

        Intent intent=getIntent();

        if (intent==null){
            return;
        }

        String json=intent.getStringExtra("j");

        if (json!=null){

            Gson gson=new Gson();

            Game game=gson.fromJson(json,Game.class);

            gameView.setGame(game);

            Log.d("game--->>",game.toString());

            gameView.setGameCallback(this::stepGame);

        }
    }


    /**
     * 每次前进一次都计算
     */
    public void stepGame(Game game,Square currentSquare){

        int step=game.getStep();

        step-=1;//减一步

        game.setStep(step);

        String info=game.getStep()+"";

        this.step.setText(info);

//        Log.d("step--->>>",game.getStep()+"");




        //判断游戏是否获胜
        //终点位置、步数、是否全部激活

        boolean isVictory=true;

        for (int i=0;i<game.getSquares().length;i++){

            for (int k=0;k<game.getSquares()[i].length;k++){

                Square square=game.getSquares()[i][k];

                if (square.getType()== Conf.NORMAL){//还有未激活的方块,不作处理

                    isVictory=false;

                    break;
                }

            }

        }

        if (step<=0&&!isVictory){//游戏结束

            gameOver(getResources().getString(R.string.over_step));
            return;
        }

        if (isVictory){//全部激活，判断最后所在位置，如果没有规定，则获胜，如果规定了，则另外判断

            if (game.getEnd()>=0){//有结束位置要求

                if (game.getEnd()==currentSquare.getRow()+currentSquare.getColumn()){//获胜

                    gameVictory(getResources().getString(R.string.victory));

                }else {//失败

                    gameOver(getResources().getString(R.string.over_end));

                }

            }else {//获胜

                gameVictory(getResources().getString(R.string.victory));


            }



        }



    }


    private void gameOver(String string){

        gameInfo.setText(getResources().getText(R.string.game_over));
        gameInfo.setTextColor(getResources().getColor(R.color.colorFei));
        gameInfo.setVisibility(View.VISIBLE);

        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message message=new Message();

                message.obj=string;

                message.what=200;

                handler.sendMessage(message);


            }
        }.start();

    }

    private void gameVictory(String string){

        gameInfo.setText(getResources().getText(R.string.game_victory));
        gameInfo.setTextColor(getResources().getColor(R.color.colorBiLv));
        gameInfo.setVisibility(View.VISIBLE);


        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message message=new Message();

                message.obj=string;

                message.what=100;

                handler.sendMessage(message);


            }
        }.start();

    }


    private Handler handler=new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String info= (String) msg.obj;

            switch (msg.what){

                case 100:

                    showVictoryDialog(info);

                    break;


                case 200:

                    showOverDialog(info);

                    break;

            }

        }
    };


    private void showOverDialog(String info){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string.game_over))
                .setMessage(info)
                .setPositiveButton("重玩",(dialog, which) -> {



        })
                .setNegativeButton("退出",(dialog, which) -> {





        });

        AlertDialog dialog=builder.create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.show();



    }


    private void showVictoryDialog(String info){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string.game_victory))
                .setMessage(info)
                .setPositiveButton("重玩",(dialog, which) -> {



                })
                .setNegativeButton("下一关",(dialog, which) -> {





                });

        AlertDialog dialog=builder.create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.show();

    }








}
