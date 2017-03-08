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

package com.github.jokar.rxupload.ui.view;

import com.github.jokar.rxupload.model.entities.UploadEntities;

/**
 * Created by JokAr on 2017/3/7.
 */

public interface UploadView {

    /**
     * 开始上传
     */
    void uploadStart();

    /**
     * 上传中
     */
    void uploading(long bytesWritten, long contentLength);

    /**
     * 上传成功
     * @param entities
     */
    void uploadSuccess(UploadEntities entities);

    /**
     * 上传失败
     * @param error
     */
    void uploadFail(String error);

    /**
     * 上传完成
     */
    void uploadComplete();
}
