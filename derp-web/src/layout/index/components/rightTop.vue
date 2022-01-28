<template>
  <div class="right-top-box">
    <div class="right-top-bx-l">
      <i
        class="el-icon-s-fold swich-icon"
        @click="swichCollapse"
        v-if="!isCollapse"
      ></i>
      <i
        class="el-icon-s-unfold swich-icon"
        @click="swichCollapse"
        v-if="isCollapse"
      ></i>
      <span class="company" v-if="companyName">公司：{{ companyName }}</span>
      <span
        class="change-company"
        v-if="companyName"
        @click="visible.show = true"
        >切换</span
      >
    </div>
    <div class="right-top-bx-r">
      <i class="el-icon-bell fs-32 clr-w c-p" @click="drawer = !drawer"></i>
      &nbsp;&nbsp;
      <el-avatar :size="40" :src="circleUrl"></el-avatar>
      &nbsp;&nbsp;
      <el-dropdown>
        <span class="el-dropdown-link">
          {{ userName }} &nbsp;&nbsp;
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <div style="font-size: 16px; padding-left: 10px; color: #333">
            Welcome !
          </div>
          <el-dropdown-item>
            <div class="jfx-dropdown-item" @click="logout">
              <i class="el-icon-switch-button"></i>
              <span>退出</span>
            </div>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <!-- 选择公司主体 -->
    <chose-company
      :visible.sync="visible"
      v-if="visible.show"
      @comfirm="visible.show = false"
    ></chose-company>
    <!-- 选择公司主体 end-->
    <!-- 打开公告 -->
    <notice
      :drawer="drawer"
      @close="drawer = false"
      @openDrawer="drawer = true"
    />
    <!-- 打开公告 end -->
  </div>
</template>
<script>
  import choseCompany from '@c/choseCompany/index'
  import notice from './rightTopNotice.vue'
  import { logout } from '@u/tool'
  export default {
    components: {
      choseCompany,
      notice
    },
    data() {
      return {
        circleUrl:
          'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        visible: {
          show: false
        },
        ruleForm: {
          id: 100108,
          oldPwd: '',
          password: ''
        },
        rules: {
          oldPwd: [
            { required: true, message: '请输入旧密码', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入新密码', trigger: 'blur' }
          ]
        },
        drawer: false,
        companyName: '',
        userName: ''
      }
    },
    computed: {
      isCollapse() {
        return this.$store.getters.isCollapse
      }
    },
    mounted() {
      let userInfo = sessionStorage.getItem('userInfo')
      userInfo = userInfo ? JSON.parse(userInfo) : {}
      this.companyName = userInfo.merchantName || ''
      this.userName = userInfo.name || ''
    },
    methods: {
      // 收缩菜单
      swichCollapse() {
        const bl = !this.isCollapse
        this.$store.dispatch('base/AC_UPDATE_STATE', {
          key: 'isCollapse',
          value: bl
        })
      },
      // 修改密码点击保存
      changPwd() {},
      // 退出登录
      logout() {
        logout()
      }
    }
  }
</script>
<style lang="scss" scoped>
  .right-top-box {
    width: 100%;
    height: 100%;
    color: #ffffff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-sizing: border-box;
    padding: 0 15px;
  }
  .right-top-bx-l {
    display: flex;
    align-items: center;
    .swich-icon {
      font-size: 30px;
      color: #ffffff;
      cursor: pointer;
    }
    .company {
      margin-left: 20px;
      margin-right: 10px;
      font-size: 18px;
      color: #ffffff;
    }
    .change-company {
      font-size: 14px;
      color: #efefef;
      text-decoration: underline;
      cursor: pointer;
      margin-top: 5px;
    }
  }

  .right-top-bx-r {
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }
  .el-dropdown-link {
    display: inline-block;
    color: #ffffff;
    font-size: 20px;
    cursor: pointer;
  }
  .jfx-dropdown-item {
    display: inline-block;
    width: 100px;
    text-align: center;
    font-size: 16px;
    margin: 5px 0;
  }
  .bor-b {
    height: 36px;
    margin-bottom: 10px;
    border-bottom: solid 1px #eaeaea;
  }
  .notice-list {
    height: 40px;
    display: flex;
    align-items: center;
    &:hover {
      color: #6ea9f3;
    }
  }
</style>
