<template>
  <div class="page-bx edit-bx bgc-w">
    <JFX-Breadcrumb :showGoback="true" className="mr-t-10" />
    <div class="pd-15" style="padding-top: 10px">
      <el-input
        class="mr-b-10"
        clearable
        placeholder="输入关键字进行过滤"
        style="width: 200px"
        v-model.lazy="filterText"
      />
    </div>
    <div class="pd-15" style="overflow: auto">
      <el-tree
        v-loading="dataLoading"
        show-checkbox
        check-strictly
        @check="clickDeal"
        :highlight-current="true"
        :default-expand-all="true"
        node-key="id"
        :data="treeData"
        :default-expanded-keys="treeExpandData"
        :default-checked-keys="treeCheckedData"
        :props="defaulTreetProps"
        :filter-node-method="filterNode"
        ref="tree"
      >
        <span class="customer-tree-node fs-14" slot-scope="{ data }">
          <span v-if="!filterText">{{ data.name }}</span>
          <span
            v-if="filterText"
            v-html="
              data.name.replace(
                new RegExp(filterText, 'g'),
                `<text style='color:red;'>${filterText}</text>`
              )
            "
          ></span>
        </span>
      </el-tree>
    </div>
    <div class="mr-t-30 flex-c-c">
      <el-button @click="confirm" type="primary">保 存</el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
  </div>
</template>
<script>
  import commomMix from '@m/index'
  import {
    getRolePermissions,
    getTreeAll
  } from '@a/systemManage/permission/index'
  import { assignP } from '@a/systemManage/role/index'
  export default {
    mixins: [commomMix],
    data() {
      return {
        roleId: '',
        filterText: '', // 过滤文字
        treeData: [], // 树数据
        defaulTreetProps: {
          // 树配置
          label: 'name',
          children: 'children'
        },
        treeExpandData: [], // 树展开节点
        treeCheckedData: [], // 树选中节点
        dataLoading: true // 页面加载
      }
    },
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val)
      }
    },
    async mounted() {
      this.roleId = this.$route.query.roleId
      await this.getTree()
      await this.getRolePermissions(this.roleId)
      // this.initTreeCheck(this.treeData)
      this.dataLoading = false
      console.log(this.$refs.tree)
    },
    methods: {
      // 获取权限列表
      async getTree() {
        const { data } = await getTreeAll()
        this.treeExpandData = []
        this.treeData = data
        return Promise.resolve('success')
      },
      //   获取角色已有权限
      async getRolePermissions(roleId) {
        const { data } = await getRolePermissions({ roleId })
        this.treeCheckedData = data.map((item) => item.permissionId)
        return Promise.resolve('success')
      },
      clickDeal(currentObj, treeStatus) {
        // 用于：父子节点严格互不关联时，父节点勾选变化时通知子节点同步变化，实现单向关联。
        const selected = treeStatus.checkedKeys.indexOf(currentObj.id) // -1未选中
        // 选中
        if (selected !== -1) {
          // 子节点只要被选中父节点就被选中
          this.selectedParent(currentObj)
          // 统一处理子节点为相同的勾选状态
          this.uniteChildSame(currentObj, true)
        } else {
          if (currentObj.children === null) currentObj.children = []
          // 未选中 处理子节点全部未选中
          if (currentObj.children.length !== 0) {
            this.uniteChildSame(currentObj, false)
          }
        }
      },
      // 统一处理子节点为相同的勾选状态
      uniteChildSame(treeList, isSelected) {
        this.$refs.tree.setChecked(treeList.id, isSelected)
        if (treeList.children === null) treeList.children = []
        for (let i = 0; i < treeList.children.length; i++) {
          this.uniteChildSame(treeList.children[i], isSelected)
        }
      },
      // 统一处理父节点为选中
      selectedParent(currentObj) {
        const currentNode = this.$refs.tree.getNode(currentObj)
        if (currentNode.parent.key !== undefined) {
          this.$refs.tree.setChecked(currentNode.parent, true)
          this.selectedParent(currentNode.parent)
        }
      },

      filterNode(value, data, node) {
        // 如果共有四级菜单
        if (!value) return true
        const one = data.name.indexOf(value) !== -1
        const two =
          node.parent &&
          node.parent.data &&
          node.parent.data.name &&
          node.parent.data.name.indexOf(value) !== -1
        const three =
          node.parent &&
          node.parent.parent &&
          node.parent.parent.data &&
          node.parent.parent.data.name &&
          node.parent.parent.data.name.indexOf(value) !== -1
        const four =
          node.parent &&
          node.parent.parent &&
          node.parent.parent.parent &&
          node.parent.parent.parent.data &&
          node.parent.parent.parent.data.name &&
          node.parent.parent.parent.data.name.indexOf(value) !== -1
        let resultOne = false
        let resultTwo = false
        let resultThree = false
        const resultFour = false
        if (node.level === 1) {
          resultOne = one
        } else if (node.level === 2) {
          resultTwo = one || two
        } else if (node.level === 3) {
          resultThree = one || two || three
        } else if (node.level === 4) {
          resultThree = one || two || three || four
        }
        return resultOne || resultTwo || resultThree || resultFour
      },
      async confirm() {
        try {
          const checkList = this.$refs.tree.getCheckedNodes(false, true)
          const ids = checkList.map((item) => item.id).toString()
          const roleId = this.$route.query.copyId || this.roleId
          await assignP({ ids, roleId })
          this.$successMsg('操作成功')
          this.closeTag()
        } catch (err) {
          console.log(err)
        }
      }
    }
  }
</script>
<style lang="scss" scoped>
  .page-bx {
    display: flex;
    flex-direction: column;
    height: 100%;
  }
  // ::v-deep .el-tree {
  //     height:calc(100vh - 350px);
  //     overflow: auto;
  // }
  ::v-deep .el-tree .el-tree-node__children .el-tree-node__children {
    display: flex;
    flex-wrap: wrap;
  }
  ::v-deep .el-tree-node__content {
    padding-top: 10px;
  }
  ::v-deep
    .el-tree
    .el-tree-node__children
    .el-tree-node__children
    .el-tree-node
    + .el-tree-node
    .el-tree-node__content {
    padding-right: 20px !important;
  }
  .customer-tree-node {
    padding: 5px;
  }
</style>
