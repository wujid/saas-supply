import store from '@/store'
import ElementUI from 'element-ui'
import { nanoid } from 'nanoid'

function initWebSocket() {
  const api = window.webConfig.gateway.substring(7)
  const uuid = nanoid(5)
  const wsUrl = `ws://${api}/message/websocket/` + store.state.user.tenantInfo.id + '/' + uuid + '/' + store.state.user.userInfo.id
  this.socket = new WebSocket(wsUrl)// 这里面的this都指向vue
  this.socket.onopen = webSocketOnOpen
  this.socket.onerror = webSocketOnError
  this.socket.onmessage = webSocketOnMessage
  this.socket.onclose = closeWebsocket
}

function webSocketOnOpen() {
  console.log('WebSocket已连接...')
}

function webSocketOnError(e) {
  console.log('WebSocket连接发生错误...' + e)
}

function webSocketOnMessage(e) {
  console.log('webSocket回调：', e)
  store.dispatch('user/getMsgInfo')
  if (e.data) {
    const messageInfo = JSON.parse(e.data)
    if (messageInfo.isRefresh) {
      return
    }
    ElementUI.Notification({
      title: '提示',
      message: messageInfo.title,
      dangerouslyUseHTMLString: true,
      type: 'success',
      duration: 10000
    })
  }
}

function closeWebsocket() {
  console.log('Websocket已关闭...')
}

function close() {
  this.socket.close()
}

function webSocketSend(agentData) {
  this.socket.send(agentData)
}

function getSocket() {
  return this.socket
}

export default {
  initWebSocket, close, webSocketSend, getSocket
}
