import React, { useContext, useState, useRef } from 'react';
import { Form, Input, Select, Button, Modal } from 'antd';
import { set, get } from 'lodash';
import WFC from '../context/OperatorContext';
import ConditionList from '../../ConditionList';

const SetCondition = ({ formItems }) => {
  const { currentNode, updateNode } = useContext(WFC);
  const [visible, setVisible] = useState(false);
  const conditionRef = useRef(null);
  const handleOpen = () => {
    setVisible(true);
    setTimeout(() => {
      conditionRef.current.setFormValue(get(currentNode, 'conditionList.conditions', []))
    }, 0);
  }
  const handleClose = () => {
    setVisible(false);
    conditionRef.current.resetFormValue();
  }
  const handleOk = () => {
    console.log('conditionRef', conditionRef)
    const conditions = conditionRef.current.getFormValue();
    set(currentNode, 'conditionList.conditions', conditions)
    handleClose();
  }
  const handleChange = ({ key, value }) => {
    set(currentNode, key, value)
    updateNode();
  }
  return (
    <>
      <Form.Item label="条件名称">
        <Input
          placeholder="请输入条件名称"
          value={currentNode?.nodeName}
          onChange={(e) => handleChange({ key: 'nodeName', value: e.target.value })}
        />
      </Form.Item>
      <Form.Item label="流转条件">
        <Select
          placeholder="请选择流转条件"
          value={currentNode?.conditionList?.type}
          onChange={(value) => handleChange({ key: 'conditionList.type', value })}
          options={[
            {
              label: '自定义条件',
              value: '1',
            },
            {
              label: 'else',
              value: '2',
            },
          ]}
        />
      </Form.Item>
      {
        currentNode?.conditionList?.type === '1' && (
          <Form.Item>
            <Button type="link" onClick={handleOpen}>编辑条件</Button>
          </Form.Item>
        )
      }
      <Modal visible={visible} width={800} title="编辑条件" onCancel={handleClose} onOk={handleOk} okText="确认" cancelText="取消">
        <ConditionList ref={conditionRef} formItems={formItems} />
      </Modal>
    </>
  );
};

export default SetCondition;
