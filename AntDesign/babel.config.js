
const IS_PROD = ['production', 'prod'].includes(process.env.NODE_ENV)

const plugins = []
if (IS_PROD) {
  // 用babel-plugin-transform-remove-console插件实现在开发环境可以用console在生产环境去掉
  plugins.push('transform-remove-console')
}
// 这个插件主要作用是用来编译类的
plugins.push('@babel/plugin-proposal-class-properties');
// lazy load ant-design-vue
// if your use import on Demand, Use this code
plugins.push([
  'import',
  {
    libraryName: 'ant-design-vue',
    libraryDirectory: 'es',
    style: true, // `style: true` 会加载 less 文件
  },
]);

module.exports = {
  presets: [
    '@vue/app',
    [
      '@babel/preset-env',
      {
        useBuiltIns: 'entry',
        corejs: 3,
      },
    ],
  ],
  // 按需加载
  plugins,
};
