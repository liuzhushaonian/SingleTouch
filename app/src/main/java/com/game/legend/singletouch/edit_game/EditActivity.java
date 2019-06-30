package com.game.legend.singletouch.edit_game;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.game.legend.singletouch.R;
import com.game.legend.singletouch.bean.Game;
import com.game.legend.singletouch.bean.Square;
import com.game.legend.singletouch.ui.MainActivity;
import com.game.legend.singletouch.utils.Database;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private EditLayout editLayout;

    private EditFragment editFragment;

    private Toolbar toolbar;

    private int step=9;

    private int start=-1;

    private int end=-1;
    EditPreferenceFragment preferenceFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getComponent();

        editLayout.setEditCardClickCallback(this::addFragment);

        redraw();

        initToolbar();

        listener();
    }

    private void getComponent(){
        editLayout=findViewById(R.id.edit_layout);

        toolbar=findViewById(R.id.toolbar);
    }

    private void redraw(){

        int w=getResources().getDisplayMetrics().widthPixels;

        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) editLayout.getLayoutParams();

        params.height=w;

        editLayout.setLayoutParams(params);
//        editLayout.setPadding(16,16,16,16);

    }

    private void initData(){

        Square[][] squares=new Square[3][3];

        for (int a=0;a<squares.length;a++){

            for (int i=0;i<squares[a].length;i++){

                Square square=new Square();

                square.setColumn(a);
                square.setRow(i);
//                square.setActive(true);

                squares[a][i]=square;

            }
        }

//        editLayout.setSquares(squares);

        Game game=new Game();

        game.setEnd(-1);
        game.setStart(-1);
        game.setStep(this.step);
        game.setSquares(squares);

        editLayout.setGame(game);

        initPreference();

    }


    private void listener(){

        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    sleep(1000);

                    handler.sendEmptyMessage(33);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 33:

                    initData();

                    break;
            }
        }
    };




    private void addFragment(Square square){

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

        this.editFragment=new EditFragment();

        Bundle bundle=new Bundle();

        bundle.putParcelable("square",square);

        this.editFragment.setArguments(bundle);

        transaction.replace(R.id.fragment_container,this.editFragment);

        transaction.addToBackStack(null);

        transaction.commit();

    }

    private void removeFragment(boolean done){

        if (this.editFragment==null){
            return;
        }


        if (done) {//确定更改

            Bundle bundle = editFragment.getArguments();

            if (bundle != null) {

                Square square = bundle.getParcelable("square");

                if (square != null) {

                    square.setScale(false);
                    editLayout.applyCard(square);

                }
            }
        }

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

        transaction.remove(editFragment);

        getSupportFragmentManager().popBackStack();

        transaction.commit();

        this.editFragment=null;

    }

    public void remove(boolean done){
        removeFragment(done);
    }

    @Override
    public void onBackPressed() {


        if (this.editFragment!=null){

            remove(false);

        }else {

            super.onBackPressed();

        }

    }

    private void initToolbar(){

        toolbar.setTitle("编辑关卡");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> {

            finish();

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.edit_done://完成，保存或上传

                editDone();

                break;

            case R.id.edit_play://试玩，先保存，后试玩 ——传尼玛的对象，直接给老子传json!

                Game game=editLayout.getGame();

                game.setStep(this.step);

                Intent intent=new Intent(EditActivity.this, MainActivity.class);

                Gson gson=new Gson();

                String json=gson.toJson(game);

                Log.d("json-->>",json);

                intent.putExtra("j",json);


                startActivity(intent);

                break;

            case R.id.edit_about://关于
                break;


        }

        return true;
    }

    private void initPreference(){

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        preferenceFragment=new EditPreferenceFragment();
        transaction.replace(R.id.per,preferenceFragment);

        transaction.commit();


    }


    /**
     * 编辑完成，保存，提示上传
     * 保存初始位置、方块信息（另建表）、总步数（0则无步数限制）、名字、作者、
     */
    private void editDone(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        EditText editText=new EditText(this);

        long time=System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        Date date=new Date(time);

        String hint=format.format(date);

        editText.setHint(hint);

        builder.setTitle("请给这个关卡起一个名字吧")
                .setView(editText)
                .setPositiveButton("确定",(dialog, which) -> {
            Game game=editLayout.getGame();

            game.setStep(this.step);

            String name=editText.getText().toString();

            if (TextUtils.isEmpty(name)){


                name=format.format(date);

            }

            game.setName(name);

            Database.getInstance(EditActivity.this).saveOrUpdate(game);

        }).setNegativeButton("取消",(dialog, which) -> {



        }).show();

    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void relayout(){

        editLayout.relayout();
    }

}
