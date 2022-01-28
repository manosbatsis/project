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
    toInvoicePage,
    saveContract,
    platformToInvoicePage
  } from '@a/receiveMoneyManage/index'
  import commomMix from '@m/index'
  import dayjs from 'dayjs'
  import { Decimal } from 'decimal.js'
  const numberAdd = function (num1, num2) {
    return new Decimal(num1).add(new Decimal(num2)).toNumber()
  }
  const numberMul = function (num1, num2) {
    return new Decimal(num1).mul(new Decimal(num2)).toNumber()
  }
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
       * isWp 是否是唯品模板
       * isDouble 是否是 一个模板内部两个相同的发票
       * invoiceStatus  1 为应收 2为平台结算单
       */
      const { tempId, ids, isWp, isDouble, invoiceStatus } = this.$route.query
      let data = null
      if (invoiceStatus === '1') {
        // 应收开票
        data = await toInvoicePage({ tempId, ids })
      } else if (invoiceStatus === '2') {
        // 平台结算单开票
        data = await platformToInvoicePage({ tempId, ids, invoiceStatus })
      }
      this.detail = data.data
      // dom渲染之后
      this.$nextTick(() => {
        this.loadTimeDom()
        // 唯品的模板才有这个联动
        if (isWp) {
          this.setAmountDisabled()
          this.setAmountCalcEvent()
        }
        if (isDouble) {
          this.doubleEvent()
        }
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
      // 千分位两位小数
      formatThousands(number) {
        return Number(number)
          .toFixed(2)
          .replace(/(\d)(?=(\d{3})+\.)/g, '$1,')
      },
      // toUrl === weipin  唯品模板 合计值不能编辑
      setAmountDisabled() {
        const wrapDom = this.$refs.detail
        // input name 为 totalPrice 的禁止编辑
        wrapDom.querySelectorAll('input[name="totalPrice"]').forEach((item) => {
          item.setAttribute('disabled', true)
        })
        // 总合计不能编辑
        wrapDom
          .querySelector('input[name="totalAllAmount"]')
          .setAttribute('disabled', true)
        const totalAllAmountTr = wrapDom.querySelector(
          'input[name="totalAllAmount"]'
        ).parentElement.parentElement.previousSibling
        totalAllAmountTr.style.backgroundColor = '#eee'
        totalAllAmountTr.querySelectorAll('input').forEach((iInput) => {
          iInput.setAttribute('disabled', true)
        })
        // 合计行不能编辑
        wrapDom.querySelectorAll('input[name="goodsName"]').forEach((item) => {
          // 商品名字中有合计两个字，不能编辑
          if (item.value.includes('合计')) {
            item.parentElement.parentElement.style.backgroundColor = '#eee'
            item.parentElement.parentElement
              .querySelectorAll('input')
              .forEach((iInput) => {
                iInput.setAttribute('disabled', true)
              })
          }
        })
      },
      // toUrl === weipin  唯品模板 获取表格中的value
      getValue(TrDom, name) {
        const input = TrDom.querySelector(`input[name="${name}"]`)
        if (!input) {
          return 0
        }
        let value = input.value
        if ([null, undefined].includes(value)) {
          return 0
        }
        value = String(value).replaceAll(',', '')
        return isNaN(Number(value)) ? 0 : Number(value)
      },
      // toUrl === weipin 唯品模板 设置表格中的value
      setValue(Trdom, name, value) {
        Trdom.querySelector(`input[name="${name}"]`).value =
          this.formatThousands(value)
      },
      // toUrl === weipin 唯品模板 设置合计计算事件
      setAmountCalcEvent() {
        const wrapDom = this.$refs.detail
        const getValue = this.getValue
        const setValue = this.setValue
        const formatThousands = this.formatThousands
        wrapDom.addEventListener('change', function (ev) {
          ev = ev || window.event // 兼容ie
          const target = ev.target || ev.srcElement // 表格当行行tr
          // 不是联动的退出该方法
          if (target.getAttribute('data-dom-type') !== 'calc') {
            return
          }
          const trTarget = target.parentElement.parentElement
          const targetName = target.getAttribute('name')
          const NameTotalNum = 'totalNum'
          const NamePrice = 'price'
          const NameTotalPrice = 'totalPrice'
          const NameList = [
            'customerReturn',
            'promotionDiscountsCust',
            'promotionDiscounts',
            'extraDiscount',
            'extraAmount'
          ]
          /****
           * 计算当行总和 total * price - nameList总和
           *  */
          const trGetValue = getValue.bind(this, trTarget)
          const nameTotal = NameList.reduce((pre, cur) => {
            pre = numberAdd(pre, trGetValue(cur))
            return pre
          }, 0)
          const trTotal = numberAdd(
            numberMul(trGetValue(NameTotalNum), trGetValue(NamePrice)),
            nameTotal
          )

          setValue(trTarget, targetName, trGetValue(targetName))
          setValue(trTarget, NameTotalPrice, trTotal)
          /****
           * 计算商品总和(几行) 上界：goodsName含有合计 下界：goodsnName含有合计 合计行：下界
           *  */
          const goodsTrList = [trTarget]
          let goodsSumarryTr = null
          let curTr = trTarget
          // 上界
          while (
            curTr.previousSibling.querySelector('[name="goodsName"]') &&
            !curTr.previousSibling
              .querySelector('[name="goodsName"]')
              .value.includes('合计')
          ) {
            goodsTrList.unshift(curTr.previousSibling)
            curTr = curTr.previousSibling
          }
          curTr = trTarget
          // 下界
          while (
            !curTr.nextSibling
              .querySelector('[name="goodsName"]')
              .value.includes('合计')
          ) {
            goodsTrList.push(curTr.nextSibling)
            curTr = curTr.nextSibling
          }
          // 商品合计行
          goodsSumarryTr = curTr.nextSibling
          setValue(
            goodsSumarryTr,
            targetName,
            goodsTrList.reduce((total, tr) => {
              total = numberAdd(total, getValue(tr, targetName))
              return total
            }, 0)
          )
          setValue(
            goodsSumarryTr,
            NameTotalPrice,
            goodsTrList.reduce((total, tr) => {
              total = numberAdd(total, getValue(tr, NameTotalPrice))
              return total
            }, 0)
          )
          /****
           * 总total计算
           *  */
          function totalCalc(clacName, calcCountName) {
            const calcCountInput = wrapDom.querySelector(
              `input[name="${calcCountName}"]`
            )
            const calcInputList = wrapDom.querySelectorAll(
              `input[name="${clacName}"]`
            )
            if (!calcCountInput) {
              return
            }
            // 计算总值
            const totalAllAmount = Array.from(calcInputList).reduce(
              (total, input) => {
                if (
                  input.parentElement.parentElement
                    .querySelector('[name="goodsName"]')
                    .value.includes('合计')
                ) {
                  return total
                }
                total = numberAdd(
                  total,
                  Number(input.value.replaceAll(',', ''))
                )
                return total
              },
              0
            )
            // 合计设置值
            calcCountInput.value = formatThousands(totalAllAmount)
            // 合计总的显示在td内
            if (calcCountName === 'totalAllAmount') {
              Array.from(
                wrapDom.querySelector('input[name="totalAllAmount"]')
                  .parentElement.parentElement.previousSibling.children
              ).pop().innerHTML = formatThousands(totalAllAmount)
            }
          }
          totalCalc('customerReturn', 'customerReturnAll')
          totalCalc('promotionDiscountsCust', 'promotionDiscountsCustAll')
          totalCalc('promotionDiscounts', 'promotionDiscountsAll')
          totalCalc('extraDiscount', 'extraDiscountAll')
          totalCalc('extraAmount', 'extraAmountAll')
          totalCalc('totalPrice', 'totalAllAmount')
        })
      },
      // toUrl === isDouble 处理
      doubleEvent() {
        const wrapDom = this.$refs.detail
        wrapDom.addEventListener('change', function (ev) {
          ev = ev || window.event // 兼容ie
          const target = ev.target || ev.srcElement // 当前输入款
          // 不是需要同步处理到另外一个输入款的跳出
          if (target.getAttribute('data-copy') !== '1') {
            return
          }
          const value = target.value
          const name = target.getAttribute('name')
          const goodsIndex = target.getAttribute('data-index')
          Array.from(wrapDom.querySelectorAll(`[name="copy_${name}"]`)).forEach(
            (item) => {
              // 只有对应行才操作
              if (goodsIndex) {
                item.getAttribute('data-index') === goodsIndex &&
                  (item.value = value)
              } else {
                item.value = value
              }
            }
          )
        })
      },
      // 提交
      async handleSubmit() {
        try {
          this.saveLoading = true
          const wrapDom = this.$refs.detail
          const { isWp, tempId, ids, codes, invoiceStatus } = this.$route.query
          const submitJson = {
            bankAccount: '',
            bankAddress: '',
            beneficiaryName: '',
            companyAddr: '',
            currency: '',
            customerEnName: '',
            customerId: 0,
            customerName: '',
            depositBank: '',
            englishRegisteredAddress: '',
            fileTempCode: '',
            fileTempId: 0,
            invoiceDate: '',
            invoiceNo: '',
            invoiceStatus: '',
            merchantId: 0,
            merchantInvoiceName: '',
            poNos: '',
            remarks: '',
            swiftCode: '',
            totalAllAmount: '',
            totalAllNum: '',
            codes: '',
            ids: '',
            merchantEnglishName: '',
            customerReturnAll: '',
            promotionDiscountsCustAll: '',
            promotionDiscountsAll: '',
            extraDiscountAll: '',
            extraAmountAll: '',
            originCity: '',
            destinationCity: '',
            description: '',
            oTelNo: '',
            date: '',
            taxNo: '',
            cusDepositBank: '',
            cusBankAccount: '',
            relCodes: '',
            billDate: '',
            buyerInvoiceNo: '',
            paymentEN: '',
            paymentCN: '',
            incotermsEN: '',
            incotermsCN: '',
            placeCN: '',
            fContactPerson: '',
            tContactPerson: '',
            enBusinessAddress: '',
            customerCode: ''
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
          const wpItemList = []
          const wpItemListJson = {
            brandName: '',
            customerReturn: '',
            extraAmount: '',
            extraDiscount: '',
            goodsName: '',
            price: '',
            promotionDiscounts: '',
            promotionDiscountsCust: '',
            totalNum: '',
            totalPrice: ''
          }
          const itemList = []
          const itemListJson = {
            barcode: '',
            brandName: '',
            goodsId: '',
            goodsName: '',
            goodsNo: '',
            index: '',
            invoiceDescription: '',
            parentBrandId: '',
            platformSku: '',
            poNo: '',
            price: '',
            projectId: '',
            totalNum: '',
            totalPrice: '',
            unit: ''
          }
          const token = sessionStorage.getItem('token')
          submitJson.token = token
          // 唯品的发票，取值情况不一致
          if (isWp) {
            Array.from(wrapDom.querySelectorAll('input[name="goodsName"]')).map(
              (goodsNameInputdom) => {
                const trDom = goodsNameInputdom.parentElement.parentElement
                const item = {}
                for (const key in wpItemListJson) {
                  const keyInptdom = trDom.querySelector(`input[name="${key}"]`)
                  const keyInpVal = keyInptdom ? keyInptdom.value : ''
                  item[key] = keyInpVal || ''
                }
                wpItemList.push(item)
              }
            )
            submitJson.wpItemList = wpItemList
          } else {
            Array.from(wrapDom.querySelectorAll('input[name="goodsName"]')).map(
              (goodsNameInputdom) => {
                const trDom = goodsNameInputdom.parentElement.parentElement
                const item = {}
                for (const key in itemListJson) {
                  const keyInptdom = trDom.querySelector(`input[name="${key}"]`)
                  const keyInpVal = keyInptdom ? keyInptdom.value : ''
                  item[key] = keyInpVal || ''
                }
                itemList.push(item)
              }
            )
            submitJson.itemList = itemList
          }
          submitJson.invoiceStatus = invoiceStatus
          submitJson.fileTempId = tempId
          // 最终结果
          console.log(submitJson, itemListJson)
          await saveContract({
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
