<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @search="getList(1)" @reset="resetSearchForm">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label=" 使用平台："
              prop="platformType"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.platformType"
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
            <el-form-item
              label="用户名："
              prop="loginName"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.loginName"
                clearable
                placeholder="请输入"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label=" 店铺名称："
              prop="shopId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.shopId"
                placeholder="请选择"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.shopList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20 mr-b-20">
      <el-col :span="24">
        <el-button
          type="primary"
          v-permission="'reptile_toAddPage'"
          @click="linkTo(`/contrast/reptileAdd`)"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          v-permission="'reptile_delReptile'"
          @click="removeItem"
        >
          删除
        </el-button>
        <!-- <el-button type="primary" >批量导入</el-button>
        <el-button type="primary" >批量导出</el-button> -->
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          v-permission="'reptile_toEditPage'"
          @click="linkTo(`/contrast/reptileAdd?id=${row.id}`, '编辑爬虫配置')"
          style="padding-left: 6px"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-permission="'reptile_toDetailsPage'"
          @click="linkTo(`/contrast/reptileDetail?id=${row.id}`)"
          style="padding-left: 6px"
        >
          详情
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { getShopList, listReptile, delReptile } from '@a/contrast'
  import { auditApi } from '@a/interfaceManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        shopList: [],
        // 查询参数
        searchProps: {
          platformType: '',
          loginName: '',
          shopId: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '使用平台',
            prop: 'platformName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '用户名',
            prop: 'loginName',
            minWidth: '120',
            align: 'center',
            hide: false
          },
          {
            label: '关联公司',
            prop: 'merchantName',
            minWidth: '120',
            align: 'center',
            hide: false
          },
          {
            label: '关联客户',
            prop: 'customerName',
            minWidth: '90',
            align: 'center',
            hide: true
          },
          {
            label: '出仓库',
            prop: 'outDepotName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '入仓库',
            prop: 'inDepotName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '店铺名称',
            prop: 'shopName',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '爬取账单时点',
            prop: 'timePoint',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '创建时间',
            prop: 'createDate',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            width: '120',
            align: 'left',
            fixed: 'right'
          }
        ]
      }
    },
    activated() {
      this.getList()
    },
    mounted() {
      this.getShopList()
      this.getList()
    },
    methods: {
      // 获取表格数据 src\views\systemModel\\configList.vue
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const {
            data: { list, total }
          } = await listReptile({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = list
          this.tableData.total = total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 删除数据项
      removeItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
        } else {
          this.$showToast('确定删除选中对象？', async () => {
            await delReptile({
              ids: this.tableChoseList.map((item) => item.id).toString()
            })
            this.$successMsg('操作成功')
            this.getList()
          })
        }
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      },
      // 启用/禁用
      auditItem(id, status) {
        this.$showToast('确定启用/禁用选中对象？', async () => {
          await auditApi({ id, status })
          this.$successMsg('操作成功')
          this.getList()
        })
      },
      // 获取店铺列表
      async getShopList() {
        const { data } = await getShopList()
        this.$set(this.selectOpt, 'shopList', data)
        // this.selectOpt.shopList = data
      }
    }
  }
</script>
