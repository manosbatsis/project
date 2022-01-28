<template>
  <JFX-Dialog
    :visible.sync="relationVisible"
    :showClose="true"
    :width="'600px'"
    :title="'关联仓库'"
    :btnAlign="'center'"
    :loading="loading"
    @comfirm="saveRel"
    :confirmBtnLoading="confirmBtnLoading"
    top="10vh"
  >
    <el-transfer
      v-model="bindDepotData"
      filterable
      :titles="['仓库信息', '已选仓库']"
      :data="data"
      :props="{
        key: 'value',
        label: 'label'
      }"
    >
    </el-transfer>
  </JFX-Dialog>
</template>

<script>
  import { getDepotSelectBean } from '@a/base/index'
  import { saveMerchandiseDepotRel } from '@a/goodsManage/index'
  export default {
    props: {
      relationVisible: {
        type: Object,
        default() {
          return { show: false }
        }
      },
      rowData: {
        default() {
          return {}
        }
      }
    },
    data() {
      return {
        loading: false,
        confirmBtnLoading: false,
        data: [],
        bindDepotData: []
      }
    },
    methods: {
      async saveRel() {
        // 新增编辑模式，不用保存
        if (this.rowData.isAddOrEdit) {
          this.$emit('close', {
            depotIds: this.bindDepotData.join(','),
            depotNames: this.data
              .filter((item) => {
                return this.bindDepotData.includes(item.value)
              })
              .map((item) => item.label)
              .join(',')
          })
          return
        }
        try {
          this.confirmBtnLoading = true
          await saveMerchandiseDepotRel({
            goodsId: this.rowData.id,
            depotIds: this.bindDepotData.join(',')
          })
          this.$successMsg('操作成功')
          this.$emit('close')
        } catch (err) {
          console.log(err)
        } finally {
          this.confirmBtnLoading = false
        }
      }
    },
    async mounted() {
      this.loading = true
      const { data } = await getDepotSelectBean()
      this.data = data
      if (this.rowData.depotIds) {
        this.bindDepotData = this.rowData.depotIds
          .split(',')
          .filter((item) => data.find((sItem) => sItem.value === item))
      }

      this.loading = false
    }
  }
</script>

<style></style>
