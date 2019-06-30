package com.game.legend.singletouch.utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.game.legend.singletouch.bean.Game;
import com.game.legend.singletouch.bean.Square;

/**
 * 数据库
 */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="single";


    private SQLiteDatabase sqLiteDatabase;

    private static Database database;

    private static final int VERSION=1;

    //edit_game表，设置id，名字，步数，开始位置（如果没指定，则为-1，默认为第一个非禁止区域的方块），结束位置（如果未指定，则为-1，表示任意位置都可以）
    private static final String game_table="CREATE TABLE IF NOT EXISTS t_game(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "step INTEGER," +
            "start INTEGER," +
            "end_position INTEGER" +
            ")";

    //card表，设置id、顺序（0~8）、类型（可激活、空白、禁止、未激活）、
    private static final String edit_card_table="CREATE TABLE IF NOT EXISTS t_card(" +
            "card_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "card_order INTEGER," +
            "card_type INTEGER," +
            "card_row INTEGER," +
            "card_column INTEGER," +
            "card_left INTEGER," +
            "card_right INTEGER," +
            "card_top INTEGER," +
            "card_bottom INTEGER" +
            ")";

    public Database(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        sqLiteDatabase=getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        sqLiteDatabase.execSQL(game_table);
        sqLiteDatabase.execSQL(edit_card_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static Database getInstance(Context context){

        if (database==null) {

            synchronized (Database.class) {

                database=new Database(context,DATABASE_NAME,null,VERSION);

            }
        }

        return database;

    }

    /**
     * 保存游戏或是更新游戏
     * 先查询是否有已存在
     * 不存在就直接保存
     */
    public void saveOrUpdate(Game game){


        if (game.getId()>0){//已存在，更新

              String gameSql="update t_game set name = '"
                      +game.getName()
                      +"',step="+game.getStep()
                      +",start="+game.getStart()
                      +",end_position="+game.getEnd()
                      +"where id = "+game.getId();


              sqLiteDatabase.execSQL(gameSql);

            Square[][] squares=game.getSquares();

            if (squares!=null) {

                for (int i=0;i<squares.length;i++){

                    for (int k=0;k<squares[i].length;k++){

                        Square square=squares[i][k];

                        int order=i+k;

                        String cardSql = "update t_card set card_order="+order
                                +",card_type="+square.getType()
                                +",card_row="+square.getRow()
                                +",card_column="+square.getColumn()
                                +",card_left="+square.getLeft()
                                +",card_right="+square.getRight()
                                +",card_top="+square.getTop()
                                +",card_bottom="+square.getBottom()
                                +"where card_id="+square.getId();
                        sqLiteDatabase.execSQL(cardSql);
                    }
                }
            }

        }else {//不存在，保存

            String gameSql="insert into t_game (name,step,start,end_position) values " +
                    "('"+game.getName()
                    +"',"+game.getStep()
                    +","+game.getStart()
                    +","+game.getEnd()+")";


            sqLiteDatabase.execSQL(gameSql);

            Square[][] squares=game.getSquares();

            if (squares!=null) {

                for (int i=0;i<squares.length;i++){

                    for (int k=0;k<squares[i].length;k++){

                        Square square=squares[i][k];

                        int order=i+k;

                        String cardSql = "insert into t_card (card_order,card_type,card_row,card_column,card_left,card_right,card_top,card_bottom)" +
                                " values("+order
                                +",+"+square.getType()
                                +","+square.getRow()
                                +","+square.getColumn()
                                +","+square.getLeft()
                                +","+square.getRight()
                                +","+square.getTop()
                                +","+square.getBottom()+")";
                        sqLiteDatabase.execSQL(cardSql);
                    }
                }
            }
        }
    }







}
