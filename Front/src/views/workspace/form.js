import * as moment from "moment";

/**
 * @description 将表单项树结构转换为扁平结构
 * @param formItemTree {Array}
 * @return Array
 */
export function flatFormItem(formItemTree) {
  return formItemTree.reduce((result, item) => {
    if (item.name === "SpanLayout") {
      return result.concat(flatFormItem(item.props.items));
    } else {
      return result.concat(item);
    }
  }, []);
}

export function formatTime(row, column) {
  if (row[column.property] === null) {
    return "";
  }
  return moment(row[column.property]).format("YYYY-MM-DD HH:mm:ss");
}

export function formatBusinessStatus(row, column) {
  const map = {
    "1": "正在处理",
    "2": "已撤销",
    "3": "驳回",
    "4": "办结",
  };
  return map[row[column.property]];
}
