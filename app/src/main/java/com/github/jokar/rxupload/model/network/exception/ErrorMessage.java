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

package com.github.jokar.rxupload.model.network.exception;

import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Locale;

/**
 * Created by JokAr on 2017/3/7.
 */

public class ErrorMessage {
    //设置语言

    /**
     * 返回错误信息，捕捉常用3个Exception
     *
     * @param e
     * @return
     */
    public static String getMessage(Throwable e) {
        //设置语言
        boolean isZH = false;
        if (Locale.getDefault().getLanguage().equals("zh")) {
            isZH = true;
        }
        String message;
        try {
            if (e.getClass() == SocketTimeoutException.class) {
                Log.e("Error", e.getClass().toString());
                if (!isZH) {
                    message = "SocketTimeoutException";
                } else {
                    message = "请求连接超时";
                }
            } else if (e.getClass() == ConnectException.class) {
                Log.e("Error", e.getClass().toString());
                if (!isZH) {
                    message = "ConnectException";
                } else {
                    message = "连接服务器出错";
                }
            } else
                message = e.getMessage();
        } catch (Exception e1) {
            e1.printStackTrace();
            message = "";
        }
        return message;
    }

}

