<template>
  <el-row class="jfx-table" :id="refName">
    <!-- 是否固定表头 -->
    <div
      class="hack-table-top flex-c-c c-p"
      v-if="tableData.total > tableData.pageSize && showFixedTop"
      :class="fixedTop ? 'clr-act' : 'clr-III'"
      :title="fixedTop ? '取消固定头部' : '固定表头'"
      @click="fixedTableTop"
    >
      <i class="iconfont icon-gudingbiaotou fs-12"></i>
    </div>
    <!-- <el-tooltip class="hack-table-top flex-c-c c-p" :class="fixedTop ? 'clr-act' : 'clr-III'" :content="fixedTop ? '取消固定头部?' : '固定表头?'" effect="light" placement="top" >
      <i class="iconfont icon-gudingbiaotou fs-12 "></i>
    </el-tooltip> -->
    <!-- 是否固定表头  end-->
    <el-col :span="24" class="com-table">
      <el-table
        :class="{
          'hide-cell-border': isHideCellBorder,
          'hide-header-border': isHideHeaderBorder
        }"
        ref="el-table"
        v-sticky="{
          open: isStickyOpen,
          top: 0,
          parent: '.page-right-route'
        }"
        :data="tableData.list"
        :stripe="stripe"
        :border="border"
        @select="handleSelect"
        @selection-change="handleSelectChange"
        @select-all="selectAll"
        @sort-change="sortChange"
        v-loading="tableData.loading"
        :max-height="height"
        :show-summary="!!getSummaryMethod || showSummary"
        :summary-method="getSummaryMethod"
        :header-cell-style="headerCellStyle"
        :span-method="spanMethod"
        :cell-style="cellStyle"
        :cell-class-name="cellClass"
        :row-class-name="rowClassName"
        :row-style="rowStyle"
        style="width: 100%"
        v-bind="$attrs"
      >
        <!-- 复选框 -->
        <el-table-column
          v-if="showSelection"
          type="selection"
          align="center"
          width="55"
        ></el-table-column>
        <!-- 序号 -->
        <el-table-column
          v-if="showIndex"
          type="index"
          align="center"
          label="序号"
          width="55"
        ></el-table-column>
        <!-- 传入了tableColumn -->
        <template v-if="tableColumn">
          <template v-for="(item, index) in getTableColumn">
            <el-table-column
              v-if="!item.slotTo"
              v-bind="item"
              :key="index"
              :sortable="item.sort"
              :show-overflow-tooltip="item.hide"
            >
              <template slot="header" v-if="item.need">
                <span class="need">{{ item.label }}</span>
              </template>
            </el-table-column>
            <el-table-column
              v-else
              v-bind="item"
              :key="index"
              :sortable="item.sort"
              :show-overflow-tooltip="item.hide"
            >
              <template slot="header" v-if="item.need">
                <span class="need">{{ item.label }}</span>
              </template>
              <template slot-scope="scope">
                <slot :name="item.slotTo" v-bind="scope"></slot>
              </template>
            </el-table-column>
          </template>
        </template>
        <!-- 正常情况 -->
        <template v-else>
          <slot></slot>
        </template>
      </el-table>
    </el-col>
    <el-col
      :span="24"
      class="pagination-bx mr-t-20"
      v-if="tableData.total > 0 && showPagination"
    >
      <div class="fs-12 clr-II">
        <span>每页</span>
        <el-select
          v-model="tableData.pageSize"
          style="width: 100px"
          @change="changePageSizes"
        >
          <el-option
            v-for="item in pageSizeOpt"
            :key="item"
            :label="item"
            :disabled="tableData.loading"
            :value="item"
          ></el-option>
        </el-select>
        <span>项</span>
        <span>
          当前显示第 {{ (tableData.pageNum - 1) * tableData.pageSize + 1 }} 项至
          {{ tableData.pageNum * tableData.pageSize }} 项, 共
          {{ tableData.total }} 项
        </span>
      </div>
      <div v-if="tableData.total > tableData.pageSize">
        <el-pagination
          background
          :page-size.sync="tableData.pageSize"
          :page-count="tableData.total"
          :total="tableData.total"
          :current-page="tableData.pageNum"
          layout="prev, pager, next, jumper"
          :disabled="tableData.loading"
          @current-change="currentChange"
          :pager-count="5"
        ></el-pagination>
      </div>
    </el-col>
  </el-row>
</template>
<script>
  /**
   * @description 通用表格 参照element-uitable
   * 部分参数说明
   * showPagination 是否启用分页
   * tableData Object 表格通用数据
   *  -- list 表格数据
   *  -- loading 加载loading
   *  -- pageNum 页码
   *  -- pageSize 条数
   *  -- total 总条数
   * select function 当用户手动勾选数据行的 Checkbox 时触发的事件
   * selection-change function 当选择项发生变化时会触发该事件
   * selection-all function 当用户手动勾选全选 Checkbox 时触发的事件
   * change function 表格pageNum/pageSize改变时触发
   * showFixedTop 是否显示固定表头icon
   * tableHeight table高度 设置tableHeight最高高度
   * showSummary 是否在footer 统计
   */
  export default {
    props: {
      // 表格通用数据
      tableData: {
        type: Object,
        default: function () {
          return {
            list: [],
            loading: false,
            pageNum: 1,
            pageSize: 10,
            total: 0
          }
        }
      },
      stripe: {
        type: Boolean,
        default: true
      },
      border: {
        type: Boolean,
        default: true
      },
      // 是否启用分页
      showPagination: {
        type: Boolean,
        default: true
      },
      showFixedTop: {
        type: Boolean,
        default: true
      },
      tableHeight: {
        type: String,
        default: ''
      },
      showSummary: {
        type: Boolean,
        default: false
      },
      summaryMethod: {
        type: Function | null,
        default: null
      },
      // 显示序号
      showIndex: {
        type: Boolean,
        default: false
      },
      // 显示复选框
      showSelection: {
        type: Boolean,
        default: false
      },
      // 表格列
      tableColumn: {
        type: Array,
        default: null
      },
      headerCellStyle: {
        type: Function,
        default: function () {}
      },
      spanMethod: {
        type: Function,
        default: function () {}
      },
      rowClassName: {
        type: Function,
        default: function () {}
      },
      rowStyle: {
        type: Function,
        default: function () {}
      },
      cellClass: {
        type: Function || String
      },
      cellStyle: {
        type: Function,
        default: function () {}
      },
      // 隐藏表头的边框
      isHideHeaderBorder: {
        type: Boolean,
        default: false
      },
      // 隐藏单元格的边框
      isHideCellBorder: {
        type: Boolean,
        default: false
      },
      sortChange: {
        type: Function,
        default: function () {}
      },
      isStickyOpen: {
        type: Boolean,
        default: false
      },
      /*
        需要合计的字段集合 格式{字段名：保留的位数}
        :summaryMap="{
          num: 0,
          amount: 2,
          taxAmount: 2,
          tax: 2
        }"
      */
      summaryMap: {
        type: Object,
        default: null
      }
    },
    data() {
      return {
        refName: 'tab' + Date.now(),
        pageSize: 10,
        pageSizeOpt: [10, 25, 50, 100],
        fixedTop: false,
        windowClientHeight: document.documentElement.clientHeight, // 浏览器可视高度
        height: 100000
      }
    },
    computed: {
      getTableColumn() {
        return this?.tableColumn?.map((item) => ({
          ...item,
          align: item.align || 'center',
          hide: !(item.slotTo && !item.hide)
        }))
      },
      getSummaryMethod() {
        if (this.summaryMethod) {
          return this.summaryMethod
        }
        if (this.summaryMap) {
          return this.defaultSummaryMethod
        }
        return null
      }
    },
    mounted() {
      if (this.tableHeight) {
        this.height = this.tableHeight
      }
    },
    methods: {
      // 当用户手动勾选数据行的 Checkbox 时触发的事件
      handleSelect(rows, row) {
        this.$emit('select', rows, row)
      },
      //  当选择项发生变化时会触发该事件
      handleSelectChange(rows) {
        this.$emit('selection-change', rows)
      },
      selectAll(rows) {
        this.$emit('selection-all', rows)
      },
      changePageSizes(e) {
        this.tableData.pageSize = e
        this.$emit('change')
      },
      currentChange(num) {
        this.tableData.pageNum = num
        this.$emit('change')
      },
      // 点击切换固定表头
      fixedTableTop() {
        this.fixedTop = !this.fixedTop
        if (this.fixedTop) {
          // 需要固定表头
          const ref = document.getElementById(this.refName)
          const data = ref.getBoundingClientRect()
          this.height =
            this.windowClientHeight -
            data.top -
            (this.showPagination ? 110 : 20)
        } else {
          this.height = 100000
        }
      },
      defaultSummaryMethod({ data, columns }) {
        const sums = []
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计'
            return
          }
          const fields = Object.keys(this.summaryMap)
          if (fields.includes(column.property)) {
            const values = data.map((item) => Number(item[column.property]))
            if (values.every((value) => isNaN(value))) {
              sums[index] = ''
              return
            }
            sums[index] = values
              .reduce((prev, curr) => {
                const value = Number(curr)
                if (!isNaN(value)) {
                  return prev + curr
                } else {
                  return prev
                }
              }, 0)
              ?.toFixed(this.summaryMap[column.property] ?? 2)
          }
        })
        return sums
      }
    }
  }
</script>
<style lang="scss">
  // 隐藏单元格边框
  .hide-cell-border td,
  .is-leaf {
    border-left: none;
    border-right: none;
    border-top: none;
  }
  // 隐藏表头边框
  .hide-header-border th,
  .is-leaf {
    border-left: none;
    border-right: none;
    border-top: none;
  }
  .jfx-table {
    width: 100%;
    margin-bottom: 20px;
    position: relative;
    .hack-table-top {
      position: absolute;
      top: 0px;
      left: -3px;
      width: 26px;
      height: 20px;
      z-index: 2021;
      // transform: rotatez(-35deg);
      //  border: solid 1px red;
    }
    /* 解决表格抖动 */
    .el-table {
      width: 99.9% !important;
    }
    .el-table th.gutter {
      display: table-cell !important;
    }
    .el-table--small td {
      padding: 5px 0;
    }
    .el-table--small th {
      padding: 10px 0;
    }
    .cell .el-button:nth-child(1) {
      padding-left: 5px;
    }
    .el-button + .el-button {
      margin-left: 5px;
    }
    .has-gutter .cell {
      white-space: pre-line !important;
    }
  }
  .el-tooltip__popper {
    background: #6ea9f3 !important; /*背景色  !important优先级*/
    opacity: 1 !important; /*背景色透明度*/
    color: #ffffff !important; /*字体颜色*/
  }
  .el-tooltip__popper[x-placement^='top'] .popper__arrow::after {
    border-top-color: #6ea9f3;
  }
  .el-tooltip__popper[x-placement^='top'] .popper__arrow {
    border-top-color: #6ea9f3;
  }
  .pagination-bx {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .el-input--small .el-input__inner {
      height: 26px;
      line-height: 26px;
      text-align: center;
      font-size: 12px;
    }
    .el-input--small .el-input__icon {
      line-height: 26px;
    }
    .el-pagination {
      display: flex;
      align-items: center;
      font-weight: 500;
    }
    .el-pagination button,
    .el-pagination span:not([class*='suffix']) {
      font-size: 12px;
      height: 25px;
      line-height: 25px;
    }
    .el-pagination.is-background .btn-next,
    .el-pagination.is-background .btn-prev,
    .el-pagination.is-background .el-pager li {
      min-width: 26px;
    }
    .el-pager li {
      font-size: 12px;
      min-width: 35.5px;
      height: 25px;
      line-height: 25px;
    }
  }
</style>
