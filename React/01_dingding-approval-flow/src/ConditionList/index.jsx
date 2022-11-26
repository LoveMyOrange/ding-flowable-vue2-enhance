import { useImperativeHandle, forwardRef } from 'react';
import { Form, Input, Button, Space, Select } from 'antd';
import { get, find, set, cloneDeep } from 'lodash'
import { PlusOutlined } from '@ant-design/icons';
import { findOperatorByType } from './util';


const findOptions = (formItems) => (value, index) => {
  const optionByFields = find(formItems, item => item.value === get(value, `${index}.field`));
  return findOperatorByType(optionByFields?.type);
}

const ConditionList = forwardRef(({ formItems = [] }, ref) => {
  const [form] = Form.useForm();
  const getOptions = findOptions(formItems);
  useImperativeHandle(ref, () => ({
    getFormValue: () => {
      return form.getFieldsValue()
    },
    setFormValue: (values) => form.setFieldsValue(values),
    resetFormValue: () => form.resetFields()
  }))

  return (
    <Form form={form} name="dynamic_form_nest_item" autoComplete="off">
      <Form.List name="conditions">
        {(fields, { add, remove }) => (
          <>
            <Form.Item>
              <Button type="dashed" style={{width: '200px'}} onClick={() => add()} block icon={<PlusOutlined />}>
                添加条件
              </Button>
            </Form.Item>
            {fields.map(field => (
              <Space key={field.key} align="baseline">
                <Form.Item
                  {...field}
                  name={[field.name, 'name']}
                  fieldKey={[field.fieldKey, 'name']}
                >
                  <Input placeholder="请输入条件名称"/>
                </Form.Item>
                <Form.Item
                  {...field}
                  name={[field.name, 'field']}
                  fieldKey={[field.fieldKey, 'field']}
                >
                  <Select options={formItems} style={{width: '150px'}}/>
                </Form.Item>
                <Form.Item
                  noStyle
                  shouldUpdate={(prevValues, curValues) => {
                    if (get(prevValues, `conditions.${field.fieldKey}.field`) !== get(curValues, `conditions.${field.fieldKey}.field`)) {
                      const source = cloneDeep(form.getFieldValue('conditions'));
                      set(source, `${field.fieldKey}.operator`, '');
                      form.setFieldsValue({
                        conditions: source
                      });
                      return true;
                    }
                    return false;
                  }
                  }
                >
                  {() => (
                    <Form.Item
                      {...field}
                      name={[field.name, 'operator']}
                      fieldKey={[field.fieldKey, 'operator']}
                    >
                      <Select options={getOptions(form.getFieldValue('conditions'), field.fieldKey)} style={{width: '150px'}}/>
                    </Form.Item>
                  )}
                </Form.Item>
                
                <Form.Item
                  {...field}
                  name={[field.name, 'content']}
                  fieldKey={[field.fieldKey, 'content']}
                >
                  <Input placeholder="请输入文本内容，多个使用;分开输入"/>
                </Form.Item>
                <Button type="link" onClick={() => remove(field.name)} style={{color: 'red'}}>删除</Button>
                {/* <MinusCircleOutlined  /> */}
              </Space>
            ))}
          </>
        )}
      </Form.List>
      <Form.Item name="expression" label="条件表达式">
        <Input.TextArea autoSize={{ minRows: 4, maxRows: 4 }} placeholder="请输入条件表达式，且为and，或为or，可以用括号分隔。示例：a or b and (c and d)" />
      </Form.Item>
    </Form>
  );
});

export default ConditionList;