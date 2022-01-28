import { untieTree } from '@u/tool'
const state = {
  userInfo: {}, // 用户信息
  menuList: {}, // 菜单信息
  menuUrlMap: new Map()
}

const mutations = {
  SET_USER_INFO: (state, data) => {
    state.userInfo = data
  },
  SET_MENU_LIST: (state, data) => {
    state.menuList = data
  },
  SET_MENU_MAP: (state, data) => {
    state.menuUrlMap = data
  }
}

const actions = {
  AC_SET_USER_INFO({ commit }, data) {
    commit('SET_USER_INFO', data)
  },
  AC_SET_MENU_LIST({ commit }, data) {
    // 树转数组
    const untieTreeList = untieTree(data)
    // 数组转map
    const urlTreeMap = new Map()
    untieTreeList.map((item) => {
      const url = item.url
      if (url) {
        urlTreeMap.set(url, item)
      }
    })
    commit('SET_MENU_LIST', data)
    commit('SET_MENU_MAP', urlTreeMap)
  }
}

export default {
  namespaced: true, // 命名空间 this.$store.commit('XXX/SETXXX',sth); this.$store.getters['XXX/getXXX'];
  state,
  mutations,
  actions
}
