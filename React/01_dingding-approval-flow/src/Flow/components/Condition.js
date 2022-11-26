import React, { useContext } from 'react'
import { CloseCircleOutlined, RightOutlined } from '@ant-design/icons';
import AddNode from './Add'
import Render from './Render'
import { NodeTypes} from './Constants'
import WFC from '../context/OperatorContext'

function CoverLine({first = false, last = false}) {
    return (<React.Fragment>
        {first && <div className="top-left-cover-line"></div>}
        {first && <div className="bottom-left-cover-line"></div>}
        {last && <div className="top-right-cover-line"></div>}
        {last && <div className="bottom-right-cover-line"></div>}
    </React.Fragment>)
}

function BranchNode(props) {
    const {first = false, last = false} = props
    return (
        <div className="condition-node">
            <div className="condition-node-box" >
                <div className="auto-judge" onClick={()=>props.onBranchClick(props.objRef)}>
                    <div className="title-wrapper">
                        <span className="editable-title">{props.nodeName}</span>
                        <CloseCircleOutlined onClick={(e) => { e.stopPropagation(); props.delBranch()}} />
                    </div>
                    <div className="content">
                        <div className="text">
                            {props.owner ? props.owner : <span className="placeholder">请设置条件</span>}
                        </div>
                        <RightOutlined />
                        {/* <i className="anticon anticon-right arrow"></i> */}
                    </div>
                </div>
                <AddNode objRef={props.objRef}  />
            </div>
        </div> 
    )
}


function ConditionNode({conditionNodes:branches = [], ...restProps}) {
    const {onAddNode, onDeleteNode, onSelectNode, handleOpenConditionDrawer } = useContext(WFC)
    function addBranch() {
        onAddNode(NodeTypes.BRANCH,restProps.pRef, restProps.objRef)
    }
    function delBranch(i){
        if(branches.length === 2){
            onDeleteNode(restProps.pRef, restProps.objRef)
            return
        }
        console.log('delBranch(i)',(i))
        onDeleteNode(restProps.pRef, restProps.objRef, NodeTypes.BRANCH, i)
    }
    function onBranchClick(objRef) {
        onSelectNode(restProps.objRef, objRef)
        handleOpenConditionDrawer();
    }
    
    return (
        branches && branches.length > 0 && <div className="branch-wrap">
            <div className="branch-box-wrap">
                <div className="branch-box">
                    <button className="add-branch" onClick={addBranch}>添加条件</button>
                    {branches.map((item, index) => {
                        return (<div className="col-box" key={index.toString()}>
                            <BranchNode {...item} first={index === 0} onBranchClick={onBranchClick} delBranch={()=> delBranch(index)} last={index === branches.length-1} objRef={item} /> 
                            {item.childNode && <Render pRef={item} config={item.childNode} />}                     
                            <CoverLine first={index===0} last={index===branches.length-1} />
                        </div>)
                    })}                
                </div>
                <AddNode objRef={restProps.objRef} />
            </div>
        </div>
    )
}

export default ConditionNode