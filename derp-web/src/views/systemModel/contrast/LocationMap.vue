<template>
  <!-- 加价比例配置页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetSearchForm" @search="getList(1)">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item label="公司：" prop="merchantId">
              <el-select
                v-model="searchProps.merchantId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getMerchantSelectBean('merchantList')"
              >
                <el-option
                  v-for="item in selectOpt.merchantList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="原货号："
              prop="originalGoodsNo"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="searchProps.originalGoodsNo"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="库位类型："
              prop="type"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.type"
                placeholder="请选择"
                filterable
                clearable
                :data-list="getSelectList('invenlocaitonMapping_TypeList')"
              >
                <el-option
                  v-for="item in selectOpt.invenlocaitonMapping_TypeList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
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
          v-permission="'inventoryLocationMapping_add'"
          @click="showModal('add')"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          v-permission="'inventoryLocationMapping_del'"
          @click="deleteTableItem"
        >
          删除
        </el-button>
        <el-button
          type="primary"
          v-permission="'inventoryLocationMapping_export'"
          @click="exportList"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'inventoryLocationMapping_import'"
          @click="importFile"
        >
          导入
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      @selection-change="selectionChange"
      @change="getList"
      showSelection
    >
      <template slot="modifyDate" slot-scope="{ row }">
        <span>{{ row.modifyDate || row.createDate }}</span>
      </template>
      <template slot="modifyName" slot-scope="{ row }">
        <span>{{ row.modifyName || row.createName }}</span>
      </template>
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          style="padding-left: 6px"
          v-permission="'inventoryLocationMapping_modify'"
          @click="showModal('edit', row.id)"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-if="row.isorRappoint === '0'"
          v-permission="'inventoryLocationMapping_updateIsorRappoint'"
          @click="updateIsorRappoint(row.id)"
        >
          指定为唯一出库货号
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 新增/编辑弹窗 -->
    <EditLocationMap
      v-if="editLocationMap.visible.show"
      :isVisible.sync="editLocationMap.visible"
      :data.sync="editLocationMap.data"
      @comfirm="closeModal"
    ></EditLocationMap>
    <!-- 新增/编辑弹窗 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    listInventoryLocationMapping,
    exportInventoryLocationMappingUrl,
    importInventoryLocationMappingUrl,
    updateIsorRappoint,
    deleteInventoryLocationMapping
  } from '@a/contrast'
  export default {
    mixins: [commomMix],
    components: {
      EditLocationMap: () => import('./components/EditLocationMap')
    },
    data() {
      return {
        // 查询数据
        searchProps: {
          merchantId: '',
          originalGoodsNo: '',
          type: ''
        },
        // 表格列数据
        tableColumn: [
          {
            label: '公司',
            prop: 'merchantName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '原货号',
            prop: 'originalGoodsNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '商品名称',
            prop: 'goodsName',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '库位货号',
            prop: 'goodsNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '库位类型',
            prop: 'typeLabel',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '是否指定唯一',
            prop: 'isorRappointLabel',
            width: '120',
            align: 'center',
            hide: true
          },
          {
            label: '编辑日期',
            slotTo: 'modifyDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '编辑人',
            slotTo: 'modifyName',
            width: '120',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '140', align: 'left' }
        ],
        // 新增/编辑弹窗
        editLocationMap: {
          visible: { show: false },
          data: {}
        }
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listInventoryLocationMapping({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          })
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 导出
      exportList() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        if (this.tableChoseList.length) {
          this.$showToast('确定导出勾选数据？', async () => {
            const ids = this.tableChoseList.map((item) => item.id).toString()
            this.$exportFile(exportInventoryLocationMappingUrl, { ids })
          })
        } else {
          this.$exportFile(exportInventoryLocationMappingUrl, {
            ...this.searchProps
          })
        }
      },
      // 导入
      importFile() {
        this.linkTo(
          '/common/importfile?templateId=' +
            141 +
            '&url=' +
            importInventoryLocationMappingUrl
        )
      },
      // 删除表格项
      deleteTableItem() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        this.$showToast('确定删除数据？', async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          try {
            await deleteInventoryLocationMapping({ ids })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      // 指定为唯一出库货号
      updateIsorRappoint(id) {
        this.$showToast('确定确定指定唯一出库货号吗?', async () => {
          try {
            const {
              data: { status }
            } = await updateIsorRappoint({ id })
            if (status === '00') {
              this.$successMsg('修改成功')
              this.getList()
            }
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      showModal(type, id) {
        this.editLocationMap.data =
          type === 'edit' ? { title: '编辑', id } : { title: '新增' }
        this.editLocationMap.visible.show = true
      },
      closeModal() {
        this.editLocationMap.visible.show = false
        this.getList()
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList(1)
        })
      }
    }
  }
</script>
