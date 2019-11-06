import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

class 从放弃到入门Test {

    @Test
    void test() {

        AtomicReference<String> result = new AtomicReference<>();

        Single value = Single.just("Hello");



        assertEquals("Hello", result.get());
    }
}