package com.supply.bpm.util;

import cn.hutool.core.util.XmlUtil;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description 解析流程图通用工具类.
 * @Author wjd
 * @Date 2021/12/29
 */
public class ActivityUtil {


    /**
     * @author wjd
     * @description 获取流程上所有用户节点信息.
     * @date 2022/2/24
     * @param flowElements 全流程节点集合
     * @return 用户节点信息
     **/
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
      * @date 2023/6/7
      * @param
      * @return java.lang.String
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
}
