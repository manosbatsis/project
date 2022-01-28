<template>
  <div class="page-bx bgc-w edit-bx">
    <JFX-Form :model="ruleForm" ref="ruleForm" :rules="rules">
      <!-- 面包屑 -->
      <JFX-Breadcrumb :showGoback="true" />
      <!-- 面包屑 end -->
      <!-- title -->
      <JFX-title title="基础信息" className="mr-t-10" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="商品货号：" prop="goodsNo">
            <el-input
              v-model.trim="ruleForm.goodsNo"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="商品条形码：" prop="barcode">
            <el-input
              v-model.trim="ruleForm.barcode"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="商品名称：" prop="name">
            <el-input
              v-model.trim="ruleForm.name"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="平台备案关区：" prop="customsAreaIds">
            <el-select
              v-model="ruleForm.customsAreaIds"
              style="width: 120%"
              placeholder="请选择"
              filterable
              multiple
              collapse-tags
              clearable
              :data-list="getCustomsSelectBean('guanquList')"
            >
              <el-option
                v-for="item in selectOpt.guanquList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="hs编码：" prop="hsCode">
            <el-input
              v-model.trim="ruleForm.hsCode"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="商品英文名称：" prop="englishGoodsName">
            <el-input
              v-model.trim="ruleForm.englishGoodsName"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="工厂编码：" prop="factoryNo">
            <el-input
              v-model.trim="ruleForm.factoryNo"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="商品品牌：" prop="brandId">
            <el-select
              v-model="ruleForm.brandId"
              style="width: 120%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in brandBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="一级类目：" prop="productTypeId0">
            <el-select
              v-model="ruleForm.productTypeId0"
              style="width: 120%"
              placeholder="请选择"
              filterable
              clearable
              @change="changeProductTypeId0"
            >
              <el-option
                v-for="item in oneCatBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="二级类目：" prop="productTypeId">
            <el-select
              v-model="ruleForm.productTypeId"
              style="width: 120%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in twoCatBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="关联仓库：" prop="depotIds">
            <el-input
              v-model="ruleForm.depotIds"
              style="
                width: 0;
                height: 0;
                border: 0;
                overflow: hidden;
                display: inherit;
              "
            ></el-input>
            <el-input
              readonly
              @click.native="selectDepot"
              style="width: 120%"
              v-model="ruleForm.depotNames"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <JFX-title title="备案信息" className="mr-t-10" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="商品净重(kg)：" prop="netWeight">
            <JFX-Input
              v-model="ruleForm.netWeight"
              :min="0"
              :precision="5"
              placeholder="请输入"
              clearable
              style="width: 120%"
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="商品毛重(kg)：" prop="grossWeight">
            <JFX-Input
              v-model="ruleForm.grossWeight"
              :min="0"
              :precision="5"
              placeholder="请输入"
              clearable
              style="width: 120%"
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="规格型号：" prop="spec">
            <el-input
              v-model.trim="ruleForm.spec"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="保质天数：" prop="shelfLifeDays">
            <el-input
              v-model.trim="ruleForm.shelfLifeDays"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="备案单价：" prop="filingPrice">
            <JFX-Input
              v-model="ruleForm.filingPrice"
              :min="0"
              :precision="2"
              placeholder="请输入"
              clearable
              style="width: 120%"
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="计量单位：" prop="unit">
            <el-select
              v-model="ruleForm.unit"
              style="width: 120%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in unitBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="原产国：" prop="countyId">
            <el-select
              v-model="ruleForm.countyId"
              style="width: 120%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in countryBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="原产地区：" prop="countyArea">
            <el-input
              v-model.trim="ruleForm.countyArea"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="申报要素：" prop="declareFactor">
            <el-input
              v-model.trim="ruleForm.declareFactor"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="商品成分：" prop="component">
            <el-input
              v-model.trim="ruleForm.component"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="生产企业名称：" prop="manufacturer">
            <el-input
              v-model.trim="ruleForm.manufacturer"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="生产企业地址：" prop="manufacturerAddr">
            <el-input
              v-model.trim="ruleForm.manufacturerAddr"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="第一法定单位：" prop="unitNameOne">
            <el-input
              v-model.trim="ruleForm.unitNameOne"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="第二法定单位：" prop="unitNameTwo">
            <el-input
              v-model.trim="ruleForm.unitNameTwo"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="海关商品备案号：" prop="customsGoodsRecordNo">
            <el-input
              v-model.trim="ruleForm.customsGoodsRecordNo"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <JFX-title title="箱规设置" className="mr-t-10" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          箱转换：1箱转换为
          <JFX-Input
            v-model="ruleForm.boxToUnit"
            :min="0"
            :precision="0"
            placeholder="请输入"
            clearable
            style="width: 100px"
          />
          件
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          托转换：1托转换为
          <JFX-Input
            v-model="ruleForm.torrToUnit"
            :min="0"
            :precision="0"
            placeholder="请输入"
            clearable
            style="width: 100px"
          />
          件
        </el-col>
      </el-row>
      <JFX-title title="规格描述" className="mr-t-10" />
      <el-row :gutter="10" class="fs-14 clr-II">
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="长(cm)：" prop="length">
            <JFX-Input
              v-model="ruleForm.length"
              :min="0"
              :precision="2"
              placeholder="请输入"
              clearable
              style="width: 120%"
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="宽(cm)：" prop="width">
            <JFX-Input
              v-model="ruleForm.width"
              :min="0"
              :precision="2"
              placeholder="请输入"
              clearable
              style="width: 120%"
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="高(cm)：" prop="height">
            <JFX-Input
              v-model="ruleForm.height"
              :min="0"
              :precision="2"
              placeholder="请输入"
              clearable
              style="width: 120%"
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="体积(cm³)：" prop="volume">
            <JFX-Input
              v-model="ruleForm.volume"
              :min="0"
              :precision="2"
              placeholder="请输入"
              clearable
              style="width: 120%"
            />
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="包装方式：" prop="packType">
            <el-select
              v-model="ruleForm.packType"
              style="width: 120%"
              placeholder="请选择"
              filterable
              clearable
            >
              <el-option
                v-for="item in packBean"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col class="mr-b-0" :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <el-form-item label="描述：" prop="describe">
            <el-input
              type="textarea"
              :row="1"
              v-model="ruleForm.describe"
              clearable
              style="width: 120%"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </JFX-Form>
    <div class="mr-t-30 flex-c-c">
      <el-button type="primary" :loading="saveLoading" @click="save">
        保 存
      </el-button>
      <el-button @click="closeTag">取 消</el-button>
    </div>
    <!-- 关联仓库 -->
    <RelationDepot
      v-if="relation.visible.show"
      :relationVisible.sync="relation.visible"
      :rowData="relation.filterData"
      @close="selectDepotAfter"
    />
  </div>
</template>
<script>
  import { toAddPage, saveMerchandise, getTwoLevel } from '@a/goodsManage/index'
  import commomMix from '@m/index'
  export default {
    mixins: [commomMix],
    components: {
      RelationDepot: () => import('./components/relatiionDepot')
    },
    data() {
      return {
        ruleForm: {
          goodsNo: '',
          barcode: '',
          name: '',
          customsAreaIds: '',
          englishGoodsName: '',
          factoryNo: '',
          netWeight: '',
          grossWeight: '',
          spec: '',
          shelfLifeDays: '',
          filingPrice: '',
          unit: '',
          countyId: '',
          countyArea: '',
          declareFactor: '',
          component: '',
          manufacturer: '',
          manufacturerAddr: '',
          unitNameTwo: '',
          unitNameOne: '',
          boxToUnit: '',
          torrToUnit: '',
          length: '',
          width: '',
          height: '',
          volume: '',
          packType: '',
          describe: '',
          productTypeId0: '',
          productTypeId: '',
          brandId: '',
          hsCode: '',
          customsGoodsRecordNo: '',
          depotIds: '',
          depotNames: ''
        },
        rules: {
          goodsNo: {
            required: true,
            message: '请输入商品货号',
            trigger: 'blur'
          },
          barcode: {
            required: true,
            message: '请输入商品条形码',
            trigger: 'blur'
          },
          name: { required: true, message: '请输入商品名称', trigger: 'blur' },
          countyId: {
            required: true,
            message: '原产国不能为空',
            trigger: 'change'
          },
          unit: {
            required: true,
            message: '计量单位不能为空',
            trigger: 'change'
          },
          spec: {
            required: true,
            message: '规格型不能为空',
            trigger: 'change'
          },
          brandId: {
            required: true,
            message: '商品品牌不能为空',
            trigger: 'change'
          },
          productTypeId0: {
            required: true,
            message: '一级类目不能为空',
            trigger: 'change'
          },
          productTypeId: {
            required: true,
            message: '二级类目不能为空',
            trigger: 'change'
          }
        },
        countryBean: [],
        unitBean: [],
        packBean: [],
        oneCatBean: [],
        brandBean: [],
        twoCatBean: [],
        saveLoading: false,
        relation: {
          filterData: null,
          visible: { show: false }
        }
      }
    },
    mounted() {
      this.toAddPage()
    },
    methods: {
      // 获取各种下拉数据
      async toAddPage() {
        const res = await toAddPage()
        const { countryBean, brandBean, unitBean, packBean, catBean } = res.data
        this.countryBean = countryBean || []
        this.unitBean = unitBean || []
        this.brandBean = brandBean || []
        this.packBean = packBean || []
        this.oneCatBean = catBean || []
      },
      // 选择仓库
      selectDepot() {
        this.relation.filterData = {
          isAddOrEdit: true,
          depotIds: this.ruleForm.depotIds
        }
        this.relation.visible.show = true
      },
      // 选择仓库后
      selectDepotAfter(data) {
        this.ruleForm.depotIds = data.depotIds
        this.ruleForm.depotNames = data.depotNames
        this.relation.visible.show = false
      },
      // 保存
      save() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (!valid) return false
          let customsAreaIds = ''
          if (this.ruleForm.customsAreaIds) {
            customsAreaIds = this.ruleForm.customsAreaIds
              .map((item) => item)
              .toString()
          }
          this.saveLoading = true
          try {
            await saveMerchandise({ ...this.ruleForm, customsAreaIds })
            this.$successMsg('保存成功')
            this.closeTag()
          } catch (error) {
            console.log(error)
          }
          this.saveLoading = false
        })
      },
      // 选择一级类目
      async changeProductTypeId0() {
        const { productTypeId0 } = this.ruleForm
        this.ruleForm.productTypeId = ''
        if (productTypeId0) {
          try {
            const res = await getTwoLevel({ id: productTypeId0 })
            this.twoCatBean = res.data
          } catch (error) {
            console.log(error)
          }
        }
      }
    }
  }
</script>
<style>
  .edit-bx .el-form-item__label {
    text-align: right;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 40px;
    padding: 0 12px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 130px;
  }
  .edit-bx .el-form-item--mini.el-form-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
</style>
<style lang="scss" scoped>
  .image-preview {
    display: inline-block;
  }
</style>
