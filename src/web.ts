import { WebPlugin } from '@capacitor/core';

import type { EstimotePluginPlugin } from './definitions';
import { PermissionStatus } from './definitions';


export class EstimotePluginWeb
  extends WebPlugin
  implements EstimotePluginPlugin
{
//  async echo(options: { value: string }): Promise<{ handle: string }> {
//    console.log('ECHO', options);
//    return { "handle": options.value };
//  }
  async createManager(): Promise<{ handle: string }> {
    console.log('createManager', " foobar");
    return { "handle": " foobar"};
  }
  
  async startScanning(handle: string, autoConnect: boolean, ids: string): Promise<any>{
    console.log('start scanning', " foobar");
    return handle + ids + autoConnect;

  };
  async stopScanning(handle: string): Promise<any>{
    console.log('stop scanning', " foobar");
    return handle

  };
  async connect(handle: string, beacon: string): Promise<any>{
    console.log('connect', " foobar");
    return handle+beacon

  };
  async disconnect(handle: string, beacon: string): Promise<any>{
    console.log('disconnect', " foobar");
    return handle+beacon

  };
  async checkPermissions(): Promise<PermissionStatus> {
    console.log('check perms', " foobar");
    // TODO
    throw this.unimplemented('Not implemented on web.');
  }

  async requestPermissions(): Promise<PermissionStatus> {
    console.log('requst perms', " foobar");
    // TODO
    throw this.unimplemented('Not implemented on web.');
  }
}

