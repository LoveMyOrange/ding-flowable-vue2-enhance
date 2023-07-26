package com.dingding.mid.controller.antd;

import com.alibaba.fastjson.JSONObject;
import com.dingding.mid.common.Result;
import com.dingding.mid.enums.ModeEnums;
import com.dingding.mid.utils.IdWorker;
import com.dingding.mid.utils.SpringContextHolder;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static org.flowable.bpmn.model.ImplementationType.IMPLEMENTATION_TYPE_CLASS;

/**
 * @author : lzgabel
 * @version : 1.0
 */
@RestController
@RequestMapping("/antd")
@Tag(name = "AntDesign 版本 的Json转Bpmn")
@ApiSort(5)
public class AntJsonToBpmnController {


    @PostMapping("/jsonToBpmn")
    public Result saveForm(@RequestBody JSONObject object) throws InvocationTargetException, IllegalAccessException {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        List<SequenceFlow> sequenceFlows = Lists.newArrayList();
        model.addProcess(process);
        process.setName("sb");
        process.setId("nt");
        StartEvent startEvent = createStartEvent();
        process.addFlowElement(startEvent);
        Map<String,JSONObject> nodeMap=new HashMap<>();
        String lastNode = create(startEvent.getId(), object,model,process,sequenceFlows,nodeMap);
        EndEvent endEvent = createEndEvent();
        process.addFlowElement(endEvent);
        process.addFlowElement(connect(lastNode, endEvent.getId(),sequenceFlows));

        new BpmnAutoLayout(model).execute();


        RepositoryService repositoryService = SpringContextHolder.getBean(RepositoryService.class);

        Deployment deploy = repositoryService.createDeployment()
                .category("AntDesign")
                .name("AntDesign版本的liuchengtu")
                .addBpmnModel("test.bpmn",model)
                .deploy();
        System.err.println(deploy.getId());


        return Result.OK("发布成功");
    }


    private static String id(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    private static ServiceTask serviceTask(String name) {
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setName(name);
        return serviceTask;
    }

    protected static SequenceFlow connect(String from, String to,List<SequenceFlow> sequenceFlows) {
        SequenceFlow flow = new SequenceFlow();
        flow.setId(id("sequenceFlow"));
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        sequenceFlows.add(flow);
        return flow;
    }

    protected static StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(id("start"));
        return startEvent;
    }

    protected static EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(id("end"));
        return endEvent;
    }


    private static String create(String fromId, JSONObject flowNode,BpmnModel bpmnModel,Process process,
                                 List<SequenceFlow> sequenceFlows,Map<String,JSONObject> nodeMap) throws InvocationTargetException, IllegalAccessException {
        nodeMap.put(flowNode.getString("id"),flowNode);
        Integer nodeType = flowNode.getInteger("type");
        if (Type.PARALLEL.type.equals(nodeType)) {
            return createParallelGatewayBuilder(fromId, flowNode,bpmnModel,process,sequenceFlows,nodeMap);
        }
        else if (Type.EXCLUSIVE.type.equals(nodeType)) {
            return createExclusiveGatewayBuilder(fromId, flowNode,bpmnModel,process,sequenceFlows,nodeMap);
        }
        else if (Type.USER_TASK.type.equals(nodeType)) {
            flowNode.put("incoming", Collections.singletonList(fromId));
            String id = createUserTask(flowNode,bpmnModel,process,sequenceFlows);

            // 如果当前任务还有后续任务，则遍历创建后续任务
            JSONObject nextNode = flowNode.getJSONObject("childNode");
            if (Objects.nonNull(nextNode)) {
                FlowElement flowElement = bpmnModel.getFlowElement(id);
                return create(id, nextNode,bpmnModel,process,sequenceFlows,nodeMap);
            } else {
                return id;
            }
        }

        else if (Type.ROOT.type.equals(nodeType)) {
            flowNode.put("incoming", Collections.singletonList(fromId));
            String id = createSimpleUserTask(flowNode,bpmnModel,process,sequenceFlows);

            // 如果当前任务还有后续任务，则遍历创建后续任务
            JSONObject nextNode = flowNode.getJSONObject("childNode");
            if (Objects.nonNull(nextNode)) {
                FlowElement flowElement = bpmnModel.getFlowElement(id);
                return create(id, nextNode,bpmnModel,process,sequenceFlows,nodeMap);
            } else {
                return id;
            }
        }

        else if (Type.CC.type.equals(nodeType)) {
            flowNode.put("incoming", Collections.singletonList(fromId));
            String id = createTask(flowNode,bpmnModel,process,sequenceFlows);

            // 如果当前任务还有后续任务，则遍历创建后续任务
            JSONObject nextNode = flowNode.getJSONObject("childNode");
            if (Objects.nonNull(nextNode)) {
                FlowElement flowElement = bpmnModel.getFlowElement(id);
                return create(id, nextNode,bpmnModel,process,sequenceFlows,nodeMap);
            } else {
                return id;
            }
        }
        else if (Type.OPERATE_TASK.type.equals(nodeType)) {
            flowNode.put("incoming", Collections.singletonList(fromId));
            String id = createTask(flowNode,bpmnModel,process,sequenceFlows);

            // 如果当前任务还有后续任务，则遍历创建后续任务
            JSONObject nextNode = flowNode.getJSONObject("childNode");
            if (Objects.nonNull(nextNode)) {
                FlowElement flowElement = bpmnModel.getFlowElement(id);
                return create(id, nextNode,bpmnModel,process,sequenceFlows,nodeMap);
            } else {
                return id;
            }
        }
        else if (Type.NOTICE_TASK.type.equals(nodeType)) {
            flowNode.put("incoming", Collections.singletonList(fromId));
            String id = createTask(flowNode,bpmnModel,process,sequenceFlows);

            // 如果当前任务还有后续任务，则遍历创建后续任务
            JSONObject nextNode = flowNode.getJSONObject("childNode");
            if (Objects.nonNull(nextNode)) {
                FlowElement flowElement = bpmnModel.getFlowElement(id);
                return create(id, nextNode,bpmnModel,process,sequenceFlows,nodeMap);
            } else {
                return id;
            }
        }
        else if (Type.EVENT_TASK.type.equals(nodeType)) {
            flowNode.put("incoming", Collections.singletonList(fromId));
            String id = createTask(flowNode,bpmnModel,process,sequenceFlows);

            // 如果当前任务还有后续任务，则遍历创建后续任务
            JSONObject nextNode = flowNode.getJSONObject("childNode");
            if (Objects.nonNull(nextNode)) {
                FlowElement flowElement = bpmnModel.getFlowElement(id);
                return create(id, nextNode,bpmnModel,process,sequenceFlows,nodeMap);
            } else {
                return id;
            }
        }
        else {
            throw new RuntimeException("未知节点类型: nodeType=" + nodeType);
        }
    }

    private static String createExclusiveGatewayBuilder(String formId, JSONObject flowNode,BpmnModel bpmnModel,
                                                        Process process,List<SequenceFlow> sequenceFlows,
    Map<String,JSONObject> map) throws InvocationTargetException, IllegalAccessException {
        String name = flowNode.getString("name");
        String exclusiveGatewayId = id("exclusiveGateway");
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(exclusiveGatewayId);
        exclusiveGateway.setName(name);
        process.addFlowElement(exclusiveGateway);
        process.addFlowElement(connect(formId, exclusiveGatewayId,sequenceFlows));

        if (Objects.isNull(flowNode.getJSONArray("conditionNodes")) && Objects.isNull(flowNode.getJSONObject("childNode"))) {
            return exclusiveGatewayId;
        }
        List<JSONObject> flowNodes = Optional.ofNullable(flowNode.getJSONArray("conditionNodes")).map(e -> e.toJavaList(JSONObject.class)).orElse(Collections.emptyList());
        List<String> incoming = Lists.newArrayListWithCapacity(flowNodes.size());

        List<JSONObject> conditions = Lists.newCopyOnWriteArrayList();
        for (JSONObject element : flowNodes) {
            JSONObject childNode = element.getJSONObject("childNode");

            String nodeName = element.getString("content");
            String expression = element.getString("${1 ==1 }");

            if (Objects.isNull(childNode)) {
                incoming.add(exclusiveGatewayId);
                JSONObject condition = new JSONObject();
                condition.fluentPut("content", nodeName)
                        .fluentPut("expression", expression);
                conditions.add(condition);
                continue;
            }
            // 只生成一个任务，同时设置当前任务的条件
            childNode.put("incoming", Collections.singletonList(exclusiveGatewayId));
            String identifier = create(exclusiveGatewayId, childNode,bpmnModel,process,sequenceFlows,map);
            List<SequenceFlow> flows = sequenceFlows.stream().filter(flow -> StringUtils.equals(exclusiveGatewayId, flow.getSourceRef()))
                    .collect(Collectors.toList());
            flows.stream().forEach(
                    e -> {
                        if (StringUtils.isBlank(e.getName()) && StringUtils.isNotBlank(nodeName)) {
                            e.setName(nodeName);
                        }
                        // 设置条件表达式
                        if (Objects.isNull(e.getConditionExpression()) && StringUtils.isNotBlank(expression)) {
                            e.setConditionExpression(expression);
                        }
                    }
            );
            if (Objects.nonNull(identifier)) {
                incoming.add(identifier);
            }
        }


        JSONObject childNode = flowNode.getJSONObject("childNode");
        ExclusiveGateway exclusiveGatewayNet=new ExclusiveGateway();
        exclusiveGatewayNet.setId("ex"+flowNode.getString("id")+"net");
        process.addFlowElement(exclusiveGatewayNet);
        if (Objects.nonNull(childNode)) {
            if (incoming == null || incoming.isEmpty()) {
                return create(exclusiveGatewayId, childNode,bpmnModel,process,sequenceFlows,map);
            } else {
                // 所有 service task 连接 end exclusive gateway
                childNode.put("incoming", incoming);
                FlowElement flowElement = bpmnModel.getFlowElement(incoming.get(0));
                // 1.0 先进行边连接, 暂存 nextNode
                JSONObject nextNode = childNode.getJSONObject("childNode");
                childNode.put("childNode", null);
                String identifier = create(flowElement.getId(), childNode,bpmnModel,process,sequenceFlows,map);
                for (int i = 1; i < incoming.size(); i++) {
                    process.addFlowElement(connect(incoming.get(i), identifier,sequenceFlows));
                }

                //  针对 gateway 空任务分支 添加条件表达式
                if (!conditions.isEmpty()) {
                    FlowElement flowElement1 = bpmnModel.getFlowElement(identifier);
                    // 获取从 gateway 到目标节点 未设置条件表达式的节点
                    List<SequenceFlow> flows = sequenceFlows.stream().filter(flow -> StringUtils.equals(flowElement1.getId(), flow.getTargetRef()))
                            .filter(flow -> StringUtils.equals(flow.getSourceRef(), exclusiveGatewayId))
                            .collect(Collectors.toList());
                    flows.stream().forEach(sequenceFlow -> {
                        if (!conditions.isEmpty()) {
                            JSONObject condition = conditions.get(0);
                            String nodeName = condition.getString("name");
                            String expression = condition.getString("${1==1}");

                            if (StringUtils.isBlank(sequenceFlow.getName()) && StringUtils.isNotBlank(nodeName)) {
                                sequenceFlow.setName(nodeName);
                            }
                            // 设置条件表达式
                            if (Objects.isNull(sequenceFlow.getConditionExpression()) && StringUtils.isNotBlank(expression)) {
                                sequenceFlow.setConditionExpression(expression);
                            }

                            conditions.remove(0);
                        }
                    });

                }

                // 1.1 边连接完成后，在进行 nextNode 创建
                if (Objects.nonNull(nextNode)) {
                    return create(identifier, nextNode,bpmnModel,process,sequenceFlows,map);
                } else {
                    return identifier;
                }
            }
        }
        else{
            // 所有 service task 连接 end exclusive gateway
            String identifier = exclusiveGatewayNet.getId();
            for (int i = 0; i < incoming.size(); i++) {
                process.addFlowElement(connect(incoming.get(i), identifier,sequenceFlows));
            }
            return identifier;
        }

    }

    private static String createParallelGatewayBuilder(String formId, JSONObject flowNode,BpmnModel bpmnModel,
                                                       Process process, List<SequenceFlow> sequenceFlows,Map<String,JSONObject> map) throws InvocationTargetException, IllegalAccessException {
        String name = flowNode.getString("name");
        ParallelGateway parallelGateway = new ParallelGateway();
        String parallelGatewayId = id("parallelGateway");
        parallelGateway.setId(parallelGatewayId);
        parallelGateway.setName(name);
        process.addFlowElement(parallelGateway);
        process.addFlowElement(connect(formId, parallelGatewayId,sequenceFlows));

        if (Objects.isNull(flowNode.getJSONArray("conditionNodes"))
                && Objects.isNull(flowNode.getJSONObject("childNode"))) {
            return parallelGatewayId;
        }

        List<JSONObject> flowNodes = Optional.ofNullable(flowNode.getJSONArray("conditionNodes")).map(e -> e.toJavaList(JSONObject.class)).orElse(Collections.emptyList());
        List<String> incoming = Lists.newArrayListWithCapacity(flowNodes.size());
        for (JSONObject element : flowNodes) {
            JSONObject childNode = element.getJSONObject("childNode");
            if (Objects.isNull(childNode)) {
                incoming.add(parallelGatewayId);
                continue;
            }
            String identifier = create(parallelGatewayId, childNode,bpmnModel,process,sequenceFlows,map);
            if (Objects.nonNull(identifier)) {
                incoming.add(identifier);
            }
        }

        JSONObject childNode = flowNode.getJSONObject("childNode");
        if (Objects.nonNull(childNode)) {
            // 普通结束网关
            if (CollectionUtils.isEmpty(incoming)) {
                return create(parallelGatewayId, childNode,bpmnModel,process,sequenceFlows,map);
            } else {
                // 所有 service task 连接 end parallel gateway
                childNode.put("incoming", incoming);
                FlowElement flowElement = bpmnModel.getFlowElement(incoming.get(0));
                // 1.0 先进行边连接, 暂存 nextNode
                JSONObject nextNode = childNode.getJSONObject("childNode");
                String parallelGatewayIdNet="";
                if(Type.PARALLEL_XX.type.equals(childNode.getInteger("type"))){
                    ParallelGateway parallelGatewayNet = new ParallelGateway();
                     parallelGatewayIdNet = "ex"+childNode.getString("pid")+"net";
                    parallelGatewayNet.setId(parallelGatewayIdNet);
                    parallelGatewayNet.setName(name);
                    process.addFlowElement(parallelGatewayNet);
                    process.addFlowElement(connect(incoming.get(0), parallelGatewayIdNet,sequenceFlows));
                }

                childNode.put("childNode", null);
                String identifier = parallelGatewayIdNet;/*create(incoming.get(0), childNode,bpmnModel,process,sequenceFlows);*/
                for (int i = 1; i < incoming.size(); i++) {
                    FlowElement flowElement1 = bpmnModel.getFlowElement(incoming.get(i));
                    process.addFlowElement(connect(flowElement1.getId(), identifier,sequenceFlows));
                }
                // 1.1 边连接完成后，在进行 nextNode 创建
                if (Objects.nonNull(nextNode)) {
                    return create(identifier, nextNode,bpmnModel,process,sequenceFlows,map);
                } else {
                    return identifier;
                }
            }
        }
        return parallelGatewayId;
    }

    private static String createTask(JSONObject flowNode,BpmnModel bpmnModel,Process process,List<SequenceFlow> sequenceFlows) {
        List<String> incoming = flowNode.getJSONArray("incoming").toJavaList(String.class);
        // 自动生成id
        String id = id("serviceTask");
        if (incoming != null && !incoming.isEmpty()) {
            ServiceTask serviceTask = new ServiceTask();
            serviceTask.setName(flowNode.getString("name"));
            serviceTask.setImplementationType(IMPLEMENTATION_TYPE_CLASS);
            serviceTask.setImplementation("com.dingding.mid.listener.ServiceListener");
            serviceTask.setId(id);
            process.addFlowElement(serviceTask);
            process.addFlowElement(connect(incoming.get(0), id,sequenceFlows));
        }
        return id;
    }
    private static String createUserTask(JSONObject flowNode,BpmnModel bpmnModel,Process process,List<SequenceFlow> sequenceFlows) {
        List<String> incoming = flowNode.getJSONArray("incoming").toJavaList(String.class);
        // 自动生成id
        String id = id("userTask");
        if (incoming != null && !incoming.isEmpty()) {
            UserTask userTask = new UserTask();
            userTask.setName(flowNode.getString("name"));
            userTask.setId(id);
            JSONObject attr = flowNode.getJSONObject("attr");
            Integer approvalMethod = attr.getInteger("approvalMethod");
            if(approvalMethod!=null){
                MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
                // 审批人集合参数
                multiInstanceLoopCharacteristics.setInputDataItem(userTask.getId()+"assigneeList");
                // 迭代集合
                multiInstanceLoopCharacteristics.setElementVariable("assigneeName");
                // 并行
                multiInstanceLoopCharacteristics.setSequential(false);
                userTask.setAssignee("${assigneeName}");
                // 设置多实例属性
                userTask.setLoopCharacteristics(multiInstanceLoopCharacteristics);
                if(approvalMethod.equals(4)){
                    multiInstanceLoopCharacteristics.setCompletionCondition("${nrOfCompletedInstances/nrOfInstances > 0}");
                }
                else if (approvalMethod.equals(1)){
                    multiInstanceLoopCharacteristics.setSequential(true);
                }
            }


            process.addFlowElement(userTask);
            process.addFlowElement(connect(incoming.get(0), id,sequenceFlows));
        }
        return id;
    }
    private static String createSimpleUserTask(JSONObject flowNode,BpmnModel bpmnModel,Process process,List<SequenceFlow> sequenceFlows) {
        List<String> incoming = flowNode.getJSONArray("incoming").toJavaList(String.class);
        // 自动生成id
        String id = id("userTask");
        if (incoming != null && !incoming.isEmpty()) {
            UserTask userTask = new UserTask();
            userTask.setName(flowNode.getString("name"));
            userTask.setId(id);
            process.addFlowElement(userTask);
            process.addFlowElement(connect(incoming.get(0), id,sequenceFlows));
        }
        return id;
    }


    private enum Type {
        ROOT(0,UserTask.class),
        CC(2,ServiceTask.class),
        SEQUENCE_FLOW(3,SequenceFlow.class),
        OPERATE_TASK(6,UserTask.class),
        NOTICE_TASK(20,ServiceTask.class),
        EVENT_TASK(5,ServiceTask.class),
        /**
         * 并行事件
         */
        PARALLEL(9, ParallelGateway.class),
        PARALLEL_S(10, SequenceFlow.class),
        PARALLEL_XX(11, ParallelGateway.class),

        /**
         * 排他事件
         */
        EXCLUSIVE(4, ExclusiveGateway.class),

        /**
         * 任务
         */
        USER_TASK(1, UserTask.class);

        private Integer type;

        private Class<?> typeClass;

        Type(Integer type, Class<?> typeClass) {
            this.type = type;
            this.typeClass = typeClass;
        }

        public final static Map<Integer, Class<?>> TYPE_MAP = Maps.newHashMap();

        static {
            for (Type element : Type.values()) {
                TYPE_MAP.put(element.type, element.typeClass);
            }
        }

        public boolean isEqual(String type) {
            return this.type.equals(type);
        }

    }

}



