import { preAxios } from '@/utils/request'

const downloadUrl = '/protFile/common/download'
// https://blog.csdn.net/xiaoranzhizhu/article/details/70473734
const mimeMap = {
  xlsx: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  zip: 'application/zip',
  xml: 'application/xml',
  pdf: 'application/pdf',
  png: 'image/png',
  jpg: 'image/jpeg',
  pptx: 'application/vnd.openxmlformats-officedocument.presentationml.presentation',
  ppt: 'application/vnd.ms-powerpoint',
  doc: 'application/msword',
  docx: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
}

/**
 * 下载.png文件
 * @param {String} filename 文件名
 */
export function downloadPng(filename) {
  preAxios({
    url: downloadUrl,
    method: 'get',
    params: { fileName: filename, delete: true },
    responseType: 'blob'
  }).then(res => {
    resolveBlob(res, mimeMap.png)
  })
}

/**
 * 下载.jpg文件
 * @param {String} filename 文件名
 */
export function downloadJpg(filename) {
  preAxios({
    url: downloadUrl,
    method: 'get',
    params: { fileName: filename, delete: true },
    responseType: 'blob'
  }).then(res => {
    resolveBlob(res, mimeMap.jpg)
  })
}

/**
 * 下载.xlsx文件
 * @param {String} filename 文件名
 */
export function downloadXlsx(filename) {
  preAxios({
    url: downloadUrl,
    method: 'get',
    params: { fileName: filename, delete: true },
    responseType: 'blob'
  }).then(res => {
    resolveBlob(res, mimeMap.xlsx)
  })
}

/**
 * 下载.pdf文件
 * @param {String} filename 文件名
 */
export function downloadPdf(filename) {
  preAxios({
    url: downloadUrl,
    method: 'get',
    params: { fileName: filename, delete: true },
    responseType: 'blob'
  }).then(res => {
    resolveBlob(res, mimeMap.pdf)
  })
}

/**
 * 下载.pptx文件
 * @param {String} filename 文件名
 */
export function downloadPptx(filename) {
  preAxios({
    url: downloadUrl,
    method: 'get',
    params: { fileName: filename, delete: true },
    responseType: 'blob'
  }).then(res => {
    resolveBlob(res, mimeMap.pptx)
  })
}

/**
 * 下载.ppt文件
 * @param {String} filename 文件名
 */
export function downloadPpt(filename) {
  preAxios({
    url: downloadUrl,
    method: 'get',
    params: { fileName: filename, delete: true },
    responseType: 'blob'
  }).then(res => {
    resolveBlob(res, mimeMap.ppt)
  })
}

/**
 * 下载.doc文件
 * @param {String} filename 文件名
 */
export function downloadDoc(filename) {
  preAxios({
    url: downloadUrl,
    method: 'get',
    params: { fileName: filename, delete: true },
    responseType: 'blob'
  }).then(res => {
    resolveBlob(res, mimeMap.doc)
  })
}

/**
 * 下载.docx文件
 * @param {String} filename 文件名
 */
export function downloadDocx(filename) {
  preAxios({
    url: downloadUrl,
    method: 'get',
    params: { fileName: filename, delete: true },
    responseType: 'blob'
  }).then(res => {
    resolveBlob(res, mimeMap.docx)
  })
}

/**
 * 解析blob响应内容并下载
 * @param {*} res blob响应内容
 * @param {String} mimeType MIME类型
 */
export function resolveBlob(res, mimeType) {
  const aLink = document.createElement('a')
  var blob = new Blob([res.data], { type: mimeType })
  // //从response的headers中获取filename, 后端response.setHeader("Content-disposition", "attachment; filename=xxxx.docx") 设置的文件名;
  var patt = new RegExp('filename=([^;]+\\.[^\\.;]+);*')
  var contentDisposition = decodeURI(res.headers['content-disposition'])
  var result = patt.exec(contentDisposition)
  var fileName = result[1]
  aLink.href = URL.createObjectURL(blob)
  aLink.setAttribute('download', fileName) // 设置下载文件名称
  document.body.appendChild(aLink)
  aLink.click()
  document.body.removeChild(aLink)
  window.URL.revokeObjectURL(aLink.href)
}
