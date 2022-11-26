import React,{useState} from 'react'
import EndNode from './Nodes/End'
import Render from './Nodes/Render'
import ZoomLayout from './ZoomLayout'
import {OptionTypes, NodeTemplates, NodeTypes} from './Nodes/Constants'
import WFC from './OperatorContext'

function WorkFlow({config:_config}) {
    let [config, setConfig] = useState(_config);
    function updateNode() {
        setConfig({...config})
    }

    let currentNode = null;
    // 链表操作: 几种行为， 添加行为，删除行为，点击行为     pRef.childNode -> objRef.childNode -> 后继
    // 添加节点
    function onAddNode(type, pRef, objRef){
        const o = objRef.childNode
        if(type === OptionTypes.APPROVER) {
            objRef.childNode = {...NodeTemplates[OptionTypes.APPROVER], childNode: o}
        }
        if(type === OptionTypes.NOTIFIER) {
            objRef.childNode = {...NodeTemplates[OptionTypes.NOTIFIER], childNode: o}
        }
        if(type === OptionTypes.CONDITION) {
            objRef.childNode =  {...NodeTemplates[OptionTypes.CONDITION], conditionNodes:[
                {...NodeTemplates[OptionTypes.BRANCH], nodeName: '条件1', childNode: o},
                {...NodeTemplates[OptionTypes.BRANCH], nodeName: '条件2'},
            ]}
        }
        if(type === OptionTypes.BRANCH) {
            objRef.conditionNodes.push({...NodeTemplates[NodeTypes.BRANCH]})
        }
        updateNode()
    }
    // 删除节点
    function onDeleteNode(pRef, objRef, type, index) {
        if(window.confirm('是否删除节点？')){
            if(type === NodeTypes.BRANCH) {
                console.log([...objRef.conditionNodes],index)
                objRef.conditionNodes.splice(index, 1)
                console.log(objRef.conditionNodes)
            } else {
                const newObj = objRef.childNode
                pRef.childNode = newObj
            }
            updateNode()
        }
        
    }


    // 获取节点  
    function onSelectNode(pRef,objRef){
        currentNode = {
            current: objRef,
            prev: pRef
        }
        console.log('currentNode:', currentNode)
    }

    return (
        <WFC.Provider value={{config, updateNode, onAddNode, onDeleteNode, onSelectNode}}>
            <section className="dingflow-design">
                <ZoomLayout>
                    <Render config={config} />
                    <EndNode />
                </ZoomLayout>
            </section>
        </WFC.Provider>
    )
}

export default WorkFlow