<template>
  <el-drawer
    :visible.sync="drawer"
    :direction="'rtl'"
    :with-header="false"
    :size="'25%'"
    :show-close="false"
    :before-close="close"
  >
    <el-row class="pd-10">
      <el-col :span="24" class="fs-16 mr-t-20 clr-I bor-b">
        系统公告（
        <span class="clr-r">{{ account }}</span>
        ）
      </el-col>
      <el-row
        class="pd-10 notice-list fs-14 clr-III c-p"
        v-for="(item, index) in noticeList"
        :key="index"
      >
        <el-col :span="18" @click.native="clickItem(item)">{{
          item.title
        }}</el-col>
        <el-col :span="6">{{ item.publishDate.slice(0, 10) }}</el-col>
      </el-row>
    </el-row>
    <JFX-Dialog
      :visible.sync="detailVisible"
      :showClose="true"
      :width="'1000px'"
      :title="'公告详情'"
      :btnAlign="'center'"
      :append-to-body="true"
      :showCloseBtn="false"
      :showConfirmBtn="false"
      top="15vh"
    >
      <div class="t-c mr-b-10 fs-16" style="font-weight: 600">
        {{ currentNotice.title }}
      </div>
      <div class="t-c mr-b-10">
        公告类型:{{ currentNotice.typeLabel }} 发布时间：
        {{ currentNotice.publishDate }}
      </div>
      <hr />
      <div v-html="currentNotice.contentBody" style="padding: 15px"></div>
      <template slot="footer_expand_btn">
        <el-button
          type="primary"
          :disabled="!before"
          @click="prevNotice"
          :loading="beforeOrAfterLoading"
          >{{ before ? '上一条' : '没有上一条了' }}</el-button
        >
        <el-button
          type="primary"
          :disabled="!after"
          @click="nextNotice"
          :loading="beforeOrAfterLoading"
          >{{ after ? '下一条' : '没有下一条了' }}</el-button
        >
      </template>
    </JFX-Dialog>
  </el-drawer>
</template>

<script>
  import {
    getUserNotice,
    getAroundNotice,
    changeReadStatus
  } from '@a/noticeManage/index'
  export default {
    props: {
      drawer: Boolean
    },
    data() {
      return {
        pageNo: 1,
        account: 0,
        noticeList: [],
        unReadAmount: 0,
        detailVisible: { show: false },
        currentNotice: {},
        detailContent: '',
        before: null,
        after: null,
        beforeOrAfterLoading: false
      }
    },
    methods: {
      //  点击公告
      async clickItem(item) {
        this.detailVisible.show = true
        this.showItem(item)
      },
      // 显示公告
      async showItem(item) {
        this.beforeOrAfterLoading = true
        this.currentNotice = item
        // 获取上一条 下一条
        const {
          data: { before, after }
        } = await getAroundNotice({ id: item.id })
        // 未读设置当前为已读
        if (item.readStatus === '0') {
          await changeReadStatus({ noticeId: item.id })
        }
        this.beforeOrAfterLoading = false
        this.before = before
        this.after = after
      },
      close(done) {
        this.$emit('close')
        done()
      },
      prevNotice() {
        this.showItem(this.before)
      },
      nextNotice() {
        this.showItem(this.after)
      }
    },
    async mounted() {
      const { data } = await getUserNotice({ pageNo: this.pageNo })
      const { account, noticeList, unReadAmount } = data
      this.account = account
      this.noticeList = noticeList
      this.unReadAmount = unReadAmount
      if (this.unReadAmount) {
        setTimeout(() => {
          this.$emit('openDrawer')
          this.$nextTick(() => {
            this.clickItem(noticeList[0])
          })
        }, 2000)
      }
    }
  }
</script>

<style lang="scss" scoped>
  .notice-list {
    height: 40px;
    display: flex;
    align-items: center;
    &:hover {
      color: #6ea9f3;
    }
  }
</style>
