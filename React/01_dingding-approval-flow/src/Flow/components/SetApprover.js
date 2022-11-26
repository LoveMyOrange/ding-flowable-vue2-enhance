import React, { useContext } from 'react';
import { Form, Input, Select, Button, Modal } from 'antd';
import WFC from '../context/OperatorContext';

const SetApprover = () => {
  const { currentNode, updateNode } = useContext(WFC);
  const handleChange = ({ key, value }) => {
    currentNode[key] = value;
    updateNode();
  }
  return (
    <>
      <Form.Item label="节点名称">
        <Input
          placeholder="请输入节点名称"
          value={currentNode?.nodeName}
          onChange={(e) => handleChange({ key: 'nodeName', value: e.target.value })}
        />
      </Form.Item>
      <Form.Item label="节点名称">
        <Button type="link">编辑处理人</Button>
      </Form.Item>
      <Form.Item label="处理规则">
        <Select
          placeholder="请选择处理规则"
          value={currentNode?.rule}
          onChange={(value) => handleChange({ key: 'rule', value })}
          options={[
            {
              label: '会签',
              value: '1',
            },
            {
              label: '或签',
              value: '2',
            },
          ]}
        />
      </Form.Item>
    </>
  );
};

export default SetApprover;
