import Foundation
import Capacitor
import EstimoteUWB
 
/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(EstimotePlugin)
public class EstimotePlugin: CAPPlugin {
    //private let implementation = sduwb()
    //let uwb = sduwb()
    var ListenerKey: String = "UWBInfo"
    var counter :Int64 = 1;
    var t: String = "";
    var shouldConnect : Bool = true;
    var ids:[String]=[];
    var millisecondsSince1970: Int64 {
        Int64((Date().timeIntervalSince1970 * 1000.0).rounded())
    }
    @objc  func getMS() -> String {
        let currentTimeInMiliseconds: String = String(self.millisecondsSince1970);
        return currentTimeInMiliseconds;
    }

    var ManagerHandles:[String:EstimoteUWB.EstimoteUWBManager ] = [:];
    //var connectedDevices:[String:String ] = [:];

    @objc func createManager(_ call: CAPPluginCall) {
        //let value = call.getString("value") ?? ""
        var timekey: String=self.getMS();
        if(t == ""){
            t = timekey
            ManagerHandles[timekey]=EstimoteUWBManager(positioningObserver: self, discoveryObserver: self, beaconRangingObserver: self)
        } else {
            timekey = t;
        }
        call.resolve(["handle":timekey])
    }
    @objc func startScanning(_ call: CAPPluginCall) {
        let Manager = call.getString("handle")!
        let shouldConnectParm:Bool = call.getBool("autoConnect") ?? true
        print("autoconnect parm=\(String(shouldConnectParm))");
        if(shouldConnectParm == true){
            shouldConnect = true
            print("scan start set autoconnect parm to true")
        }
        if(shouldConnectParm == false){
            shouldConnect = false;
             print("scan start set autoconnect parm to false")
        }
        //shouldConnect = (call.getString("autoConnect") != nil)
        ids = (call.getString("IDs") ?? "").components(separatedBy:",");
        print("start scanning called")
        ManagerHandles[Manager]!.startScanning()
        call.resolve()
    }
    @objc func Connect(_ call: CAPPluginCall) {
        let Manager = call.getString("handle")!
        let device = call.getString("beacon")
        ManagerHandles[Manager]?.connect(to: device ?? "  " );
                       /* notifyListeners(ListenerKey, data: ["name": device.name ?? "no name", "counter":"\(counter)","type": "connecting","device":device.publicId]);*/
        call.resolve();
    }
    @objc func Disconnect(_ call: CAPPluginCall) {
        let Manager = call.getString("handle")!
        let device = call.getString("beacon")
        ManagerHandles[Manager]?.disconnect(from: device ?? " ");
/*                        notifyListeners(ListenerKey, data: ["name": device.name ?? "no name", "counter":"\(counter)","type": "discconnecting","device":device.publicId, ]);*/
        call.resolve();
    }    
    @objc func stopScanning(_ call: CAPPluginCall) {
        print("stop scanning entered")
        let Manager : String = call.getString("handle")!
        ManagerHandles[Manager]!.stopScanning()    
        // disconnect from all connected devices
       // ManagerHandles.removeValue(forKey:Manager)
        call.resolve()
    }    
}
extension EstimotePlugin:UWBPositioningObserver {
    public func didUpdatePosition(for device: UWBDevice) {
       // print("position updated for device: \(String(describing: device.vector)) \(device.distance)") 
       // dump("position \(getMS()) \(counter) device= \(device)")
        
        if(device.vector != nil){

            let yy:String = "{\"x\":\(device.vector?.x ?? 1),\"y\":\(device.vector?.y ?? 1),\"z\":\(device.vector?.z ?? 1)}"
       //     print(" vector = \(yy)")
            notifyListeners(ListenerKey, data: ["counter":"\(counter)", "type": "updatePosition","device":device.publicId, "distance":device.distance, "vector":yy])
        }
        else{
             notifyListeners(ListenerKey, data: ["counter":"\(counter)","type": "updatePosition","device":device.publicId, "distance":device.distance])
        }
        counter+=1;
    }
}
// OPTIONAL PROTOCOL FOR BEACON BLE RANGING
extension EstimotePlugin: BeaconRangingObserver {
    public func didRange(for beacon: BLEDevice) {
        print("beacon did range: \(beacon)")
          notifyListeners(ListenerKey, data: ["counter":"\(counter)","type": "range","device":beacon.publicId, "distance":beacon.rssi ])
                 counter+=1;
    }    
}

// OPTIONAL PROTOCOL FOR DISCOVERY AND CONNECTIVITY CONTROL
extension EstimotePlugin: UWBDiscoveryObserver {
    public var shouldConnectAutomatically: Bool {
        return shouldConnect; // set this to false if you want to manage when and what devices to connect to for positioning updates
    }
    
    public func didDiscover(device: UWBIdentifable, with rssi: NSNumber, from manager: EstimoteUWBManager) {
        print("Discovered Device: \(device.publicId) rssi: \(rssi)")
         dump("discover device= \(device)")

        
        // if shouldConnectAutomatically is set to false - then you could call manager.connect(to: device)
        // additionally you can globally call discoonect from the scope where you have inititated EstimoteUWBManager -> disconnect(from: device) or disconnect(from: publicId)
        print("autoconnect in didDiscover=\(shouldConnect)");
        if(shouldConnect == true){
            dump("id list= \(ids)")
            if(ids.count == 0 || ids.contains(device.publicId)) {
                print("connecting to device="+device.publicId);
                manager.connect(to: device)
                notifyListeners(ListenerKey, data: ["name": device.name ?? "no name", "counter":"\(counter)","type": "discover","device":device.publicId, "distance":rssi,"aux":"auto connecting"]); //, name:device.name])
                counter+=1;
            }
        } else {
            notifyListeners(ListenerKey, data: ["name": device.name ?? "no name", "counter":"\(counter)","type": "discover","device":device.publicId, "distance":rssi,"aux":"NOT auto connecting"]); //, name:device.name])                
        }
    }
    
    public func didConnect(to device: UWBIdentifable) {
        print("Successfully Connected to: \(device.publicId)")
        print("connect device= \(device)");
         notifyListeners(ListenerKey, data: ["name": device.name ?? "no name","counter":"\(counter)","type": "connect","device":device.publicId]); //, name:device.name])
        counter+=1;

    }
    
    public func didDisconnect(from device: UWBIdentifable, error: Error?) {
        print("Disconnected from device: \(device.publicId)- error: \(String(describing: error))")
         notifyListeners(ListenerKey, data: ["counter":"\(counter)","type": "disconnect","device":device.publicId]) //, name:device.name])
        counter+=1;
    }
    public func didFailToConnect(to device: UWBIdentifable, error: Error?) {
        print("Failed to connect to: \(device.publicId) - error: \(String(describing: error))")
         notifyListeners(ListenerKey, data: ["counter":"\(counter)", "type": "connectFailed","device":device.publicId]) //, name:device.name])
                counter+=1;
    }
}
