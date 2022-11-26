import React, {useState, useContext} from 'react'
import AddNodeList from './AddOptionList'
import WFC from '../OperatorContext'
function AddNode(props) {
    let [showPop, setShowPop] = useState(false)
    function onClick() {
        setShowPop(!showPop)
    }
    const {onAddNode} = useContext(WFC)
    function onOptionClick(type) {
        onAddNode(type, props.pRef, props.objRef)
        setShowPop(false)
    }
    return (<div className="add-node-btn-box">
                <div className="add-node-btn">
                    { showPop && <div className="add-popover" style={{"position": "absolute", "zIndex": '10'}}>
                        <AddNodeList onOptionClick={onOptionClick} />
                        </div>
                    }
                    <span>
                        <button className="btn" onClick={onClick}>
                            <span className="iconfont"></span>
                        </button>
                    </span>
                </div>
            </div>)
}

export default AddNode