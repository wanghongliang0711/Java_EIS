import { axios } from '@/utils/request'

// 查询 用户 查询 记录
export function selectQueryRecord() {
  return axios({
    url: '/protDailyReportQueryRecord/selectQueryRecord',
    method: 'post'
  })
}

// 更新 用户 查询 记录
export function updateDailyReportQueryRecord(data) {
  return axios({
    url: '/protDailyReportQueryRecord/saveOrUpdate',
    method: 'post',
    data: data
  })
}

// 获取 second 的 Tool Track List 数据
export function selectToolTrackList(data) {
  return axios({
    url: '/protToolTrackList/findForDailyReport',
    method: 'post',
    data: data
  })
}

// 获取 first 的 prot Status 数据
export function selectProtStatus() {
  return axios({
    url: '/protStatus/findForDashboard',
    method: 'post'
  })
}
