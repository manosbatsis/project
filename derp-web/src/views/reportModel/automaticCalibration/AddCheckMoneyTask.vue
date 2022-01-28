<template>
  <!-- 预售订单新增/编辑页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- title -->
    <JFX-title title="上传文件" className="mr-t-10 title-box">
      <div style="display: flex; margin-right: 30%">
        <div
          class="clr-act c-p fs-14"
          @click="downTemplate(144)"
          style="margin-right: 10px"
        >
          导入模板
        </div>
        <div class="clr-act c-p fs-14" @click="linkTo('/common/ExplainList')">
          查看模板说明
        </div>
      </div>
    </JFX-title>
    <!-- title end -->
    <!-- 面包屑 end -->
    <el-row :gutter="10">
      <el-col :span="20">
        <el-upload
          action=""
          drag
          :on-change="selectFile"
          :show-file-list="false"
          :auto-upload="false"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">
            将文件拖到此处，或
            <em>点击上传</em>
          </div>
          <div class="el-upload__tip clr-warn fs-16" slot="tip">
            填写Excle模板小贴士: 1.按照格式进行填写数据;
          </div>
          <div class="el-upload__tip clr-r fs-16" slot="tip">
            {{ tips }}
          </div>
        </el-upload>
        <span style="color: #797979">
          {{ fileName ? '导入文件：' + fileName : '' }}
        </span>
      </el-col>
    </el-row>
    <el-row class="flex-c-c">
      <el-button type="primary" @click="onSaveForm" :loading="saveBtnLoading">
        开始核对
      </el-button>
      <el-button @click="closeTag">取销</el-button>
    </el-row>
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { getBaseUrl } from '@u/tool'
  import { downTemplateUrl } from '@a/base'
  import { saveTaskCheckResult } from '@a/reportManage'
  export default {
    mixins: [commomMix],
    data() {
      return {
        file: null,
        fileName: '',
        tips: '',
        saveBtnLoading: false
      }
    },
    methods: {
      selectFile(file) {
        const { name, raw } = file
        this.fileName = name
        this.file = raw
      },
      // 下载模板
      downTemplate(templateId) {
        const url =
          getBaseUrl(downTemplateUrl) +
          downTemplateUrl +
          `?token=${sessionStorage.getItem('token')}&ids=${templateId}`
        window.open(url, '_blank')
      },
      // 保存
      async onSaveForm() {
        if (!this.file) {
          this.$errorMsg('请正上传文件')
          return false
        }
        try {
          this.saveBtnLoading = true
          const formData = new FormData()
          formData.append('taskType', '3')
          formData.append('file', this.file)
          const { data } = await saveTaskCheckResult(formData)
          this.$successMsg(`已创建任务:${data},请在列表查看进度`)
          this.closeTag()
        } catch (error) {
          this.tips = error.message || ''
          this.$errorMsg(error.message || '系统异常')
        } finally {
          this.saveBtnLoading = false
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep .el-upload {
    width: 100%;
    max-width: 600px;
  }
  ::v-deep .el-upload-dragger {
    width: 100%;
    height: 220px;
  }
  .title-box {
    display: flex;
    justify-content: space-between;
  }
</style>
