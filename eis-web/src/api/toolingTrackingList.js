import { axios } from '@/utils/request'

// 通过 子项目id 查询 最新 的数据
export function selectBySonProtId(query) {
  return axios({
    url: '/protToolTrackList/findByProtId/' + query,
    method: 'get'
  })
}

// 通过 子项目id 查询 最新 的 更新时间、更新人
export function selectNewTimeAndUser(query) {
  return axios({
    url: '/protToolTrackList/findLastTime/' + query,
    method: 'get'
  })
}

// 通过 子项目id 获取 最新的 Latest Tx
export function selectLatestTx(query) {
  return axios({
    url: '/protToolTrackList/selectLatestTx/' + query,
    method: 'get'
  })
}

// 同步 tooling plan 数据
export function syncToolPlanData(query) {
  return axios({
    url: '/protToolTrackList/syncToolPlan/' + query,
    method: 'get'
  })
}

// 下载 ToolTrackList
export function downloadToolTrackList(data) {
  return axios({
    url: '/protToolTrackList/downloadToolTrackList/' + data,
    method: 'get'
  })
}

// 通过 子项目id 获取 Tx History
export function selectTxHistory(query) {
  return axios({
    url: '/protToolTrackList/selectTxHistory/' + query,
    method: 'get'
  })
}

// 获取 所有 root cause
export function selectAllRootCauseTips() {
  return axios({
    url: '/protToolTrackList/selectAllRootCause',
    method: 'post'
  })
}

// 添加 protToolTrackList 新 issue
export function addToolTrackListNewIssue(data) {
  return axios({
    url: '/protToolTrackList/addIssue',
    method: 'post',
    data: data
  })
}

// 修改 protToolTrackList  issue
export function updateToolTrackListNewIssue(data) {
  return axios({
    url: '/protToolTrackList/editIssue',
    method: 'post',
    data: data
  })
}

// 删除 protToolTrackList issue
export function deleteToolTrackListIssue(id, parentId) {
  return axios({
    url: '/protToolTrackList/deleteById/' + id + '/' + parentId,
    method: 'get'
  })
}

// 获取 Issue Report
export function selectIssueReportBySonProtId(id) {
  return axios({
    url: '/protToolTrackList/selectIssueReport/' + id,
    method: 'get'
  })
}

// 获取 Issue Report
export function selectIssueTracking(sonProtId, issueId) {
  return axios({
    url: '/protToolTrackList/selectIssueTracking/' + sonProtId + '/' + issueId,
    method: 'get'
  })
}

// reopen Issue
export function reopenIssue(sonProtId, issueId) {
  return axios({
    url: '/protToolTrackList/reOpenIssue/' + sonProtId + '/' + issueId,
    method: 'get'
  })
}
