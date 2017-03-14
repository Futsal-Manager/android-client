package com.futsal.manager.NetworkModule;

/**
 * Created by stories2 on 2017. 3. 14..
 */

public class FileUploadRequest {

    String file;
    public void SetFile(String file) {
        this.file = file;
    }

    public String GetFile() {
        return file;
    }
}
