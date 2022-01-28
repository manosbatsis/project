<template>
  <el-button
    :type="type"
    size="mini"
    :icon="icon"
    @click="back"
    id="jfx_goback_btn"
  >
    {{ btnText }}
  </el-button>
</template>
<script>
  export default {
    props: {
      btnText: {
        type: String,
        default: '返 回'
      },
      icon: {
        type: String,
        default: 'el-icon-arrow-left'
      },
      type: {
        type: String,
        default: 'primary'
      },
      backPathUrl: {
        type: String,
        default: ''
      }
    },
    methods: {
      back() {
        this.$emit('beforeGoback')
        const routeParentUrl = this.$route.meta.routeParentUrl
        const formFullPath = this.$route.meta.formFullPath
        const curRoute = this.$route
        const go = (url) => this.$router.replace(url)
        // 有定义固定返回
        if (this.backPathUrl) {
          this.$store.dispatch('tags/AC_DEL_TAG', curRoute)
          go(this.backPathUrl)
        } else if (formFullPath) {
          /** 跳转到来源页 */
          this.$store.dispatch('tags/AC_DEL_TAG', curRoute)
          go(formFullPath)
        } else if (routeParentUrl) {
          /** 是否关联到列表页，跳转到列表页 */
          this.$store.dispatch('tags/AC_DEL_TAG', curRoute)
          go(routeParentUrl)
        } else {
          /** 关闭tag */
          const activePath = this.$route.fullPath
          const dom = document.getElementById('closeTag_' + activePath)
          dom.click()
        }
      }
    }
  }
</script>
