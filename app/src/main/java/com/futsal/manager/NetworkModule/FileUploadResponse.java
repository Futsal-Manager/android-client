package com.futsal.manager.NetworkModule;

/**
 * Created by stories2 on 2017. 3. 14..
 */

public class FileUploadResponse {
    String s3url;

    public void SetS3url(String s3url) {
        this.s3url = s3url;
    }

    public String GetS3url() {
        return s3url;
    }
}
