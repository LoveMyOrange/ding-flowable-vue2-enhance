import React from 'react'
import {NodeTypes} from './Constants'
import AddNode from './Add'
function NodeWrap(props) {
    return (
        <div>
            <div className="node-wrap">
                <div className={"node-wrap-box "+ (props.type === NodeTypes.START ? 'start-node' : '')}  onClick={props.onContentClick} >
                    <div className="title" style={props.titleStyle}>
                        {props.title}
                    </div>
                    <div className="content">
                        {props.children}
                    </div>
                </div>
                <AddNode objRef={props.objRef} />
            </div>
            
        </div>
    )
}
export default NodeWrap