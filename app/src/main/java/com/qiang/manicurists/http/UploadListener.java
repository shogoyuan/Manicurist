package com.qiang.manicurists.http;

import okhttp3.Callback;

/**
 * Created by dzc on 15/12/13.
 */
public interface UploadListener extends Callback{
    void onProgress(long totalBytes, long remainingBytes);
}
