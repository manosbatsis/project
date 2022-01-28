<template>
  <JFX-Dialog
    title="物流委托书"
    :width="'1000px'"
    :top="'20vh'"
    :showClose="true"
    :visible.sync="visible"
    @comfirm="comfirm"
    class="edit-bx"
  >
    <JFX-Form :model="ruleForm" ref="ruleForm">
      <el-row :gutter="10" class="fs-12 clr-II">
        <el-col class="mr-b-10" :span="24">
          <el-form-item label="选择模板：" prop="logisticsAttorneyModel">
            <el-select
              v-model="ruleForm.logisticsAttorneyModel"
              style="width: 220px"
              placeholder="请选择"
              filterable
              clearable
              @change="changeTemplateLink"
            >
              <el-option
                v-for="item in tempalteList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-10" :span="12">
          <div class="flex-bx">
            <span class="fs-14 clr-II">发货人/提货信息：</span>
            <el-select
              v-model="ruleForm.shipperTempId"
              style="width: 220px"
              placeholder="请选择"
              filterable
              clearable
              @change="changeTemplateItem('shipperTemp')"
            >
              <el-option
                v-for="item in shipperTempList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <div class="flex-bx mr-t-5">
            <el-input
              v-model="ruleForm.shipperTempDetails"
              type="textarea"
              :rows="6"
              clearable
              style="width: 430px"
              @change="detailsChange('shipperTempDetails', 'shipperTempId')"
            ></el-input>
          </div>
        </el-col>
        <el-col class="mr-b-10" :span="12">
          <div class="flex-bx">
            <span class="fs-14 clr-II">收货人/卸货信息：</span>
            <el-select
              v-model="ruleForm.consigneeTempId"
              style="width: 220px"
              placeholder="请选择"
              filterable
              clearable
              @change="changeTemplateItem('consigneeTemp')"
            >
              <el-option
                v-for="item in consigneeTempList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <div class="flex-bx mr-t-5">
            <el-input
              v-model="ruleForm.consigneeTempDetails"
              type="textarea"
              :rows="6"
              clearable
              style="width: 430px"
              @change="detailsChange('consigneeTempDetails', 'consigneeTempId')"
            ></el-input>
          </div>
        </el-col>
        <el-col class="mr-b-10" :span="12">
          <div class="flex-bx">
            <span class="fs-14 clr-II">通知人：</span>
            <el-select
              v-model="ruleForm.notifierTempId"
              style="width: 220px"
              placeholder="请选择"
              filterable
              clearable
              @change="changeTemplateItem('notifierTemp')"
            >
              <el-option
                v-for="item in notifierTempList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <div class="flex-bx mr-t-5">
            <el-input
              v-model="ruleForm.notifierTempDetails"
              type="textarea"
              :rows="6"
              clearable
              style="width: 430px"
              @change="detailsChange('notifierTempDetails', 'notifierTempId')"
            ></el-input>
          </div>
        </el-col>
        <el-col class="mr-b-10" :span="12">
          <div class="flex-bx">
            <span class="fs-14 clr-II">对接人：</span>
            <el-select
              v-model="ruleForm.dockingTempId"
              style="width: 220px"
              placeholder="请选择"
              filterable
              clearable
              @change="changeTemplateItem('dockingTemp')"
            >
              <el-option
                v-for="item in dockingTempList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <div class="flex-bx mr-t-5">
            <el-input
              v-model="ruleForm.dockingTempDetails"
              type="textarea"
              :rows="6"
              clearable
              style="width: 430px"
              @change="detailsChange('dockingTempDetails', 'dockingTempId')"
            ></el-input>
          </div>
        </el-col>
        <el-col class="mr-b-10" :span="12">
          <div class="flex-bx">
            <span class="fs-14 clr-II">承运商信息：</span>
            <el-select
              v-model="ruleForm.carrierInformationTempId"
              style="width: 220px"
              placeholder="请选择"
              filterable
              clearable
              @change="changeTemplateItem('carrierInformationTemp')"
            >
              <el-option
                v-for="item in carrierInformationTempList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <div class="flex-bx mr-t-5">
            <el-input
              v-model="ruleForm.carrierInformationTempDetails"
              type="textarea"
              :rows="6"
              clearable
              style="width: 430px"
              @change="
                detailsChange(
                  'carrierInformationTempDetails',
                  'carrierInformationTempId'
                )
              "
            ></el-input>
          </div>
        </el-col>
      </el-row>
    </JFX-Form>
  </JFX-Dialog>
</template>
<script>
  import commomMix from '@m/index'
  import { listTemplateLink, listTemplate } from '@a/purchaseManage/index'
  import { modifyLogisticsAttorney } from '@a/salesManage/index'
  export default {
    mixins: [commomMix],
    props: {
      visible: {
        type: Object,
        default: function () {
          return {
            show: false
          }
        }
      },
      detail: {
        type: Object,
        default() {
          return {}
        }
      }
    },
    data() {
      return {
        ruleForm: {
          logisticsAttorneyModel: '',
          shipperTempId: '',
          shipperTempDetails: '',
          consigneeTempId: '',
          consigneeTempDetails: '',
          notifierTempId: '',
          notifierTempDetails: '',
          dockingTempId: '',
          dockingTempDetails: '',
          carrierInformationTempId: '',
          carrierInformationTempDetails: ''
        },
        tempalteList: [],
        shipperTempList: [],
        consigneeTempList: [],
        notifierTempList: [],
        dockingTempList: [],
        carrierInformationTempList: []
      }
    },
    mounted() {
      this.init()
      // 获取模板列表
      this.getTemplateList()
    },
    methods: {
      init() {
        const keyList = [
          'shipperTempId',
          'shipperTempDetails',
          'consigneeTempId',
          'consigneeTempDetails',
          'notifierTempId',
          'notifierTempDetails',
          'dockingTempId',
          'dockingTempDetails',
          'carrierInformationTempId',
          'carrierInformationTempDetails'
        ]
        keyList.forEach((key) => {
          this.ruleForm[key] = this.detail[key] ? this.detail[key] + '' : ''
        })
      },
      // 点击保存
      async comfirm() {
        try {
          const {
            shipperTempId,
            shipperTempDetails,
            consigneeTempId,
            consigneeTempDetails,
            notifierTempId,
            notifierTempDetails,
            dockingTempId,
            dockingTempDetails,
            carrierInformationTempId,
            carrierInformationTempDetails
          } = this.ruleForm
          const submitOpt = {
            shipperTempId,
            shipperTempName: shipperTempId
              ? this.shipperTempList.find((item) => item.id === shipperTempId).name
              : '',
            shipperText: shipperTempDetails,
            consigneeTempId,
            consigneeTempName: consigneeTempId
              ? this.consigneeTempList.find(
                  (item) => item.id === consigneeTempId
                ).name
              : '',
            consigneeText: consigneeTempDetails,
            notifierTempId,
            notifierTempName: notifierTempId
              ? this.notifierTempList.find(
                  (item) => item.id === notifierTempId
                ).name
              : '',
            notifierText: notifierTempDetails,
            dockingTempId,
            dockingTempName: dockingTempId
              ? this.dockingTempList.find((item) => item.id === dockingTempId).name
              : '',
            dockingText: dockingTempDetails,
            carrierInformationTempId,
            carrierInformationTempName: carrierInformationTempId
              ? this.carrierInformationTempList.find(
                  (item) => item.id === carrierInformationTempId
                ).name
              : '',
            carrierInformationText: carrierInformationTempDetails
          }
          submitOpt.orderId = this.detail.id
          await modifyLogisticsAttorney(submitOpt)
          this.$successMsg('操作成功')
          this.$emit('close')
        } catch (err) {
          console.log(err)
        }
      },
      // 获取模板列表
      async getTemplateList() {
        try {
          const { data: tempalteList } = await listTemplateLink()
          this.tempalteList = tempalteList || []
          const { data: typeList } = await listTemplate({ type: '' })
          if (typeList && typeList.length) {
            const list = typeList.map((item) => ({
              ...item,
              id: item.id.toString()
            }))
            this.shipperTempList = list.filter((item) => item.type === '1')
            this.consigneeTempList = list.filter((item) => item.type === '2')
            this.notifierTempList = list.filter((item) => item.type === '3')
            this.dockingTempList = list.filter((item) => item.type === '4')
            this.carrierInformationTempList = list.filter(
              (item) => item.type === '5'
            )
          }
        } catch (error) {
          this.$errorMsg(error.message)
        }
      },
      // 选择模板
      changeTemplateLink() {
        const { logisticsAttorneyModel } = this.ruleForm
        const target =
          this.tempalteList.find(
            (item) => +item.id === logisticsAttorneyModel
          ) || {}
        this.ruleForm.logisticsAttorneyModel = target.name || ''
        const keyArr = [
          'shipperTemp',
          'consigneeTemp',
          'notifierTemp',
          'dockingTemp',
          'carrierInformationTemp'
        ]
        keyArr.forEach((item) => {
          const key1 = item + 'Id'
          this.ruleForm[key1] = target[key1] ? target[key1].toString() : ''
          this.changeTemplateItem(item)
        })
      },
      changeTemplateItem(key) {
        const id = this.ruleForm[key + 'Id']
        const target = this[key + 'List'].find((item) => +item.id === +id) || {}
        this.ruleForm[key + 'Name'] = target.name || ''
        this.ruleForm[key + 'Details'] = target.details || ''
      },
      detailsChange(detailsKey, detailsSelectId) {
        const content = this.ruleForm[detailsKey]
        if (!content) {
          this.ruleForm[detailsSelectId] = ''
        }
      }
    }
  }
</script>
<style scoped lang="scss"></style>
