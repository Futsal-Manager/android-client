package com.futsal.manager.NetworkModule;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
    @SerializedName("list")
    ArrayList<s3url> list;

    public void SetList(ArrayList<s3url> list) {
        this.list = list;
    }

    public ArrayList<s3url> GetList() {
        return list;
    }

    public class s3url {
        public String s3url;

        public s3url() {
            s3url = "";
        }

        public void SetS3url(String s3url) {
            this.s3url = s3url;
        }

        public String GetS3url() {
            return s3url;
        }
    }
}
