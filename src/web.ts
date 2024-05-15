import { WebPlugin } from '@capacitor/core';

import type { EstimotePluginPlugin } from './definitions';

export class EstimotePluginWeb
  extends WebPlugin
  implements EstimotePluginPlugin
{
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
