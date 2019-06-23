package com.game.legend.singletouch.edit_game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.bean.Square;

public class EditActivity extends AppCompatActivity {

    EditLayout editLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editLayout=findViewById(R.id.edit_layout);

        redraw();

        listener();
    }

    private void redraw(){

        int w=getResources().getDisplayMetrics().widthPixels;

        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) editLayout.getLayoutParams();

        params.height=w;

        editLayout.setLayoutParams(params);

    }

    private void initData(){

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

        editLayout.setSquares(squares);

    }


    private void listener(){

        editLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                initData();


                editLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }





}
