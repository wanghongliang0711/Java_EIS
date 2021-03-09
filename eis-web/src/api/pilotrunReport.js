import { axios } from '@/utils/request'

// 获取PilotrunReportQueryRecord 数据库中 用户 最后 一次的查询 参数
export function selectPRRQueryRecord() {
  return axios({
    url: '/protPilotRunReportQueryRecord/selectQueryRecord',
    method: 'post'
  })
}

// 保存 PilotrunReportQueryRecord 数据库中 用户 查询 参数
export function savePRRQueryRecord(query) {
  return axios({
    url: '/protPilotRunReportQueryRecord/saveOrUpdate',
    method: 'post',
    data: query
  })
}

// 查询  PilotrunReport 表
export function selectPilotrunReport(query) {
  return axios({
    url: '/protPilotRunReport/find',
    method: 'post',
    data: query
  })
}

// 获取PilotrunReport Severity & Status 用于提示
export function selectPRRSeverityStatusTips() {
  return axios({
    url: '/protPilotRunReport/selectQueryTips',
    method: 'post'
  })
}

// 获取PilotrunReport 7 个 提示 用于 添加 修改 数据
export function selectPRRAddEditTips() {
  return axios({
    url: '/protPilotRunReport/selectAllTips',
    method: 'post'
  })
}

// 添加 ProtPilotRunReport 数据
export function addProtPilotRunReport(mainProtId, data) {
  return axios({
    url: '/protPilotRunReport/add/' + mainProtId,
    method: 'post',
    data: data
  })
}

// 修改 ProtPilotRunReport 数据
export function editProtPilotRunReport(mainProtId, data) {
  return axios({
    url: '/protPilotRunReport/edit/' + mainProtId,
    method: 'post',
    data: data
  })
}

// 删除 ProtPilotRunReport 数据
export function deleteProtPilotRunReport(id, mainProtId) {
  return axios({
    url: '/protPilotRunReport/deleteById/' + id + '/' + mainProtId,
    method: 'get'
  })
}

// 生成版本
export function addNewVersion(mainProtId, data) {
  return axios({
    url: '/protPilotRunReport/addVersion/' + mainProtId,
    method: 'post',
    data: data
  })
}

// 返回 之前的 版本
export function backBeforeVersion(mainProtId, data) {
  return axios({
    url: '/protPilotRunReport/backVersion/' + mainProtId,
    method: 'post',
    data: data
  })
}

// 下载 Pilot Run Report
export function downloadPilotRunReport(data) {
  return axios({
    url: '/protPilotRunReport/downloadPilotRunReport',
    method: 'post',
    data: data
  })
}
