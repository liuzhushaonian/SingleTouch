package com.game.legend.singletouch.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.game.legend.singletouch.utils.Conf;

import lombok.Data;

@Data
public class Square implements Parcelable {

    private int id;
    private int column;//所在列
    private int row;//所在行
    private boolean isActive;//是否已激活

    private int left=1;//1为开放，-1为关闭，不允许通过

    private int right=1;
    private int top=1;
    private int bottom=1;

//    private boolean isBlank=false;//空白块，无需激活

    private int type= Conf.NORMAL;

    private boolean isScale=false;

    public Square() {
    }


    protected Square(Parcel in) {
        id = in.readInt();
        column = in.readInt();
        row = in.readInt();
        isActive = in.readByte() != 0;
        left = in.readInt();
        right = in.readInt();
        top = in.readInt();
        bottom = in.readInt();
        type = in.readInt();
        isScale = in.readByte() != 0;
    }

    public static final Creator<Square> CREATOR = new Creator<Square>() {
        @Override
        public Square createFromParcel(Parcel in) {
            return new Square(in);
        }

        @Override
        public Square[] newArray(int size) {
            return new Square[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(column);
        dest.writeInt(row);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeInt(left);
        dest.writeInt(right);
        dest.writeInt(top);
        dest.writeInt(bottom);
        dest.writeInt(type);
        dest.writeByte((byte) (isScale ? 1 : 0));
    }
}
