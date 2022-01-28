/**
 * @description 用户操作是具有权限
 * 使用示例
 * <bottom v-permission="sale_retail_admin"></bottom>
 */
import config from '@u/config'
// 校验权限
const checkPermission = function (el, binding) {
  if (!config.btnPermission) return ''
  const { value } = binding
  if (value) {
    const btnList = sessionStorage.getItem('btnList')
      ? JSON.parse(sessionStorage.getItem('btnList'))
      : []
    const isExist = btnList.includes(value) && el
    el.style.display = isExist ? '' : 'none'
  } else {
    throw new Error(`need roles! Like v-permission="sale_retail_admin", ${el}`)
  }
}
export default {
  inserted(el, binding, vnode) {
    checkPermission(el, binding)
  },
  update(el, binding) {
    checkPermission(el, binding)
  }
}
