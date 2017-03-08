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

package com.github.jokar.rxupload.model.network.upload;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by JokAr on 2017/3/6.
 */

public class UpLoadProgressInterceptor implements Interceptor {
    private UploadListener mUploadListener;

    public UpLoadProgressInterceptor(UploadListener uploadListener) {
        mUploadListener = uploadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(null == request.body()){
            return chain.proceed(request);
        }

        Request build = request.newBuilder()
                .method(request.method(),
                        new CountingRequestBody(request.body(),
                        mUploadListener))
                .build();
        return chain.proceed(build);
    }
}
