import React from 'react'
import NodeWrap from './Wrap'
function getOwner(flowPermission) {
    console.log('flowPermission:',flowPermission)
}
function StartNode(props) {
    
    return (<NodeWrap type={0} objRef={props.objRef} onContentClick={props.onContentClick} title={<span>{props.nodeName}</span>}>
        <div className="text">
            {getOwner(props.flowPermission) || '所有人'}
        </div>
        <i className="anticon anticon-right arrow"></i>
    </NodeWrap>)
}
export default StartNode