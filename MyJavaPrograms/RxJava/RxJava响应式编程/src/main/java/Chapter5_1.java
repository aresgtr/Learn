import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

public class Chapter5_1 {

    final static double PiValue = 3.14159265;

    @Test
    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(1);
        PI pi = new PI();
        final Double[] result = {Double.valueOf(0)};
        pi.getPi(4, 10000000)
                .subscribe(new Action1<Double>() {
                    @Override
                    public void call(Double aDouble) {
                        System.out.println(aDouble);
                        result[0] = aDouble;
                        latch.countDown();
                    }
                });

        try {
            latch.await();;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(PiValue, result[0], 0.001);
    }


}

class PI {
    private Random mRandom = new Random();
    private Observable<Double> createObservable(final int num) {
        return Observable.range(0, num)
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        double x = mRandom.nextDouble() * 2 - 1;
                        double y = mRandom.nextDouble() * 2 - 1;
                        return (x * x + y * y) < 1 ? 1 : 0;
                    }
                })
                .reduce(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        int reduce = integer + integer2;
                        return reduce;
                    }
                })
                .map(new Func1<Integer, Double>() {
                    @Override
                    public Double call(Integer integer) {
                        double v = 4.0 * integer / num;
                        System.out.println("V: " + v);
                        return v;
                    }
                })
                .subscribeOn(Schedulers.computation());
    }

    public Observable<Double> getPi (int workNum, int num) {
        ArrayList<Observable<Double>> list = new ArrayList<>(workNum);
        for (int i = 0; i < workNum; i++) {
            list.add(createObservable(num));
        }
        return Observable.zip(list, new FuncN<Double>() {
            @Override
            public Double call(Object... args) {
                int len = args.length;
                double result = 0;
                for (int i = 0; i < len; i++) {
                    result += (Double) (args[i]);
                }
                return result / len;
            }
        });
    }
}