package com.company;

import java.util.List;
import static com.company.生产者.卖完了;

public class 消费者 implements Runnable {
    private List<String> xiaoMis;
    private String outputColor;
    private String name;

    public 消费者(List<String> xiaoMis, String outputColor, String name) {
        this.xiaoMis = xiaoMis;
        this.outputColor = outputColor;
        this.name = name;
    }

    @Override
    public void run() {
        int counter = 0;
        while (true) {
            synchronized (xiaoMis) {
                if (xiaoMis.isEmpty()) {
                    continue;   //  接着抢
                } else if (xiaoMis.get(0).equals(卖完了)) {
                    System.out.println(outputColor + name + ":“卧槽卖完了，不抢了。老子抢到了" + counter + "个小米产品。“");
                    break;
                } else {
                    //  终于轮到我了
                    String 买到的玩意儿 = xiaoMis.get(0);
                    System.out.println(outputColor + name + ":“老子抢到了[" + 买到的玩意儿 + "]！！！“");
                    xiaoMis.remove(0);
                    counter++;
                }
            }
        }
    }
}
