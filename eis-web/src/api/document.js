import { axios } from '@/utils/request'

// 查询 子项目 最新 文件
export function selectNewestFile(data) {
  return axios({
    url: '/protFile/selectBySonProtId/' + data,
    method: 'post'
  })
}

// 查找 所有 文件
export function selectAllFile(data) {
  return axios({
    url: '/protFile/select',
    method: 'post',
    data: data
  })
}

// 下载
export function downloadFile(param) {
  return axios({
    url: '/protFile/download/' + param,
    method: 'get'
  })
}
