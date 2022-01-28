const getters = {
  isCollapse: (state) => state.base.isCollapse,
  tagsList: (state) => (state.tags.tagsList ? state.tags.tagsList || [] : []),
  tagBxWidth: (state) => state.tags.tagBxWidth,
  userInfo: (state) => state.user.userInfo,
  menuList: (state) => state.user.menuList,
  menuUrlMap: (state) => state.user.menuUrlMap
}
export default getters
