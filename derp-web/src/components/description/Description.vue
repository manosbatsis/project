<template>
  <div>
    <JFX-title :title="title" className="mr-t-10" />
    <el-row :gutter="16" class="fs-12 clr-II detail-row">
      <el-col
        class="mr-b-10"
        v-for="item in schema"
        v-bind="column"
        :key="item.field"
      >
        {{ item.label }}：{{
          [undefined, null, ''].includes(data[item.field])
            ? '- -'
            : data[item.field]
        }}
      </el-col>
    </el-row>
  </div>
</template>

<script>
  /*
    详情组件
    用例：<JFX-Description title="基本信息" :data="detail" :schema="baseSchema" />
  */
  export default {
    props: {
      title: {
        type: String,
        default: ''
      },
      /* 渲染的数据 */
      data: {
        type: Object,
        default: () => {}
      },
      /*
        格式：Array<{ label: string, field: string }>
        label：显示的中文名
        field：需要渲染的字段，对应data的key值
      */
      schema: {
        type: Array,
        default: () => []
      },
      column: {
        type: Object,
        default: () => ({
          xs: 24,
          sm: 12,
          md: 8,
          lg: 6,
          xl: 6
        })
      }
    }
  }
</script>
