package com.sd.plugins.estimoteplugin


import android.Manifest.permission
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.estimote.uwb.api.*
import com.estimote.uwb.api.ranging.EstimoteUWBRangingResult
import com.estimote.uwb.api.scanning.EstimoteDevice
import com.estimote.uwb.api.scanning.EstimoteUWBScanResult
import com.getcapacitor.JSObject
import com.getcapacitor.PermissionState
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.*

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.util.Calendar
import com.getcapacitor.JSArray;
import org.json.*

const val BEACONS:String= "beacons"
@CapacitorPlugin(
    name = "EstimotePlugin",
    permissions = [
        //     @Permission(alias = "bluetooth", strings = { Manifest.permission.BLUETOOTH_SCAN }),
        Permission(
            alias = BEACONS,
            strings = [permission.UWB_RANGING, permission.BLUETOOTH_CONNECT, permission.BLUETOOTH_SCAN]
        )
    ]
)
class EstimotePluginPlugin : Plugin() {
    //private val implementation = EstimotePlugin()
    //private var ListenerKey = "UWBInfo"
    //private var isFirstRequest: Boolean = true
    private var ids = emptyArray<String>()
    private var job: Job? = null;

    private lateinit var ourContext : AppCompatActivity ;
    private val managerHandles = hashMapOf<String, EstimoteUWBManager>();
    private val PERMISSION_DENIED_ERROR = "User denied access to Bluetooth and UWB"

    private final fun getMS(): String {
        return Calendar.getInstance().timeInMillis.toString();
    };

    private final fun permissionGranted():Boolean {
        return (getPermissionState(BEACONS) == PermissionState.GRANTED)
    }

    @PermissionCallback
    fun BeaconsPermissionsCallback(call: PluginCall) {
        Log.i("estimoteplugin", "BeaconsPermissionsCallback entered")
        if (getPermissionState(BEACONS) != PermissionState.GRANTED) {
            Log.d(
                "estimoteplugin",
                "User denied Bluetooth permission: " + getPermissionState(BEACONS).toString()
            )

            Log.i("estimoteplugin", "BeaconsPermissionsCallback returning rejected, call is not null")
            call.reject(PERMISSION_DENIED_ERROR)
            return
        }
    }
    @PluginMethod
    override fun checkPermissions(call: PluginCall){
        Log.i("estimoteplugin", "entered check permissions")
        var result:Boolean = false
        val needBeaconsPerms:Boolean = isPermissionDeclared(BEACONS);
        Log.i("estimoteplugin", "permissions in manifest=$needBeaconsPerms")
        val hasBeaconsPerms:Boolean = getPermissionState(BEACONS) == PermissionState.GRANTED;
        Log.i("estimoteplugin", "permissions already granted=$hasBeaconsPerms")
        // only need one permission set
        //if (!hasBeaconsPerms) {
            //if( !hasBeaconsPerms )
            //requestPermissionForAlias(BEACONS, call, "BeaconsPermissionsCallback");
           val check_result= super.checkPermissions(call);
            Log.i("estimoteplugin", "check results=")
            result = true
        //}

        val ret = JSObject()
        ret.put("granted", result)
        Log.i("estimoteplugin", "exiting  check permissions result=$result")
        call.resolve(ret)
    };
    @PluginMethod
    override fun requestPermissions(call: PluginCall) {
        var result:Boolean = false;

        Log.i("estimoteplugin", "entered request permissions")
        // If the camera permission is defined in the manifest, then we have to prompt the user
        // or else we will get a security exception when trying to present the camera. If, however,
        // it is not defined in the manifest then we don't need to prompt and it will just work.
        if (isPermissionDeclared(BEACONS)) {
            // just request normally
            Log.i("estimoteplugin", " requesting permissions")
            //super.requestAllPermissions(call);
            super. requestPermissionForAlias(BEACONS, call, "checkPermissions");
            result= true
        } else {
            // the manifest does not define camera permissions, so we need to decide what to do
            // first, extract the permissions being requested
            Log.i("estimoteplugin", "processing array of permissions")
            val providedPerms: JSArray = call.getArray("permissions");
            var permsList: List<String> = emptyList<String>()
            if (providedPerms != null) {
                try {
                    permsList = providedPerms.toList();
                } catch (e: JSONException) {;

                }
            }

            if (!permsList.isEmpty() && permsList.size == 1 && (permsList.contains(BEACONS))) {
                // the only thing being asked for was the camera so we can just return the current state
                Log.i("estimoteplugin", "request, from literal list of permissions")
                checkPermissions(call);
            } else {
                if( call != null) {
                    // we need to ask about gallery so request storage permissions
                    requestPermissionForAlias(BEACONS, call, "BeaconsPermissionsCallback");
                }
            }
        }
        val ret = JSObject()
        ret.put("granted", result)
        Log.i("estimoteplugin", "exiting  request permissions result=$result")
        call.resolve(ret)
    }
    override fun requestPermissionForAliases(
        aliases: Array<String>,
        call: PluginCall,
        callbackName: String
    ) {
     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            for (i in aliases.indices) {
                if (aliases[i] == SAVE_GALLERY) {
                    aliases[i] = PHOTOS
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            for (i in aliases.indices) {
                if (aliases[i] == SAVE_GALLERY) {
                    aliases[i] = READ_EXTERNAL_STORAGE
                }
            }
        } */
        Log.i("estimoteplugin", "requestPermission for aliases ")
        super.requestPermissionForAliases(aliases, call, callbackName)
    }
    @PluginMethod
    fun createManager(call: PluginCall) {
        ourContext = this.activity;
        Log.i("estimoteplugin", "createManager enter");
        val timeKey: String = getMS();
         Log.i("estimoteplugin", "createManager enter $timeKey");
        // estimote api for createManager
        //val uwbManager: String = "foo"
        val uwbManager:EstimoteUWBManager = EstimoteUWBFactory.create();
        //uwbManager.init(ourContext);
        managerHandles[timeKey] = uwbManager;
        if(!permissionGranted()) {
            Log.i("estimoteplugin", "createManager requesting permissions");
            requestPermissions(call);
        }

        Log.i("estimoteplugin", "createManager exit $timeKey");
        val ret = JSObject()
        ret.put("handle", timeKey)
        call.resolve(ret)
    }

    @PluginMethod
    fun startScanning(call: PluginCall) {
        val manager = call.getString("handle", "123")
        Log.i("estimoteplugin","startscanning enter manager = $manager");
        val shouldConnectOnDiscovery = call.getBoolean("autoConnect", true)
        // save the restricted list of beacon ids, came in as comma separated values
        ids = call.getString("IDs", "")?.split(",".toRegex())?.dropLastWhile { it.isNotEmpty() }!!.toTypedArray()
        // get the manager handle from the hash
        val uwbManager: EstimoteUWBManager? = managerHandles[manager]
        // if set

        if (uwbManager != null) {
            Log.i("estimoteplugin", "scanning uwbmanager found")
            // if job hasn't been started or has been stopped
            if (job == null || !job!!.job.isActive && shouldConnectOnDiscovery== true) {
                // loop thru each of the devices
                Log.i("estimoteplugin", "scanning job not started")
                uwbManager.uwbDevices.onEach { scanResult: EstimoteUWBScanResult ->
                    when (scanResult) {
                        // if this is a scanresult
                        is EstimoteUWBScanResult.Devices -> {
                            Log.i(
                                "estimoteplugin",
                                "UWB Found ${scanResult.devices.size} UWB Beacons"
                            )
                            job = null;

                            // create a new job for processing the results
                            job = this.activity.lifecycleScope.launch {
                                // loop thru the devices found
                                scanResult.devices.onEach { beacon: EstimoteDevice ->
                                    // if no specific devices, OR this one matches
                                    Log.i("UWB", "processing for beacon= $beacon.deviceId with rssi=$beacon.rssi.toString()")
                                    if (true || ids.isEmpty() || ids.contains(beacon.deviceId)) {
                                        // report it to the app
                                        val data = JSObject()
                                        data.put("type", "discover");
                                        data.put("distance", beacon.rssi.toString())
                                        data.put("device", beacon.deviceId.toString())
                                        // notify app listeners we found a beacon
                                        Log.i("UWB","sending event info")
                                        notifyListeners("UWBInfo", data)
                                        // if we should connect on discovery
                                        Log.i("UWB","should Connect="+shouldConnectOnDiscovery)
                                        if (shouldConnectOnDiscovery == true) {
                                            // do it
                                            beacon.device?.let {
                                                uwbManager.connect(
                                                    it,
                                                    ourContext
                                                )
                                            }
                                        };
                                    }
                                }
                            }
                        }

                        is EstimoteUWBScanResult.Error -> {
                            Log.e("UWB", "Error: ${scanResult.errorCode}")
                        }

                        is EstimoteUWBScanResult.ScanNotStarted -> {
                            Log.i("estimoteplugin", "UWB Error: scan not started")
                        }

                        else -> {}
                    }
                }.launchIn(this.activity.lifecycleScope)

                // process ranging results after connected
                uwbManager.rangingResult.onEach { rangingResult: EstimoteUWBRangingResult ->
                    when (rangingResult) {
                        // if this is a position ranging result
                        is EstimoteUWBRangingResult.Position -> {
                            // built the data to send to app
                            val data = JSObject()
                            Log.i(
                                "estimoteplugin",
                                "UWB distance ${rangingResult.position.distance?.value.toString()}"
                            )

                            if (rangingResult.position.azimuth !== null) {
                                data.put("vector", rangingResult.position.azimuth?.value)
                            }
                            data.put("type", "updatePosition");
                            data.put(
                                "distance",
                                rangingResult.position.distance?.value.toString()
                            )
                            data.put("device", rangingResult.device.address.toString())
                            // notify app listeners
                            notifyListeners("UWBInfo", data)
                        }

                        is EstimoteUWBRangingResult.Error -> {
                            Log.i("estimoteplugin", "UWB Error: ${rangingResult.message}")
                        }

                        else -> Unit
                    }
                }.launchIn(this.activity.lifecycleScope)
            }
            // start the scanning process, the handlers above will trigger
            Log.i("estimoteplugin", "scanning uwbstart scanner")
            uwbManager.startDeviceScanning(ourContext)
        }
        Log.i("estimoteplugin","startscanning exit")
        val ret = JSObject()
        ret.put("empty", 1)
        call.resolve(ret)
    }

    @PluginMethod
    fun stopScanning(call: PluginCall) {
        //print("stop scanning entered");
        val manager = call.getString("handle")
        // if there IS a job, and its active

        if(job?.job?.isActive == true) {
            // stop it
            managerHandles[manager]?.stopDeviceScanning();
            // and stop the job
            job?.job?.cancel()
        }

        // clear the restricted list of ids
        ids = emptyArray<String>()
        val ret = JSObject()
        ret.put("empty", 1)
        call.resolve(ret)
    }

    @PluginMethod
    fun connect(call: PluginCall) {
        val manager = call.getString("handle", "")
        val beacon = call.getString("beacon", "")

       // managerHandles[manager]?.connect(, ourContext);

        val ret = JSObject()
        ret.put("empty", manager + beacon)
        call.resolve(ret)
    }

    @PluginMethod
    fun disconnect(call: PluginCall) {

        val manager = call.getString("handle", "")
        val beacon = call.getString("beacon", "")

        //managerHandles[manager]?.disconnect(, ourContext);
        val ret = JSObject()
        ret.put("empty", manager + beacon)
        call.resolve(ret)
    }
}