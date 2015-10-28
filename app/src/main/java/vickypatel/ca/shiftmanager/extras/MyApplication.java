package vickypatel.ca.shiftmanager.extras;

import android.app.Application;
import android.content.Context;

/**
 * Created by VickyPatel on 2015-10-18.
 */
public class MyApplication extends Application {

    private static MyApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getsInstance(){
        return instance;
    }

    public static Context getAppContext(){
        return instance.getApplicationContext();
    }



}