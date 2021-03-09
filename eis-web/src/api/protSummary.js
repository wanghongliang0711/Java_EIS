import { axios } from '@/utils/request'

// 获取所有 protStatus 数据
export function selectAllProtStatus() {
  return axios({
    url: '/protStatus/find',
    method: 'post'
  })
}

// 获取 protStatus 数据，通过 MainProtId
export function selectByMainProtId(mainProtId) {
  return axios({
    url: '/protStatus/find/' + mainProtId,
    method: 'post'
  })
}

// 根据prot Status 数据id, 获取所有 prot Status Aux表 最新 数据
export function findByProtStatusId(query) {
  return axios({
    url: '/protStatus/findByProtStatusId/' + query,
    method: 'post'
  })
}

// 获取所有 prot Status Aux表 EVT 类型 options
export function evtOptions(query) {
  return axios({
    url: '/protStatus/getEVTOptionsByStatusId/' + query,
    method: 'post'
  })
}

// 获取所有 prot Status Aux表 DVT 类型 options
export function dvtOptions(query) {
  return axios({
    url: '/protStatus/getDVTOptionsByStatusId/' + query,
    method: 'post'
  })
}

// 获取所有 prot Status Aux表 PVT 类型 options
export function pvtOptions(query) {
  return axios({
    url: '/protStatus/getPVTOptionsByStatusId/' + query,
    method: 'post'
  })
}

// 根据 prot Status 数据id，dataType EVT DVT PVT 新增prot Status Aux表 数据
export function addProtStatusAux(data) {
  return axios({
    url: '/protStatus/addProtStatusAux',
    method: 'post',
    data: data
  })
}

// 根据 prot Status Aux id 查询 数据
export function selectProtStatusAuxById(query) {
  return axios({
    url: '/protStatus/selectProtStatusAuxById/' + query,
    method: 'post'
  })
}

// 修改
export function editProtStatus(data) {
  return axios({
    url: '/protStatus/edit',
    method: 'post',
    data: data
  })
}
