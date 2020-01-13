import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Chapter3 {

    public static void main(String[] args) throws InterruptedException {

       Observable.create(new Observable.OnSubscribe<Integer>() {
           @Override
           public void call(Subscriber<? super Integer> subscriber) {
               System.out.println("start: " + Thread.currentThread().getName());
               subscriber.onNext(1);
               subscriber.onCompleted();
           }
       })
               .subscribeOn(Schedulers.newThread())
               .map(new Func1<Integer, Integer>() {
                   @Override
                   public Integer call(Integer integer) {
                       System.out.println(integer + ": " + Thread.currentThread().getName());
                       return integer + 1;
                   }
               })
               .observeOn(Schedulers.io())
               .map(new Func1<Integer, Integer>() {
                   @Override
                   public Integer call(Integer integer) {
                       System.out.println(integer + ": " + Thread.currentThread().getName());
                       return integer + 1;
                   }
               })
               .subscribeOn(Schedulers.computation())   //  不起作用，因为上面已经有了subscribeOn
               .subscribe(new Action1<Integer>() {
                   @Override
                   public void call(Integer integer) {
                       System.out.println("action: " + Thread.currentThread().getName());
                   }
               });
        Thread.sleep(3000);

    }
}
