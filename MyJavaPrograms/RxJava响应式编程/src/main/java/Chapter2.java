import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

public class Chapter2 {

    public static void main(String[] args) {

        //  range
        Observable.range(10, 5).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println(integer);
            }
        });

        //  defer - only create Observable when there is a Subscriber
        //  just - quickly create Observable
        Observable<Long> deferObservable = getDefer();
        Observable<Long> justObservable = getJust();

        deferObservable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long time) {
                System.out.println("defer: " + time);
            }
        });

        justObservable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long time) {
                System.out.println("just: " + time);
            }
        });

    }

    private static Observable<Long> getJust() {
        return Observable.just(System.currentTimeMillis());
    }

    private static Observable<Long> getDefer() {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return getJust();
            }
        });
    }
}
