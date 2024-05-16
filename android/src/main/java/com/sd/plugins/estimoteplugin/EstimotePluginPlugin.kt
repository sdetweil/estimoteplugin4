package com.sd.plugins.estimoteplugin

import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
//import androidx.appcompat.app.AppCompatActivity
import android.util.Log;




@CapacitorPlugin(name = "EstimotePlugin")
class EstimotePluginPlugin : Plugin() {
    private val implementation = EstimotePlugin()

    @PluginMethod
    fun echo(call: PluginCall) {
        val value = call.getString("value")
        Log.i("estimotePlugin","input value=${value}")
        val ret = JSObject()
        ret.put("handle", value)
        call.resolve(ret)
    }


    @PluginMethod
    fun createManager(call: PluginCall) {
        Log.i("estimoteplugin", "createManager enter");
        val  handle = implementation.createManager(this.activity);

        val ret = JSObject()
        ret.put("handle", handle)
        Log.i("estimoteplugin", "createManager exit $handle");
        call.resolve(ret)
    }
}
