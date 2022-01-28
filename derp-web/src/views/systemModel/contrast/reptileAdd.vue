<template>
  <!-- 客户新增/编辑页面 -->
  <div class="page-bx edit-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" />
    <!-- 面包屑 end -->
    <!-- 基本信息 -->
    <JFX-title title="基本信息" className="mr-t-10" />
    <JFX-Form :model="baseForm" ref="baseForm" :rules="rules">
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="使用平台：" prop="platformType">
            <el-select
              v-model="baseForm.platformType"
              placeholder="请选择"
              filterable
              clearable
              :data-list="getSelectList('crawler_typeList')"
            >
              <el-option
                v-for="item in selectOpt.crawler_typeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="用户名：" prop="loginName">
            <el-input
              v-model="baseForm.loginName"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="爬取账单时间点(天)：" prop="timePoint">
            <JFX-Input
              v-model="baseForm.timePoint"
              :min="0"
              :max="10000"
              :precision="1"
              placeholder="请输入"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="公司：" prop="merchantId">
            <el-select
              v-model="baseForm.merchantId"
              placeholder="请选择"
              clearable
              filterable
              :data-list="getSelectMerchantBean('merchant_list')"
              @change="changeMerchant"
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
          <el-form-item label="客户：" prop="customerId">
            <el-select
              v-model="baseForm.customerId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in selectOpt.customerList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="店铺名称：" prop="shopId">
            <el-select
              v-model="baseForm.shopId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in selectOpt.shopList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="出仓库：" prop="outDepotId">
            <el-select
              v-model="baseForm.outDepotId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in selectOpt.outDepotIdList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <el-form-item label="入仓库：" prop="inDepotId">
            <el-select
              v-model="baseForm.inDepotId"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in selectOpt.inDepotIdList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
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
    queryReptile,
    getShopList,
    saveReptile,
    toEditPage,
    modifyReptile
  } from '@a/contrast'
  export default {
    mixins: [commomMix],
    components: {},
    data() {
      return {
        id: '',
        // 基本信息
        baseForm: {
          platformType: '',
          loginName: '',
          timePoint: '',
          merchantId: '',
          customerId: '',
          shopId: '',
          outDepotId: '',
          inDepotId: ''
        },
        // 表单规则
        rules: {
          platformType: {
            required: true,
            message: '请选择使用平台',
            trigger: 'change'
          },
          loginName: {
            required: true,
            message: '请输入用户名',
            trigger: 'change'
          },
          merchantId: {
            required: true,
            message: '请选择公司',
            trigger: 'change'
          },
          customerId: {
            required: true,
            message: '请选择客户',
            trigger: 'change'
          },
          outDepotId: {
            required: true,
            message: '请选择出仓库',
            trigger: 'change'
          },
          inDepotId: {
            required: true,
            message: '请选择入仓库',
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
        const { data } = await toEditPage({ id })
        for (const key in this.baseForm) {
          this.baseForm[key] = data[key] ? String(data[key]) : ''
        }
        this.changeMerchant(true)
      },
      // 改变公司
      async changeMerchant(isInit = false) {
        // 重新渲染仓库
        const { merchantId } = this.baseForm
        if (!isInit) {
          this.baseForm.outDepotId = ''
          this.baseForm.inDepotId = ''
          this.baseForm.shopId = ''
          this.baseForm.customerId = ''
        }
        delete this.selectOpt.outDepotIdList
        delete this.selectOpt.inDepotIdList
        this.getSelectBeanByMerchantRel('outDepotIdList', { merchantId })
        this.getSelectBeanByMerchantRel('inDepotIdList', { merchantId })
        // 重新渲染店铺
        const { data: shopData } = await getShopList({ merchantId })
        this.$set(this.selectOpt, 'shopList', shopData)
        console.log('店铺数据', shopData)
        // 重新渲染客户
        const { data: cusData } = await queryReptile({ merchantId })
        this.$set(this.selectOpt, 'customerList', cusData)
        console.log('客户数据', cusData)
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
          this.id ? await modifyReptile(params) : await saveReptile(params)
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
    width: 170px;
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
