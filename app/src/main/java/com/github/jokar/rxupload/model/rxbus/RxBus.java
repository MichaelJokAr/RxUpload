/*
 * Copyright (C) 2016 JokAr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jokar.rxupload.model.rxbus;

import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by JokAr on 2017/3/6.
 */

public class RxBus {
    private final FlowableProcessor<Object> _bus;
    private volatile static RxBus instance;

    public RxBus() {
        _bus = PublishProcessor.create().toSerialized();
    }

    public static RxBus getBus() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null)
                    instance = new RxBus();
            }
        }
        return instance;
    }

    public void send(Object o) {
        _bus.onNext(o);
    }

    /**
     * dosen't designation to use special thread
     * ,It's depending on what the 'send' method use
     *
     * @param lifecycleTransformer rxLifecycle
     * @return
     */
    public Flowable toObservable(LifecycleTransformer lifecycleTransformer) {
        return _bus
                .compose(lifecycleTransformer)
                .onBackpressureDrop();
    }

    /**
     * designation use the MainThread, whatever the 'send' method use
     *
     * @param lifecycleTransformer rxLifecycle
     * @return
     */
    public Flowable toMainThreadObservable(LifecycleTransformer lifecycleTransformer) {
        return _bus
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer)
                .onBackpressureDrop();
    }
}
