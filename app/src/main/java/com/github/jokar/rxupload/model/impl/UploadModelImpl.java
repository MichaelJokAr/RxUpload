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

package com.github.jokar.rxupload.model.impl;

import android.support.annotation.NonNull;

import com.github.jokar.rxupload.model.entities.UploadEntities;
import com.github.jokar.rxupload.model.event.UploadModel;
import com.github.jokar.rxupload.model.network.UploadNetWorkConfig;
import com.github.jokar.rxupload.model.network.result.HttpResultFunc;
import com.github.jokar.rxupload.model.network.services.UploadFileService;
import com.github.jokar.rxupload.model.network.upload.UploadListener;
import com.trello.rxlifecycle2.LifecycleTransformer;


import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by JokAr on 2017/3/6.
 */

public class UploadModelImpl implements UploadModel {

    @Override
    public void uploadFile(File file,
                           UploadListener listener,
                           @NonNull LifecycleTransformer transformer,
                           @NonNull final UploadFileCallBack callBack) {
        if (file != null && file.exists()) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("application/otcet-stream"), file);

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            UploadNetWorkConfig.getRetrofit(listener)
                    .create(UploadFileService.class)
                    .uploadHead(body)
                    .compose(transformer)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(Schedulers.newThread())
                    .map(new HttpResultFunc())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceObserver<UploadEntities>() {
                        @Override
                        protected void onStart() {
                            super.onStart();
                            callBack.onStart();
                        }

                        @Override
                        public void onNext(UploadEntities data) {
                            callBack.result(data);
                        }

                        @Override
                        public void onError(Throwable e) {
                            callBack.requestError(e);
                        }

                        @Override
                        public void onComplete() {
                            callBack.complete();
                        }
                    });
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @param listener
     * @param callBack
     */
    @Override
    public void uploadFile(File file,
                           UploadListener listener,
                           @NonNull final UploadFileCallBack callBack) {
        if (file != null && file.exists()) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("application/otcet-stream"), file);

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            UploadNetWorkConfig.getRetrofit(listener)
                    .create(UploadFileService.class)
                    .uploadHead(body)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(Schedulers.newThread())
                    .map(new HttpResultFunc())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceObserver<UploadEntities>() {
                        @Override
                        protected void onStart() {
                            super.onStart();
                            callBack.onStart();
                        }

                        @Override
                        public void onNext(UploadEntities entities) {
                            callBack.result(entities);
                        }

                        @Override
                        public void onError(Throwable e) {
                            callBack.requestError(e);
                        }

                        @Override
                        public void onComplete() {
                            callBack.complete();
                        }
                    });
        }
    }

    /**
     * 上传多个文件
     *
     * @param pathList
     * @param listener
     * @param transformer
     * @param callBack
     */
    @Override
    public void uploadMultiFile(String[] pathList,
                                UploadListener listener,
                                @NonNull LifecycleTransformer transformer,
                                @NonNull final UploadMultiFileCallBack callBack) {
        if (pathList != null && pathList.length > 0) {
            ArrayList<Observable> observables = new ArrayList<>();

            for (String path : pathList) {
                File file = new File(path);
                if (file.exists()) {
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("application/otcet-stream"), file);

                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                    Observable<String> observable = UploadNetWorkConfig.getRetrofit(listener)
                            .create(UploadFileService.class)
                            .uploadHead(body)
                            .compose(transformer)
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(Schedulers.newThread())
                            .map(new HttpResultFunc());
                    observables.add(observable);
                }
                file = null;
            }
            Observable[] observables1 = new Observable[observables.size()];
            observables1 = (Observable[]) observables.toArray();
            observables = null;

            Observable.zipArray(new Function<Object[], ArrayList<UploadEntities>>() {
                /**
                 * Apply some calculation to the input value and return some other value.
                 *
                 * @param objects the input value
                 * @return the output value
                 * @throws Exception on error
                 */
                @Override
                public ArrayList<UploadEntities> apply(@NonNull Object[] objects) throws Exception {
                    ArrayList<UploadEntities> arrayList = new ArrayList<>();
                    for (Object object : objects) {
                        arrayList.add((UploadEntities) object);
                    }
                    return arrayList;
                }

            }, true, 1, observables1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceObserver<ArrayList<UploadEntities>>() {
                        @Override
                        protected void onStart() {
                            super.onStart();
                            callBack.onStart();
                        }


                        @Override
                        public void onNext(ArrayList<UploadEntities> data) {
                            callBack.result(data);
                        }

                        @Override
                        public void onError(Throwable e) {
                            callBack.requestError(e);
                        }

                        @Override
                        public void onComplete() {
                            callBack.complete();
                        }
                    });

        }
    }
}
