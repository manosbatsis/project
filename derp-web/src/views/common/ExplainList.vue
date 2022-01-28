<template>
  <!-- 唯品账单校验页面 -->
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb showGoback />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel
      :showOpenBtn="false"
      @reset="resetSearchForm"
      @search="getList(1)"
    >
      <JFX-Form :model="searchProps" ref="searchForm">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="编码："
              prop="code"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.code"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="名称："
              prop="name"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model="searchProps.name"
                placeholder="请输入"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
      </JFX-Form>
    </JFX-search-panel>
    <!-- 搜索面板 end -->
    <div class="container">
      <div class="tabs">
        <div
          v-for="item in tabsList"
          :key="item.key"
          class="fs-12 clr-II tabs-item"
          :class="{ active: item.key === currentTabIdx }"
          @click="tabsChange(item.key)"
        >
          <div>{{ item.value }}</div>
        </div>
      </div>
      <!-- 表格 -->
      <JFX-table
        class="table"
        :tableData.sync="tableData"
        :tableColumn="tableColumn"
        :tableHeight="'500px'"
        showIndex
        @change="getList"
      >
      </JFX-table>
      <!-- 表格 end-->
    </div>
  </div>
</template>

<script>
  import { listExplain, listTemplateExplainCategory } from '@a/base'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        // 查询数据
        searchProps: {
          code: '',
          name: '',
          type: ''
        },
        // 表格列数据
        tableColumn: [],
        // 表格列结构map
        columnMap: {
          DEPOT: [
            { label: '仓库自编码', prop: 'code' },
            { label: '仓库名称', prop: 'name' }
          ],
          BUSINESS_UNIT: [
            { label: '事业部编码', prop: 'code' },
            { label: '事业部名称', prop: 'name' }
          ],
          COUNTRY_ORIGIN: [
            { label: '编码', prop: 'code' },
            { label: '名称', prop: 'name' }
          ],
          MERCHANDISE_CAT: [
            { label: '编码', prop: 'code' },
            { label: '二级类目', prop: 'name' }
          ],
          UNIT: [
            { label: '计量单位编码', prop: 'code' },
            { label: '计量单位', prop: 'name' }
          ],
          CUSTOMSAREA: [{ label: '平台关区', prop: 'name' }],
          BRAND: [{ label: '商品品牌', prop: 'name' }],
          PACKTYPE: [
            { label: '编码', prop: 'code' },
            { label: '包装方式名称', prop: 'name' }
          ]
        },
        tabsList: [], // tabs列表
        currentTabIdx: '' // 当前点击的tab
      }
    },
    mounted() {
      this.listTemplateExplainCategory()
    },
    methods: {
      // 获取表格数据
      async getList() {
        this.searchProps.type = this.currentTabIdx
        try {
          this.tableData.loading = true
          const { data } = await listExplain({
            ...this.searchProps
          })
          this.tableData.list = data || []
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 获取tabs列表
      async listTemplateExplainCategory() {
        try {
          const { data } = await listTemplateExplainCategory()
          if (data && data.length) {
            this.tabsList = data
            this.currentTabIdx = this.tabsList[0].key
            this.tableColumn =
              [
                ...this.columnMap[this.currentTabIdx].map((item) => ({
                  ...item,
                  minWidth: '140',
                  align: 'center',
                  hide: true
                }))
              ] || []
          }
          this.getList()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // tabs切换
      tabsChange(key) {
        this.currentTabIdx = key
        this.tableColumn =
          [
            ...this.columnMap[this.currentTabIdx].map((item) => ({
              ...item,
              minWidth: '140',
              align: 'center',
              hide: true
            }))
          ] || []
        this.getList()
      },
      // 重置搜索表单
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.getList()
        })
      }
    }
  }
</script>

<style scoped lang="scss">
  .container {
    margin-top: 20px;
    display: flex;
    .tabs {
      width: 180px;
      &-item {
        padding: 10px 0;
        border: 1px solid #999;
        cursor: pointer;
        margin-bottom: 4px;
        border-radius: 6px;
        text-align: center;
      }
      &-item.active {
        background: #d7d7d7;
      }
    }
    .table {
      margin-left: 20px;
      flex: 1;
    }
  }
</style>
