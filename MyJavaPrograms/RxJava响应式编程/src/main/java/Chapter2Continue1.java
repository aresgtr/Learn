import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class Chapter2Continue1 {

    public static void main(String[] args) throws InterruptedException {

        //  debounce - throttleWithTimeout
        throttleWithTimeoutObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("throttleWithTimeout: " + integer);
            }
        });
        Thread.sleep(3000);

    }

    /**
     * 下面是有关 Obvervable 的 操作符
     */

    //  debounce - throttleWithTimeout
    private static Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i ++) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(i);
                    }

                    int sleep = 100;
                    if (i % 3 == 0) {
                        sleep = 300;
                    }

                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation());
    }

    private static Observable<Integer> throttleWithTimeoutObserver() {
        return createObserver().throttleWithTimeout(200, TimeUnit.MILLISECONDS);
    }

}
