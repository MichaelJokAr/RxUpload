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

package com.github.jokar.rxupload.model.event;

import android.support.annotation.NonNull;

import com.github.jokar.rxupload.model.entities.UploadEntities;
import com.github.jokar.rxupload.model.network.upload.UploadListener;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by JokAr on 2017/3/6.
 */

public interface UploadModel {

    interface BaseCallBack {
        void onStart();

        void requestError(Throwable e);

        void complete();
    }

    interface UploadFileCallBack extends BaseCallBack {

        void result(UploadEntities data);
    }

    interface UploadMultiFileCallBack extends BaseCallBack {
        void result(ArrayList<UploadEntities> data);
    }

    /* 上传文件
    *
    * @param file
    * @param listener
    * @param lifecycleTransformer
    * @param callBack
    */
    void uploadFile(File file,
                    UploadListener listener,
                    @NonNull LifecycleTransformer lifecycleTransformer,
                    @NonNull UploadFileCallBack callBack);

    /**
     * 上传文件
     *
     * @param file
     * @param listener
     * @param callBack
     */
    void uploadFile(File file,
                    UploadListener listener,
                    @NonNull UploadFileCallBack callBack);

    /**
     * 上传多个文件
     *
     * @param pathList
     * @param listener
     * @param callBack
     */
    void uploadMultiFile(String[] pathList,
                         UploadListener listener,
                         @NonNull LifecycleTransformer lifecycleTransformer,
                         @NonNull UploadMultiFileCallBack callBack);
}
