const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  tenantCode: state => state.user.tenantInfo.code,
  avatar: state => state.user.avatar,
  name: state => state.user.name
}
export default getters
