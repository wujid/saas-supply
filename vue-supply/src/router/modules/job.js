export default [
  {
    path: '/job',
    name: 'job-task',
    component: () => import('@/views/system/job'),
    meta: { title: '定时任务' }
  }
]
