import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.omg.PortableInterceptor.INACTIVE;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;

public class Chapter2 {

    /**
     * main class are the subscribers
     */
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

        //  flatMap
        flatMapObserver().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });

        flapMapIterableObserver().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });

        //  groupBy
        groupByObserver().subscribe(new Action1<GroupedObservable<Integer, Integer>>() {
            @Override
            public void call(GroupedObservable<Integer, Integer> groupedObservable) {
                groupedObservable.count().subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("key " + groupedObservable.getKey() + " contains: " + integer + " numbers");
                    }
                });
            }
        });

        groupByStringObserver().subscribe(new Action1<GroupedObservable<Integer, String>>() {
            @Override
            public void call(GroupedObservable<Integer, String> groupedObservable) {
                if (groupedObservable.getKey() == 0) {
                    groupedObservable.subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            System.out.println(s);
                        }
                    });
                }
            }
        });

        //  map
        mapObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("map: " + integer);
            }
        });

        //  cast
        castObserver().subscribe(new Action1<Dog>() {
            @Override
            public void call(Dog dog) {
                System.out.println("cast: " + dog.getName());
            }
        });

        //  scan
        scanObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("scan: " + integer);
            }
        });

        //  window
        windowCountObserver().subscribe(new Action1<Observable<Integer>>() {
            @Override
            public void call(Observable<Integer> i) {
                System.out.println(i.getClass().getName());
                i.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("window: " + integer);
                    }
                });
            }
        });

        windowTimeObserver().subscribe(new Action1<Observable<Long>>() {
            @Override
            public void call(Observable<Long> i) {
                System.out.println(System.currentTimeMillis() / 1000);
                i.subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.out.println("windowTime: " + aLong);
                    }
                });
            }
        });
    }

    /**
     * 下面是有关 Obvervable 的 操作符
     */

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

    //  flatMap
    private static Observable<String> flatMapObserver() {
        return Observable.just(1, 2, 3)
                .flatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        return Observable.just("flat map: " + integer);
                    }
                });
    }

    private static Observable<String> flapMapIterableObserver() {
        return Observable.just(1, 2, 3)
                .flatMapIterable(new Func1<Integer, Iterable<String>>() {
                    @Override
                    public Iterable<String> call(Integer integer) {
                        ArrayList<String> s = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            s.add("flatMapIteralble: " + integer);
                        }
                        return s;
                    }
                });
    }

    //  groupBy
    private static Observable<GroupedObservable<Integer, Integer>> groupByObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .groupBy(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer % 2;
                    }
                });
    }

    //key 为 0 的 GroupedObservable 的所有数据都输出
    private static Observable<GroupedObservable<Integer, String>> groupByStringObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .groupBy(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer % 2;
                    }
                }, new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return "groupByKeyValue: " + integer;
                    }
                });
    }

    //  Map
    private static Observable<Integer> mapObserver() {
        return Observable.just(1, 2, 3).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer * 10;
            }
        });
    }

    //  cast
    static Dog dog = new Dog();

    private static Observable<Dog> castObserver() {
        return Observable.just(dog.getAnimal())
                .cast(Dog.class);
    }

    //  scan
    static Integer[] array2 = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
    static List<Integer> list2 = new ArrayList<>(Arrays.asList(array2));

    private static Observable<Integer> scanObserver() {
        return Observable.from(list2).scan(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer x, Integer y) {
                return x * y;
            }
        });
    }

    //  window need to be fixed
    private static Observable<Observable<Integer>> windowCountObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).window(3);
    }

    private static Observable<Observable<Long>> windowTimeObserver() {
        return Observable.interval(1000, TimeUnit.MILLISECONDS)
                .window(3000, TimeUnit.MILLISECONDS);
    }
}


