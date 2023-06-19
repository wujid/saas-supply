package com.supply.bpm.util;

import cn.hutool.core.util.XmlUtil;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Gateway;
import org.activiti.bpmn.model.InclusiveGateway;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.ReceiveTask;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author wjd
 * @description 流程通用工具类.
 * @date 2023-06-19
 */
public class ActivityUtil {

    /**
      * @description 获取流程的开始节点.
      * @author wjd
      * @date 2023/6/19
      * @param flowElements 所有流程节点
      */
    public static FlowElement getStartFlowElement(Collection<FlowElement> flowElements) {
        for (FlowElement flowElement : flowElements){
            if(flowElement instanceof StartEvent){
                return flowElement;
            }
        }
        return null;
    }

    /**
      * @description 获取流程上所有用户节点信息.
      * @author wjd
      * @date 2023/6/19
      * @param flowElements 全流程节点集合
      */
    public static List<UserTask> getUserTaskList(Collection<FlowElement> flowElements) {
        List<UserTask> userTaskList = new ArrayList<>();
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                userTaskList.add(userTask);
            }
        }
        return userTaskList;
    }

    /**
      * @description 通过bpmnXml获取流程名称.
      * @author wjd
      * @date 2023/6/19
      * @param bpmnXml xml
      */
    public static String getBpmnNameByXml(String bpmnXml) {
        Document document = XmlUtil.parseXml(bpmnXml);
        final NodeList elementsByTagName = document.getElementsByTagName("bpmn2:process");
        final Node node = elementsByTagName.item(0).getAttributes().getNamedItem("name");
        if (null == node) {
            return null;
        }
        return node.getNodeValue();
    }

    /**
      * @description 查询下一步节点.
      * @author wjd
      * @date 2023/6/19
      * @param flowElements  全流程节点集合
      * @param flowElement   当前节点
      * @param map           业务数据
      * @param nextUser      下一步用户节点
      */
    public static void getNextNode(Collection<FlowElement> flowElements, FlowElement flowElement, Map<String, Object> map, List<UserTask> nextUser) {
        //如果是结束节点
        if(flowElement instanceof EndEvent) {
            //如果是子任务的结束节点
            if(getSubProcess(flowElements,flowElement) != null){
                flowElement = getSubProcess(flowElements,flowElement);
            }
        }
        //获取Task的出线信息--可以拥有多个
        List<SequenceFlow> outGoingFlows = null;
        if(flowElement instanceof UserTask) {
            outGoingFlows = ((UserTask) flowElement).getOutgoingFlows();
        }else if(flowElement instanceof Gateway){
            outGoingFlows = ((Gateway) flowElement).getOutgoingFlows();
        }else if(flowElement instanceof StartEvent){
            outGoingFlows = ((StartEvent) flowElement).getOutgoingFlows();
        }else if(flowElement instanceof SubProcess){
            outGoingFlows = ((SubProcess) flowElement).getOutgoingFlows();
        }
        if(outGoingFlows != null && !outGoingFlows.isEmpty()) {
            // 遍历所有的出线--找到可以正确执行的那一条
            for (SequenceFlow sequenceFlow : outGoingFlows) {
                // 1.有表达式，且为true
                // 2.无表达式
                String expression = sequenceFlow.getConditionExpression();
                boolean isTrue = StringUtils.isBlank(expression) || expressionParsing(expression, map);
                if(isTrue) {
                    // 出线的下一个节点
                    String nextFlowElementID = sequenceFlow.getTargetRef();
                    // 查询下一个节点信息
                    FlowElement nextFlowElement = getFlowElementById(nextFlowElementID, flowElements);

                    // 用户任务
                    if (nextFlowElement instanceof UserTask) {
                        nextUser.add((UserTask) nextFlowElement);
                    }
                    // 排他网关
                    else if (nextFlowElement instanceof ExclusiveGateway) {
                        getNextNode(flowElements, nextFlowElement, map, nextUser);
                    }
                    // 并行网关
                    else if (nextFlowElement instanceof ParallelGateway) {
                        getNextNode(flowElements, nextFlowElement, map, nextUser);
                    }
                    // 包含网关
                    else if (nextFlowElement instanceof InclusiveGateway) {
                        getNextNode(flowElements, nextFlowElement, map, nextUser);
                    }
                    // 接收任务
                    else if (nextFlowElement instanceof ReceiveTask) {
                        getNextNode(flowElements, nextFlowElement, map, nextUser);
                    }
                    // 子任务的起点
                    else if(nextFlowElement instanceof StartEvent) {
                        getNextNode(flowElements, nextFlowElement, map, nextUser);
                    }
                    // 结束节点
                    else if(nextFlowElement instanceof EndEvent) {
                        getNextNode(flowElements, nextFlowElement, map, nextUser);
                    }
                }
            }
        }
    }

    /**
      * @description 查询一个节点的是否子任务中的节点,如果是,返回子任务.
      * @author wjd
      * @date 2023/6/19
      * @param flowElements 全流程的节点集合
      * @param flowElement   当前节点
      */
    public static FlowElement getSubProcess(Collection<FlowElement> flowElements,FlowElement flowElement) {
        for(FlowElement flowElement1 : flowElements) {
            if(flowElement1 instanceof SubProcess) {
                for(FlowElement flowElement2 : ((SubProcess) flowElement1).getFlowElements()) {
                    if(flowElement.equals(flowElement2)) {
                        return flowElement1;
                    }
                }
            }
        }
        return null;
    }

    /**
      * @description 解析EL表达式并获取结果.
      * @author wjd
      * @date 2023/6/19
      * @param skipExpress 表达式
      * @param map 参数
      */
    public static Boolean expressionParsing(String skipExpress, Map<String, Object> map) {
        if (StringUtils.isBlank(skipExpress) && map.isEmpty()){
            return false;
        }
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        TemplateParserContext templateParserContext = new TemplateParserContext("${", "}");
        MapAccessor propertyAccessor = new MapAccessor();
        context.setVariables(map);
        context.setPropertyAccessors(List.of(propertyAccessor));

        SpelExpression expression = (SpelExpression) parser.parseExpression(skipExpress, templateParserContext);
        expression.setEvaluationContext(context);
        return expression.getValue(map, boolean.class);
    }

    /**
      * @description 根据ID查询流程节点对象,如果是子任务,则返回子任务的开始节点.
      * @author wjd
      * @date 2023/6/19
      * @param Id 节点ID
      * @param flowElements  流程节点集合
      */
    public static FlowElement getFlowElementById(String Id,Collection<FlowElement> flowElements) {
        for(FlowElement flowElement : flowElements){
            if(flowElement.getId().equals(Id)){
                //如果是子任务,则查询出子任务的开始节点
                if(flowElement instanceof SubProcess){
                    return getStartFlowElement(((SubProcess) flowElement).getFlowElements());
                }
                return flowElement;
            }
            if(flowElement instanceof SubProcess){
                FlowElement flowElement1 = getFlowElementById(Id,((SubProcess) flowElement).getFlowElements());
                if(flowElement1 != null){
                    return flowElement1;
                }
            }
        }
        return null;
    }


}
