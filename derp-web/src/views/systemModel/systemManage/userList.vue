<template>
  <!-- 用户列表页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索栏 start -->
    <JFX-search-panel
      @reset="resetForm"
      @search="getList(1)"
      :showOpenBtn="false"
    >
      <JFX-Form :model="ruleForm" ref="ruleForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="员工姓名："
              prop="name"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model.trim="ruleForm.name" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="用户名："
              prop="username"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model.trim="ruleForm.username" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="状态："
              prop="disable"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model.trim="ruleForm.disable"
                placeholder="请选择"
                filterable
                clearable
              >
                <el-option
                  v-for="item in [
                    { label: '启用', value: 0 },
                    { label: '禁用', value: 1 }
                  ]"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索栏 end -->
    <!-- 按钮组 start -->
    <el-row class="mr-t-20">
      <el-col :span="24">
        <el-button
          type="primary"
          :size="'small'"
          v-permission="'user_add'"
          @click="linkTo('/system/userlist/add', '用户新增')"
        >
          新增
        </el-button>
      </el-col>
    </el-row>
    <div class="mr-t-20"></div>
    <!-- 按钮组 end -->
    <!-- 表格start -->
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column prop="code" label="工号" align="center" width="110" />
      <el-table-column
        prop="username"
        label="登录账号"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column
        prop="name"
        label="员工姓名"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column prop="depotName" label="性别" align="center" width="90">
        <template slot-scope="{ row }">
          {{ row.sex === 'm' ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="disable" label="状态" align="center" width="90">
        <template slot-scope="{ row }">
          {{ row.disable == 0 ? '启用' : '禁用' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="accountType"
        label="账号类型"
        align="center"
        width="120"
      >
        <template slot-scope="{ row }">
          {{ row.accountType == 0 ? '后台管理员' : '普通账号' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="roles"
        label="角色"
        align="center"
        width="140"
        show-overflow-tooltip
      />
      <el-table-column
        prop="createDate"
        label="创建时间"
        align="center"
        width="150"
      />
      <el-table-column label="操作" align="center" fixed="right" width="160">
        <template slot-scope="scope">
          <el-button
            type="text"
            v-permission="'user_toEditPage'"
            @click="
              linkTo('/system/userlist/add?id=' + scope.row.id, '编辑用户')
            "
          >
            编辑
          </el-button>
          <el-button
            type="text"
            v-if="scope.row.disable === '0'"
            v-permission="'user_toEditPage'"
            @click="enable(scope.row.id, '1')"
          >
            禁用
          </el-button>
          <el-button
            type="text"
            v-if="scope.row.disable === '1'"
            v-permission="'user_toEditPage'"
            style="color: red"
            @click="enable(scope.row.id, '0')"
          >
            启用
          </el-button>
          <el-button
            type="text"
            v-permission="'user_assignRole'"
            @click="assignRole(scope.row)"
          >
            授予角色
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <userGrantrole
      v-if="grantVisible.show"
      :grantVisible="grantVisible"
      :userId="grantVisible.userId"
      :roleList="grantVisible.roleList"
      @close="
        () => {
          grantVisible.show = false
          getList()
        }
      "
    />
    <!-- 表格end -->
  </div>
</template>
<script>
  import { getUserList, enableUser } from '@a/systemManage/user/index'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    components: {
      userGrantrole: () => import('./components/userGrantrole.vue')
    },
    data() {
      return {
        ruleForm: {
          name: '',
          username: '',
          disable: ''
        },
        grantVisible: {
          show: false,
          userId: '',
          roleList: ''
        }
      }
    },
    activated() {
      this.getList()
    },
    mounted() {
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        try {
          this.tableData.loading = true
          const opt = {
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.ruleForm
          }
          const res = await getUserList(opt)
          this.tableData = {
            list: res.data.list,
            loading: false,
            pageNum: pageNum || res.data.pageNo,
            total: res.data.total,
            pageSize: res.data.pageSize
          }
        } catch (err) {
          this.tableData.loading = false
        }
      },
      // 重置搜索栏
      resetForm() {
        this.reset('ruleForm', () => {
          this.getList(1)
        })
      },
      // 禁用和启用
      enable(id, type) {
        this.$confirm('你确认要禁用/启用吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          await enableUser({ id, type })
          this.$successMsg('操作成功')
          this.getList()
        })
      },
      // 授予角色
      assignRole(row) {
        this.grantVisible.userId = row.id
        this.grantVisible.roleList = row.roleIds.split(',')
        this.grantVisible.show = true
      }
    }
  }
</script>
