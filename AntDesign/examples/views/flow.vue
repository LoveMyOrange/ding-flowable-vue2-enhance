<template>
  <!-- <FlowDesign ref="FlowDesign" :node.sync="childNode" /> -->
  <!--<FlowDesign ref="FlowDesign" :node.sync="childNode" :readable="readable" />-->
  <FlowDesign ref="FlowDesign" @publish="onPublish" />
</template>
<script>
  import { jsonToBpmn } from '../api/design';
  export default {
    data() {
      return {
        readable: true,
        childNode: {
          uid: '5f414cf2-7c80-40a2-83f7-fd828107212a',
          nodeName: '发起人',
          type: 0,
          status: 2,
          error: false,
          childNode: {
            uid: '1fb48043-e89e-489f-a28c-6206529a9140',
            nodeName: '审核人',
            type: 1,
            status: 1,
            error: false,
            approvalMethod: 1,
            approvalMode: '1',
            approverGroup: [{ id: '15230a69-3104-4274-8654-5adddd283f7e', approverType: 1, levelMode: 1, approverIds: [], approverNames: [] }],
            sameMode: 2,
            noHander: null,
            privilege: [{ id: null, name: '姓名', writable: false, readable: true, displayable: false, required: false }],
            childNode: {
              uid: '0a2ea568-e788-48e9-935b-a0499738da1c',
              nodeName: '审核人',
              type: 1,
              status: 0,
              error: false,
              approvalMethod: 1,
              approvalMode: '1',
              approverGroup: [{ id: '26aeecc8-4278-4506-94ac-286aa71b64a2', approverType: 1, levelMode: 1, approverIds: [], approverNames: [] }],
              sameMode: 2,
              noHander: null,
              privilege: [{ id: null, name: '姓名', writable: false, readable: true, displayable: false, required: false }],
              childNode: null,
              showAdd: true,
              content: null,
              pid: '1fb48043-e89e-489f-a28c-6206529a9140',
            },
            showAdd: true,
            content: null,
            pid: '5f414cf2-7c80-40a2-83f7-fd828107212a',
          },
          showAdd: true,
        },
        /* childNode: {
          nodeName: '审批节点',
          type: 1,
          settype: 2,
          childNode: {
            nodeName: '路由',
            type: 4,
            settype: 1,
            childNode: {
              nodeName: '抄送人',
              type: 2,
              ccSelfSelectFlag: 1,
              childNode: {
                nodeName: '钉钉通知',
                type: 5,
                settype: 1,
              },
              nodeUserList: [],
              error: false,
            },
            conditionNodes: [
              {
                nodeName: '条件1',
                type: 3,
                settype: 1,
                childNode: {
                  nodeName: '审核人',
                  type: 1,
                  settype: 1,
                  childNode: null,
                  conditionNodes: [],
                },
                conditionNodes: [],
              },
              {
                nodeName: '条件2',
                type: 3,
                settype: 1,
                childNode: null,
                conditionNodes: [],
              },
            ],
          },
          nodeUserList: [],
        }, */
      };
    },
    created() {
      jsonToBpmn();
    },
    methods: {
      toReturn() {
        this.$router.push({ path: '/flow/model' });
      },
      onPublish(nodeData) {
     
        jsonToBpmn(nodeData).then(res=>{
          alert("流程数据保存成功")
          // console.log('流程节点信息:', nodeData,res);
          // console.log('流程复制内容:', JSON.stringify(nodeData));
        })
      },

      handleSave() {
        this.$refs.FlowDesign.handleSave();
      },

    },
  };
</script>
