import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import locale from 'element-ui/lib/locale/lang/en' // lang i18n

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control

import CodeEditor from 'bin-code-editor'
import 'bin-code-editor/lib/styles/index.css'
Vue.use(CodeEditor)

import '@/directives'// 自定义指令

import myTable from '@/components/table'
Vue.component('MyTable', myTable)

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
if (process.env.NODE_ENV === 'production') {
  const { mockXHR } = require('../mock')
  mockXHR()
}

// 全局修改默认配置， 点击空白处不关闭弹窗
ElementUI.Dialog.props.closeOnClickModal.default = false
// 全局修改默认配置， 按下ESC不关闭弹窗
ElementUI.Dialog.props.closeOnPressEscape.default = false

// 全局注册websocket
import websocket from '@/utils/websocket'
Vue.prototype.$websocket = websocket

// set ElementUI lang to EN
// Vue.use(ElementUI, { locale })
// 如果想要中文版 element-ui，按如下方式声明
Vue.use(ElementUI)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
