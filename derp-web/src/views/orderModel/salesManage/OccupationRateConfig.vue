<template>
  <!-- 资金占用计算表页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel @reset="resetSearchForm" @search="getList(1)">
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="事业部："
              prop="buId"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.buId"
                placeholder="请选择"
                clearable
                filterable
                :data-list="getBUSelectBean('businessList')"
              >
                <el-option
                  v-for="item in selectOpt.businessList"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
            <el-form-item
              label="生效时间："
              prop="time"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.time"
                :clearable="true"
                :format="'yyyy-MM-dd hh:mm:ss'"
                :value-format="'yyyy-MM-dd hh:mm:ss'"
                type="datetimerange"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <!-- 操作 -->
    <el-row class="mr-t-20">
      <el-col :span="24">
        <el-button
          type="primary"
          @click="handleClickAdd"
          v-permission="'OccupationRateConfigAdd'"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          @click="handleClickDel"
          v-permission="'OccupationRateConfigDel'"
        >
          删除
        </el-button>
      </el-col>
    </el-row>
    <el-row class="mr-t-20"></el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template #action="{ row }">
        <el-button
          type="text"
          v-permission="'OccupationRateConfigEdit'"
          @click="handleClickEdit(row)"
          >编辑</el-button
        >
      </template>
    </JFX-table>
    <!-- 表格 end-->

    <!-- 新增/编辑弹窗 -->
    <EditOccupationRateConfig
      v-if="editOccupationRateConfig.visible.show"
      :isVisible.sync="editOccupationRateConfig.visible"
      :data="editOccupationRateConfig.data"
      @comfirm="getList"
    />
    <!-- 新增/编辑弹窗 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    ListOccupationRateConfig,
    delOccuptionRateCongfig
  } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    components: {
      EditOccupationRateConfig: () =>
        import('./components/EditOccupationRateConfig.vue')
    },
    data() {
      return {
        // 查询数据
        searchProps: {
          buId: '',
          time: []
        },
        // 表格列数据
        tableColumn: [
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '资金占用费率（%）',
            prop: 'occupationRate',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '生效时间',
            prop: 'effectiveDate',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '失效时间',
            prop: 'expirationDate',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '操作',
            slotTo: 'action',
            align: 'center'
          }
        ],
        /* 新增/编辑弹窗组件状态 */
        editOccupationRateConfig: {
          visible: { show: false },
          data: {}
        }
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      // 获取表格数据
      async getList(pageNum) {
        const effectiveStartDate = this.searchProps.time[0] || ''
        const effectiveEndDate = this.searchProps.time[1] || ''
        // 订单日期
        this.searchProps.effectiveStartDate = effectiveStartDate
        this.searchProps.effectiveEndDate = effectiveEndDate
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const param = {
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.searchProps
          }
          delete param.time
          const { data } = await ListOccupationRateConfig(param)
          this.tableData.list = data.list
          this.tableData.total = data.total
        } catch (err) {}
        this.tableData.loading = false
      },
      // 新增
      handleClickAdd() {
        this.editOccupationRateConfig.visible.show = true
        this.editOccupationRateConfig.data = {}
      },
      //  编辑
      handleClickEdit(row) {
        this.editOccupationRateConfig.visible.show = true
        this.editOccupationRateConfig.data = row
      },
      // 删除
      handleClickDel() {
        if (this.tableChoseList.length < 1) {
          this.$errorMsg('至少选择一条记录！')
          return false
        }
        this.$confirm('是否确定删除?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const ids = this.tableChoseList.map((item) => item.id).toString()
          await delOccuptionRateCongfig({ ids })
          this.$successMsg('操作成功')
          this.getList()
        })
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.date = []
          this.getList(1)
        })
      }
    }
  }
</script>
