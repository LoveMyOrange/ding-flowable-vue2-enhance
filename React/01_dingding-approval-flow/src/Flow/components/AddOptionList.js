import React from 'react';
import {
  AuditOutlined,
  DingtalkOutlined,
  ApartmentOutlined,
} from '@ant-design/icons';
import { OptionNames, OptionTypes } from './Constants';

function AddNodeList(props) {
  return (
    <div className="add-node-popover-body">
      <div className="wrap-container">
        <div className="wrap">
          <AuditOutlined
            style={{ color: '#ff943e', fontSize: '32px' }}
            onClick={() => props.onOptionClick(OptionTypes.APPROVER)}
          />
        </div>
        <span className="text">{OptionNames[OptionTypes.APPROVER]}</span>
      </div>
      
      <div className="wrap-container">
      <div className="wrap">
        <DingtalkOutlined
          style={{ color: '#3296fa', fontSize: '32px' }}
          onClick={() => props.onOptionClick(OptionTypes.NOTIFIER)}
        />
        </div>
        <span className="text">{OptionNames[OptionTypes.NOTIFIER]}</span>
      </div>
      <div className="wrap-container">
      <div className="wrap">
        <ApartmentOutlined
          style={{ color: '#15bc83', fontSize: '32px', borderRadius: '16px' }}
          onClick={() => props.onOptionClick(OptionTypes.CONDITION)}
        />
        </div>
        <span className="text">{OptionNames[OptionTypes.CONDITION]}</span>
        </div>
    </div>
  );
}

export default AddNodeList;
