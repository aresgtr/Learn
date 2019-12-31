package io.github.aresgtr.tankwargame;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Bullet {
    int x, y;
    Direction dir;
    private boolean islive = true;

    public static final int XSPEED = 13, YSPEED = 13, BULLETW = 10, BULLETH = 10;
    public static final int BULLET_DAMAGE = 10;

    public boolean just;

    TankClient tc = null;

    private static Image [] imgs = null;
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Map<String, Image> bulletImages = new HashMap<>();


}
