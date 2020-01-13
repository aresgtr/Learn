import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.disposables.Disposable;

public class Chapter6_4 {

    public static void main(String[] args) {

        Maybe.just("1")
                .subscribe(new MaybeObserver<String>() {
                    public void onSubscribe(Disposable disposable) {
                        System.out.println("onSubscribe");
                    }

                    public void onSuccess(String s) {
                        System.out.println("onSuccess: " + s);
                    }

                    public void onError(Throwable throwable) {
                        System.out.println("onError");
                    }

                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });

        Maybe.create(new MaybeOnSubscribe<String>() {

            public void subscribe(MaybeEmitter<String> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onSuccess("1");
                    e.onComplete();
                }
            }
        }).subscribe(new MaybeObserver<String>() {
            public void onSubscribe(Disposable disposable) {
                System.out.println("onSubscribe");
            }

            public void onSuccess(String s) {
                System.out.println("onSuccess: " + s);
            }

            public void onError(Throwable e) {
                System.out.println("onError: " + e.getMessage());
            }

            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
