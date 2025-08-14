// eslint-disable-next-line @typescript-eslint/no-var-requires
const path = require('path')

module.exports = {
  root: true,
  parser: 'vue-eslint-parser',
  parserOptions: {
    // Parser for <script> blocks
    parser: '@typescript-eslint/parser',
    sourceType: 'module',
    ecmaVersion: 2020,
    ecmaFeatures: {
      jsx: true,
    },
  },
  env: {
    browser: true,
    node: true,
  },
  plugins: ['@typescript-eslint'],
  extends: [
    'airbnb-base',
    'plugin:@typescript-eslint/recommended',
    'plugin:import/recommended',
    'plugin:import/typescript',
    'plugin:prettier/recommended',
  ],
  settings: {
    'import/resolver': {
      typescript: {
        project: path.resolve(__dirname, './tsconfig.json'),
      },
      node: {
        extensions: ['.js', '.jsx', '.ts', '.tsx', '.vue'],
      },
    },
  },
  rules: {
    // Prettier warning as warning (1)
    'prettier/prettier': 1,

    // Vue rules modifications
    'vue/no-reserved-component-names': 0,
    'vue/require-default-prop': 0,
    'vue/singleline-html-element-content-newline': 0,
    'vue/max-attributes-per-line': 0,
    'vue/multi-word-component-names': 0,
    'vue/require-valid-default-prop': 0,
    'vue/attribute-hyphenation': 0,

    // Typescript rules
    '@typescript-eslint/ban-ts-comment': 0,
    '@typescript-eslint/no-empty-function': 1,
    '@typescript-eslint/no-explicit-any': 0,
    '@typescript-eslint/no-unused-vars': 0,
    '@typescript-eslint/ban-types': 0,

    // Import rules
    'import/extensions': [
      2,
      'ignorePackages',
      {
        js: 'never',
        jsx: 'never',
        ts: 'never',
        tsx: 'never',
      },
    ],
    'import/no-extraneous-dependencies': 0,

    // Other
    'no-debugger': process.env.NODE_ENV === 'production' ? 2 : 0,
    'no-param-reassign': 0,
    'prefer-regex-literals': 0,
    'no-plusplus': 'off',
  },
}
