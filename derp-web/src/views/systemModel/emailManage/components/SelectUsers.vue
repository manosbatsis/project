<template>
  <div>
    <JFX-Dialog
      :visible.sync="isVisible"
      :width="'700px'"
      :top="'150px'"
      :btnAlign="'center'"
      :showClose="true"
      title="选择绑定用户"
      @comfirm="comfirm"
    >
      <el-transfer
        v-model="selectUser"
        :data="userList"
        :titles="['所有用户', '已选择用户']"
        class="flex-c-c"
        filterable
      />
    </JFX-Dialog>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import { getUserList, getUserDetail } from '@a/base'
  export default {
    mixins: [commomMix],
    props: {
      isVisible: {
        type: Object,
        default: () => {}
      },
      filterData: {
        type: Object,
        default: function () {
          return {}
        }
      }
    },
    data() {
      return {
        selectUser: [],
        userList: [],
        emails: []
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      init() {
        this.getUserList()
      },
      // 获取用户列表
      async getUserList() {
        try {
          const { data } = await getUserList()
          if (data && data.length) {
            this.userList = data.map((item) => ({
              label: item.text || '',
              key: item.value || ''
            }))
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      async comfirm() {
        if (!this.selectUser.length) {
          this.$emit('update:isVisible', { show: false })
          return false
        }
        const emails = []
        try {
          for (let i = 0; i < this.selectUser.length; i++) {
            const id = this.selectUser[i]
            const {
              data: { detail = {} }
            } = await getUserDetail({ id })
            const { email, name, id: userId } = detail
            emails.push({ email, name, userId })
          }
          this.$emit('comfirm', emails)
          this.$emit('update:isVisible', { show: false })
        } catch (error) {
          this.$errorMsg(error.message)
        }
      }
    }
  }
</script>
<style lang="scss" scoped></style>
