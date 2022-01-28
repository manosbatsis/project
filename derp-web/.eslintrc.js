module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: ['plugin:vue/essential', '@vue/standard'],
  parserOptions: {
    parser: 'babel-eslint'
  },
  rules: {
    indent: ['off', 2],
    'space-before-function-paren': [0, 'never'],
    // allow async-await
    'generator-star-spacing': 'off',
    'no-tabs': 'off',
    'no-case-declarations': 'off',
    'standard/computed-property-even-spacing': 'off',
    'vue/require-prop-type-constructor': 'off',
    'no-console': 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'off' : 'off'
  }
}
