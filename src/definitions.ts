import type { PermissionState } from '@capacitor/core';
import { Plugin } from '@capacitor/core'

export interface PermissionStatus {
  // TODO: change 'location' to the actual name of your alias!
  beacons: PermissionState;
}

export interface EstimotePluginPlugin extends Plugin { 
//  echo(options: { value: string }): Promise<{ handle: string }>;
  createManager(): Promise<any>;
  startScanning(handle: string, autoConnect: boolean, ids: string): Promise<any>;
  stopScanning(handle: string): Promise<any>;
  connect(handle: string, beacon: string): Promise<any>;
  disconnect(handle: string, beacon: string): Promise<any>;
  checkPermissions(): Promise<PermissionStatus>;
  requestPermissions(): Promise<PermissionStatus>;
}


