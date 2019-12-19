package io.github.aresgtr.tankwargame;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by jasonsmac on 2019-12-18.
 */


//自我改进：gun，Bullet应该是Tank的内部类；
//建立能够创造敌我机器人坦克

public class Tank {

    public static final int XSPEED = 7, YSPEED = 7, TANKSIZE_W = 50, TANKSIZE_H = 50;
    public int x, y, oldx, oldy;
    private int bx, by;
    private int life = 10;
    boolean bl = false, bu = false, br = false, bd = false;
    private Direction dir = Direction.STOP;     //  Stop as default
    private Direction gun_dir = Direction.D;
    TankClient tc = null;
    private boolean isjust, islive = true;
    private static Random rn = new Random();
    private static Random rs = new Random();
    private int step = rs.nextInt(10)+ 3;
    private static Random rf = new Random();
    private int firetimes = rf.nextInt(100);
    private static Image [] imgs = null;
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Map<String, Image> tank_images = new HashMap<>();
}
