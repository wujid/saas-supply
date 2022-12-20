import request from '@/utils/request'
const api = process.env.VUE_APP_BASE_API + '/bpm'

// 新增流程分类
export function addCategory(data) {
  return request({
    url: api + '/actModel/addCategory',
    method: 'post',
    data
  })
}

// 修改流程分类
export function updateCategory(data) {
  return request({
    url: api + '/actModel/updateCategory',
    method: 'post',
    data
  })
}

// 删除流程分类
export function delCategory(params) {
  return request({
    url: api + '/actModel/delCategory',
    method: 'get',
    params
  })
}

// 流程分类树结构
export function getCategoryTreeByParams(params) {
  return request({
    url: api + '/actModel/getCategoryTreeByParams',
    method: 'get',
    params
  })
}
