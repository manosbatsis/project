import lodash from 'lodash'

import { getPessionList } from '@a/base/index'

// 获取app-config.json中的配置
const globalBaseApiUrl = {
  SYSTEM_BASE_API: process.env.VUE_APP_SYSTEM_BASE_API, // system 模块对应的API地址
  STORAGE_BASE_API: process.env.VUE_APP_STORAGE_BASE_API, // storage 模块对应的API地址
  ORDER_BASE_API: process.env.VUE_APP_ORDER_BASE_API, // order 模块对应的API地址
  INVENTTORY_BASE_API: process.env.VUE_APP_INVENTTORY_BASE_API, // inventory 模块对应的API地址
  REPORT_BASE_API: process.env.VUE_APP_REPORT_BASE_API, // report 模块对应的API地址
  APP_BASE_API: process.env.VUE_APP_BASE_API
}
window.globalBaseApiUrl = globalBaseApiUrl

// 获取系统各模块请求的地址
export function getBaseUrl(url) {
  // return url
  if (url.includes('http')) {
    return url
  } else if (url.includes('/system/')) {
    return globalBaseApiUrl.SYSTEM_BASE_API // system 模块对应的API地址
  } else if (url.includes('/storage/')) {
    return globalBaseApiUrl.STORAGE_BASE_API // storage 模块对应的API地址
  } else if (url.includes('/order/') || url.includes('/transfer/')) {
    return globalBaseApiUrl.ORDER_BASE_API // order 模块对应的API地址
  } else if (url.includes('/inventory/')) {
    // inventory 库存模块对应的API地址
    return globalBaseApiUrl.INVENTTORY_BASE_API
  } else if (url.includes('/report/')) {
    // report 报表模块对应的API地址
    return globalBaseApiUrl.REPORT_BASE_API
  } else {
    return globalBaseApiUrl.APP_BASE_API
  }
}
// 获取url带的参数
export function getParam(name, str) {
  const reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
  const tstr = str || window.location.href
  const tmp = tstr.split('?')
  if (tmp.length <= 1) return null
  const r = tmp[tmp.length - 1].match(reg)
  if (r != null) {
    return unescape(r[2])
  } else {
    return null
  }
}
// 函数节流 不管事件被调用多少次，总是按规定时间间隔执行
export function throttle(fn, gapTime = 1000) {
  let _lastTime = null
  return function () {
    const _nowTime = +new Date()
    if (_nowTime - _lastTime > gapTime || !_lastTime) {
      fn.apply(this, arguments)
      _lastTime = _nowTime
    }
  }
}
// 函数防抖 事件被调用后，在执行之前无论被调用多少次都会从头开始计时
export function debounce(fn, delay = 500) {
  let timer
  // 返回一个函数，这个函数会在一个时间区间结束后的 delay 毫秒时执行 func 函数
  return function () {
    const context = this
    const args = arguments
    clearTimeout(timer)
    timer = setTimeout(function () {
      fn.apply(context, args)
    }, delay)
  }
}

/**
 * @description 转换数组对象某个key对应的值成以某个字符拼接的字符串
 * @param {Array} arr 数组对象
 * @param {String} key 键值
 * @param {String} symbol 默认','
 * @returns {string} '1,2,3,4'
 */
export function transformArrTosy(arr, key, symbol = ',') {
  if (!arr || !key) return ''
  const strArr = []
  arr.forEach((item) => item[key] && strArr.push(item[key]))
  return lodash.join(strArr, symbol) || ''
}

/**
 *  data 需要传入的去除null值的对象或者值
 *  defaultStr 将null值转为该字符串, 不传默认为 空字符串 ''
 */
export function removeNull(data, defaultStr = '') {
  // 普通数据类型
  if (typeof data !== 'object' || data === null) {
    if (data === null || data === 'null') {
      return defaultStr
    } else {
      return data
    }
  }
  // 引用数据类型
  for (const v in data) {
    if (data[v] === null || data[v] === 'null') {
      data[v] = defaultStr
    }
    if (typeof data[v] === 'object') {
      removeNull(data[v])
    }
  }
}

/**
 * 将人民币小写金额转换为大写
 */
export function convertCurrency(money) {
  // 汉字的数字
  var cnNums = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖']
  // 基本单位
  var cnIntRadice = ['', '拾', '佰', '仟']
  // 对应整数部分扩展单位
  var cnIntUnits = ['', '万', '亿', '兆']
  // 对应小数部分单位
  var cnDecUnits = ['角', '分', '毫', '厘']
  // 整数金额时后面跟的字符
  var cnInteger = '整'
  // 整型完以后的单位
  var cnIntLast = '元'
  // 最大处理的数字
  var maxNum = 999999999999999.9999
  // 金额整数部分
  var integerNum
  // 金额小数部分
  var decimalNum
  // 输出的中文金额字符串
  var chineseStr = ''
  // 分离金额后用的数组，预定义
  var parts
  if (money === '') {
    return ''
  }
  money = parseFloat(money)
  if (money >= maxNum) {
    // 超出最大处理数字
    return 'N/A'
  }
  if (money === 0) {
    chineseStr = cnNums[0] + cnIntLast + cnInteger
    return chineseStr
  }
  // 转换为字符串
  money = money.toString()
  if (money.indexOf('.') === -1) {
    integerNum = money
    decimalNum = ''
  } else {
    parts = money.split('.')
    integerNum = parts[0]
    decimalNum = parts[1].substr(0, 4)
  }
  // 获取整型部分转换
  if (parseInt(integerNum, 10) > 0) {
    var zeroCount = 0
    var IntLen = integerNum.length
    for (let i = 0; i < IntLen; i++) {
      var n = integerNum.substr(i, 1)
      var p = IntLen - i - 1
      var q = p / 4
      var m = p % 4
      if (n === '0') {
        zeroCount++
      } else {
        if (zeroCount > 0) {
          chineseStr += cnNums[0]
        }
        // 归零
        zeroCount = 0
        chineseStr += cnNums[parseInt(n)] + cnIntRadice[m]
      }
      if (m === 0 && zeroCount < 4) {
        chineseStr += cnIntUnits[q]
      }
    }
    chineseStr += cnIntLast
  }
  // 小数部分
  if (decimalNum !== '') {
    var decLen = decimalNum.length
    for (let i = 0; i < decLen; i++) {
      const n = decimalNum.substr(i, 1)
      if (n !== '0') {
        chineseStr += cnNums[Number(n)] + cnDecUnits[i]
      }
    }
  }
  if (chineseStr === '') {
    chineseStr += cnNums[0] + cnIntLast + cnInteger
  } else if (decimalNum === '') {
    chineseStr += cnInteger
  }
  return chineseStr
}
/**
 * 数字格式化 千位数格式
 * @param {Number | String} num 数字
 * @param {String} [separator] 分隔符号，可选参数，默认 英文逗号（,）。
 */
export function numberFormat(num, separator) {
  if (num === null || num === undefined) num = '0'
  if (!num) num = '0'
  num = num.toString()
  separator = separator || ','
  return num.replace(/\B(?=(\d{3})+(?!\d))/g, separator)
}

/**
 * 当前时间的前几天、前几周、前几个月、前几年的时间
 * @param {String} type 类型
 * @param {number} number 前后时间
 * @param {String} [separator] 分隔符号，可选参数，默认-
 */
export function getDate(type, number = 0, separator = '-') {
  var nowdate = new Date()
  let retrundate = ''
  switch (type) {
    case 'day': {
      // 取number天前、后的时间
      nowdate.setTime(nowdate.getTime() + 24 * 3600 * 1000 * number)
      const y = nowdate.getFullYear()
      const m = nowdate.getMonth() + 1
      const d = nowdate.getDate()
      retrundate =
        y +
        separator +
        (m.toString().length >= 2 ? m : '0' + m) +
        separator +
        (d.toString().length >= 2 ? d : '0' + d)
      break
    }
    case 'week': {
      // 取number周前、后的时间
      const weekdate = new Date(nowdate + 7 * 24 * 3600 * 1000 * number)
      const y = weekdate.getFullYear()
      const m = weekdate.getMonth() + 1
      const d = weekdate.getDate()
      retrundate =
        y +
        separator +
        (m.toString().length >= 2 ? m : '0' + m) +
        separator +
        (d.toString().length >= 2 ? d : '0' + d)
      break
    }
    case 'month': {
      // 取number月前、后的时间
      nowdate.setMonth(nowdate.getMonth() + number)
      const y = nowdate.getFullYear()
      const m = nowdate.getMonth() + 1
      const d = nowdate.getDate()
      retrundate =
        y +
        separator +
        (m.toString().length >= 2 ? m : '0' + m) +
        separator +
        (d.toString().length >= 2 ? d : '0' + d)
      break
    }
    case 'year': {
      // 取number年前、后的时间
      nowdate.setFullYear(nowdate.getFullYear() + number)
      const y = nowdate.getFullYear()
      const m = nowdate.getMonth() + 1
      const d = nowdate.getDate()
      retrundate =
        y +
        separator +
        (m.toString().length >= 2 ? m : '0' + m) +
        separator +
        (d.toString().length >= 2 ? d : '0' + d)
      break
    }
    default: {
      // 取当前时间
      const y = nowdate.getFullYear()
      const m = nowdate.getMonth() + 1
      const d = nowdate.getDate()
      retrundate =
        y +
        separator +
        (m.toString().length >= 2 ? m : '0' + m) +
        separator +
        (d.toString().length >= 2 ? d : '0' + d)
    }
  }
  return retrundate
}

// 随机生成十六进制颜色
export function randomHexColor() {
  return (
    '#' + ('00000' + ((Math.random() * 0x1000000) << 0).toString(16)).substr(-6)
  )
}

// 等待
export function sleep(time) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve()
    }, time)
  })
}

// 树形转换为数组
export function untieTree(tree, children_key = 'children') {
  if (
    !tree ||
    Object.prototype.toString.call(tree) !== '[object Array]' ||
    tree.length <= 0
  ) {
    return []
  }
  return tree.reduce(
    (pre, cur) => pre.concat(cur, untieTree(cur[children_key], children_key)),
    []
  )
}

// 数组转为树
export function toTree(list, parId = null) {
  const obj = {}
  const result = []
  // 将数组中数据转为键值对结构 (这里的数组和obj会相互引用)
  list.map((el) => {
    obj[el.id] = el
  })
  for (let i = 0, len = list.length; i < len; i++) {
    const id = list[i].parentId
    if (id === parId) {
      result.push(list[i])
      continue
    }
    if (obj[id].children) {
      obj[id].children.push(list[i])
    } else {
      obj[id].children = [list[i]]
    }
  }
  return result
}

// 创建a标签后点击
export function createATagClick(href) {
  var a = document.createElement('a')
  a.setAttribute('href', href)
  document.body.appendChild(a)
  a.click()
}

// 登出
export function logout() {
  const token = sessionStorage.getItem('token')
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('btnList')
  sessionStorage.removeItem('treeMenuList')
  sessionStorage.removeItem('companys')
  sessionStorage.removeItem('userInfo')
  sessionStorage.removeItem('merchantId')
  let href =
    getBaseUrl('/webapi/system/login/logout') + '/logout' + '?token=' + token
  // 本地开发跳转到 /login, 同时打开新窗口退出单点登录
  if (process.env.NODE_ENV === 'development') {
    window.open(href)
    href = '/login'
  }
  createATagClick(href)
}

// 进入系统时，获取用户信息以及菜单
export async function getUserAndPermission(store) {
  // 已加载不再重新请求
  if (store.getters.userInfo.isLoad) {
    return Promise.resolve()
  }
  const token = sessionStorage.getItem('token')
  try {
    const res = await getPessionList(token)
    const {
      treeMenuList = [],
      btnList,
      merchantList,
      merchantId,
      merchantName,
      name,
      userType,
      userId,
      username,
      systemName
    } = res.data
    const userInfo = {
      isLoad: true,
      merchantId,
      merchantName,
      userType,
      userId,
      username,
      name,
      systemName
    }
    sessionStorage.setItem('treeMenuList', JSON.stringify(treeMenuList)) // 保存用户菜单权限列表
    sessionStorage.setItem('btnList', JSON.stringify(btnList)) // 保存用户按钮权限列表
    sessionStorage.setItem('companys', JSON.stringify(merchantList)) // 保存公司列表
    sessionStorage.setItem('merchantId', JSON.stringify(merchantId)) // 保存当前id
    sessionStorage.setItem('userInfo', JSON.stringify(userInfo))
    store.dispatch('user/AC_SET_MENU_LIST', treeMenuList)
    store.dispatch('user/AC_SET_USER_INFO', userInfo)
    return Promise.resolve()
  } catch (err) {
    sessionStorage.removeItem('treeMenuList')
    sessionStorage.removeItem('btnList')
  }
}
