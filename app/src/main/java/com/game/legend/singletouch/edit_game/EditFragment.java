package com.game.legend.singletouch.edit_game;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.bean.Square;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {

    private Square square;

    private EditCard editCard;

    private LinearLayout about;
    private FloatingActionButton cancel,done;


    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_edit, container, false);

        getComponent(view);

        redraw();

        init();

        click();

        return view;
    }

    private void getComponent(View view){

        editCard=view.findViewById(R.id.card);
        about=view.findViewById(R.id.about);
        cancel=view.findViewById(R.id.cancel);
        done=view.findViewById(R.id.done);


    }

    private void init(){

        Bundle bundle=getArguments();

        if(bundle==null){
            return;
        }

        this.square=bundle.getParcelable("square");

        if (this.square!=null){

            this.square.setScale(true);

            editCard.setSquare(square);


        }


    }

    private void click(){

        cancel.setOnClickListener(v -> {

            cancel();
        });

        done.setOnClickListener(v -> {

            done();
        });

        about.setOnClickListener(v -> {

            showDialog();

        });

        editCard.setOnClickListener(v -> {

            editCard.changeType();

        });

    }

    private void cancel(){

        this.square.setScale(false);
        ((EditActivity) Objects.requireNonNull(getActivity())).remove(false);

    }

    private void done(){

        this.square.setScale(false);
        ((EditActivity) Objects.requireNonNull(getActivity())).remove(true);

    }

    /**
     * 重新设置大小
     */
    private void redraw(){

        int w=getResources().getDisplayMetrics().widthPixels/2;//取屏幕1/2宽来作为宽高

        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) editCard.getLayoutParams();

        layoutParams.width=w;
        layoutParams.height=w;


        editCard.setLayoutParams(layoutParams);

        editCard.setScale(true);


    }

    private void showDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle("编辑说明").setMessage(getString(R.string.edit_about)).setPositiveButton("",(dialog, which) -> {



        }).show();


    }

}
