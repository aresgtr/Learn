import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


        //  From
        FromArray().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("FromArray: " + integer);
            }
        });

        FromIterable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("From Iterable:" + integer);
            }
        });

        //  buffer
        bufferObserver().subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                System.out.println("bufferTime:" + integers);
            }
        });



    }

    //  Just and Defer
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

    //  From (array or list both ok)
    static Integer[] array = {0, 1, 2, 3, 4, 5};
    private static Observable<Integer> FromArray() {
        return Observable.from(array);
    }

    static List<Integer> list = new ArrayList<>(Arrays.asList(array));
    private static Observable<Integer> FromIterable() {
        return Observable.from(list);
    }

    //  buffer
    private static Observable<List<Integer>> bufferObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .buffer(2, 3);
    }
}
