<template>
  <JFX-Dialog
    :visible.sync="grantVisible"
    :showClose="true"
    :width="'650px'"
    :title="'选择绑定角色（只能绑定一个角色）'"
    :btnAlign="'center'"
    top="10vh"
    @comfirm="comfirm"
  >
    <el-transfer
      class="noCheckAll"
      ref="transfer"
      v-loading="dataLoading"
      filterable
      :titles="['未绑定', '已绑定']"
      :button-texts="['解绑', '绑定']"
      @left-check-change="checkLeft($event)"
      @change="selectChange"
      :props="{
        key: 'id',
        label: 'name'
      }"
      :filter-method="filterMethod"
      filter-placeholder="输入角色名称"
      v-model="value"
      :data="data"
    ></el-transfer>
    <div class="fixedHeaderTop"></div>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { getList } from '@a/systemManage/role/index'
  import { saveUserBindRole } from '@a/systemManage/user/index'
  export default {
    mixins: [commomMix],
    props: {
      grantVisible: {
        default() {
          return { show: false }
        }
      },
      userId: '',
      roleList: ''
    },
    data() {
      return {
        data: [],
        value: [],
        filterMethod(query, item) {
          return item.name.indexOf(query) > -1
        },
        dataLoading: true
      }
    },
    async mounted() {
      this.init()
    },
    methods: {
      async init() {
        try {
          const {
            data: { list }
          } = await getList({
            begin: 0,
            pageSize: 10000
          })
          this.data = list.map((item) => {
            item.disabled = false
            return item
          })
          this.value = this.roleList.map(Number)
          this.dataLoading = false
        } catch (err) {
          console.log(err)
        }
      },
      // 单选
      checkLeft() {
        const leftPanel = this.$refs.transfer.$refs.leftPanel
        const checkedList = leftPanel.checked
        leftPanel.checkChangeByUser = false
        checkedList.splice(0, checkedList.length - 1)
      },
      selectChange(changeValue, type, keyList) {
        if (type === 'left') {
          return
        }
        this.value = keyList.slice()
      },
      async comfirm() {
        if (this.value.length > 1) {
          return this.$warningMsg('只能绑定一个角色')
        }
        try {
          await saveUserBindRole({ id: this.userId, roleId: this.value[0] })
          this.$successMsg('操作成功')
          this.$emit('close')
        } catch (err) {
          console.log(err)
        }
      }
    }
  }
</script>
<style lang="scss" scoped>
  ::v-deep.noCheckAll .el-transfer-panel__header .el-checkbox__input {
    display: none;
  }
  .fixedHeaderTop {
    position: absolute;
    top: 0;
    width: 100%;
    height: 65px;
  }
</style>
