import { SET_NODE, ADD_NODE, ADD_BRANCH, DEL_NODE, UP_NODE, UP_MAP } from './mutation-types';
import { getStartNode, addNode, addBranch, delNode, delBranchNode, addCondition, updateNode, updateMap } from '../util/nodeUtil';

const state = {
  //  节点数据
  node: getStartNode(),
  //  缩略图
  mapImg: '',
  // 意见分支
  suggestBranchEnable: true,
  // 并行节点
  parallelBranchEnable: true,
};

// getters
const getters = {
  node: (state) => state.node,
  suggestBranchEnable: (state) => state.suggestBranchEnable,
  parallelBranchEnable: (state) => state.parallelBranchEnable,
};

const mutations = {
  /**
   *  初始节点
   */
  [SET_NODE](state, { node }) {
    if (node) {
      state.node = node;
    } else {
      state.node = getStartNode();
    }
  },
  /**
   * 添加节点
   */
  [ADD_NODE](state, { data }) {
    if (data.nodeType == 0) {
      //  开始
      if (state.node.hasOwnProperty('name')) {
        // 如果添加的是并行节点
        if (data.addNode.type == 9) {
          data.addNode.childNode.childNode = state.node;
          data.addNode.childNode.childNode.pid = data.addNode.childNode.id;
        } else {
          data.addNode.childNode = state.node;
          data.addNode.childNode.pid = data.addNode.id;
        }
        data.addNode.pid = 0;
      }
      state.node = data.addNode;
    } else {
      if (data.id) {
        data.currNode.conditionNodes.forEach((conditionNode, i) => {
          if (conditionNode.id == data.id) {
            // 获取当前操作节点
            addNode(state.node, conditionNode, data.addNode);
          }
        });
      } else {
        // 获取当前操作节点
        addNode(state.node, data.currNode, data.addNode);
      }
    }
    // 更新地图
    updateMap(state);
    //console.log('node', state.node);
    console.info(JSON.stringify(state.node));
  },
  /**
   * 添加分支
   */
  [ADD_BRANCH](state, { node }) {
    let len = node.conditionNodes.length;
    let conditionNode = node.conditionNodes[len - 1];
    conditionNode.attr.priorityLevel = len + 1 + '';
    if (conditionNode.type == 3) {
      // 分支
      node.conditionNodes.splice(len - 1, 0, addCondition(node, len));
    } else {
      // 并行
      node.conditionNodes.push(addCondition(node, len + 1));
    }
    addBranch(state.node, node);
    // 更新地图
    updateMap(state);
  },
  /**
   * 删除节点
   */
  [DEL_NODE](state, { node }) {
    if (node.id == state.node.id) {
      if (node.childNode) {
        state.node = node.childNode;
      } else {
        state.node = {};
      }
    } else if (node.type == 3 || node.type == 8 || node.type == 10) {
      // 条件(意见)分支节点和并行节点
      delBranchNode(state, state.node, node);
    } else {
      delNode(state.node, node);
    }
    // 更新地图
    updateMap(state);
  },
  /**
   * 更新节点
   */
  [UP_NODE](state, { currNode, field, value }) {
    if (currNode.id == state.node.id) {
      state.node[field] = value;
    } else {
      updateNode(state.node, currNode, field, value);
    }
    console.info(JSON.stringify(state.node));
  },
  /**
   * 更新地图
   */
  [UP_MAP](state, { element }) {
    // 更新地图
    updateMap(state);
  },
};

const actions = {
  setNode({ commit }, node) {
    commit(SET_NODE, { node });
  },
  addNode({ commit }, data) {
    commit(ADD_NODE, { data });
  },
  addBranch({ commit }, node) {
    commit(ADD_BRANCH, { node });
  },
  delNode({ commit }, node) {
    commit(DEL_NODE, { node });
  },
  updateNode({ commit }, node) {
    commit(UP_NODE, node);
  },
  updateMap({ commit }, node) {
    commit(UP_MAP, node);
  },
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};
