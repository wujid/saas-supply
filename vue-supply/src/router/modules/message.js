export default [
  {
    path: '/message-template',
    name: 'message-template',
    component: () => import('@/views/message/template'),
    meta: { title: '消息模板' }
  },
  {
    path: '/message-content',
    name: 'message-content',
    component: () => import('@/views/message/content'),
    meta: { title: '我的消息' }
  }
]
