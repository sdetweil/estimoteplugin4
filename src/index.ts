import { registerPlugin } from '@capacitor/core';

import type { EstimotePluginPlugin } from './definitions';

const EstimotePlugin = registerPlugin<EstimotePluginPlugin>('EstimotePlugin', {
  web: () => import('./web').then(m => new m.EstimotePluginWeb()),
});

export * from './definitions';
export { EstimotePlugin };
