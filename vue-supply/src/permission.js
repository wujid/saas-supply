import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

// 白名单,不需要权限
const whiteList = ['/login', '/404', '/403', '/dashboard', '/user-info']

// 不需要Token
const noNeedToken = ['/login', '/404', '/403']

const routeGuard = (to, from, next) => {
  if (whiteList.indexOf(to.path) > -1) {
    next()
    return
  }
  const hasAuth = store.state.user.menuMap.some(menu => menu.path && (menu.path === to.path || menu.path === to.meta?.parentPath))
  if (hasAuth) {
    next()
  } else {
    next({ path: '/403' })
  }
}

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      const hasGetUserInfo = store.state.user.userInfo.name
      if (hasGetUserInfo) {
        routeGuard(to, from, next)
      } else {
        try {
          // get user info
          await store.dispatch('user/getInfo')
          routeGuard(to, from, next)
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/

    if (noNeedToken.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
