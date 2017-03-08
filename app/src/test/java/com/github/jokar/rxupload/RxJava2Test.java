package com.github.jokar.rxupload;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by JokAr on 2017/2/26.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RxJava2Test {
    private static final String TAG = "RxJava2Test";

    @Before
    public void before() {
        //输出日志
        ShadowLog.stream = System.out;
    }

    @Test
    public void test() {
        String[] arrays = new String[]{
                "2",
                "3",
                "4",
                "5",
                "6",
                "7"
        };

        Observable[] observables = new Observable[arrays.length];
        for (int i = 0; i < arrays.length; i++) {
            Observable<String> just = Observable.just(arrays[i]).map(new Function<String, String>() {
                @Override
                public String apply(@NonNull String s) throws Exception {
                    return s;
                }
            });
            observables[i] = just;
        }

        Observable.zipArray(new Function<Object[], String[]>() {


            /**
             * Apply some calculation to the input value and return some other value.
             *
             * @param objects the input value
             * @return the output value
             * @throws Exception on error
             */
            @Override
            public String[] apply(@NonNull Object[] objects) throws Exception {
                String[] strings = new String[objects.length];
                for (int i = 0; i < objects.length; i++) {
                    strings[i] = (String) objects[i];
                }
                return strings;
            }

        }, true, 1, observables)
                .subscribe(new Consumer<String[]>() {
                    /**
                     * Consume the given value.
                     *
                     * @param strings the value
                     * @throws Exception on error
                     */
                    @Override
                    public void accept(@NonNull String[] strings) throws Exception {
                        for (String string : strings) {
                            Log.d(TAG, "accept: " + string);
                        }
                    }

                });

    }
}
