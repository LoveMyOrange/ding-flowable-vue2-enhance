/**
 *  rule由key和配置数组组成
 *  如：'key:[0, 'always', 72]'
 *    数组中第一位为level，可选0,1,2，0为disable，1为warning，2为error，
 *         第二位为是否应用，可选always|never，
 *         第三位该rule的值
 *
 * 提交信息可自定义规范
 *  feat：新功能（feature）
    fix：修补bug
    docs：文档（documentation）
    style： 格式（不影响代码运行的变动）
    refactor：重构（即不是新增功能，也不是修改bug的代码变动）
    test：增加测试
    chore：构建过程或辅助工具的变动
 * **/
module.exports = {
  ignores: [(commit) => commit.includes('init')],
  extends: ['@commitlint/config-conventional'],
  rules: {
    'body-leading-blank': [2, 'always'],
    'footer-leading-blank': [1, 'always'],
    'header-max-length': [2, 'always', 108],
    'subject-empty': [2, 'never'],
    'type-empty': [2, 'never'],
    'subject-case': [0],
    'type-enum': [2, 'always', ['feat', 'fix', 'perf', 'style', 'docs', 'test', 'refactor', 'build', 'ci', 'chore', 'revert', 'wip', 'workflow', 'types', 'release']],
  },
};
