package com.sd.plugins.estimoteplugin;

import android.util.Log;

public class EstimotePlugin {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
