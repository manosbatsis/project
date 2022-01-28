<template>
  <JFX-Dialog
    :visible.sync="isVisible"
    :showClose="true"
    :width="'860px'"
    :top="'3vh'"
    title="导入商品"
    closeBtnText="取 消"
    confirmBtnText="确 认"
    @comfirm="comfirm"
  >
    <ImportFile
      :url="url"
      :filterData="data"
      :templateId="templateId"
      :downText="downText"
      @successUpload="successUpload"
    />
  </JFX-Dialog>
</template>

<script>
  export default {
    props: {
      downText: {
        type: String,
        default: '下载模板'
      },
      templateId: {
        type: String,
        default: ''
      },
      data: {
        type: Object,
        default: () => {}
      },
      url: {
        type: String,
        default: ''
      },
      isVisible: {
        type: Object,
        default: () => ({
          show: false
        })
      }
    },
    components: {
      /* 导入组件 */
      ImportFile: () => import('@/components/importfile/index')
    },
    methods: {
      comfirm() {
        this.$emit('update:isVisible', { show: false })
        this.$emit('comfirm')
      },
      successUpload({ data }) {
        if (data.failure + '' !== '0') {
          this.$errorMsg('导入失败')
          return false
        }
        this.$emit('update:isVisible', { show: false })
        console.log(data.data)
        if (!data?.data?.length) {
          this.$errorMsg('返回数据为空')
          return false
        }
        this.$emit('successUpload', data.data || {})
      }
    }
  }
</script>
