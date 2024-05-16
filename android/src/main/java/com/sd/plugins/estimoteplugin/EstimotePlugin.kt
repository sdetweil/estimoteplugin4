package com.sd.plugins.estimoteplugin

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
/* uncomment her and below when using lib
import com.estimote.uwb.api.EstimoteUWBFactory;
import com.estimote.uwb.api.scanning.EstimoteUWBScanResult;
import com.estimote.uwb.api.ranging.EstimoteUWBRangingResult;
import com.estimote.uwb.api.*
import com.estimote.uwb.api.EstimoteUWBManager;
import com.estimote.uwb.api.scanning.EstimoteDevice
*/

import java.util.Calendar

class EstimotePlugin {
    //private var ids= emptyArray<String>()

    //private var job: Job ? = null;
    private final var ourContext: AppCompatActivity?= null;
    private val managerHandles = hashMapOf<String,String /*EstimoteUWBManager*/>();

    private fun getMS(): String{
        return Calendar.getInstance().timeInMillis.toString();
    };

    fun echo(value: String): String {
        Log.i("Echo", value)
        return value
    }

    fun createManager(context: AppCompatActivity ): String {
        ourContext = context;
        val timeKey = getMS()
        Log.i("estimoteplugin", "createManagerc enter $timeKey")
        // estimote api for createManager
        /*  uncomment out to use lib
        val uwbManager: EstimoteUWBManager = EstimoteUWBFactory.create();
        uwbManager.init(ourContext!!)
         */
        // comment out when using lib
        val uwbManager:String = "foo"
        managerHandles[timeKey] = uwbManager
        Log.i("estimoteplugin", "createManagerc exit $timeKey");
        return timeKey
    }
}
