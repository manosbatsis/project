<template>
  <!-- 客户新增/编辑页面 -->
  <div class="page-bx edit-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10">
      <el-button type="primary" @click="inputKey">
        点击生成appKey和密钥
      </el-button>
    </JFX-title>
    <JFX-Form :model="baseForm" ref="baseForm" :rules="rules">
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="使用对象：" prop="platformName">
            <el-input
              v-model="baseForm.platformName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="选择公司：" prop="merchantId">
            <el-select
              v-model="baseForm.merchantId"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectMerchantBean('merchant_list')"
            >
              <el-option
                v-for="item in selectOpt.merchant_list"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="备注：" prop="remark">
            <el-input
              v-model="baseForm.remark"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="app_key：" prop="appKey">
            <el-input
              v-model="baseForm.appKey"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="秘钥：" prop="appSecret">
            <el-input
              v-model="baseForm.appSecret"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <div class="mr-t-30 flex-c-c">
      <el-button @click="handleSubmit" type="primary">保 存</el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    getAppKeyAndAppSecret,
    saveApiExternal,
    etoDetailsPage,
    modifyApiExternal
  } from '@a/interfaceManage/index'
  export default {
    mixins: [commomMix],
    components: {},
    data() {
      return {
        id: '',
        // 基本信息
        baseForm: {
          platformName: '',
          merchantId: '',
          remark: '',
          appKey: '',
          appSecret: ''
        },
        // 表单规则
        rules: {
          platformName: {
            required: true,
            message: '请输入使用对象',
            trigger: 'change'
          },
          merchantId: {
            required: true,
            message: '请选择公司',
            trigger: 'change'
          },
          appKey: {
            required: true,
            message: '请输入appkey',
            trigger: 'change'
          },
          appSecret: {
            required: true,
            message: '请输入秘钥',
            trigger: 'change'
          }
        }
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const { id } = this.$route.query
        id && this.editInit(id)
      },
      // 编辑页面初始化
      async editInit(id) {
        this.id = id
        const { data } = await etoDetailsPage({ id })
        for (const key in this.baseForm) {
          this.baseForm[key] = data[key] ? String(data[key]) : ''
        }
      },
      async inputKey() {
        const { data: inputRes } = await getAppKeyAndAppSecret()
        console.log('key', inputRes)
        this.baseForm.appKey = inputRes.appKey
        this.baseForm.appSecret = inputRes.appSecret
      },
      // 保存或者修改传参
      getParams() {
        const result = {
          ...this.baseForm
        }
        if (this.id) {
          result.id = this.id
        }
        return result
      },
      // 提交表单
      handleSubmit() {
        this.$refs.baseForm.validate(async (valid) => {
          if (!valid) {
            return this.$warningMsg('请先填写必填字段')
          }
          const params = this.getParams()
          console.log(params)
          this.id
            ? await modifyApiExternal(params)
            : await saveApiExternal(params)
          this.$successMsg('保存成功')
          this.closeTag()
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
    padding: 0 12px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 130px;
  }
  ::v-deep.edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }

  ::v-deep.textarea-container {
    display: flex;
    align-items: flex-start;
    .el-form-item__label {
      width: 130px;
    }
    .el-form-item__content {
      width: 700px;
    }
  }
</style>
