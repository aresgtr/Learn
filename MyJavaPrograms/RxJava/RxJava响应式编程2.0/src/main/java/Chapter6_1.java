import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Chapter6_1 {

    public static void main(String[] args) throws InterruptedException {
        final long time = System.currentTimeMillis();

        Flowable.range(1, 100000)
                .subscribeOn(Schedulers.io())
                .map(new Function<Integer, Integer>() {

                    public Integer apply(@NonNull Integer i) throws Exception {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return i;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    public void accept(Integer integer) throws Exception {
//                        System.out.println(integer);
                        if (integer == 10000) {
                            System.out.println("Flowable time:" + (System.currentTimeMillis() - time));
                        }
                    }
                });
        Thread.sleep(15000);

        final long time2 = System.currentTimeMillis();
        Observable.range(1, 100000)
                .subscribeOn(Schedulers.io())
                .map(new Function<Integer, Integer>() {
                    public Integer apply(@NonNull Integer i) throws Exception {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return i;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    public void accept(Integer integer) throws Exception {
//                        System.out.println(integer);
                        if (integer == 10000) {
                            System.out.println("Observable time: " + (System.currentTimeMillis() - time2));
                        }
                    }
                });
        Thread.sleep(15000);
    }

}
