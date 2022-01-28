<template>
  <el-form v-bind="$attrs" v-on="$listeners" ref="ruleForm">
    <slot />
  </el-form>
</template>

<script>
  export default {
    methods: {
      validate(cb) {
        this.$refs.ruleForm.validate(async (valid, errorList) => {
          if (!valid) {
            await this.$nextTick()
            const target = this.$parent.$el.querySelectorAll('.is-error')[0]
            target.scrollIntoView({ behavior: 'smooth' })
          }
          cb.call(this.$parent, valid, errorList)
        })
      },
      validateField(...args) {
        this.$refs.ruleForm.validateField(...args)
      },
      resetFields(...args) {
        this.$refs.ruleForm.resetFields(...args)
      },
      clearValidate(...args) {
        this.$refs.ruleForm.clearValidate(...args)
      }
    }
  }
</script>
