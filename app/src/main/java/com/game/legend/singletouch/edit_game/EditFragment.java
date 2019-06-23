package com.game.legend.singletouch.edit_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.bean.Square;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {

    private Square square;

    private EditCard editCard;



    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_edit, container, false);

        editCard=view.findViewById(R.id.card);

        init();

        return view;
    }

    private void init(){

        Bundle bundle=getArguments();

        if(bundle==null){
            return;
        }

        this.square=bundle.getParcelable("square");

        if (this.square!=null){

            editCard.setSquare(square);


        }


    }
}
