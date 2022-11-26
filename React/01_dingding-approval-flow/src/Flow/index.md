Demo:

```tsx
import React from 'react';
import { Flow } from 'raycloud-apaas-flow';

const config = {}; // 流程配置
const formItems = [{
  label: 'String',
  value: 'input_123456',
  type: 'string'
}, {
  label: 'Array',
  value: 'checkbox_123456',
  type: 'array'
}, {
  label: 'Object',
  value: 'checkbox2_123456',
  type: 'object'
}, {
  label: 'Boolean',
  value: 'boolean_123456',
  type: 'boolean'
}]; // 工单列表字段

export default () => <Flow config={config} formItems={formItems} />;
```
