package com.supply.bpm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.cvt.UserNodeCvt;
import com.supply.bpm.model.po.UserNodePo;
import com.supply.bpm.model.request.UserNodeRequest;
import com.supply.bpm.model.response.UserNodeResponse;
import com.supply.bpm.repository.IUserNodeRepository;
import com.supply.bpm.service.IUserNodeService;
import com.supply.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-06-15
 */
@Service
public class UserNodeServiceImpl implements IUserNodeService {
    private static final Logger logger = LoggerFactory.getLogger(UserNodeServiceImpl.class);

    private final IUserNodeRepository userNodeRepository;

    public UserNodeServiceImpl(IUserNodeRepository userNodeRepository) {
        this.userNodeRepository = userNodeRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserNodeFormUrl(Long id, String formUrl) {
        logger.info("[根据用户节点ID修改详情表单url]---待修改的用户节点id{}对应的表单url为{}", id, formUrl);
        UserNodePo userNodePo = new UserNodePo();
        userNodePo.setId(id);
        userNodePo.setFormUrl(formUrl);
        userNodeRepository.updateById(userNodePo);
    }

    @Override
    public IPage<UserNodeResponse> getUserNodePage(UserNodeRequest request) {
        Page<UserNodePo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<UserNodePo> poPage = userNodeRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<UserNodePo> poList = poPage.getRecords();
        final List<UserNodeResponse> responseList = UserNodeCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }
}
