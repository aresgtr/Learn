import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class Chapter6_3 {

    static Disposable disposable;

    public static void main(String[] args) throws InterruptedException {

        Single.just(1)
                .subscribe(new SingleObserver<Integer>() {
                    public void onSubscribe(Disposable disposable) {
                        System.out.println("onSubscribe");
                    }

                    public void onSuccess(Integer integer) {
                        System.out.println("onSuccess: " + integer);
                    }

                    public void onError(Throwable throwable) {
                    }
                });

        Thread.sleep(3000);


        Single.just(1)
                .delay(1, TimeUnit.SECONDS)
                .subscribe(new SingleObserver<Integer>() {
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe");
                        disposable = d;
                    }

                    public void onSuccess(Integer integer) {
                        System.out.println("onSuccess: " + integer);
                    }

                    public void onError(Throwable throwable) {

                    }
                });
        disposable.dispose();

        Thread.sleep(3000);
    }

}
