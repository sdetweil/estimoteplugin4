export interface EstimotePluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
