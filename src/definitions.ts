export interface EstimotePluginPlugin {
  echo(options: { value: string }): Promise<{ handle: string }>;
  createManager(): Promise<any>;
  startScanning(handle: string, autoConnect: boolean, ids: string): Promise<any>;
  stopScanning(handle: string): Promise<any>;
  connect(handle: string, beacon: string): Promise<any>;
  disconnect(handle: string, beacon: string): Promise<any>;
}