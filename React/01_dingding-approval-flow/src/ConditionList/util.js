import { toLower } from 'lodash';

const EQUAL = {
  label: '等于',
  value: 'equal'
}

const NOT_EQUAL = {
  label: '不等于',
  value: 'not_equal'
}

const ANY = {
  label: '等于任意一个',
  value: 'any'
}

const NOT_ANY = {
  label: '不等于任意一个',
  value: 'not_any'
}

const INCLUDES = {
  label: '包含',
  value: 'includes'
}

const NOT_INCLUDES = {
  label: '不包含',
  value: 'not_includes'
}

const LESS = {
  label: '小于',
  value: 'less'
}

const LESS_EQUAL = {
  label: '小于等于',
  value: 'less_equal'
}

const GREATER = {
  label: '大于',
  value: 'greater'
}
const GREATER_EQUAL = {
  label: '大于等于',
  value: 'greater_equal'
}

export const findOperatorByType = (type) => {
  switch (toLower(type)) {
    case 'boolean':
      return [EQUAL, NOT_EQUAL];
      break;
    case 'string':
      return [EQUAL, NOT_EQUAL, ANY, NOT_ANY, INCLUDES, NOT_INCLUDES, LESS, LESS_EQUAL,GREATER, GREATER_EQUAL];
      break;
    case 'object':
    case 'array':
      return [EQUAL, NOT_EQUAL, ANY, NOT_ANY, INCLUDES, NOT_INCLUDES];
      break;
    default:
      return [];
      break;
  }
}