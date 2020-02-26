package xiaoli;

/**
 * https://blog.csdn.net/fengye454545/article/details/80198446
 */

public class Main {
    public static void main(String[] args) {
        XiaoMing xm = new XiaoMing();
        xm.eatFood();
    }
}

interface EatRice {
    void eat(String food);
}

class XiaoMing implements EatRice{

    public void eatFood() {
        XiaoLi xl = new XiaoLi();

        xl.setEatRiceListener(this, "大龙虾");
    }

    @Override
    public void eat(String food) {
        System.out.println("小明和小李一起去吃" + food);
    }
}

class XiaoLi {

    EatRice er;

    public void setEatRiceListener(EatRice er, String food) {
        this.er = er;
        washFace(food);
    }

    public void washFace(String food) {
        System.out.println("小李要洗漱");

        new Thread(() -> {
            try {
                play();
                Thread.sleep(10000);
                System.out.println("10秒后 ");
                er.eat(food);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void play() {
        System.out.println("小明要玩手机");
    }
}
