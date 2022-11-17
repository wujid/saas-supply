/* eslint-disable no-unused-vars */
import Vue from 'vue'
import store from '@/store/index'
import router from '@/router/index'

// 按钮权限
Vue.directive('auth', {
  inserted: function(el, binding) {
    const hasAuth = store.state.user.buttonList.some(item => item.code === binding.value)
    if (!hasAuth) {
      el.remove()
    }
  }
})
