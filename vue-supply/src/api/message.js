import request from '@/utils/request'
const api = process.env.VUE_APP_BASE_API + '/message'

// 新增消息模板
export function addTemplate(data) {
  return request({
    url: api + '/template/addTemplate',
    method: 'post',
    data
  })
}

// 修改消息模板
export function updateTemplate(data) {
  return request({
    url: api + '/template/updateTemplate',
    method: 'post',
    data
  })
}

// 删除消息模板
export function delTemplate(params) {
  return request({
    url: api + '/template/delTemplate',
    method: 'get',
    params
  })
}

// 根据自定义条件查询模板分页信息
export function getTemplatePageByParams(params) {
  return request({
    url: api + '/template/getTemplatePageByParams',
    method: 'get',
    params
  })
}

// 根据模板ID获取模板详细信息
export function getTemplateInfoById(params) {
  return request({
    url: api + '/template/getTemplateInfoById',
    method: 'get',
    params
  })
}

// 发送消息
export function sendMessage(data) {
  return request({
    url: api + '/content/sendMessage',
    method: 'post',
    data
  })
}

// 修改消息内容为已读状态
export function updateReaderById(params) {
  return request({
    url: api + '/content/updateReaderById',
    method: 'get',
    params
  })
}

// 修改消息全部已读
export function updateAllReader(params) {
  return request({
    url: api + '/content/updateAllReader',
    method: 'get',
    params
  })
}

// 删除消息内容
export function delContentById(params) {
  return request({
    url: api + '/content/delContentById',
    method: 'get',
    params
  })
}

// 消息内容分页信息
export function getContentPage(params) {
  return request({
    url: api + '/content/getContentPage',
    method: 'get',
    params
  })
}

