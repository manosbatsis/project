<template>
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
              label="公告类型："
              prop="type"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.type"
                placeholder="请选择"
                :data-list="getSelectList('notice_typelist')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.notice_typelist"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <el-form-item
              label="状态："
              prop="status"
              class="search-panel-item fs-14 clr-II"
            >
              <el-select
                v-model="searchProps.status"
                placeholder="请选择"
                :data-list="getSelectList('notice_statuslist')"
                filterable
                clearable
              >
                <el-option
                  v-for="item in selectOpt.notice_statuslist"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="18" :md="18" :lg="12" :xl="12">
            <el-form-item prop="date" label="发布时间：">
              <el-date-picker
                v-model="date"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                clearable
              />
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
          v-permission="'notice_add'"
          @click="linkTo(`/notice/NoticeAdd`)"
        >
          新建公告
        </el-button>
      </el-col>
    </el-row>
    <div class="mr-t-20"></div>
    <!-- 操作 end -->
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      showSelection
      @selection-change="selectionChange"
      @change="getList"
    >
      <template slot="title" slot-scope="{ row }">
        <el-button type="text" @click="showDetail(row.id)">
          {{ row.title }}
        </el-button>
      </template>
      <template slot="action" slot-scope="{ row }">
        <el-button
          type="text"
          v-permission="'notice_edit'"
          v-if="row.status === '001'"
          @click="linkTo(`/notice/NoticeEdit?id=${row.id}`)"
        >
          编辑
        </el-button>
        <el-button
          type="text"
          v-permission="'notice_publish'"
          v-if="row.status === '001'"
          @click="modifyStatus(row.id, '002')"
          style="color: #32cd32"
        >
          发布
        </el-button>
        <el-button
          type="text"
          v-permission="'notice_del'"
          v-if="row.status === '001'"
          @click="modifyStatus(row.id, '006')"
          style="color: red"
        >
          删除
        </el-button>
      </template>
    </JFX-table>
    <!-- 表格 end -->
    <!-- 公告详情 -->
    <NoticeDetail
      v-if="noticeDetail.visible.show"
      :isVisible="noticeDetail.visible"
      :data="noticeDetail.data"
    ></NoticeDetail>
    <!-- 公告详情 end -->
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { listNotice, modifyStatus } from '@a/noticeManage'
  export default {
    mixins: [commomMix],
    components: {
      // 公告详情
      NoticeDetail: () => import('./NoticeDetail.vue')
    },
    data() {
      return {
        // 查询数据
        searchProps: {
          type: '',
          status: ''
        },
        // 发布时间
        date: [],
        tableColumn: [
          {
            label: '公告类型',
            prop: 'typeLabel',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '标题',
            slotTo: 'title',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '状态',
            prop: 'statusLabel',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '创建日期',
            prop: 'createDate',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '发布日期',
            prop: 'publishDate',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          { label: '操作', slotTo: 'action', width: '130', align: 'left' }
        ],
        // 公告详情
        noticeDetail: {
          visible: { show: false },
          data: {}
        }
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
        // 发布时间
        this.searchProps.publishStartDateStr =
          this.date && this.date.length ? this.date[0] : ''
        this.searchProps.publishEndDateStr =
          this.date && this.date.length ? this.date[1] : ''
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const { data } = await listNotice({
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
      modifyStatus(id, status) {
        const helper = {
          '002': '发布',
          '006': '删除'
        }
        try {
          this.$showToast(`是否${helper[status]}该公告`, async () => {
            await modifyStatus({ id, status })
            this.$successMsg(`${helper[status]}成功`)
            this.getList()
          })
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      // 展示公告详情
      showDetail(id) {
        this.noticeDetail.visible.show = true
        this.noticeDetail.data = { id }
      },
      // 重置搜索框
      resetSearchForm() {
        this.reset('searchForm', () => {
          this.data = []
          this.getList(1)
        })
      }
    }
  }
</script>
<style lang="scss" scoped></style>
