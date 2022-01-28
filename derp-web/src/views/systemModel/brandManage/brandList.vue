<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb />
    <!-- 面包屑 end -->
    <!-- 搜索面板 -->
    <JFX-search-panel
      @reset="resetForm"
      @search="getList(1)"
      :showOpenBtn="false"
    >
      <JFX-Form :model="ruleForm" ref="ruleForm" @submit.native.prevent>
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="品牌名称："
              prop="name"
              class="search-panel-item fs-14 clr-II"
            >
              <el-input
                v-model.trim="ruleForm.name"
                clearable
                placeholder="请输入"
              ></el-input>
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
          :size="'small'"
          @click="addVisible.show = true"
          v-permission="'brandSuperior_add_new'"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          :size="'small'"
          @click="exportExcel"
          v-permission="'brandSuperior_add'"
        >
          导出
        </el-button>
      </el-col>
    </el-row>
    <div class="mr-t-20"></div>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      @selection-change="selectionChange"
      @change="getList"
    >
      <el-table-column
        type="index"
        label="序号"
        width="55"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="name"
        label="品牌名称"
        align="center"
        min-width="200"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="chinaBrandName"
        label="中文品牌名称"
        align="center"
        min-width="200"
      ></el-table-column>
      <el-table-column
        prop="englishBrandName"
        label="英文品牌名称"
        align="center"
        min-width="200"
      ></el-table-column>
      <el-table-column
        prop="parentName"
        label="标准品牌"
        align="center"
        min-width="160"
      ></el-table-column>
      <el-table-column label="创建人" align="center" width="150">
        <template slot-scope="{ row }">
          {{ row.createrName }}
          <br />
          {{ row.createDate }}
        </template>
      </el-table-column>
      <el-table-column label="修改人" align="center" width="150">
        <template slot-scope="{ row }">
          {{ row.operatorName }}
          <br />
          {{ row.operationDate }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button
            type="text"
            @click="openLay(scope.row)"
            v-permission="'brand_modify'"
          >
            编辑标准品牌
          </el-button>
          <el-button
            type="text"
            @click="openLog(scope.row)"
            v-permission="'brand_log'"
          >
            日志
          </el-button>
        </template>
      </el-table-column>
    </JFX-table>
    <!-- 表格 end-->
    <!-- 批量修改品类 -->
    <JFX-Dialog
      v-if="visible.show"
      :visible.sync="visible"
      :showClose="true"
      @comfirm="comfirm"
      :width="'30vw'"
      :title="'编辑标准品牌'"
      :btnAlign="'center'"
      top="30vh"
    >
      <div class="change-lve" v-loading="changeloading">
        <div>
          <span class="clr-II">品牌名称：{{ targetData.name }}</span>
        </div>
        <div>
          <span class="need">标准品牌：</span>
          <el-select
            v-model="parentId"
            placeholder="请选择"
            clearable
            filterable
            :data-list="getBrandParent('brandList', { type: 1 })"
          >
            <el-option
              v-for="item in selectOpt.brandList"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            ></el-option>
          </el-select>
        </div>
      </div>
    </JFX-Dialog>
    <!-- 新增品牌 -->
    <add
      v-if="addVisible.show"
      :addVisible="addVisible"
      @close="
        () => {
          addVisible.show = false
          getList()
        }
      "
    />
    <!--  日志 -->
    <log-list
      v-if="logVisible.show"
      :visible.sync="logVisible"
      :filterData="filterData"
      :type="'system'"
    ></log-list>
  </div>
</template>
<script>
  import { brandList, exportBrandUrl, brandModify } from '@a/brandManage/index'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    components: {
      add: () => import('./components/addBrand.vue'),
      logList: () => import('@c/logList/index.vue')
    },
    data() {
      return {
        ruleForm: {
          name: ''
        },
        visible: { show: false },
        addVisible: { show: false },
        logVisible: { show: false },
        parentId: '',
        changeloading: false,
        targetData: {},
        filterData: {
          module: '5'
        } // log日志传参
      }
    },
    mounted() {
      this.getList(1)
    },
    activated() {
      this.getList()
    },
    methods: {
      async getList(pageNum) {
        try {
          this.tableData.loading = true
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          const opt = {
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize,
            ...this.ruleForm
          }
          // 同步方法
          const res = await brandList(opt)
          this.tableData.list = res.data.list
          this.tableData.total = res.data.total
        } catch (err) {
          console.log(err)
        }
        this.tableData.loading = false
      },
      // 重置搜索栏
      resetForm() {
        this.reset('ruleForm', () => {
          this.getList(1)
        })
      },
      // 打开弹窗
      openLay(row) {
        this.targetData = row
        this.parentId = row.parentId ? row.parentId.toString() : ''
        this.visible.show = true
      },
      // 打开日志
      openLog(row) {
        this.logVisible.show = true
        this.filterData.id = row.id
      },
      async comfirm() {
        if (!this.parentId) {
          this.$errorMsg('请选择标准品牌')
          return false
        }
        if (this.changeloading) return false // 拦截多次提交
        this.changeloading = true
        try {
          await brandModify({ id: this.targetData.id, parentId: this.parentId })
          this.$successMsg('操作成功')
          this.visible.show = false
          this.getList()
        } catch (error) {
          console.log(error)
        }
        this.changeloading = false
      },
      exportExcel() {
        if (this.tableData.total < 1) {
          this.$errorMsg('暂无数据可导出')
          return false
        }
        const opt = {
          begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
          pageSize: this.tableData.pageSize || 10,
          ...this.ruleForm
        }
        this.$exportFile(exportBrandUrl, opt)
      }
    }
  }
</script>
<style lang="scss" scoped>
  .change-lve {
    width: 100%;
    height: 80px;
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: space-around;
    align-items: flex-start;
  }
</style>
