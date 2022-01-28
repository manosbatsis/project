<template>
  <div>
    <JFX-Dialog
      :visible.sync="settlementVisible"
      :showClose="true"
      @comfirm="comfirm"
      :width="'850px'"
      :title="'选择费用项目'"
      :btnAlign="'center'"
      :confirmBtnLoading="confirmBtnLoading"
      top="15vh"
    >
      <JFX-search-panel @reset="resetForm" @search="getList(1)">
        <JFX-Form :model="ruleForm" ref="ruleForm">
          <el-row :gutter="10">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="1级费项："
                prop="parentId"
                class="search-panel-item fs-14 clr-II"
              >
                <el-select
                  v-model="ruleForm.parentId"
                  clearable
                  @change="level1Change"
                >
                  <el-option
                    v-for="item in selectOpt.level1List"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="8">
              <el-form-item
                label="2级费项："
                prop="id"
                class="search-panel-item fs-14 clr-II"
              >
                <el-select v-model="ruleForm.id" clearable>
                  <el-option
                    v-for="item in selectOpt.level2List"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </JFX-Form>
      </JFX-search-panel>
      <JFX-table
        :tableData.sync="tableData"
        :tableHeight="'480px'"
        :showSelection="true"
        :tableColumn="tableColumn"
        @selection-change="selectionChange"
        @change="getList"
      ></JFX-table>
    </JFX-Dialog>
  </div>
</template>

<script>
  import commomMix from '@m/index'
  import {
    getSettlemenList,
    getSettlemenConfigList
  } from '@a/receiveMoneyManage'
  export default {
    mixins: [commomMix],
    props: {
      settlementVisible: {
        type: Object,
        default: function () {
          return { show: false }
        }
      },
      customerId: {
        type: String,
        required: true
      }
    },
    data() {
      return {
        ruleForm: {
          parentId: '',
          id: ''
        },
        tableColumn: [
          {
            label: '1级费项',
            prop: 'parentProjectName',
            minWidth: '140',
            align: 'center',
            hide: true
          },
          {
            label: '2级费项',
            prop: 'projectName',
            minWidth: '140',
            align: 'center',
            hide: true
          }
        ],
        confirmBtnLoading: false
      }
    },
    async mounted() {
      const [{ data: level2Res }, { data: level1Res }] = await Promise.all([
        getSettlemenList({
          level: 2,
          customerId: '',
          type: 1,
          moduleType: 2
        }),
        getSettlemenList({
          level: 1,
          customerId: '',
          type: 1,
          moduleType: 2
        })
      ])
      this.$set(this.selectOpt, 'level1List', level1Res)
      this.$set(this.selectOpt, 'level2List', level2Res)
      this.getList()
    },
    methods: {
      // 获取模板
      async getList(pageNum) {
        try {
          this.tableData.pageNum = pageNum || this.tableData.pageNum
          this.tableData.loading = true
          const {
            data: { list, total }
          } = await getSettlemenConfigList({
            begin: (this.tableData.pageNum - 1) * this.tableData.pageSize,
            pageSize: this.tableData.pageSize || 10,
            ...this.ruleForm,
            customerId: this.customerId,
            type: 1,
            moduleType: 2
          })
          this.tableData.total = total
          this.tableData.list = list
        } catch (error) {
          this.$errorMsg(error.message)
        } finally {
          this.tableData.loading = false
        }
      },
      resetForm() {
        this.reset('ruleForm', () => {
          this.getList(1)
        })
      },
      async level1Change() {
        const parentId = this.ruleForm.parentId
        const { data } = await getSettlemenList({
          level: 2,
          customerId: '',
          type: 1,
          moduleType: 2,
          parentId
        })
        this.ruleForm.id = ''
        this.$set(this.selectOpt, 'level2List', data)
      },
      // 提交
      async comfirm() {
        const chooseList = this.tableChoseList
        const chooseListLength = chooseList.length
        if (chooseListLength !== 1) {
          return this.$errorMsg(
            chooseListLength === 0 ? '至少选择一条记录' : '只能选择一条记录'
          )
        }
        this.$emit('comfirm', chooseList[0])
      }
    }
  }
</script>

<style lang="scss" scoped></style>
