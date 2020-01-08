import java.util.Random;

import rx.Observable;
import rx.Subscriber;

public class Chapter1 {

    private Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    for (int i = 0; i < 5; i++) {
                        int temp = new Random().nextInt(10);
                        if (temp > 8) {
                            //  /如果 value > 8,则创建一个异常
                        }
                    }
                }
            }
        })
    }
}
