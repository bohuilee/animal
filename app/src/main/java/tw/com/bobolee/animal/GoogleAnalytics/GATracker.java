package tw.com.bobolee.animal.GoogleAnalytics;

import android.content.Context;

import com.google.android.gms.analytics.Tracker;

import tw.com.bobolee.animal.Application.MyApplication;

/**
 * Created by bohuilee on 2016/7/7.
 */
public class GATracker {
    Tracker mTracker;

    GATracker(Context context){
        MyApplication application = (MyApplication) context;
        mTracker = application.getDefaultTracker();
    }

    public Tracker getGATracker(){
        return mTracker;
    }
}
