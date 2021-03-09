import { axios } from '@/utils/request'

export function getToken() {
  return axios({
    url: '/qiniu/upload/token', // 假地址 自行替换
    method: 'get'
  })
}
