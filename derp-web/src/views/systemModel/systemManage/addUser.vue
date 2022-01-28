<template>
  <div class="page-bx bgc-w edit-bx" v-loading="loadEditLoading">
    <JFX-Form
      :model="ruleForm"
      ref="ruleForm"
      :rules="rules"
      style="overflow: hidden"
    >
      <!-- 面包屑 -->
      <JFX-Breadcrumb
        :breadcrumb="editId ? breadcrumb : []"
        :showGoback="true"
      />
      <!-- 面包屑 end -->
      <JFX-title title="基础信息" className="mr-t-10" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="姓名：" prop="name">
            <el-input
              v-model.trim="ruleForm.name"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="工号：" prop="code">
            <el-input
              v-model.trim="ruleForm.code"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="登录账号：" prop="username">
            <el-input
              v-model.trim="ruleForm.username"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="性别：" prop="sex">
            <el-select
              v-model="ruleForm.sex"
              style="width: 120%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in [
                  { label: '男', value: 'm' },
                  { label: '女', value: 'f' }
                ]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="电话号码：" prop="tel">
            <el-input
              v-model.trim="ruleForm.tel"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="邮箱：" prop="email">
            <el-input
              v-model.trim="ruleForm.email"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="账号类型：" prop="accountType">
            <el-select
              v-model="ruleForm.accountType"
              style="width: 120%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in [
                  { label: '普通账号', value: '1' },
                  { label: '后台管理员', value: '0' }
                ]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 普通账户选择事业部和公司 -->
      <template v-if="ruleForm.accountType === '1'">
        <!-- 选择公司 start -->
        <JFX-title title="绑定公司" className="mr-t-10" />
        <el-col :span="24">
          <el-button type="primary" :size="'small'" @click="bindCompany">
            新增
          </el-button>
          <el-button type="primary" :size="'small'" @click="unbindCompany">
            删除
          </el-button>
        </el-col>
        <el-table
          ref="companyListTable"
          :data="bindCompanyList"
          style="width: 100%"
        >
          <el-table-column
            type="selection"
            align="center"
            width="55"
          ></el-table-column>
          <el-table-column prop="name" label="公司简称">
            <template slot-scope="{ row }">
              <!-- 已选择状态 -->
              <template v-if="row.id">
                {{ row.name }}
              </template>
              <!-- 未选择状态 -->
              <template v-else>
                <el-select
                  v-model="selectAddCompanyValue"
                  placeholder="请选择公司"
                  filterable
                  clearable
                >
                  <el-option
                    v-for="item in selectAddCompanyList"
                    :key="item.key"
                    :label="item.value"
                    :value="item.key"
                  ></el-option>
                </el-select>
              </template>
            </template>
          </el-table-column>
          <el-table-column prop="code" label="公司编号" />
        </el-table>
        <!-- 选择公司 end -->
        <!-- 选择事业部 start -->
        <JFX-title title="选择事业部" className="mr-t-10" />
        <el-col :span="24">
          <el-button type="primary" :size="'small'" @click="bindBuss">
            新增
          </el-button>
          <el-button type="primary" :size="'small'" @click="unbindBuss">
            删除
          </el-button>
        </el-col>
        <el-table ref="bussListTable" :data="bindBussList" style="width: 100%">
          <el-table-column
            type="selection"
            align="center"
            width="55"
          ></el-table-column>
          <el-table-column prop="id" label="事业部简称">
            <template slot-scope="{ row }">
              <!-- 已选择状态 -->
              <template v-if="row.id">{{ row.name }}</template>
              <!-- 未选择状态 -->
              <template v-else>
                <el-select
                  v-model="selectAddBussValue"
                  placeholder="请选择事业部"
                  filterable
                  clearable
                >
                  <el-option
                    v-for="item in selectAddBussList"
                    :key="item.key"
                    :label="item.value"
                    :value="item.key"
                  ></el-option>
                </el-select>
              </template>
            </template>
          </el-table-column>
          <el-table-column prop="code" label="事业部编号" />
        </el-table>
      </template>
    </JFX-Form>
    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" :loading="saveLoading" @click="save">
        保 存
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    getCompanyDetail,
    getBussDetail,
    saveUser,
    editUser,
    toEditPage
  } from '@a/systemManage/user/index'
  export default {
    name: 'addUser',
    mixins: [commomMix],
    data() {
      return {
        editId: '',
        ruleForm: {
          name: '',
          code: '',
          username: '',
          sex: '',
          tel: '',
          email: '',
          accountType: ''
        },
        rules: {
          name: { required: true, message: '请输入姓名', trigger: 'blur' },
          code: {
            required: true,
            message: '请输入工号',
            trigger: 'blur'
          },
          username: {
            required: true,
            trigger: 'blur',
            validator(_, value, callback) {
              if (!value) return callback(new Error('请输入登录账号'))
              if (!/^[a-zA-Z]+[^\u4E00-\u9FA5]*$/.test(value)) {
                return callback(
                  new Error('登录账号只能以字母开头，且不包含中文！')
                )
              }
              return callback()
            }
          },
          sex: {
            required: true,
            message: '请选择性别',
            trigger: 'change'
          },
          tel: {
            required: true,
            message: '请输入电话号码',
            trigger: 'blur'
          },
          email: { required: true, message: '请输入邮箱', trigger: 'blur' },
          accountType: {
            required: true,
            message: '请选择账号类型',
            trigger: 'change'
          }
        },
        bindCompanyList: [], // 绑定的公司列表
        bindBussList: [], // 绑定的事业部列表
        selectAddCompanyValue: '', // 已选择的绑定公司
        selectAddBussValue: '', // 已选择的绑定事业部
        loadEditLoading: true,
        saveLoading: false,
        breadcrumb: [
          { path: '', meta: { title: '系统管理' } },
          { path: '', meta: { title: '编辑用户' } }
        ]
      }
    },
    watch: {
      //   监听选择了新增公司
      async selectAddCompanyValue(newVal) {
        if (!newVal) {
          return
        }
        // 获取到公司详情
        const { data } = await getCompanyDetail({ id: newVal })
        //   修改最后一条记录
        const addData = {
          code: data.code,
          id: data.id,
          name: data.name
        }
        this.bindCompanyList.splice(-1, 1, addData)
        this.selectAddCompanyValue = ''
      },
      //   监听选择了新增事业部
      async selectAddBussValue(newVal) {
        if (!newVal) {
          return
        }
        const { data } = await getBussDetail({ id: newVal })
        const addData = {
          code: data.code,
          id: data.id,
          name: data.name
        }
        this.bindBussList.splice(-1, 1, addData)
        this.selectAddBussValue = ''
      }
    },
    computed: {
      // 根据已选择的公司列表得到未选择的公司列表
      selectAddCompanyList() {
        return this.selectOpt.allCompanyList.filter((item) => {
          const id = item.key
          return !this.bindCompanyList.find(
            (bItem) => Number(bItem.id) === Number(id)
          )
        })
      },
      //  根据已选择的事业部列表得到未选择的事业部列表
      selectAddBussList() {
        return this.selectOpt.allBussList.filter((item) => {
          const id = item.key
          return !this.bindBussList.find(
            (bItem) => Number(bItem.id) === Number(id)
          )
        })
      }
    },
    async mounted() {
      await Promise.all([
        this.getUserMerchantSelectBean('allCompanyList'),
        this.getAllSelectBean('allBussList')
      ])
      this.$route.query.id ? this.toEditPage() : (this.loadEditLoading = false)
    },
    methods: {
      // 获取各种下拉数据
      async toEditPage() {
        this.editId = this.$route.query.id
        const { data } = await toEditPage({ id: this.editId })
        //   基础属性
        Object.keys(this.ruleForm).forEach((key) => {
          this.ruleForm[key] = data.detail[key]
        })
        //   初始化选择公司
        if (data.merchantInfoModels.length) {
          this.bindCompanyList = data.merchantInfoModels.map((item) => {
            return {
              id: item.id,
              name: item.name,
              code: item.code
            }
          })
        }
        //   初始化选择事业部
        if (data.buRelList.length) {
          this.bindBussList = data.buRelList.map((item) => {
            return {
              id: item.buId,
              name: item.buName,
              code: item.buCode
            }
          })
        }
        this.$nextTick(() => {
          this.loadEditLoading = false
        })
      },
      //   绑定公司
      bindCompany() {
        const companyLength = this.bindCompanyList.length
        if (
          companyLength &&
          this.bindCompanyList[companyLength - 1].id === ''
        ) {
          return this.$warningMsg('请先处理好当前新增记录')
        }
        this.bindCompanyList.push({
          id: '',
          name: '',
          code: ''
        })
      },
      // 删除公司
      unbindCompany() {
        const selectList = this.$refs.companyListTable.selection
        if (!selectList.length) {
          return this.$warningMsg('请先选择一条记录')
        }
        this.bindCompanyList = this.bindCompanyList.filter((bItem) => {
          return !selectList.find(
            (sItem) => Number(sItem.id) === Number(bItem.id)
          )
        })
      },
      // 绑定事业部
      bindBuss() {
        const bussListLength = this.bindBussList.length
        if (bussListLength && this.bindBussList[bussListLength - 1].id === '') {
          return this.$warningMsg('请先处理好当前新增记录')
        }
        this.bindBussList.push({
          id: '',
          name: '',
          code: ''
        })
      },
      // 解绑事业部
      unbindBuss() {
        const selectList = this.$refs.bussListTable.selection
        if (!selectList.length) {
          return this.$warningMsg('请先选择一条记录')
        }
        this.bindBussList = this.bindBussList.filter((bItem) => {
          return !selectList.find(
            (sItem) => Number(sItem.id) === Number(bItem.id)
          )
        })
      },
      // 保存与修改用户
      save() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) return false
          // 普通账户选择事业部和公司
          if (this.ruleForm.accountType === '1') {
            if (!this.bindCompanyList.length) {
              return this.$warningMsg('请先选择绑定的公司')
            }
            if (!this.bindBussList.length) {
              return this.$warningMsg('请先选择绑定的事业部')
            }
          }
          this.saveLoading = true
          const submitData = {
            ...this.ruleForm,
            id: this.editId,
            merchantIds: this.bindCompanyList.map((item) => item.id).toString(),
            buIds: this.bindBussList.map((item) => item.id).toString()
          }
          try {
            // 编辑接口和新增接口不一致
            const saveApi = this.editId ? editUser : saveUser
            await saveApi(submitData)
            this.$successMsg('保存成功')
            this.closeTag()
          } catch (error) {
            console.log(error)
          }
          this.saveLoading = false
        })
      }
    }
  }
</script>
<style scoped lang="scss">
  ::v-deep.edit-bx {
    .el-form-item__label {
      width: 100px;
    }
    .el-form-item__error {
      white-space: nowrap;
    }
  }
</style>
