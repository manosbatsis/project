<template>
  <el-select v-model="value" v-bind="$attrs" ref="selectRef">
    <el-option
      v-for="item in selectList"
      :key="item.key"
      :label="item.value"
      :value="item.key"
    >
      <div @click.stop="handleChange(item.key)">
        {{ item.value }}
      </div>
    </el-option>
  </el-select>
</template>

<script>
  export default {
    model: {
      prop: 'value',
      event: 'update:value'
    },
    props: {
      /* select绑定的value值 */
      value: {
        type: String,
        default: ''
      },
      /* 下拉列表数据 */
      selectList: {
        type: Array,
        default: () => []
      },
      /* 选择value前的校验方法 */
      beforeChange: {
        type: Function | String,
        default: ''
      }
    },
    methods: {
      async handleChange(value) {
        this.$nextTick()
        /* 有校验方法 */
        if (this.beforeChange) {
          const isPassed = await this.beforeChange(value)
          /* 校验通过才修改v-model的值和触发change事件 */
          if (isPassed) {
            this.$emit('update:value', value)
            this.$emit('change', value)
          }
          /* 收起下拉 */
          this.$refs.selectRef.blur()
          return
        }
        this.$emit('update:value', value)
        this.$emit('change', value)
        this.$refs.selectRef.blur()
      }
    }
  }
</script>
