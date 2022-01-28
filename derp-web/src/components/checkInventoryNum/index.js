import { checkInventoryNum as checkInventoryNumApi } from '@a/base/index'
import Vue from 'vue'
import checkInventoryNumComponent from '@c/checkInventoryNum/checkInventoryNum'

/**
 * 库存量校验
 * @param {Object} filterData 校验库存量接口传参
 * @param {Object} dialogConfig 弹框配置
 * @param {String} dialogConfig.title  弹框标题
 * @param {String} dialogConfig.btnText  弹框确认按钮
 * @param {String} dialogConfig.cancelBtnText  弹框取消按钮
 * @returns promise 弹框关闭以及校验通过 promise finish
 */
export default function checkInventoryNum(filterData, dialogConfig = {}) {
  return new Promise((resolve, reject) => {
    console.log('校验库存量请求参数', filterData)
    // 校验库存可用量
    checkInventoryNumApi(filterData)
      .then(({ data }) => {
        console.log('校验库存量返回参数', data)
        // 数组为空，校验通过 promise finishing
        if (!data?.length) {
          resolve({ isInventoryValidate: true })
          return
        }
        //  创建组件
        const Component = Vue.extend(checkInventoryNumComponent)
        // 生成实例
        const instance = new Component().$mount()
        // props传参
        instance.data = {
          list: data
        }
        const { title, btnText, cancelBtnText } = dialogConfig
        title && (instance.title = title)
        btnText && (instance.btnText = btnText)
        cancelBtnText && (instance.cancelBtnText = cancelBtnText)

        // 关闭后销毁 promise finishing
        instance.close = function ({ isComfirm }) {
          const destoryFnList = instance?.$options?.destroyed
          if (destoryFnList?.length) {
            destoryFnList.map((fn) => () => fn())
          }
          instance.$el.remove()
          // 校验不通过
          resolve({ isInventoryValidate: false, list: data, isComfirm })
        }
        // 挂载
        document.body.appendChild(instance.$el)
        // 显示
        Vue.nextTick(() => {
          instance.isVisible = { show: true }
        })
      })
      .catch((error) => {
        console.log(error)
        reject(new Error(error.message || '系统异常'))
      })
  })
}
