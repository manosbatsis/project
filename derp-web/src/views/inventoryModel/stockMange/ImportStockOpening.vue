<template>
  <div class="page-bx bgc-w">
    <!-- 面包屑 -->
    <JFX-Breadcrumb :showGoback="true" :breadcrumb="breadcrumb" />
    <!-- 面包屑 end -->
    <importfile
      :templateId="templateId"
      :url="url"
      :accept="accept"
      @successUpload="successUpload"
    ></importfile>
    <JFX-title title="商品详情" className="mr-t-30" />
    <!-- 表格 -->
    <JFX-table
      :tableData.sync="tableData"
      :tableColumn="tableColumn"
      :showPagination="false"
      showIndex
    >
      <template slot="type" slot-scope="{ row }">
        {{ { '0': '好品', '1': '坏品' }[row.type] }}
      </template>
      <template slot="unit" slot-scope="{ row }">
        {{ { '0': '托盘', '1': '箱', '2': '件' }[row.unit] }}
      </template>
    </JFX-table>
    <!-- 表格 end -->
    <div class="flex-c-c mr-t-20 mr-b-10">
      <el-button type="primary" @click="comfirm">确认并保存</el-button>
      <el-button @click="closeTag">取消</el-button>
    </div>
  </div>
</template>
<script>
  import { getBaseUrl } from '@u/tool'
  import commomMix from '@m/index'
  import { saveInitInventory } from '@a/stockMange'
  export default {
    mixins: [commomMix],
    components: {
      importfile: () => import('@/components/importfile/index')
    },
    data() {
      return {
        breadcrumb: [
          {
            path: '',
            meta: { title: '上传文件' }
          }
        ],
        url: '',
        tips: '',
        loading: false,
        tableData: {
          list: [],
          loading: false,
          pageNum: 1,
          pageSize: 100000,
          total: 100001
        },
        failure: 0,
        success: 0,
        templateId: '',
        accept:
          '.csv, application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        // 表格列结构
        tableColumn: [
          {
            label: '仓库编码',
            prop: 'depotCode',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '事业部',
            prop: 'buName',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '商品货号',
            prop: 'goodsNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '库存类型',
            slotTo: 'type',
            minWidth: '150',
            align: 'center',
            hide: true
          },
          {
            label: '库存数量',
            prop: 'initNum',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '批次号',
            prop: 'batchNo',
            minWidth: '120',
            align: 'center',
            hide: true
          },
          {
            label: '生产日期',
            prop: 'productionDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '有效期至',
            prop: 'overdueDate',
            width: '150',
            align: 'center',
            hide: true
          },
          {
            label: '理货单位',
            slotTo: 'unit',
            width: '90',
            align: 'center',
            hide: true
          }
        ]
      }
    },
    mounted() {
      /**
       * @description 参数解析
       * @param {String} url 上传地址 如'/app/upload'  必填
       * @param {String} breadcrumb 当前位置 如'采购SD列表,调整SD导入'
       * @param {String} templateId 下载模板的id
       */
      const { url, breadcrumb, templateId, accept } = this.$route.query
      this.url = url ? getBaseUrl(url) + url + '' : ''
      this.templateId = templateId || ''
      // 设置当前位置
      if (breadcrumb) {
        this.breadcrumb = []
        const array = breadcrumb.split(',')
        const breadcrumbArray = []
        array.map((item) => {
          breadcrumbArray.push({
            path: '',
            meta: { title: item }
          })
        })
        this.breadcrumb = breadcrumbArray
      }
      // 类型拦截
      if (accept) {
        this.accept = accept
      }
    },
    methods: {
      async comfirm() {
        if (!this.tableData.list.length) {
          this.$errorMsg('当前没有数据可以提交！')
          return false
        }
        try {
          const {
            data: {
              initFailure = 0,
              initSuccess = 0,
              innventoryFailure = 0,
              innventorySuccess = 0,
              updteFailure = 0,
              updteSuccess = 0
            }
          } = await saveInitInventory({ list: this.tableData.list })
          let msg = ''
          if (initFailure === 0) {
            msg += `期初新增成功${initSuccess}条;\n`
          } else {
            msg += `期初新增成功${initSuccess}条,失败${initFailure}条;\n`
          }
          if (innventoryFailure === 0) {
            msg += `库存新增成功${innventorySuccess}条;\n`
          } else {
            msg += `库存新增成功${innventorySuccess}条,失败${innventoryFailure}条;\n`
          }
          if (updteFailure === 0) {
            msg += `库存修改成功${updteSuccess}条;\n`
          } else {
            msg += `库存修改成功${updteSuccess}条,失败${updteFailure}条;\n`
          }
          this.$successMsg(msg)
          this.closeTag()
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      successUpload({ data = {} }) {
        const { list } = data
        if (list && list.length) {
          this.tableData.list = list || []
        }
      }
    }
  }
</script>
