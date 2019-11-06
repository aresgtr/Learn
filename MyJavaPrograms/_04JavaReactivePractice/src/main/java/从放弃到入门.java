/*
 * This is my RxJava practice
 * Source: https://www.jianshu.com/p/cd3557b1a474
 * Github Source: https://github.com/kaka10xiaobang/RxJavaDemo.git
 */

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sun.rmi.runtime.Log;

public class 从放弃到入门 {

    private static final String TAG="RxJavaTag: ";
    private static Disposable mDisposable;

    public static void main(String[] args) {

//        //被观察者
//        Observable novel= Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                emitter.onNext("1");
//                emitter.onNext("Two");
//                emitter.onNext("3");
//                emitter.onComplete();
//            }
//        });
//
//        //观察者
//        Observer<String> reader=new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                mDisposable=d;
//                System.out.println(TAG + "onSubscribe");
//            }
//
//            @Override
//            public void onNext(String value) {
//                if ("2".equals(value)){
//                    mDisposable.dispose();
//                    return;
//                }
//                System.out.println(TAG + "onNext:"+value);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println(TAG + "onError="+e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//                System.out.println(TAG + "onComplete()");
//            }
//        };
//
//        novel.subscribe(reader);//一行代码搞定



        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("1");
                emitter.onNext("Two");
                emitter.onNext("3");
                emitter.onComplete();
            }
        })

                //1 main
                //2 main
                // .observeOn(Schedulers.newThread())
                //3 io
                //subscribe

                //.observeOn(Schedulers.newThread())//回调在主线程
                //.subscribeOn(Schedulers.io())//执行在io线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable=d;
                        System.out.println(TAG + "onSubscribe");
                    }

                    @Override
                    public void onNext(String value) {
                        System.out.println(TAG + "onNext:"+value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(TAG + "onError="+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(TAG + "onComplete()");
                    }
                });
    }
}
