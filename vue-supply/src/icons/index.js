import Vue from 'vue'
import SvgIcon from '@/components/SvgIcon'// svg component

// register globally
Vue.component('svg-icon', SvgIcon)

const req = require.context('./svg', false, /\.svg$/)
const requireAll = requireContext => requireContext.keys().map(requireContext)
requireAll(req)
// 将ICON存储到localhost
localStorage.setItem('IconList', JSON.stringify(requireAll(req).map(item => item.default.id.split('icon-')[1])))
