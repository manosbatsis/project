<template>
  <!-- 领料单新增/编辑页面 -->
  <div class="page-bx bgc-w edit-bx">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 表单部分 -->
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <!-- 发布公告 -->
      <JFX-title title="发布公告" className="mr-t-15" />
      <el-row :gutter="10">
        <el-col :span="24">
          <el-form-item label="公告类型：" prop="type">
            <el-select
              v-model="ruleForm.type"
              placeholder="请选择"
              clearable
              filterable
              style="width: 400px"
              :data-list="getSelectList('notice_typelist')"
            >
              <el-option
                v-for="item in selectOpt.notice_typelist"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="公告标题：" prop="title">
            <el-input
              v-model="ruleForm.title"
              placeholder="请输入"
              clearable
              style="width: 400px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <Tinymce v-model="ruleForm.contentBody" :height="400" />
        </el-col>
      </el-row>
      <!-- 发布公告 end -->
    </JFX-Form>
    <!-- 表单部分 end -->
    <!-- 底部按钮 -->
    <div class="flex-c-c mr-t-30">
      <el-button
        type="primary"
        :loading="actionBtnLoading"
        @click="handleSumbit"
      >
        保存
      </el-button>
      <el-button @click="closeTag">取消</el-button>
    </div>
    <!-- 底部按钮 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import { editPageNotice, saveOrModify } from '@a/noticeManage'
  export default {
    mixins: [commomMix],
    components: {
      Tinymce: () => import('@c/tinymce')
    },
    data() {
      return {
        // 表单数据
        ruleForm: {
          type: '',
          title: '',
          contentBody: ''
        },
        // 表单校验规则
        rules: {
          title: { required: true, message: '请输入公告标题', trigger: 'blur' },
          type: { required: true, message: '请选择公告类型', trigger: 'change' }
        },
        actionBtnLoading: false
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        try {
          this.actionBtnLoading = true
          const {
            data: { type, title, contentBody }
          } = await editPageNotice({ id })
          this.ruleForm.type = type || ''
          this.ruleForm.title = title || ''
          this.ruleForm.contentBody = contentBody || ''
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.actionBtnLoading = false
        }
      },
      // 保存
      handleSumbit() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            const { id } = this.$route.query
            try {
              this.actionBtnLoading = true
              await saveOrModify({ ...this.ruleForm, id })
              this.$successMsg('操作成功')
              this.closeTag()
            } catch (error) {
              this.$errorMsg(error.message)
            } finally {
              this.actionBtnLoading = false
            }
          } else {
            this.$errorMsg('请先正确填写表单信息！')
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep.edit-bx .el-form-item__label {
    text-align: right;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 40px;
    padding: 0 4px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 130px;
  }
  ::v-deep.edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
</style>
