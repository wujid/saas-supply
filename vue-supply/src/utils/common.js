/**
 * 根据子级类型查找所有匹配的父级类型
 * id: 子级ID
 * data: 匹配数据
 * prop: 匹配的类型,默认用ID匹配
 */
export function getFathersById(id, data, prop = 'id', children = 'children') {
  const arrRes = []
  if (!id) {
    return arrRes
  }
  const rev = (data, nodeId) => {
    for (let i = 0, length = data.length; i < length; i++) {
      const node = data[i]
      if (node[prop] === nodeId) {
        arrRes.unshift(node[prop])
        return true
      } else {
        if (node[children] && node[children].length) {
          if (rev(node[children], nodeId)) {
            arrRes.unshift(node[prop])
            return true
          }
        }
      }
    }
    return false
  }
  rev(data, id)
  return arrRes
}
