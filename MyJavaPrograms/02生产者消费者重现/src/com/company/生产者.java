package com.company;

import java.util.List;

public class 生产者 implements Runnable {
    private List<String> xiaoMis;
    private String outputColor;
    public static final String 卖完了 = "卖完了";

    public 生产者(List<String> xiaoMis, String outputColor) {
        this.xiaoMis = xiaoMis;
        this.outputColor = outputColor;
    }

    @Override
    public void run() {
        String[] manufactureList = {
                "小米5",
                "小米6",
                "小米8",
                "小米9",
                "红米青春版",
                "小米电饭煲",
                "小米电视",
                "小米平衡车"
        };

        for (String product : manufactureList) {
            try {
                System.out.println(outputColor + "雷军生产了一台: " + product);
                xiaoMis.add(product);
                Thread.sleep(Main.generateRandom());
            } catch (InterruptedException e) {
//                e.printStackTrace();
                System.out.println("生产被Interrupt");
            }
        }
        System.out.println(outputColor + "雷军" + 卖完了);
        xiaoMis.add(卖完了);
    }
}
