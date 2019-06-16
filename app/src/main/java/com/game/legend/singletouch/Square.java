package com.game.legend.singletouch;


import lombok.Data;

@Data
public class Square {

    private int column;//所在列
    private int row;//所在行
    private boolean isActive;//是否已激活

    private int left=1;//1为开放，-1为关闭，不允许通过

    private int right=1;
    private int top=1;
    private int bottom=1;

    private boolean isBlank=false;//空白块，无需激活




}
