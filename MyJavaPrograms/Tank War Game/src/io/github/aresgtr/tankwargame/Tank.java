package io.github.aresgtr.tankwargame;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
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

    static {
        imgs = new Image[] {
                tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1左.jpg")),
                tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1左上.jpg")),
                tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1上.jpg")),
                tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1右上.jpg")),
                tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1右.jpg")),
                tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1右下.jpg")),
                tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1下.jpg")),
                tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1左下.jpg"))
        };
        tank_images.put("L", imgs[0]);
        tank_images.put("LU", imgs[1]);
        tank_images.put("U", imgs[2]);
        tank_images.put("RU", imgs[3]);
        tank_images.put("R", imgs[4]);
        tank_images.put("RD", imgs[5]);
        tank_images.put("D", imgs[6]);
        tank_images.put("LD", imgs[7]);
        }

    Tank() {
        this.x = 50;
        this.y = 50;
    }

    public Tank(int x, int y, boolean isjust) {
        this.x = x;
        this.y = y;
        this.isjust = isjust;
    }

    public Tank(int x, int y, Direction dir, TankClient tc, boolean isjust) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.tc = tc;
        this.isjust = isjust;
    }

    public Tank(int x, int y, int life, Direction dir, TankClient tc, boolean isjust) {
        this.life = life;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean getIsjust() {
        return isjust;
    }

    public boolean getIslive() {
        return islive;
    }

    public void setIslive(boolean islive) {
        this.islive = islive;
    }

    public Rectangle getRectangle() {
        System.out.println(imgs[0].getWidth(null));
        return new Rectangle(x, y, imgs[0].getWidth(null), imgs[0].getHeight(null));
    }

    public void stay() {
        x = oldx;
        y = oldy;
    }

    public boolean collisionWithWall(Wall w) {
        if (getRectangle().intersects(w.getRectangle())) {
            stay();
            return true;
        }
        return false;
    }

    public boolean collisionWithTank(Tank t) {
        if (getRectangle().intersects(t.getRectangle()) && t!= this && t.islive && this.islive) {
            stay();
            t.stay();
            return true;
        }
        return false;
    }

    public int collisionWithTanks(List<Tank> tanks) {
        int num = 0;
        for (int i = 0; i < tanks.size(); i ++) {
            Tank t = tanks.get(i);
            if (collisionWithTank(t))
                num ++;
        }
        return num;
    }

    public void draw(Graphics g) {
        if (!islive) {
            return;
        }

        switch (gun_dir) {
            case L:
                g.drawImage(tank_images.get("L"), x, y, null);
                break;
            case LU:
                g.drawImage(tank_images.get("LU"), x, y, null);
                break;
            case U:
                g.drawImage(tank_images.get("U"), x, y, null);
                break;
            case RU:
                g.drawImage(tank_images.get("RU"), x, y, null);
                break;
        }
    }
}
