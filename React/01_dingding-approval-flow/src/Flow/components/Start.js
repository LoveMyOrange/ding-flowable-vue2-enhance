import React from 'react';
import NodeWrap from './Wrap';

const getOwner = (userList = []) => {
  return userList.map(user => user.name).join('、')
}
function StartNode(props) {
  return (
    <NodeWrap
      type={0}
      objRef={props.objRef}
      title={<span>{props.nodeName}</span>}
    >
      <div className="text" title={props.nodeUserList?.length > 0 && getOwner(props.nodeUserList)}>{props.nodeUserList?.length > 0 && getOwner(props.nodeUserList) }</div>
      {/* <RightOutlined /> */}
    </NodeWrap>
  );
}
export default StartNode;
