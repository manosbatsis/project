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
              label="权限名称："
              prop="authorityName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="ruleForm.authorityName"
                clearable
              ></el-input>
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
          v-permission="'permission_add'"
          @click="linkTo('/system/permission/addPermisson')"
        >
          新增
        </el-button>
        <el-button type="primary" @click="isShowTree = !isShowTree"
          >切换展示形式</el-button
        >
      </el-col>
    </el-row>
    <div class="mr-t-20"></div>
    <!-- 按钮组 end -->
    <!-- 普通列表 表格start -->
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
      v-show="!isShowTree"
    >
      <el-table-column prop="id" label="权限编码" align="center" width="90" />
      <el-table-column prop="authorityName" label="权限名称" align="center" />
      <el-table-column prop="isEnabled" label="状态" align="center" width="70">
        <template slot-scope="{ row }">
          {{ row.is_enabled == 0 ? '禁用' : '启用' }}
        </template>
      </el-table-column>
      <el-table-column prop="type" label="权限类型" align="center" width="70">
        <template slot-scope="{ row }">
          {{ row.type == 1 ? '菜单' : '按钮' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="permission"
        label="授权码"
        align="center"
        width="130"
      />
      <el-table-column prop="url" label="权限URL" align="center" width="150" />
      <el-table-column
        label="子服务URL"
        prop="serverAddr"
        align="center"
        width="120"
      ></el-table-column>
      <el-table-column prop="remark" label="备注" align="center" width="100" />
      <el-table-column
        prop="createDate"
        label="创建时间"
        align="center"
        width="150"
      />
      <el-table-column
        label="操作"
        v-permission="'permission_del'"
        align="center"
        width="130"
        fixed="right"
      >
        <template slot-scope="scope">
          <el-button
            type="text"
            v-permission="'permission_update'"
            @click="update(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            type="text"
            v-permission="'permission_del'"
            @click="del(scope.row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 普通列表 表格end -->
    <!-- S 树形列表 -->
    <JFX-table
      :tableData.sync="tableTreeData"
      v-show="isShowTree"
      row-key="id"
      lazy
      :load="loadTreeChildren"
    >
      <el-table-column prop="authorityName" label="权限名称" align="left" />
      <el-table-column
        prop="id"
        label="权限编码"
        align="center"
        minWidth="90"
      />
      <el-table-column
        prop="url"
        label="权限URL"
        align="center"
        minwidth="200"
      />
      <el-table-column
        prop="type"
        label="权限类型"
        align="center"
        minwidth="70"
      >
        <template slot-scope="{ row }">
          {{ row.type == 1 ? '菜单' : '按钮' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="seq"
        label="排序"
        width="80"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="permission"
        label="授权码"
        align="center"
        width="130"
      />
      <el-table-column label="操作" align="center" width="130" fixed="right">
        <template slot-scope="{ row }">
          <el-button
            type="text"
            v-permission="'permission_add'"
            v-if="Number(row.type) === 1"
            @click="
              linkTo('/system/permission/addPermisson?parentId=' + row.id)
            "
            >新增</el-button
          >
          <el-button
            type="text"
            v-permission="'permission_update'"
            @click="update(row)"
          >
            编辑
          </el-button>
          <el-button
            type="text"
            v-permission="'permission_del'"
            @click="del(row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- E 树形列表 -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { getList, del } from '@a/systemManage/permission/index'
  import { toTree } from '@u/tool'
  export default {
    mixins: [commomMix],
    data() {
      return {
        ruleForm: {
          authorityName: ''
        },
        isShowTree: false,
        tableTreeData: {
          loading: false
        }
      }
    },
    activated() {
      console.log('activated')
      this.getList()
      this.getTreeList()
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
        } catch (err) {
          this.tableData.loading = false
        }
      },
      async getTreeList() {
        try {
          this.tableTreeData.loading = true
          const {
            data: { list }
          } = await getList({
            begin: 0,
            pageSize: 99999
          })
          // 列表转化为树
          const treeList = toTree(list)
          this.tableTreeData = {
            list: this.returnTreeList(treeList),
            loading: false
          }
        } catch (err) {
          console.log(err)
          this.tableTreeData.loading = false
        }
      },
      returnTreeList(treeList) {
        return (
          treeList
            ?.map((item) => {
              item.hasChildren = !!item?.children?.length
              item.copyChildren = item.children
              item.children = []
              return item
            })
            ?.sort((a, b) => a.seq - b.seq) || []
        )
      },
      loadTreeChildren(row, _, resolve) {
        resolve(this.returnTreeList(row.copyChildren))
      },
      // 重置搜索栏
      resetForm() {
        this.reset('ruleForm', () => {
          this.getList(1)
        })
      },
      update(row) {
        const id = row.id
        this.linkTo('/system/permission/addPermisson?id=' + id, '编辑权限')
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
      }
    }
  }
</script>
