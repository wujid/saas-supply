package com.supply.job.file;

import com.supply.job.api.FileClient;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wjd
 * @description
 * @date 2022-09-23
 */
@Component
public class FileJob {
    private static final Logger logger = LoggerFactory.getLogger(FileJob.class);

    private final FileClient fileClient;

    public FileJob(FileClient fileClient) {
        this.fileClient = fileClient;
    }

    @XxlJob("delUnRelationAttachmentJobHandler")
    public void delUnRelationAttachmentJobHandler() {
        String message = "开始执行删除未关联的附件";
        XxlJobHelper.log(message);
        logger.info(message);

        fileClient.delUnRelationAttachment();

        message = "未关联附件删除调度完成";
        XxlJobHelper.log(message);
        logger.info(message);
    }
}
