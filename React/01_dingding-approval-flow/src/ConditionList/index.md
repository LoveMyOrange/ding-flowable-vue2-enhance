Demo:

```tsx
import React from 'react';
import { ConditionList } from 'raycloud-apaas-flow';

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
}];
export default () => <ConditionList formItems={formItems} />;
```

More skills for writing demo: https://d.umijs.org/guide/basic#write-component-demo
