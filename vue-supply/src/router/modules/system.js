export default [
  {
    path: '/account',
    name: 'account',
    component: () => import('@/views/system/account'),
    meta: { title: '账号管理' }
  },
  {
    path: '/role',
    name: 'role',
    component: () => import('@/views/system/role/list'),
    meta: { title: '角色管理' }
  },
  {
    path: '/role-auth',
    name: 'role-auth',
    component: () => import('@/views/system/role/auth'),
    meta: { title: '权限设置', parentPath: '/role', parentTitle: '角色管理' }
  },
  {
    path: '/org',
    name: 'org',
    component: () => import('@/views/system/org'),
    meta: { title: '部门管理' }
  },
  {
    path: '/user-info',
    name: 'user-info',
    component: () => import('@/views/system/user-info'),
    meta: { title: '个人信息' }
  },
  {
    path: '/user-info-edit',
    name: 'user-info-edit',
    component: () => import('@/views/system/user-info/edit'),
    meta: { title: '个人信息编辑', parentPath: '/user-info', parentTitle: '个人信息' }
  },
  {
    path: '/menu',
    name: 'menu-manage',
    component: () => import('@/views/system/menu'),
    meta: { title: '菜单管理' }
  },
  {
    path: '/tenant',
    name: 'tenant-manage',
    component: () => import('@/views/system/tenant'),
    meta: { title: '租户管理' }
  },
  {
    path: '/tenant-info-add',
    name: 'tenant-info-add',
    component: () => import('@/views/system/tenant/form/form-add'),
    meta: { title: '新建租户', parentPath: '/tenant', parentTitle: '租户管理' }
  },
  {
    path: '/tenant-info-detail',
    name: 'tenant-info-detail',
    component: () => import('@/views/system/tenant/form/form-detail'),
    meta: { title: '租户详情', parentPath: '/tenant', parentTitle: '租户管理' }
  },
  {
    path: '/tenant-info-edit',
    name: 'tenant-info-edit',
    component: () => import('@/views/system/tenant/form/form-edit'),
    meta: { title: '租户编辑', parentPath: '/tenant', parentTitle: '租户管理' }
  },
  {
    path: '/org',
    name: 'org-manage',
    component: () => import('@/views/system/org'),
    meta: { title: '组织机构' }
  },
  {
    path: '/dict',
    name: 'dict-manage',
    component: () => import('@/views/system/dict'),
    meta: { title: '字典管理' }
  },
  {
    path: '/dict-category',
    name: 'dict-category',
    component: () => import('@/views/system/dict-category'),
    meta: { title: '字典分类' }
  },
  {
    path: '/data-scope',
    name: 'data-scope',
    component: () => import('@/views/system/data-scope'),
    meta: { title: '数据权限' }
  }
]
