import React from 'react'

import StartNode from './Start'
import ApproverNode from './Approver'
import NotifierNode from './Notifier'
import ConditionNode from './Condition'

const NodeMaps = {
    0: StartNode,
    1: ApproverNode,
    2: NotifierNode,
    4: ConditionNode
}

function MatchNode({config, pRef}) {
    const Node = NodeMaps[config.type] || null
    return Node && <Node {...config} objRef={config} pRef={pRef}  />
}

export default MatchNode