package com.gleamsoft.searchdrink;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by Developer on 3/01/2017.
 */

public class App extends Application {

@Override
protected void attachBaseContext(Context context) {
    super.attachBaseContext(context);
    MultiDex.install(this);
}
@Override
public void onCreate() {
    super.onCreate();


}
}