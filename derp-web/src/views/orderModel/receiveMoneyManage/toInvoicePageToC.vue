<template>
  <div class="page-bx bgc-w">
    <JFX-Breadcrumb :showGoback="true" />
    <div
      class="box"
      v-html="detail"
      ref="detail"
      v-loading="detailLoading"
    ></div>
    <div class="mr-t-30 flex-c-c">
      <el-button @click="handleSubmit" type="primary" :loading="saveLoading">
        确认开票
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
  </div>
</template>
<script>
  import {
    toInvoicePageToC,
    saveContractToC
  } from '@a/receiveMoneyManage/index'
  import commomMix from '@m/index'
  import dayjs from 'dayjs'
  export default {
    mixins: [commomMix],
    data() {
      return { detail: '', detailLoading: true, saveLoading: false }
    },
    async mounted() {
      await this.loadScript(
        'https://cdn.jsdelivr.net/npm/layui-laydate@5.3.1/src/laydate.min.js'
      )
      console.log(window.laydate)
      /**
       * tempId 模板id
       * ids 账单id
       * invoiceStatus  1 为应收 2为平台结算单
       */
      const { tempId, ids } = this.$route.query
      let data = null
      // 应收开票
      data = await toInvoicePageToC({ fileTempId: tempId, ids })
      this.detail = data.data
      // dom渲染之后
      this.$nextTick(() => {
        this.loadTimeDom()
        this.detailLoading = false
      })
    },
    methods: {
      // 加载script
      loadScript(url) {
        return new Promise((resolve, reject) => {
          var script = document.createElement('script')
          script.type = 'text/javascript'
          if (script.readyState) {
            script.onreadystatechange = function () {
              if (
                script.readyState === 'loaded' ||
                script.readyState === 'complete'
              ) {
                script.onreadystatechange = null
                resolve('success')
              }
            }
          } else {
            // 其他浏览器
            script.onload = function () {
              resolve('success')
            }
            script.onerror = function () {
              reject(new Error())
            }
          }
          script.src = url
          document.getElementsByTagName('head')[0].appendChild(script)
        })
      },
      // 时间输入框渲染时间组件
      loadTimeDom() {
        const wrapDom = this.$refs.detail
        const timeInputList = wrapDom.querySelectorAll(
          'input[data-dom-type="time"]'
        )
        for (let i = 0; i < timeInputList.length; i++) {
          const timeItem = timeInputList[i]
          const timeValue = timeItem.value || dayjs().format('YYYY/MM/DD')
          if (timeItem.getAttribute('readonly') === '') {
            timeItem.value = timeValue
            continue
          }
          window.laydate.render({
            elem: timeItem,
            type: 'date',
            format: 'yyyy/MM/dd',
            value: timeValue,
            done: function (value, date, endDate) {
              console.log(value) // 得到日期生成的值，如：2017-08-18
              // 需要同步输入框
              if (timeItem.getAttribute('data-copy') !== '1') {
                return
              }
              wrapDom.querySelector(
                `[name="copy_${timeItem.getAttribute('name')}"]`
              ).value = value
            }
          })
        }
      },
      // 提交
      async handleSubmit() {
        try {
          this.saveLoading = true
          const wrapDom = this.$refs.detail
          const { tempId, ids, codes, invoiceStatus } = this.$route.query
          const submitJson = {
            token: '',
            codes: '',
            ids: '',
            fileTempId: '',
            fileTempCode: '',
            currency: '',
            customerId: '',
            customerName: '',
            billMonth: '',
            totalAllAmount: '',
            merchantName: '',
            depositBank: '',
            bankAccount: '',
            swiftCode: '',
            invoiceNo: '',
            invoiceStatus: '',
            code: '',
            message: '',
            expMessage: '',
            data: '',
            merchantInvoiceName: '',
            merchantEnglishName: ''
          }
          // 从页面中的name当中拿数据
          for (const key in submitJson) {
            const name = key
            const inputDom = wrapDom.querySelector(`input[name="${name}"]`)
            const value = inputDom ? inputDom.value : ''
            submitJson[name] = value || ''
          }
          submitJson.ids = ids
          submitJson.codes = codes
          const token = sessionStorage.getItem('token')
          submitJson.token = token
          submitJson.fileTempId = tempId
          const itemList = []
          const itemListJson = {
            goodsName: '',
            billDate: '',
            totalNum: '',
            totalAmount: ''
          }

          Array.from(
            wrapDom.querySelectorAll('input[data-dom-type="goodsItem"]')
          ).map((goodsNameInputdom) => {
            const trDom = goodsNameInputdom.parentElement.parentElement
            const item = {}
            for (const key in itemListJson) {
              const keyInptdom = trDom.querySelector(`input[name="${key}"]`)
              const keyInpVal = keyInptdom ? keyInptdom.value : ''
              item[key] = keyInpVal || ''
            }
            itemList.push(item)
          })
          submitJson.goodsList = itemList
          submitJson.invoiceStatus = invoiceStatus
          // 最终结果
          console.log(submitJson)
          await saveContractToC({
            ...submitJson
          })
          this.$successMsg('保存成功')
          this.closeTag()
        } catch (error) {
        } finally {
          this.saveLoading = false
        }
      }
    }
  }
</script>

<style scoped>
  .box {
    min-height: 80vh;
  }
</style>
