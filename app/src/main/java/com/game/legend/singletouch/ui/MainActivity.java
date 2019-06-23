package com.game.legend.singletouch.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.bean.Square;
import com.game.legend.singletouch.game.GameView;

public class MainActivity extends AppCompatActivity {


    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView=findViewById(R.id.game_view);

        reDraw();

        init();

    }


    private void reDraw(){

        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) gameView.getLayoutParams();

        int height=getResources().getDisplayMetrics().widthPixels;

        Log.d("height--->>>",height+"");

        layoutParams.height=height;

        gameView.setLayoutParams(layoutParams);


    }

    private void init(){

        Square[][] squares=new Square[3][3];

        for (int a=0;a<squares.length;a++){

            for (int i=0;i<squares[a].length;i++){

                Square square=new Square();

                square.setColumn(a);
                square.setRow(i);
                square.setLeft(-1);
//                square.setActive(true);

                squares[a][i]=square;

            }


        }

        gameView.setSquares(squares);



    }


}
