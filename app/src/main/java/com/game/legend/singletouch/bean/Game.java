package com.game.legend.singletouch.bean;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

@Data
public class Game implements Parcelable {

    private int step;
    private int start;
    private int end;
    private String name;
    private int id;
    private Square[][] squares;

    public Game() {
    }

    protected Game(Parcel in) {
        step = in.readInt();
        start = in.readInt();
        end = in.readInt();
        name = in.readString();
        id = in.readInt();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(step);
        dest.writeInt(start);
        dest.writeInt(end);
        dest.writeString(name);
        dest.writeInt(id);
    }
}
