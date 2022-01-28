<template>
  <JFX-Dialog
    :visible.sync="visible"
    :showClose="true"
    :width="'650px'"
    :title="'选择绑定用户'"
    :btnAlign="'center'"
    top="10vh"
    @comfirm="comfirm"
  >
    <el-transfer
      v-loading="dataLoading"
      filterable
      :titles="['未绑定', '已绑定']"
      :button-texts="['解绑', '绑定']"
      :props="{
        key: 'value',
        label: 'text'
      }"
      :filter-method="filterMethod"
      filter-placeholder="输入用户姓名"
      v-model="value"
      :data="data"
    ></el-transfer>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { searchRoleUser, bindUser } from '@a/systemManage/role/index'
  export default {
    mixins: [commomMix],
    props: {
      visible: {
        default() {
          return { show: false }
        }
      },
      roleId: ''
    },
    data() {
      return {
        data: [],
        value: [],
        filterMethod(query, item) {
          return item.text.indexOf(query) > -1
        },
        dataLoading: true
      }
    },
    async mounted() {
      await this.searchRoleUser({ roleId: this.roleId })
      this.dataLoading = false
    },
    methods: {
      async searchRoleUser(roleId) {
        try {
          const { data } = await searchRoleUser(roleId)
          this.data = data.allUser
          this.value = data.allUser
            .filter((item) => data.bindUser.includes(String(item.value)))
            .map((item) => Number(item.value))
        } catch (err) {
          console.log(err)
        }
      },
      async comfirm() {
        try {
          await bindUser({ roleId: this.roleId, ids: this.value.toString() })
          this.$successMsg('操作成功')
          this.visible.show = false
        } catch (err) {
          console.log(err)
        }
      }
    }
  }
</script>
