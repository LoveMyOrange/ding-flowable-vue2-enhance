import React from 'react'
import { CloseCircleOutlined } from '@ant-design/icons';
function TitleElement(props) {
    return (<div className="title-element">
        <span className="editable-title">{props.nodeName}</span>
        <CloseCircleOutlined onClick={props.delNode}/>
    </div>)
}
export default TitleElement