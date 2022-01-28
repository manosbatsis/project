<template>
  <div class="page-bx bgc-w edit-bx">
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <!-- 面包屑 -->
      <JFX-Breadcrumb :breadcrumb="breadcrumb" :showGoback="true" />
      <!-- 面包屑 end -->
      <div class="mr-t-20"></div>
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="上级权限：" prop="parentId">
            <el-cascader
              v-model="ruleForm.parentId"
              style="width: 120%"
              :options="permissionTreeOption"
              :props="{ checkStrictly: true, emitPath: false }"
              clearable
              expandTrigger="hover"
              filterable
            ></el-cascader>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="权限名称：" prop="authorityName">
            <el-input
              v-model.trim="ruleForm.authorityName"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="权限类型：" prop="type">
            <el-select
              v-model="ruleForm.type"
              style="width: 120%"
              placeholder="请选择"
              filterable
            >
              <el-option
                v-for="item in [
                  { label: '菜单', value: '1' },
                  { label: '按钮', value: '2' }
                ]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="权限URL：" prop="url">
            <el-input
              v-model.trim="ruleForm.url"
              clearable
              style="width: 120%"
              placeholder="请输入url"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="子服务URL：" prop="serverAddr">
            <el-input
              v-model.trim="ruleForm.serverAddr"
              clearable
              style="width: 120%"
              placeholder="请输入子服务URL"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="模块：" prop="module">
            <el-select
              v-model="ruleForm.module"
              style="width: 120%"
              placeholder="请选择"
              clearable
              filterable
            >
              <el-option
                v-for="item in [
                  { label: '主服务-main', value: '1' },
                  { label: '业务-order', value: '2' },
                  { label: '仓储-storage', value: '3' },
                  { label: '库存-inventory', value: '4' },
                  { label: '报表-report', value: '5' }
                ]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="授权码：" prop="permission">
            <el-input
              v-model.trim="ruleForm.permission"
              clearable
              style="width: 120%"
              placeholder="请输入授权码"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="序号：" prop="seq">
            <el-input
              v-model.trim="ruleForm.seq"
              clearable
              style="width: 120%"
              placeholder="请输入序号"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="备注：" prop="remark">
            <el-input
              v-model.trim="ruleForm.remark"
              clearable
              style="width: 120%"
              placeholder="请输入备注"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
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
    getTree,
    add,
    getDetail,
    modify
  } from '@a/systemManage/permission/index'
  export default {
    name: 'addPermission',
    mixins: [commomMix],
    data() {
      return {
        breadcrumb: [
          {
            path: '',
            meta: { title: '系统管理' }
          },
          {
            path: '',
            meta: { title: '新增权限' }
          }
        ],
        ruleForm: {
          authorityName: '',
          parentId: '',
          type: '1',
          url: '',
          serverAddr: '',
          module: '',
          permission: '',
          seq: '',
          remark: ''
        },
        rules: {
          authorityName: {
            required: true,
            message: '请输入权限名称',
            trigger: 'blur'
          },
          parentId: {
            required: true,
            message: '请选择上级权限',
            trigger: 'change'
          },
          module: { required: true, message: '请选择模块', trigger: 'change' },
          permission: {
            validator: (rule, value, callback) => {
              const isButton = Number(this.ruleForm.type) === 2
              if (isButton && !value) {
                // eslint-disable-next-line standard/no-callback-literal
                callback('权限类型为按钮时必填')
                return
              }
              callback()
            },
            trigger: 'blur'
          }
        },
        permissionTreeOption: [],
        saveLoading: false
      }
    },
    async created() {
      await this.getTree()
      const { id, parentId } = this.$route.query
      if (id) {
        this.getDetail(id)
      }
      if (parentId) {
        this.ruleForm.parentId = parentId
      }
    },
    methods: {
      // 渲染权限树
      async getTree() {
        const { data } = await getTree()
        function formatTree(arr) {
          return arr.map((item) => {
            let children = item.children
            if (children && children.length) {
              children = children.filter((item) => item.id)
              children = formatTree(children)
            } else {
              children = null
            }
            return {
              value: String(item.id),
              label: item.name,
              children
            }
          })
        }
        this.permissionTreeOption = formatTree(data)
        return Promise.resolve()
      },
      async getDetail(id) {
        const { data } = await getDetail({ id })
        for (const key in this.ruleForm) {
          this.ruleForm[key] = data[key] ? String(data[key]) : ''
        }
      },
      // 保存
      save() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) return false
          const { id } = this.$route.query
          this.saveLoading = true
          const submitData = {
            ...this.ruleForm
          }
          try {
            if (id) {
              submitData.id = id
              await modify(submitData)
            } else {
              await add(submitData)
            }
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
  }
</style>
