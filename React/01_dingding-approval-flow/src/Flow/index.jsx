import React, {
  forwardRef,
  useImperativeHandle,
  useState,
  useRef,
} from 'react';
import { isEmpty } from 'lodash';
import initConfig from './init.json';
import OpContext from './context/OperatorContext';
import ZoomLayout from './components/ZoomLayout';
import Render from './components/Render';
import EndNode from './components/End';
import MyDrawer from './components/MyDrawer';
import SetApprover from './components/SetApprover';
import SetCondition from './components/SetCondition';
import { OptionTypes, NodeTemplates, NodeTypes } from './components/Constants';
import './index.less';

const Flow = forwardRef((props, ref) => {
  const [config, setConfig] = useState(
    isEmpty(props.config) ? initConfig : props.config,
  );
  const [currentNode, setCurrentNode] = useState({});
  const approverRef = useRef(null);
  const conditionRef = useRef(null);
  useImperativeHandle(ref, () => ({}));
  const updateNode = () => {
    setConfig({ ...config });
  };

  const onAddNode = (type, pRef, objRef) => {
    const o = objRef.childNode;
    if (type === OptionTypes.APPROVER) {
      objRef.childNode = {
        ...NodeTemplates[OptionTypes.APPROVER],
        childNode: o,
      };
    }
    if (type === OptionTypes.NOTIFIER) {
      objRef.childNode = {
        ...NodeTemplates[OptionTypes.NOTIFIER],
        childNode: o,
      };
    }
    if (type === OptionTypes.CONDITION) {
      objRef.childNode = {
        ...NodeTemplates[OptionTypes.CONDITION],
        conditionNodes: [
          {
            ...NodeTemplates[OptionTypes.BRANCH],
            nodeName: '条件1',
            childNode: o,
          },
          { ...NodeTemplates[OptionTypes.BRANCH], nodeName: '条件2' },
        ],
      };
    }
    if (type === OptionTypes.BRANCH) {
      objRef.conditionNodes.push({ ...NodeTemplates[NodeTypes.BRANCH] });
    }
    updateNode();
  };
  const onDeleteNode = (pRef, objRef, type, index) => {
    if (window.confirm('是否删除节点？')) {
      if (type === NodeTypes.BRANCH) {
        console.log([...objRef.conditionNodes], index);
        objRef.conditionNodes.splice(index, 1);
        console.log(objRef.conditionNodes);
      } else {
        const newObj = objRef.childNode;
        pRef.childNode = newObj;
      }
      updateNode();
    }
  };

  // 获取节点
  const onSelectNode = (pRef, objRef) => {
    setCurrentNode(objRef)
  };

  // 打开审批人Drawer
  const handleOpenApproverDrawer = () => approverRef.current.openModal();
  const handleCloseApproverDrawer = () => approverRef.current.closeModal();
  const handleOpenConditionDrawer = () => conditionRef.current.openModal();
  const handleCloseConditionDrawer = () => conditionRef.current.closeModal();

  return (
    <OpContext.Provider
      value={{
        config,
        currentNode,
        updateNode,
        onAddNode,
        onDeleteNode,
        onSelectNode,
        handleOpenApproverDrawer,
        handleCloseApproverDrawer,
        handleOpenConditionDrawer,
        handleCloseConditionDrawer,
      }}
    >
      <section className="dingflow-design">
        <ZoomLayout>
          <Render config={config} />
          <EndNode />
        </ZoomLayout>
      </section>
      <MyDrawer
        ref={approverRef}
        title={currentNode?.nodeName}
        contentWrapperStyle={{ width: '736px' }}
        closable={false}
      >
        <SetApprover />
      </MyDrawer>
      <MyDrawer
        ref={conditionRef}
        title="条件设置"
        contentWrapperStyle={{ width: '736px' }}
        closable={false}
      >
        <SetCondition formItems={props?.formItems || []} />
      </MyDrawer>
    </OpContext.Provider>
  );
});

export default Flow;
