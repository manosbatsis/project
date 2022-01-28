<template>
  <!-- 退货订单管理 -->
  <div>
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="reset('searchForm')"
      @search="getList(1)"
      style="margin-top: 0px"
    >
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
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="计划月份："
              prop="month"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.month"
                :clearable="false"
                :format="'yyyy-MM'"
                :value-format="'yyyy-MM'"
                type="month"
                placeholder="选择月份"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="归属年份："
              prop="year"
              class="search-panel-item fs-14 clr-II"
            >
              <el-date-picker
                v-model="searchProps.year"
                :clearable="false"
                :format="'yyyy'"
                :value-format="'yyyy'"
                type="year"
                placeholder="选择年份"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="计划维度："
              prop="type"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.type"
                clearable
                :data-list="getSelectList('sale_target_typeList')"
              >
                <el-option
                  v-for="item in selectOpt.sale_target_typeList"
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
          v-permission="'sale_target_export'"
          @click="exportList"
        >
          导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'sale_target_import'"
          @click="importList"
        >
          导入
        </el-button>
        <el-button type="primary" v-permission="'sale_target_del'" @click="del">
          删除
        </el-button>
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column type="selection" align="center" width="55" />
      <el-table-column prop="buName" align="center" label="事业部" />
      <el-table-column prop="month" align="center" label="销售计划月份" />
      <el-table-column prop="year" align="center" label="归属年份" />
      <el-table-column prop="typeLabel" align="center" label="计划维度" />
      <el-table-column prop="creator" align="center" label="创建人" />
      <el-table-column prop="createDate" align="center" label="创建时间" />
      <el-table-column align="center" label="操作" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" @click="detail(row)">详情</el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end -->
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    listSaleTarget,
    exportSaleTargetUrl,
    importSaleTargetUrl,
    delSaleTarget
  } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        searchProps: {
          buId: '',
          month: '',
          year: '',
          type: ''
        },
        // 表格数据
        tableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 0
        }
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      // 拉取表格数据
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const {
            data: { list, total }
          } = await listSaleTarget({
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
      // 导出
      exportList() {
        const params = this.tableChoseList
          .map(({ buId, month, type }) => `${buId}_${month}_${type}`)
          .join(',')
        this.$exportFile(exportSaleTargetUrl, { params })
      },
      // 导入
      importList() {
        this.linkTo(
          '/common/importfile?templateId=' + 137 + '&url=' + importSaleTargetUrl
        )
      },
      // 删除
      del() {
        if (!this.tableChoseList.length) {
          this.$alertWarning('至少选择一条记录！')
          return false
        }
        this.$showToast('确定删除数据？', async () => {
          const params = this.tableChoseList
            .map(({ buId, month, type }) => `${buId}_${month}_${type}`)
            .join(',')
          try {
            await delSaleTarget({ params })
            this.$successMsg('操作成功')
            this.getList()
          } catch (error) {
            this.$errorMsg(error.message)
          }
        })
      },
      detail({ month, buId, type }) {
        this.linkTo(
          `/sales/salestargetdetail?month=${month}&buId=${buId}&type=${type}`
        )
      }
    }
  }
</script>
