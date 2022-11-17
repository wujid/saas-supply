import request from '@/utils/request'
const api = process.env.VUE_APP_BASE_API + '/system'

// 密码模式登录
export function loginForPwd(data) {
  return request({
    url: api + '/login/loginForPwd',
    method: 'post',
    data
  })
}

// 登出
export function logout() {
  return request({
    url: api + '/login/logout',
    method: 'post'
  })
}

// 生成随机验证码
export function generateCaptcha(params) {
  return request({
    url: api + '/login/generateCaptcha',
    method: 'get',
    responseType: 'blob',
    params
  })
}

// 获取当前登录用户信息
export function getInfo() {
  return request({
    url: api + '/login/getUserInfo',
    method: 'get'
  })
}

// 获取部门对应的员工列表
export function getUserPage(params) {
  return request({
    url: api + '/sysUser/getUserPage',
    method: 'get',
    params
  })
}

// 新增用户信息
export function addUser(data) {
  return request({
    url: api + '/sysUser/addUser',
    method: 'post',
    data
  })
}

// 修改用户信息
export function updateUser(data) {
  return request({
    url: api + '/sysUser/updateUser',
    method: 'post',
    data
  })
}

// 重置密码
export function resetPassword(params) {
  return request({
    url: api + '/sysUser/resetPassword',
    method: 'get',
    params
  })
}

// 账户解冻
export function activeUser(params) {
  return request({
    url: api + '/sysUser/activeUser',
    method: 'get',
    params
  })
}

// 账户冻结
export function freezeUser(params) {
  return request({
    url: api + '/sysUser/freezeUser',
    method: 'get',
    params
  })
}

// 账户删除
export function delUser(params) {
  return request({
    url: api + '/sysUser/delUser',
    method: 'get',
    params
  })
}

// 根据自定义条件查询用户信息集
export function getUserListByParams(params) {
  return request({
    url: api + '/sysUser/getUserListByParams',
    method: 'get',
    params
  })
}

// 查询角色信息集
export function getRoleListByParams(params) {
  return request({
    url: api + '/sysRole/getRoleListByParams',
    method: 'get',
    params
  })
}

// 查询角色分页信息
export function getRolePageByParams(params) {
  return request({
    url: api + '/sysRole/getRolePageByParams',
    method: 'get',
    params
  })
}

// 新增角色信息
export function addRole(data) {
  return request({
    url: api + '/sysRole/addRole',
    method: 'post',
    data
  })
}

// 修改角色信息
export function updateRole(data) {
  return request({
    url: api + '/sysRole/updateRole',
    method: 'post',
    data
  })
}

// 角色冻结
export function freezeRole(params) {
  return request({
    url: api + '/sysRole/freezeRole',
    method: 'get',
    params
  })
}

// 角色解冻
export function activeRole(params) {
  return request({
    url: api + '/sysRole/activeRole',
    method: 'get',
    params
  })
}

// 角色删除
export function delRole(params) {
  return request({
    url: api + '/sysRole/delRole',
    method: 'get',
    params
  })
}

// 通过角色ID获取对应的资源权限
export function getAuthByRoleId(params) {
  return request({
    url: api + '/sysRole/getAuthByRoleId',
    method: 'get',
    params
  })
}

// 设置角色资源权限
export function setResourceAuth(data) {
  return request({
    url: api + '/sysRole/setResourceAuth',
    method: 'post',
    data
  })
}

// 取消用户授权
export function delUserAuth(params) {
  return request({
    url: api + '/userRole/delUserAuth',
    method: 'get',
    params
  })
}

// 新增用户角色关联关系
export function addUserRole(data) {
  return request({
    url: api + '/userRole/addUserRole',
    method: 'post',
    data
  })
}

// 新增资源信息
export function addResource(data) {
  return request({
    url: api + '/sysResource/addResource',
    method: 'post',
    data
  })
}

// 修改资源信息
export function updateResource(data) {
  return request({
    url: api + '/sysResource/updateResource',
    method: 'post',
    data
  })
}

// 删除资源
export function delResource(params) {
  return request({
    url: api + '/sysResource/delResource',
    method: 'get',
    params
  })
}

// 资源冻结
export function freezeResource(params) {
  return request({
    url: api + '/sysResource/freezeResource',
    method: 'get',
    params
  })
}

// 资源解冻
export function activeResource(params) {
  return request({
    url: api + '/sysResource/activeResource',
    method: 'get',
    params
  })
}

// 资源树结构
export function getResourceTree(params) {
  return request({
    url: api + '/sysResource/getResourceTree',
    method: 'get',
    params
  })
}

// 新增租户信息
export function addTenant(data) {
  return request({
    url: api + '/tenant/addTenant',
    method: 'post',
    data
  })
}

// 修改租户信息
export function updateTenant(data) {
  return request({
    url: api + '/tenant/updateTenant',
    method: 'post',
    data
  })
}

// 租户冻结
export function freezeTenant(params) {
  return request({
    url: api + '/tenant/freezeTenant',
    method: 'get',
    params
  })
}

// 租户解冻
export function activeTenant(params) {
  return request({
    url: api + '/tenant/activeTenant',
    method: 'get',
    params
  })
}

// 删除租户
export function delTenant(params) {
  return request({
    url: api + '/tenant/delTenant',
    method: 'get',
    params
  })
}

// 租户续期
export function renewalDays(params) {
  return request({
    url: api + '/tenant/renewalDays',
    method: 'get',
    params
  })
}

// 查询租户分页信息
export function getTenantPage(params) {
  return request({
    url: api + '/tenant/getTenantPage',
    method: 'get',
    params
  })
}

// 可用租户列表
export function getTenantList(params) {
  return request({
    url: api + '/tenant/getTenantList',
    method: 'get',
    params
  })
}

// 根据租户ID获取详细信息
export function getTenantDetail(params) {
  return request({
    url: api + '/tenant/getDetailById',
    method: 'get',
    params
  })
}

// 新增组织机构
export function addOrg(data) {
  return request({
    url: api + '/org/addOrg',
    method: 'post',
    data
  })
}

// 修改组织机构
export function updateOrg(data) {
  return request({
    url: api + '/org/updateOrg',
    method: 'post',
    data
  })
}

// 删除租户
export function delOrg(params) {
  return request({
    url: api + '/org/delOrg',
    method: 'get',
    params
  })
}

// 组织机构冻结
export function freezeOrg(params) {
  return request({
    url: api + '/org/freezeOrg',
    method: 'get',
    params
  })
}

// 组织机构解冻
export function activeOrg(params) {
  return request({
    url: api + '/org/activeOrg',
    method: 'get',
    params
  })
}

// 根据父级查询对应的子集
export function getOrgListByParentId(params) {
  return request({
    url: api + '/org/getOrgListByParentId',
    method: 'get',
    params
  })
}

// 组织机构树结构
export function getOrgTree(params) {
  return request({
    url: api + '/org/getOrgTreeByParams',
    method: 'get',
    params
  })
}

// 新增数据字典
export function addDict(data) {
  return request({
    url: api + '/dict/addDict',
    method: 'post',
    data
  })
}

// 修改数据字典
export function updateDict(data) {
  return request({
    url: api + '/dict/updateDict',
    method: 'post',
    data
  })
}

// 删除数据字典
export function delDict(params) {
  return request({
    url: api + '/dict/delDict',
    method: 'get',
    params
  })
}

// 数据字典分页信息
export function getDictPageByParams(params) {
  return request({
    url: api + '/dict/getDictPageByParams',
    method: 'get',
    params
  })
}

// 新增数据字典集
export function addDictItem(data) {
  return request({
    url: api + '/dictItem/addDictItem',
    method: 'post',
    data
  })
}

// 修改数据字典集
export function updateDictItem(data) {
  return request({
    url: api + '/dictItem/updateDictItem',
    method: 'post',
    data
  })
}

// 删除数据字典集
export function delDictItem(params) {
  return request({
    url: api + '/dictItem/delDictItem',
    method: 'get',
    params
  })
}

// 数据字典集分页信息
export function getDictItemPageByParams(params) {
  return request({
    url: api + '/dictItem/getDictItemPageByParams',
    method: 'get',
    params
  })
}

// 新增字典分类
export function addDictCategory(data) {
  return request({
    url: api + '/dictCategory/addDictCategory',
    method: 'post',
    data
  })
}

// 修改字典分类
export function updateDictCategory(data) {
  return request({
    url: api + '/dictCategory/updateDictCategory',
    method: 'post',
    data
  })
}

// 删除字典分类
export function delDictCategory(params) {
  return request({
    url: api + '/dictCategory/delDictCategory',
    method: 'get',
    params
  })
}

// 根据自定义条件查询带分页的字典分类信息集
export function getDictCategoryPageByParams(params) {
  return request({
    url: api + '/dictCategory/getDictCategoryPageByParams',
    method: 'get',
    params
  })
}

// 根据父级ID获取子级集
export function getChildrenByParentId(params) {
  return request({
    url: api + '/dictCategory/getChildrenByParentId',
    method: 'get',
    params
  })
}

// 新增用户资源数据权限
export function addDataScope(data) {
  return request({
    url: api + '/dataScope/addDataScope',
    method: 'post',
    data
  })
}

// 修改用户资源数据权限
export function updateDataScope(data) {
  return request({
    url: api + '/dataScope/updateDataScope',
    method: 'post',
    data
  })
}

// 删除用户资源数据权限
export function delDataScopeById(params) {
  return request({
    url: api + '/dataScope/delDataScopeById',
    method: 'get',
    params
  })
}

// 用户资源数据权限分页信息
export function getDataScopePageByParams(params) {
  return request({
    url: api + '/dataScope/getDataScopePageByParams',
    method: 'get',
    params
  })
}

// 根据自定义条件查询数据权限范围信息集
export function getDataScopeRangeListByParams(params) {
  return request({
    url: api + '/dataScope/getDataScopeRangeListByParams',
    method: 'get',
    params
  })
}

