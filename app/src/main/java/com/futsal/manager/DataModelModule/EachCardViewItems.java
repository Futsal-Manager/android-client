package com.futsal.manager.DataModelModule;

/**
 * Created by stories2 on 2017. 3. 26..
 */

public class EachCardViewItems {
    int image;
    String title;

    int getImage(){
        return this.image;
    }
    String getTitle(){
        return this.title;
    }

    public EachCardViewItems(int image, String title){
        this.image=image;
        this.title=title;
    }
}