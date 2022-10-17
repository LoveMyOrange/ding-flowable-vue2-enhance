import * as moment from 'moment'

export function normalizeFormData(formItems, formData) {
    for (let formItem of formItems) {
        if (formItem.name === 'UserPicker') {

        }
    }
}

/**
 *
 * @param formItemTree {Array}
 * @return Array
 */
export function flatFormItem(formItemTree) {
    const result = []
    const candidateFormItems = []
    formItemTree.forEach(it => candidateFormItems.push(it))
    while (candidateFormItems.length > 0) {
        const formItem = candidateFormItems.pop()
        if (formItem.name === 'SpanLayout') {
            formItem.props.items.forEach(it => candidateFormItems.push(it))
        } else {
            result.push(formItem)
        }
    }
    return result
}

export function formatTime(row, column) {
    console.log(row, column)
    return moment(row[column.property]).format('YYYY-MM-DD HH:mm:ss')
}

export function formatBusinessStatus(row, column) {
    const map = {
        '1': '正在处理',
        '2': '已撤销',
        '3': '驳回',
        '4': '办结',
    }
    return map[row[column.property]]
}