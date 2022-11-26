import React, {useContext} from 'react'
import NodeWrap from './Wrap'
import TitleElement from './TitleElement'
import WFC from '../OperatorContext'
function ApproverNode(props) {
    const {onDeleteNode, onSelectNode} = useContext(WFC)
    function delNode() {
        onDeleteNode(props.pRef, props.objRef)
    }
    function onChange(val) {
        // 数据设置不用去重新渲染
        props.pRef.childNode.nodeName = val
    }

    function onContentClick() {
        onSelectNode(props.pRef, props.objRef)
        props.onContentClick && props.onContentClick()
    }
    // TODO: 这里读取props数据
    let TitleEl = <TitleElement  delNode={delNode} placeholder={props.nodeName} nodeName={props.nodeName} onTitleChange={onChange} />
    return (<NodeWrap titleStyle={{backgroundColor:'rgb(255, 148, 62)'}} onContentClick={onContentClick} title={TitleEl} objRef={props.objRef}>
        <div className="text">
            {props.owner ? props.owner :'请选择审核人'}
        </div>
        <i className="anticon anticon-right arrow"></i>
    </NodeWrap>)
}
export default ApproverNode