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

package com.github.jokar.rxupload.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.jokar.rxupload.model.entities.UploadEntities;
import com.github.jokar.rxupload.model.event.UploadModel;
import com.github.jokar.rxupload.model.impl.UploadModelImpl;
import com.github.jokar.rxupload.model.network.exception.ErrorMessage;
import com.github.jokar.rxupload.model.rxbus.RxBus;
import com.github.jokar.rxupload.model.rxbus.event.UploadProgressEvent;
import com.github.jokar.rxupload.presenter.event.UploadPresenter;
import com.github.jokar.rxupload.ui.view.UploadView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.io.File;

/**
 * Created by JokAr on 2017/3/7.
 */

public class UploadPresenterImpl implements UploadPresenter {
    private static final String TAG = "UploadFile";
    private UploadView mView;
    private UploadModel mModel;

    public UploadPresenterImpl(UploadView view) {
        mView = view;
        mModel = new UploadModelImpl();
    }

    /**
     * 上传文件
     *
     * @param file
     * @param lifecycleTransformer
     */
    @Override
    public void uploadFile(File file,
                           @NonNull LifecycleTransformer lifecycleTransformer) {
        mModel.uploadFile(file, (bytesWritten, contentLength) -> {
                    RxBus.getBus().send(new UploadProgressEvent(bytesWritten, contentLength));
//                    mView.uploading(bytesWritten, contentLength);
                }, lifecycleTransformer,
                new UploadModel.UploadFileCallBack() {
                    @Override
                    public void result(UploadEntities data) {
                        mView.uploadSuccess(data);
                    }

                    @Override
                    public void onStart() {
                        mView.uploadStart();
                    }

                    @Override
                    public void requestError(Throwable e) {
                        Log.e("UploadError", ErrorMessage.getMessage(e));
                        mView.uploadFail(ErrorMessage.getMessage(e));
                    }

                    @Override
                    public void complete() {
                        mView.uploadComplete();
                    }
                });
    }
}
