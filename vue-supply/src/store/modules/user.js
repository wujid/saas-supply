import { loginForPwd, logout, getInfo } from '@/api/system'
import { getToken, setToken, removeToken, setTenantCode } from '@/utils/auth'
import { resetRouter } from '@/router'
import variables from '@/styles/element-variables.scss'
import { Message } from 'element-ui'
import { getContentPage } from '@/api/message'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    userInfo: {},
    tenantInfo: {},
    menuList: [],
    menuMap: [],
    buttonList: [],
    msgInfo: {},
    theme: variables.theme // 主题色
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USERINFO: (state, userInfo = {}) => {
    state.userInfo = userInfo
  },
  SET_TENANT_INFO: (state, tenantInfo = {}) => {
    state.tenantInfo = tenantInfo
  },
  SET_ROLE_LIST: (state, payload = {}) => {
    state.roleList = payload
  },
  SET_MENU_LIST(state, payload = []) {
    state.menuList = payload
  },
  SET_MENU_MAP(state, payload = []) {
    state.menuMap = payload
  },
  SET_BUTTON_LIST(state, payload = []) {
    state.buttonList = payload
  },
  SET_MSG_INFO(state, payload = {}) {
    state.msgInfo = payload
  },
  SET_THEME(state, v) {
    state.theme = v
  }
}

const createMenuMap = menu => {
  const list = []
  const flatData = (menu) => {
    menu.forEach(item => {
      if (item.path === null) {
        item.path = ''
      }
      list.push(item)
      if (item.childrenList && item.childrenList.length > 0) {
        flatData(item.childrenList)
      }
    })
  }
  flatData(menu)
  return list
}

const actions = {
  login({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      loginForPwd(userInfo).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        setTenantCode(data.tenantCode)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { data } = response

        if (!data) {
          return reject('验证失败,请重新登录!')
        }
        commit('SET_USERINFO', data.userResponse)
        commit('SET_TENANT_INFO', data.tenantResponse)
        const menu = data.menuResponseList || []
        if (menu.length === 0) {
          Message.error('该用户暂未分配权限，请联系管理员')
        }
        commit('SET_ROLE_LIST', data.roleResponseList || [])
        commit('SET_MENU_LIST', menu)
        commit('SET_MENU_MAP', createMenuMap(menu))
        commit('SET_BUTTON_LIST', data.buttonResponseList || [])
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        localStorage.clear()
        sessionStorage.clear()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  },

  // 获取未读消息信息
  async getMsgInfo({ commit }) {
    const params = {
      pageIndex: 1,
      pageSize: 5,
      notifyType: 1,
      businessStatus: 6
    }
    const { code, data, message } = await getContentPage(params)
    if (code === 200) {
      const messageInfo = {
        data: data.records,
        count: data.total
      }
      commit('SET_MSG_INFO', messageInfo)
    } else {
      return Promise.reject(message)
    }
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

