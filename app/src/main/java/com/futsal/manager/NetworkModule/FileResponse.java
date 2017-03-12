package com.futsal.manager.NetworkModule;

import java.util.List;

/**
 * Created by stories2 on 2017. 3. 13..
 */

public class FileResponse {
    /*
    {
  "list": [
    {
      "s3url": "https://futsal-manager.s3.ap-northeast-2.amazonaws.com/16x16.mp4"
    },
    {
      "s3url": "https://futsal-manager.s3.ap-northeast-2.amazonaws.com/32x32.mp4"
    }
  ]
}
     */
    List fileList;

    public void SetFileList(List fileList) {
        this.fileList = fileList;
    }

    public List GetFileList() {
        return fileList;
    }
}
