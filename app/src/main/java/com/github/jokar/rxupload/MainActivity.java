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

package com.github.jokar.rxupload;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.jokar.rxupload.model.entities.UploadEntities;
import com.github.jokar.rxupload.model.rxbus.RxBus;
import com.github.jokar.rxupload.model.rxbus.event.UploadProgressEvent;
import com.github.jokar.rxupload.presenter.event.UploadPresenter;
import com.github.jokar.rxupload.presenter.impl.UploadPresenterImpl;
import com.github.jokar.rxupload.ui.view.UploadView;
import com.github.jokar.rxupload.utils.FileUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends RxAppCompatActivity implements UploadView {
    private static final String TAG = "UploadFile";
    private static final int RESULT_SELECT_FILE = 1;
    private ProgressBar mProgressBar;
    private UploadPresenter mPresenter;
    private Button mBtnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_SELECT_FILE) {
                setResult(data);
            }
        }
    }

    private void setResult(Intent data) {
        Uri uri = data.getData();
        String path = FileUtil.getPath(this, uri);
        File file = new File(path);
        mPresenter.uploadFile(file, bindUntilEvent(ActivityEvent.DESTROY));
    }

    private void init() {
        mPresenter = new UploadPresenterImpl(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressView);
        mBtnUpload = (Button) findViewById(R.id.btn_upload);

        RxBus.getBus().toMainThreadObservable(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(event -> {
                    if (event instanceof UploadProgressEvent) {
                        uploading(((UploadProgressEvent) event).getBytesWritten(),
                                ((UploadProgressEvent) event).getContentLength());
                    }
                });
    }


    /**
     * 开始上传
     */
    @Override
    public void uploadStart() {
        mBtnUpload.setClickable(false);
    }

    /**
     * 上传中
     *
     * @param bytesWritten
     * @param contentLength
     */
    @Override
    public void uploading(final long bytesWritten, final long contentLength) {
        Log.d(TAG, "onRequestProgress: " + bytesWritten + "/" + contentLength);
        mProgressBar.setMax((int) contentLength);
        mProgressBar.setProgress((int) bytesWritten);
    }

    /**
     * 上传成功
     *
     * @param entities
     */
    @Override
    public void uploadSuccess(UploadEntities entities) {
        Log.d(TAG, "uploadSuccess: " + entities.toString());
    }

    /**
     * 上传失败
     *
     * @param error
     */
    @Override
    public void uploadFail(String error) {
        mBtnUpload.setClickable(true);
        Toast.makeText(getApplicationContext(), "上传失败: " + error, Toast.LENGTH_SHORT).show();
    }

    /**
     * 上传完成
     */
    @Override
    public void uploadComplete() {
        mBtnUpload.setClickable(true);
        Toast.makeText(getApplicationContext(), "上传成功!", Toast.LENGTH_SHORT).show();
    }

    public void selectFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, RESULT_SELECT_FILE);
    }
}
