import {request} from '../utils/request.js'

// jsonToBpmn 
export function jsonToBpmn(data) {
  
  return request({
    url: 'antd/jsonToBpmn',
    method: 'POST',
    data: data
  })
}
