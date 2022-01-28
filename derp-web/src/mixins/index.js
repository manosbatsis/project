import funcsObj from './defineFunc'

export default {
  data() {
    return {
      // table 信息
      tableData: {
        list: [],
        loading: false,
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      tableChoseList: [], // table 选中的数据
      visible: { show: false }, // dialog 是否显示
      selectOpt: {},
      btnsWidth: 60,
      resetBtnsWidth: true
    }
  },
  watch: {
    'tableData.loading': function () {
      if (this.tableData.loading) this.btnsWidth = 55
    }
  },
  methods: {
    ...funcsObj,
    // 当用户手动勾选数据行的 Checkbox 时触发的事件
    selectRow(rows, row) {
      console.log('当用户手动勾选数据行的 Checkbox 时触发的事件', rows, row)
    },
    // 当选择项发生变化时会触发该事件
    selectionChange(rows) {
      console.log('当选择项发生变化时会触发该事件', rows)
      this.tableChoseList = rows
    },
    // 当用户手动勾选全选 Checkbox 时触发的事件
    selectAll(rows) {
      console.log('当用户手动勾选全选 Checkbox 时触发的事件', rows)
    },
    /**
     * 页面跳转，自动记录跳转之前的地址
     * 1.用于列表跳转到详情，新增，编辑等
     * 2.存在列表跳到列表情况，记录的地址用不上，功能无影响
     * @param {*} data
     * @param {*} tagName
     */
    linkTo(data, tagName) {
      const pathData = typeof data === 'string' ? { path: data } : { ...data }
      // 自定义tagName
      const currentFullPath = this.$route.fullPath
      this.$router.replace(pathData, async () => {
        await this.$nextTick()
        /** 跳转后更新tagName 记录跳转之前的地址 */
        this.$store.dispatch('tags/AC_UPDATE_TAG', {
          changeTagFullPath: pathData.path,
          formFullPath: currentFullPath,
          tagName: tagName || ''
        })
      })
    },
    /**
     * 关闭tag，消除keep-alive缓存
     * 用于新增编辑详情的 取消按钮 操作
     */
    closeTag() {
      /** 记录了需要返回的地址 */
      if (this.$route.meta.formFullPath) {
        this.$store.dispatch('tags/AC_DEL_TAG', this.$route)
        this.$router.replace(this.$route.meta.formFullPath)
      } else {
        /** 没有记录，直接关闭tag，触发tag.vue操作 */
        const activePath = this.$route.fullPath
        const dom = document.getElementById('closeTag_' + activePath)
        dom.click()
      }
    },
    /**
     * 关闭当前tag，打开一个新tab，
     * 用于下一步等情况需要切换路由的情况，后续补充更新操作
     * data string 路由地址
     * data {path:路由地址}
     */
    closeTagAndJump(data) {
      const nextData = typeof data === 'string' ? { path: data } : { ...data }
      this.$store.dispatch('tags/AC_DEL_TAG', this.$route)
      this.$router.replace(nextData)
    },
    // 重置
    reset(formName = 'ruleForm', callback) {
      this.$refs[formName].resetFields()
      if (callback) {
        this.$nextTick(() => {
          callback()
        })
      }
    },
    // 计算宽度
    countWidth(num) {
      if (!num) return false
      if (num < this.btnsWidth) return false
      this.btnsWidth = num + ''
    },
    // 从缓存中获取用户的主要信息
    getUserInfo() {
      let userInfo = sessionStorage.getItem('userInfo')
      userInfo = userInfo ? JSON.parse(userInfo) : {}
      return userInfo
    },
    /**
     * tab显示 是否有权限
     * @param {*} name 权限名称
     * @returns {Boolean}
     * 结合 v-if 使用
     */
    permissionShowTab(name = '') {
      const btnList = sessionStorage.getItem('btnList')
        ? JSON.parse(sessionStorage.getItem('btnList'))
        : []
      return btnList.includes(name)
    }
  }
}
