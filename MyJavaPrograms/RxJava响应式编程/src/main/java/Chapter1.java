import java.util.Random;

import rx.Completable;
import rx.CompletableSubscriber;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;

public class Chapter1 {

    public static void main(String[] args) {

        //  Subscribe to Observable
        createObserver().subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("onComplete!");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError: " + throwable.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext: " + integer);
            }
        });

        //  Make a Single and subscribe (produce 1 once)
        Single.create((Single.OnSubscribe<Integer>) singleSubscriber -> {               //  Create producer
            if (!singleSubscriber.isUnsubscribed()) {
                singleSubscriber.onSuccess(1);
            }
        }).subscribe(new SingleSubscriber<Integer>() {                                  //  Create consumer
            @Override
            public void onSuccess(Integer integer) {
                System.out.println(integer);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });

        //  Make a Single and subscribe (produce error)
        Single.create((Single.OnSubscribe<Integer>) singleSubscriber -> {
            if (!singleSubscriber.isUnsubscribed()) {
                singleSubscriber.onError(new Throwable("Single error"));
            }
        }).subscribe(new SingleSubscriber<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                System.out.println(integer);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });

        //  Completable error
        Completable.error(new Throwable("Completable error"))
                .subscribe(new CompletableSubscriber() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }

                    @Override
                    public void onSubscribe(Subscription subscription) {

                    }
                });

        //  onCompleted
        Completable.complete()
                .subscribe(new CompletableSubscriber() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }

                    @Override
                    public void onSubscribe(Subscription subscription) {

                    }
                });
    }

    private static Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    for (int i = 0; i < 5; i++) {
                        int temp = new Random().nextInt(10);
                        if (temp > 8) {
                            //  如果 value > 8,则创建一个异常
                            subscriber.onError(new Throwable("value > 8"));
                            break;
                        } else {
                            subscriber.onNext(temp);
                        }
                        //  没有发生异常,正常结束
                        if (i == 4) {
                            subscriber.onCompleted();
                        }
                    }
                }
            }
        });
    }


}
