import request from '@/utils/request'
const api = process.env.VUE_APP_BASE_API + '/file'

// 单附件上传
export function upload(data) {
  return request({
    url: api + '/attachment/upload',
    method: 'post',
    data
  })
}

// 单附件下载
export function download(params) {
  return request({
    url: api + '/attachment/downloadByParams',
    method: 'get',
    params
  })
}

// 根据附件ID删除附件
export function delUnRelationAttachmentById(params) {
  return request({
    url: api + '/attachment/delUnRelationAttachmentById',
    method: 'get',
    params
  })
}

// 根据条件查询唯一附件信息
export function getAttachmentByParams(params) {
  return request({
    url: api + '/attachment/getAttachmentByParams',
    method: 'get',
    params
  })
}
