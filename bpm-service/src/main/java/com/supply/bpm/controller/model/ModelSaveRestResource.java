/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.supply.bpm.controller.model;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.supply.bpm.model.po.ActModelPo;
import com.supply.bpm.model.po.ProcessDefinitionPo;
import com.supply.bpm.model.request.ActModelRequest;
import com.supply.bpm.model.request.ProcessDefinitionRequest;
import com.supply.bpm.repository.IActModelRepository;
import com.supply.bpm.repository.IProcessDefinitionRepository;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author wjd
 * @description 流程保存.
 * @date 2022-12-19
 */
@Slf4j
@RestController
@RequestMapping("/bpm/act")
public class ModelSaveRestResource implements ModelDataJsonConstants {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IActModelRepository actModelRepository;

    @Autowired
    private IProcessDefinitionRepository processDefinitionRepository;

    @RequestMapping(value = "/model/{modelId}/save", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional(rollbackFor = Exception.class)
    public void saveModel(@PathVariable String modelId,
                          @RequestParam String name, @RequestParam String description,
                          @RequestParam String json_xml, @RequestParam String svg_xml) {

        try {
            Model model = repositoryService.getModel(modelId);

            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            int newVersion = model.getVersion() + 1;
            modelJson.put(MODEL_NAME, name);
            modelJson.put(MODEL_DESCRIPTION, description);
            modelJson.put(MODEL_REVISION, newVersion);

            model.setMetaInfo(modelJson.toString());
            model.setName(name);
            model.setVersion(newVersion);
            repositoryService.saveModel(model);

            repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes(StandardCharsets.UTF_8));

            InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes(StandardCharsets.UTF_8));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);

            // Do the transformation
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();

            // 更新模型
            final ActModelPo actModelPo = this.saveModel(modelId, name, json_xml, description, newVersion);
            // 部署流程
            this.deployModel(modelId, actModelPo);
        } catch (Exception e) {
            final String message = "保存模型出错";
            log.error(message, e);
            throw new ActivitiException(message, e);
        }
    }

    private ActModelPo saveModel(String modelId, String name, String json_xml, String description, int version) {
        ActModelRequest request = new ActModelRequest();
        request.setModelId(modelId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final ActModelPo actModelPo = actModelRepository.getByParams(request);
        if (null == actModelPo) {
            final String message = StrUtil.format("根据模型ID{}未查询到流程模型拓展信息", modelId);
            log.error(message);
            throw new ApiException(message);
        }
        ActModelPo actModel = new ActModelPo();
        actModel.setModelName(name);
        String key = StrUtil.subBetween(json_xml, "\"process_id\":\"", "\",\"name\"");
        actModel.setModelKey(key);
        actModel.setDescription(description);
        actModel.setVersion(version);
        actModelPo.setBusinessStatus(BusinessStatusEnum.DEPLOYED.getStatus());
        actModelRepository.updateById(actModelPo);
        return actModelRepository.getById(actModelPo.getId());
    }

    private void deployModel(String modelId, ActModelPo actModelPo) throws IOException {
        // 获取流程信息并转换成bpmn流程
        byte[] bytes = repositoryService.getModelEditorSource(modelId);
        JsonNode modelNode = new ObjectMapper().readTree(bytes);
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        if (CollectionUtil.isEmpty(bpmnModel.getProcesses())) {
            final String message = StrUtil.format("模型ID{}转换成BPMN模型异常", modelId);
            log.error(message);
            throw new ApiException(message);
        }
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
        // 部署发布模型流程
        String processName = actModelPo.getModelName() + ".bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name(actModelPo.getModelName())
                .addString(processName, new String(bpmnBytes, StandardCharsets.UTF_8))
                .deploy();
        // 根据流程部署ID查询出对应的流程定义信息
        final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

        // 历史数据修改为非主要流程
        ProcessDefinitionPo processDefinitionPo = new ProcessDefinitionPo();
        processDefinitionPo.setIsMain(false);
        ProcessDefinitionRequest request = new ProcessDefinitionRequest();
        request.setIsMain(true);
        request.setProcessKey(actModelPo.getModelKey());
        request.setStatus(Constant.STATUS_NOT_DEL);
        processDefinitionRepository.updateByParams(processDefinitionPo, request);
        // 保存当前流程定义信息
        processDefinitionPo = new ProcessDefinitionPo();
        processDefinitionPo.setCategoryId(actModelPo.getCategoryId());
        processDefinitionPo.setDeploymentId(deployment.getId());
        processDefinitionPo.setProcessDefinitionId(processDefinition.getId());
        processDefinitionPo.setProcessKey(actModelPo.getModelKey());
        processDefinitionPo.setProcessName(actModelPo.getModelName());
        processDefinitionPo.setDescription(actModelPo.getDescription());
        processDefinitionPo.setVersion(processDefinition.getVersion());
        processDefinitionPo.setDiagramName(processDefinition.getDiagramResourceName());
        processDefinitionPo.setXmlName(processDefinition.getResourceName());
        processDefinitionPo.setIsMain(true);
        processDefinitionPo.setBusinessStatus(BusinessStatusEnum.PROCESS_STATUS_ACTIVE.getStatus());
        processDefinitionPo.setTenantId(actModelPo.getTenantId());
        processDefinitionRepository.save(processDefinitionPo);
    }
}
