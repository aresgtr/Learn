package io.github.aresgtr;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * https://www.cnblogs.com/zhangchengzi/p/9718507.html
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here
        int userNumber = 10000;

        Set<Thread> threadSet = new HashSet<>();

        List<TicketNumberHandler> handlerList = new Vector<>();
        List<Long> ticketNumberList = new Vector<>();

        for (int i = 0; i < userNumber; i++) {
            Thread t = new Thread() {
                public void run() {
                    TicketNumberHandler handler = TicketNumberHandler3.getInstance();
                    handlerList.add(handler);

                    Long ticketNumber = handler.getTicketNumber();
                    ticketNumberList.add(ticketNumber);
                };
            };
            threadSet.add(t);
        }
        System.out.println("当前购票人数："+threadSet.size()+" 人");

        //记录购票开始时间
        long beginTime = System.currentTimeMillis();
        for(Thread t : threadSet) {
            //开始购票
            t.start();
        }


        //开始统计
        System.out.println("票号生成类实例对象数目："+new HashSet(handlerList).size());
        System.out.println("共出票："+ticketNumberList.size()+"张");
        System.out.println("实际出票："+new HashSet(ticketNumberList).size()+"张");
//        System.out.println("出票用时："+(entTime - beginTime)+" 毫秒");

    }
}
