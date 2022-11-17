import Cookies from 'js-cookie'

const TokenKey = 'accessToken'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

const TenantKey = 'tenantCode'

export function getTenantCode() {
  return Cookies.get(TenantKey)
}

export function setTenantCode(tenantCode) {
  return Cookies.set(TenantKey, tenantCode, { expires: 2 })
}

export function removeTenantCode() {
  return Cookies.remove(TenantKey)
}

