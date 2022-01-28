<template>
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
      <JFX-Form :model="ruleForm" ref="ruleForm" @submit.native.prevent>
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="角色名称："
              prop="name"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input v-model.trim="ruleForm.name" clearable></el-input>
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
          v-permission="'role_add'"
          @click="addVisible.show = true"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          :size="'small'"
          v-permission="'role_copy'"
          @click="copy"
        >
          复制
        </el-button>
      </el-col>
    </el-row>
    <div class="mr-t-20"></div>
    <!-- 按钮组 end -->
    <!-- 表格start -->
    <JFX-table
      :tableData.sync="tableData"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column prop="id" label="角色id" align="center" width="90" />
      <el-table-column
        prop="name"
        label="角色名称"
        align="center"
        width="200"
      />
      <el-table-column prop="remark" label="角色备注" align="center" />
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button
            type="text"
            v-permission="'role_assignPermissions'"
            @click="linkTo('/system/roleAssign?roleId=' + scope.row.id)"
          >
            分配权限
          </el-button>
          <el-button
            type="text"
            style="color: red"
            v-permission="'role_del'"
            @click="del(scope.row.id)"
          >
            删除
          </el-button>
          <el-button
            type="text"
            v-permission="'role_grantUser'"
            @click="
              () => {
                editId = scope.row.id
                grantVisible.show = true
              }
            "
          >
            授予用户
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格end -->
    <!-- 新增角色 start -->
    <addRole
      v-if="addVisible.show"
      :addRoleVisible.sync="addVisible"
      :copyId="addVisible.copyId"
      @close="copyClose"
    />
    <!-- 新增角色 end -->
    <!-- 授予用户 start -->
    <roleGrantUser
      v-if="grantVisible.show"
      :visible.sync="grantVisible"
      :roleId="editId"
    />
    <!-- 授予用户 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { getList, del } from '@a/systemManage/role/index'
  export default {
    mixins: [commomMix],
    components: {
      addRole: () => import('./components/addRole'),
      roleGrantUser: () => import('./components/roleGrantUser')
    },
    data() {
      return {
        ruleForm: {
          name: ''
        },
        addVisible: {
          show: false,
          copyId: ''
        },
        assignVisible: {
          show: false
        },
        grantVisible: {
          show: false
        },
        editId: ''
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
          const res = await getList(opt)
          this.tableData = {
            list: res.data.list,
            loading: false,
            pageNum: pageNum || res.data.pageNo,
            total: res.data.total,
            pageSize: res.data.pageSize
          }
          return Promise.resolve()
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
      // 删除
      del(id) {
        this.$confirm('你确认要删除吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          await del({ id })
          this.$successMsg('操作成功')
          this.getList()
        })
      },
      // 复制
      copy() {
        if (this.tableChoseList.length !== 1) {
          this.$alertWarning(
            this.tableChoseList.length > 1
              ? '只能选择一条记录'
              : '至少选择一条记录！'
          )
          return false
        }
        this.addVisible.show = true
        this.addVisible.copyId = this.tableChoseList[0].id
      },
      async copyClose(copyId) {
        await this.getList()
        this.addVisible.show = false
        if (this.addVisible.copyId) {
          const id = this.addVisible.copyId
          this.addVisible.copyId = ''
          this.linkTo(`/system/roleAssign?roleId=${id}&copyId=${copyId}`)
        }
      }
    }
  }
</script>
