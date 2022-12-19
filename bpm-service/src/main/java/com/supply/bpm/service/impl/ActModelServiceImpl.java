package com.supply.bpm.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.supply.bpm.cvt.ActModelCvt;
import com.supply.bpm.model.po.ActModelPo;
import com.supply.bpm.model.request.ActModelRequest;
import com.supply.bpm.repository.IActModelRepository;
import com.supply.bpm.service.IActModelService;
import com.supply.common.exception.ApiException;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author wjd
 * @description .
 * @date 2022-12-19
 */
@Service
public class ActModelServiceImpl implements IActModelService {
    private static final Logger logger = LoggerFactory.getLogger(ActModelServiceImpl.class);

    private final IActModelRepository actModelRepository;

    private final ObjectMapper objectMapper;

    private final RepositoryService repositoryService;

    public ActModelServiceImpl(IActModelRepository actModelRepository, ObjectMapper objectMapper,
                               RepositoryService repositoryService) {
        this.actModelRepository = actModelRepository;
        this.objectMapper = objectMapper;
        this.repositoryService = repositoryService;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addModel(ActModelRequest request) {
        logger.info("[新增流程模型]---其中待新增的流程模型信息为{}", JSON.toJSONString(request));
        // 初始化一个空模型
        Model model = repositoryService.newModel();
        // 创建模型节点
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, request.getModelName());
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, request.getDescription());
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, model.getVersion());
        // 保存模型
        repositoryService.saveModel(model);
        String modelId = model.getId();

        // 完善ModelEditorSource
        ObjectNode editorNode = objectMapper.createObjectNode();
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        ObjectNode properties = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.replace("stencilset", stencilSetNode);
        properties.put("process_id", request.getModelKey());
        editorNode.replace("properties", properties);
        try {
            repositoryService.addModelEditorSource(modelId, editorNode.toString().getBytes(StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            final String message = "新增模型失败";
            throw new ApiException(message, e);
        }

        // 保存扩展模型至数据库
        final ActModelPo actModelPo = ActModelCvt.INSTANCE.requestToPo(request);
        actModelPo.setModelId(modelId);
        actModelPo.setVersion(model.getVersion());
        actModelRepository.save(actModelPo);
    }
}
