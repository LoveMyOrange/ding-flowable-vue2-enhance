import React, { useContext } from 'react';
import { RightOutlined } from '@ant-design/icons';
import NodeWrap from './Wrap'
import TitleElement from './TitleElement'
import WFC from '../context/OperatorContext'
function ApproverNode(props) {
    const { onDeleteNode, onSelectNode, handleOpenApproverDrawer } = useContext(WFC)
    const delNode = (e) => {
        e.stopPropagation();
        onDeleteNode(props.pRef, props.objRef)
    }

    function onContentClick() {
        onSelectNode(props.pRef, props.objRef)
        handleOpenApproverDrawer();
    }
    // TODO: 这里读取props数据
    let TitleEl = <TitleElement delNode={delNode} nodeName={props.nodeName} />
    return (<NodeWrap titleStyle={{backgroundColor:'rgb(255, 148, 62)'}} onContentClick={onContentClick} title={TitleEl} objRef={props.objRef}>
        <div className="text">
            {props.owner ? props.owner :'请选择审核人'}
        </div>
        <RightOutlined />
    </NodeWrap>)
}
export default ApproverNode