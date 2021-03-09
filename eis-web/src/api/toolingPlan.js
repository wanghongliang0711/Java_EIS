import { axios } from '@/utils/request'

// 通过 子项目id 查询  需要从 me part list中添加的数据
export function selectByMePartList(query) {
  return axios({
    url: '/protToolPlan/selectByMePartList/' + query,
    method: 'post'
  })
}

// 新增 tool plan 子项
export function addToolPlanSon(data) {
  return axios({
    url: '/protToolPlan/add',
    method: 'post',
    data: data
  })
}

// 修改 tool plan 子项
export function editProtToolPlan(data) {
  return axios({
    url: '/protToolPlan/edit',
    method: 'post',
    data: data
  })
}

// 删除 tool plan 数据
export function deleteProtToolPlanById(id, protParentId, parentId) {
  return axios({
    url: '/protToolPlan/deleteById/' + id + '/' + protParentId + '/' + parentId,
    method: 'post'
  })
}

// 通过 子项目id 查询 最新 的 更新时间、更新人
export function selectNewTimeAndUser(query) {
  return axios({
    url: '/protToolPlan/findLastTime/' + query,
    method: 'get'
  })
}

// 生成版本
export function addVersion(data) {
  return axios({
    url: '/protToolPlan/addVersion',
    method: 'post',
    data: data
  })
}

// 添加从Me Part List中找出的数据
export function batchAddByMePartList(sonProtId, parentId, data) {
  return axios({
    url: '/protToolPlan/addByMePartList/' + sonProtId + '/' + parentId,
    method: 'post',
    data: data
  })
}

// 查询项目 tool plan 表
export function selectByProtIdAndVersion(query) {
  return axios({
    url: '/protToolPlan/find',
    method: 'post',
    data: query
  })
}

// 获取所有 VERSION 用于提示
export function selectAllVersion() {
  return axios({
    url: '/protToolPlan/selectAllVersion',
    method: 'post'
  })
}

// 返回 之前的 版本
export function backBeforeVersion(data) {
  return axios({
    url: '/protToolPlan/backVersion',
    method: 'post',
    data: data
  })
}

// 修改 PR Number
export function updatePrNum(data) {
  return axios({
    url: '/protToolPlan/updatePrNum',
    method: 'post',
    data: data
  })
}

// 下载 Tool plan
export function downloadToolPlan(data) {
  return axios({
    url: '/protToolPlan/downloadToolPlan',
    method: 'post',
    data: data
  })
}

// 获取所有 MATERIAL 第三级 用于提示
export function selectAllMaterial() {
  return axios({
    url: '/protToolPlan/selectAllMaterial',
    method: 'post'
  })
}

// 获取所有 COLOR No  用于提
export function selectAllColorNo() {
  return axios({
    url: '/protToolPlan/selectAllColorNo',
    method: 'post'
  })
}

// 获取所有 PAINTING COLOR No. 用于提示
export function selectAllPaintingColorNo() {
  return axios({
    url: '/protToolPlan/selectAllPaintingColorNo',
    method: 'post'
  })
}

// 获取所有 PRINTING COLOR No. 用于提示
export function selectAllPrintingColorNo() {
  return axios({
    url: '/protToolPlan/selectAllPrintingColorNo',
    method: 'post'
  })
}

// 获取所有 COATING  CATEGORY 用于提示
export function selectAllCoatingCategory() {
  return axios({
    url: '/protToolPlan/selectAllCoatingCategory',
    method: 'post'
  })
}

// 获取所有 TEXTURE  CATEGORY 用于提示
export function selectAllTextureCategory() {
  return axios({
    url: '/protToolPlan/selectAllTextureCategory',
    method: 'post'
  })
}

// 获取所有 INSERT NUT SPEC. 用于提示
export function selectAllInsertNutSpec() {
  return axios({
    url: '/protToolPlan/selectAllInsertNutSpec',
    method: 'post'
  })
}

// 获获取所有 TOOLING  VENDER 用于提示
export function selectAllToolingVender() {
  return axios({
    url: '/protToolPlan/selectAllToolingVender',
    method: 'post'
  })
}

// 获取所有 PR NUMBER. 用于提示
export function selectAllPrNumber() {
  return axios({
    url: '/protToolPlan/selectAllPrNumber',
    method: 'post'
  })
}

// 获取所有 SUPPLIED VENDOR 用于提示
export function selectAllSuppliedVendor() {
  return axios({
    url: '/protToolPlan/selectAllSuppliedVendor',
    method: 'post'
  })
}

