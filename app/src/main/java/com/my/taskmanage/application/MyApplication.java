package com.my.taskmanage.application;


import android.app.Application;

import com.my.taskmanage.utils.TakePhotoUtils;

import org.xutils.x;

import java.io.File;
import java.io.IOException;



public class MyApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);

        createFile();
    }

    private void createFile(                      ) {
        File saveFile1= TakePhotoUtils.getOwnCacheDirectory(this,"TaskManage"+ File.separator+"photo");
        if (!saveFile1.exists()){
            try {
                saveFile1.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
