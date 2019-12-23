package io.github.aresgtr.tankwargame;


//制作全屏爆炸
//优化爆炸的坐标

import java.awt.*;

public class Explode {
    int x, y;
    private boolean isLive = true;
    private int [] diameter = {5, 12, 22, 35, 47, 66, 40, 25};
    public TankClient tc;
    private int step = 0;
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private boolean initialize = false;

    private static Image [] imgs = {
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸1.jpg")),
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸2.jpg")),
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸3.jpg")),
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸4.jpg")),
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸5.jpg")),
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸6.jpg")),
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸7.jpg")),
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸8.jpg")),
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸9.jpg")),
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸10.jpg")),
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸11.jpg")),
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸12.jpg")),
            tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克爆炸13.jpg"))
    };

    public Explode(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (initialize == false) {
            for (int i = 0; i < imgs.length; i ++) {
                g.drawImage(imgs[i], -100, -100, null);
            }
        }

        if (!isLive) { //   if dead
            tc.explode.remove(this);
            return;
        }
        if (step == imgs.length) {
            isLive = false;
            step = 0;
            return;
        }

        g.drawImage(imgs[step], x, y, null);
        step ++;
    }
}
