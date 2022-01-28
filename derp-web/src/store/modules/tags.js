const state = {
  tagsList: [] // tag list
}
/** 判断fullPath是否相等 */
const isEqualFullPath = function (fullPath1, fullPath2) {
  return decodeURIComponent(fullPath1) === decodeURIComponent(fullPath2)
}
const mutations = {
  /** 新增tag */
  ADD_TAG(state, data) {
    /** 判断现tags中是存在 */
    const isExist = state.tagsList.find(({ fullPath }) =>
      isEqualFullPath(fullPath, data.fullPath)
    )
    !isExist && state.tagsList.push(data)
  },
  /** 删除tag */
  DEL_TAG(state, tag) {
    /** 清理keep-alive 缓存 */
    try {
      const cache = window.VUE_APP.$children[0].$children[0].$children.find(
        (item) => item.$el.className.includes('AppContainer')
      ).$vnode.parent.componentInstance.cache
      cache[tag.fullPath].componentInstance.$destroy()
      delete cache[tag.fullPath]
      delete cache[decodeURIComponent(tag.fullPath)]
    } catch (error) {
      console.log('清理keep-alive 缓存 error', error)
    }
    /** 清理tag */
    state.tagsList.splice(
      state.tagsList.findIndex(({ fullPath }) =>
        isEqualFullPath(fullPath, tag.fullPath)
      ),
      1
    )
  },
  /** 更新滚动条位置 */
  UPDATE_SCROLL(state, { fullPath, scrollTop }) {
    /**
     * data: {fullPath,scrollTop}
     */
    state.tagsList.some((tag) => {
      /** fullPath找到tag */
      if (isEqualFullPath(tag.fullPath, fullPath)) {
        if (tag.meta) {
          tag.meta.scrollTop = scrollTop
        } else {
          tag.meta = { scrollTop }
        }
        return true
      }
    })
  },
  /** 更新tagName 和 跳转过来的路径 */
  UPDATE_TAG(state, { changeTagFullPath, formFullPath, tagName }) {
    /**
     * data {
     *  changeTagFullPath
     *  formFullPath
     *  tagName
     * }
     */
    state.tagsList.some((tag) => {
      if (isEqualFullPath(tag.fullPath, changeTagFullPath)) {
        if (tag.meta) {
          tag.meta.formFullPath = formFullPath
          tagName && (tag.meta.title = tagName)
        } else {
          tag.meta = { formFullPath, title: tagName }
        }
        return true
      }
    })
  }
}

const actions = {
  AC_ADD_TAG({ commit }, payload) {
    commit('ADD_TAG', payload)
  },
  AC_DEL_TAG({ commit }, payload) {
    commit('DEL_TAG', payload)
  },
  AC_UPDATE_SCROLL({ commit }, payload) {
    commit('UPDATE_SCROLL', payload)
  },
  AC_UPDATE_TAG({ commit }, payload) {
    commit('UPDATE_TAG', payload)
  }
}

export default {
  namespaced: true, // 命名空间 this.$store.commit('XXX/SETXXX',sth); this.$store.getters['XXX/getXXX'];
  state,
  mutations,
  actions
}
