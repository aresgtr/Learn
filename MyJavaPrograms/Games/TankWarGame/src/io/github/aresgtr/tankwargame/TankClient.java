package io.github.aresgtr.tankwargame;


//用容器装坦克
//增加难度选择

import java.awt.*;

public class TankClient extends Frame {

    public static final int SCREENWIDE = 800, SCREENHIGH = 600;
    Tank mytank = new Tank(50, 50, Direction.STOP, 100, this);
}
