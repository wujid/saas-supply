package com.supply.bpm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.mapper.ProcessRunMapper;
import com.supply.bpm.model.request.TaskRequest;
import com.supply.bpm.model.response.TaskResponse;
import com.supply.common.model.Result;
import io.swagger.annotations.Api;
import org.activiti.engine.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wjd
 * @description .
 * @date 2023-06-25
 */
@Api(tags = "外部服务调用")
@RestController
@RequestMapping("/test")
public class TestController {

    private final TaskService taskService;

    private final ProcessRunMapper processRunMapper;

    public TestController(TaskService taskService, ProcessRunMapper processRunMapper) {
        this.taskService = taskService;
        this.processRunMapper = processRunMapper;
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam Long userId) {
        Page<TaskRequest> page = new Page<>(1, 10);
        TaskRequest request = new TaskRequest();
        request.setAssigneeId(userId.toString());
        Set<String> assigneeGroups = new HashSet<>();
        assigneeGroups.add("role:7");
        request.setAssigneeGroups(assigneeGroups);
        final IPage<TaskResponse> data = processRunMapper.getMyTask(page, request);
        return Result.ok(data);
    }
}
