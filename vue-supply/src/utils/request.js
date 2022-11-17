import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import router from '@/router'

// create an axios instance
const service = axios.create({
  // baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 6000 * 1000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['Token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data

    if (res instanceof Blob) {
      if (response.status === 203) {
        return Promise.resolve(res)
      }
    }

    if (res.code !== 200) {
      Message({
        message: res.message || '异常',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error(res.message || '操作失败'))
    } else {
      return res
    }
  },
  async(error) => {
    console.log('err' + error) // for debug
    const message = error?.response?.data?.message
    Message({
      message: message || '操作失败',
      type: 'error',
      duration: 5 * 1000
    })
    if (error?.response?.data?.code === 401) {
      // token过期
      await store.dispatch('user/resetToken')
      router.replace('/login')
    }
    return Promise.reject(error)
  }
)

export default service
