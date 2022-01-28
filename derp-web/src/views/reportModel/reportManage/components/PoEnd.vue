<template>
  <!-- PO完结组件 -->
  <div class="page-bx bgc-w" style="padding-top: 0">
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="reset('searchForm', getList)"
      @search="getList(1)"
      :showOpenBtn="false"
      style="margin-top: 0"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="PO号："
              prop="po"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.po"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="完结状态："
              prop="status"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.status"
                placeholder="请选择"
                clearable
                :data-list="getSelectList('vipPoBillVerification_statusList')"
              >
                <el-option
                  v-for="item in selectOpt.vipPoBillVerification_statusList"
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
          v-permission="'vipPoBillVerification_exportMain'"
          @click="exportMain"
        >
          汇总表导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'vipPoBillVerification_exportDatails'"
          @click="exportDetails"
        >
          明细表导出
        </el-button>
        <el-button
          type="primary"
          v-permission="'vipPoBillVerification_flashVipPoBillVeris'"
          @click="refreshList"
        >
          刷新报表
        </el-button>
        <span class="tips fs-12" v-if="labelTime"
          >生成时间：{{ labelTime }}</span
        >
      </el-col>
    </el-row>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
      class="mr-t-20"
    >
      <el-table-column type="index" align="center" width="55" />
      <el-table-column
        prop="merchantName"
        align="center"
        min-width="80"
        label="公司"
      />
      <el-table-column prop="po" align="center" min-width="120" label="PO号" />
      <el-table-column
        prop="poDate"
        align="center"
        min-width="150"
        label="PO时间"
      />
      <el-table-column
        prop="statusLabel"
        align="center"
        min-width="80"
        label="完结状态"
      />
      <el-table-column
        prop="overDate"
        align="center"
        min-width="150"
        label="完结时间"
      />
      <el-table-column
        prop="operator"
        align="center"
        min-width="80"
        label="操作人"
      />
      <el-table-column align="center" width="100" label="操作" fixed="right">
        <template slot-scope="{ row }">
          <el-button
            v-if="row.status != '1'"
            v-permission="'vipPoBillVerification_changeStatus'"
            type="text"
            @click="changeStatus(row)"
            >完结</el-button
          >
          <el-button
            v-permission="'vipPoBillVerification_endDetail'"
            type="text"
            @click="
              linkTo(
                `/report/poverifidetail?poNo=${row.po}&commbarcode=${row.commbarcode}&depotId=${row.depotId}`
              )
            "
            >详情</el-button
          >
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end-->
  </div>
</template>

<script>
  import {
    listVipPoBillVeriPoList,
    exportMainUrl,
    exportDetails,
    flashVipPoBillVeris,
    countUnsettledAccount,
    changeStatus
  } from '@a/reportManage/index'
  import dayjs from 'dayjs'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          po: '',
          status: ''
        },
        // 表格数据
        tableData: {
          list: [
            {
              merchantId: 1000031,
              merchantName: '健太',
              pageNo: 1,
              pageSize: 10,
              po: '2011090822',
              poDate: '2020-09-10 19:56:18',
              status: '0',
              statusLabel: '未完结'
            }
          ],
          loading: false,
          pageNum: 1,
          pageSize: 10,
          total: 11
        }
      }
    },
    computed: {
      // 计算生成时间，对比数据中的modifyDate，获取最大的
      labelTime() {
        let timeArr = this.tableData.list.filter((item) => !!item.modifyDate)
        // 为空退出
        if (!timeArr.length) {
          return ''
        }
        // 转换
        timeArr = timeArr.map((item) => new Date(item.modifyDate).getTime())
        // 取最大值
        const result = Math.max(...timeArr)
        if (!result) {
          return
        }
        return dayjs(result).format('YYYY-MM-DD HH:mm:ss')
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
          const params = { ...this.searchProps }
          const {
            data: { list, total }
          } = await listVipPoBillVeriPoList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...params
          })
          this.tableData.list = list
          this.tableData.total = total
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 汇总表导出
      exportMain() {
        this.$showToast('确定导出选中对象？', () => {
          this.$exportFile(exportMainUrl, { ...this.searchProps })
        })
      },
      // 明细表导出
      async exportDetails() {
        // this.$exportFile(exportDetailsUrl, { ...this.searchProps })
        try {
          const { data } = await exportDetails({ ...this.searchProps })
          if (data.code === '00') {
            this.$alertSuccess('导出成功，请在报表任务列表查看进度')
          }
        } catch (err) {
          console.log(err)
        }
      },
      // 刷新
      refreshList() {
        const { po } = this.searchProps
        let params = {}
        let tips = '确定刷新?'
        if (po) {
          params = { po }
          tips = '确定刷新该PO号?'
        }
        this.$showToast(tips, async () => {
          try {
            const { data } = await flashVipPoBillVeris(params)
            if (data.code === '00') {
              this.$successMsg('正在刷新，稍后点击【查询】，查询结果')
            }
          } catch (err) {
            console.log(err)
          }
        })
      },
      // 完结
      async changeStatus(row) {
        try {
          this.tableData.loading = true
          const { data: countData } = await countUnsettledAccount({
            po: row.po
          })
          this.$showToast(
            'PO存在商品未结算量为' + countData + '，确认是否完结?',
            async () => {
              try {
                await changeStatus({
                  po: row.po
                })
                this.$successMsg('正在刷新，稍后点击【查询】，查询结果')
                this.getList()
              } catch (err) {
                console.log(err)
              }
            }
          )
          console.log(countData)
        } catch (error) {
          console.log(error)
        } finally {
          this.tableData.loading = false
        }
      }
    }
  }
</script>
