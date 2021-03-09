<template>
  <div class="app-container">
    <template>
      <el-tabs v-model="activeName" :tab-position="tabPosition" @tab-click="handleTabClick">
        <el-tab-pane label="PilotrunReport" name="first">
          <h3 align="center"> Pilot Run Report </h3>
          <el-form :inline="true">
            <el-form-item>
              <el-tag type="danger" v-text="'Project Name :   '+ protName">主项目名 / 子项目名</el-tag>&nbsp;&nbsp;&nbsp;&nbsp;
            </el-form-item>
            <el-form-item label="Severity">
              <el-select
                v-model="queryParams.severity"
                style="width: 240px"
                multiple
                filterable
                allow-create
                default-first-option
                placeholder="请选择 Severity"
                @change="handleChangeQueryRecord"
              >
                <el-option
                  v-for="item in severityQueryOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="Status">
              <el-select
                v-model="queryParams.status"
                style="width: 240px"
                multiple
                filterable
                allow-create
                default-first-option
                placeholder="请选择 Status"
                @change="handleChangeQueryRecord"
              >
                <el-option
                  v-for="item in statusQueryOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                size="mini"
                @click="handleAddData"
              >新增数据</el-button>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                size="mini"
                @click="handleDownloadAllPicture"
              >下载所有图片</el-button>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                size="mini"
                @click="handleBuildVersion"
              >生成版本</el-button>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-upload2"
                @click="handleImport"
              >导入</el-button>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-download"
                @click="handleDownload('now')"
              >下载</el-button>
            </el-form-item>
          </el-form>
          <el-table
            ref="table1"
            v-loading="loading"
            :data="pilotrunReportList"
            :row-style="{height:'20px'}"
            :cell-style="{padding:'0px'}"
            style="width: 100%;font-size: 10px"
            border
            stripe
            :height="tableHeight"
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
          >
            <el-table-column fixed align="center" width="50" prop="item" label="Item" />
            <el-table-column fixed width="200" prop="problemDes" label="Problem Description" />
            <el-table-column align="center" width="90" prop="dateTime" label="Date" />
            <el-table-column align="center" width="72" prop="failRate" label="Fail rate" />
            <el-table-column align="center" width="72" prop="severity" label="Severity" />
            <el-table-column align="center" width="95" prop="issuer" label="Issuer" />
            <el-table-column align="center" width="95" prop="owner" label="Owner" />
            <el-table-column align="center" label="Factor">
              <el-table-column align="center" width="70" prop="design" label="Design" />
              <el-table-column align="center" width="60" prop="work" label="Work" />
              <el-table-column align="center" width="70" prop="material" label="Material" />
            </el-table-column>
            <el-table-column width="220" prop="rootCause" label="Root Cause" />
            <el-table-column width="220" prop="action" label="Action" />
            <el-table-column align="center" width="90" prop="dueDate" label="Due Date" />
            <el-table-column align="center" width="80" prop="status" label="Status" />
            <el-table-column width="400" prop="picture" label="Picture">
              <template slot-scope="scope">
                <el-button
                  v-for="(onePicture, index1) in scope.row.picture"
                  :key="index1"
                  size="mini"
                  type="text"
                  @click="handleDownloadPicture(onePicture)"
                >{{ onePicture.name }}</el-button>
              </template>
            </el-table-column>
            <el-table-column width="240" fixed="right" label="操作" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                >删除</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-upload2"
                  @click="handleImportPicture(scope.row)"
                >传图</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="查看历史版本" name="second">
          <el-table
            :data="pilotrunReportAllFileVer"
            border
            stripe
            height="200"
            :row-style="{height:'20px'}"
            :cell-style="{padding:'0px'}"
            style="width: 855px;font-size: 10px"
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
            :default-sort="{prop: 'fileVer', order: 'descending'}"
          >
            <el-table-column
              width="110"
              prop="fileVer"
              label="文件版本"
              sortable
            />
            <el-table-column
              width="350"
              prop="remark"
              label="备注"
            />
            <el-table-column
              width="140"
              prop="approval"
              label="Approval"
            />
            <el-table-column label="操作" width="240" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleViewVer(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-refresh-left"
                  @click="handleBackOldVer(scope.row)"
                >恢复</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-download"
                  @click="handleDownload(scope.row.fileVer)"
                >下载</el-button>
              </template>
            </el-table-column>
          </el-table>
          <h3 align="center"> Pilot Run Report </h3>
          <el-table
            ref="table2"
            v-loading="loading"
            :data="pilotrunReportList"
            :row-style="{height:'20px'}"
            :cell-style="{padding:'0px'}"
            style="width: 100%;font-size: 10px"
            border
            stripe
            :height="tableHeight2"
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
          >
            <el-table-column fixed align="center" width="50" prop="item" label="Item" />
            <el-table-column fixed width="200" prop="problemDes" label="Problem Description" />
            <el-table-column align="center" width="90" prop="dateTime" label="Date" />
            <el-table-column align="center" width="72" prop="failRate" label="Fail rate" />
            <el-table-column align="center" width="72" prop="severity" label="Severity" />
            <el-table-column align="center" width="95" prop="issuer" label="Issuer" />
            <el-table-column align="center" width="95" prop="owner" label="Owner" />
            <el-table-column align="center" label="Factor">
              <el-table-column align="center" width="70" prop="design" label="Design" />
              <el-table-column align="center" width="60" prop="work" label="Work" />
              <el-table-column align="center" width="70" prop="material" label="Material" />
            </el-table-column>
            <el-table-column width="220" prop="rootCause" label="Root Cause" />
            <el-table-column width="220" prop="action" label="Action" />
            <el-table-column align="center" width="90" prop="dueDate" label="Due Date" />
            <el-table-column align="center" width="80" prop="status" label="Status" />
            <el-table-column width="400" prop="picture" label="Picture">
              <template slot-scope="scope">
                <el-button
                  v-for="(onePicture, index1) in scope.row.picture"
                  :key="index1"
                  size="mini"
                  type="text"
                  @click="handleDownloadPicture(onePicture)"
                >{{ onePicture.name }}</el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" fixed="right" width="70" prop="fileVer" label="文件版本" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </template>

    <!-- 生成 新版本 -->
    <el-dialog :title="title" :visible.sync="openBuildVer" :close-on-click-modal="false">
      <el-form ref="buildVerForm" :model="buildVerForm" :rules="buildVerRules" label-width="110px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="文件版本号" prop="fileVer">
              <el-input v-model="buildVerForm.fileVer" placeholder="请输入文件版本号" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Approval " prop="approval">
              <el-input v-model="buildVerForm.approval" placeholder="请输入Approval " />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="buildVerForm.remark" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openBuildVer = false">取 消</el-button>
        <el-button type="primary" @click="submitBuildVerForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="openAddEditDialog" width="900px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="155px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="Item" prop="item">
              <el-input-number v-model="form.item" style="width: 200px" size="small" :precision="0" :step="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Date" prop="dateTime">
              <el-input v-model="form.dateTime" placeholder="请输入 Date" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="Problem Description" prop="problemDes">
              <el-autocomplete
                v-model="form.problemDes"
                style="width: 700px"
                class="inline-input1"
                :fetch-suggestions="queryProblemDes"
                placeholder="请输入Problem Description"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Fail rate" prop="failRate">
              <el-input v-model="form.failRate" placeholder="请输入Fail rate" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Severity" prop="severity">
              <el-autocomplete
                v-model="form.severity"
                style="width: 275px"
                class="inline-input1"
                :fetch-suggestions="querySeverity"
                placeholder="请输入 Severity"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Issuer" prop="issuer">
              <el-autocomplete
                v-model="form.issuer"
                style="width: 275px"
                class="inline-input1"
                :fetch-suggestions="queryIssuer"
                placeholder="请输入 Issuer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Owner" prop="owner">
              <el-autocomplete
                v-model="form.owner"
                style="width: 275px"
                class="inline-input1"
                :fetch-suggestions="queryOwner"
                placeholder="请输入 Owner"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Design" prop="design">
              <el-input v-model="form.design" placeholder="请输入 Design" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Work" prop="work">
              <el-input v-model="form.work" placeholder="请输入 Work" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Material" prop="material">
              <el-input v-model="form.material" placeholder="请输入 Material" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Due Date" prop="dueDate">
              <el-input v-model="form.dueDate" placeholder="请输入 Due Date" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Status" prop="status">
              <el-autocomplete
                v-model="form.status"
                style="width: 275px"
                class="inline-input1"
                :fetch-suggestions="queryStatus"
                placeholder="请输入 Status"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="Root Cause" prop="rootCause">
              <el-autocomplete
                v-model="form.rootCause"
                style="width: 700px"
                class="inline-input1"
                :fetch-suggestions="queryRootCause"
                placeholder="请输入 Root Cause"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="Action" prop="action">
              <el-autocomplete
                v-model="form.action"
                style="width: 700px"
                class="inline-input1"
                :fetch-suggestions="queryAction"
                placeholder="请输入 Action"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openAddEditDialog = false">取 消</el-button>
        <el-button type="primary" @click="submitAddOrUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 上传图片 对话框-->
    <el-dialog :title="title" :visible.sync="openUploadPicture" width="400px" :close-on-click-modal="false">
      <el-upload
        ref="uploadFile"
        class="upload-demo"
        drag
        :data="uploadPicture"
        :limit="1"
        :headers="uploadHeader"
        :auto-upload="false"
        :before-upload="beforePictureUpload"
        :on-success="handlePictureSuccess"
        accept=".jpg, .png"
        :action="uploadPictureUrl()"
      >
        <i class="el-icon-upload" />
        <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip" style="color:red">提示：仅允许导入“jpg”或“png”格式图片！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openUploadPicture = false">取 消</el-button>
        <el-button type="primary" @click="submitUploadPicture">导 入</el-button>
      </div>
    </el-dialog>

    <!-- 上传文件 对话框   -->
    <el-dialog :title="title" :visible.sync="openUploadExcel" width="400px" :close-on-click-modal="false">
      <el-upload
        ref="uploadExcelFile"
        class="upload-demo"
        drag
        :data="uploadExcelData"
        :limit="1"
        :headers="uploadHeader"
        :auto-upload="false"
        :before-upload="beforeUploadExcel"
        :on-success="handleUploadExcelSuccess"
        accept=".xlsx, .xls"
        :action="uploadExcelUrl()"
      >
        <i class="el-icon-upload" />
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip" style="color:red">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openUploadExcel = false">取 消</el-button>
        <el-button type="primary" @click="submitUploadExcel">导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { getToken } from '@/utils/auth'
import { download, selectProtByID } from '@/api/prot'
import { selectPRRQueryRecord, downloadPilotRunReport, backBeforeVersion, addNewVersion, editProtPilotRunReport, deleteProtPilotRunReport, addProtPilotRunReport, selectPRRAddEditTips, savePRRQueryRecord, selectPilotrunReport, selectPRRSeverityStatusTips } from '@/api/pilotrunReport'
import { downloadJpg, downloadPng, downloadXlsx } from '@/utils/download'
import { selectProtFileVer } from '@/api/mePartList'

export default {
  name: 'PilotrunReport',
  data() {
    return {
      tableHeight: 450,
      tableHeight2: 450,
      // 上传 文件 的头
      uploadHeader: {},
      // 遮罩层
      loading: true,
      // 弹出层标题
      title: '',
      // 项目 id 可能是主项目 也 可能是 子项目
      protId: undefined,
      // 主项目id， 用于删除时使用
      mainProtId: undefined,
      // severity Query Options 数据字典
      severityQueryOptions: [],
      // status Query Options 数据字典
      statusQueryOptions: [],
      // pilot run Report 数据
      pilotrunReportList: [],
      // pilot run Report 所有文件版本
      pilotrunReportAllFileVer: [],
      // 表单参数
      form: {},
      // 生成版本 表单参数
      buildVerForm: {},
      // 所有提示信息
      allTipsForAddEdit: {},
      // 是否显示 新增、修改 弹出层
      openAddEditDialog: false,
      // dialog 传图
      openUploadPicture: false,
      // dialog 传图
      openUploadExcel: false,
      // 是否显示 buildVer 弹出层
      openBuildVer: false,
      // 传 Excel 参数
      uploadExcelData: { protId: undefined, mainProtId: undefined },
      // 传图 参数
      uploadPicture: { protId: undefined, category: 'P3', remark: 'Pilot Run Report Picture' },
      // 查询 参数
      queryParams: { protId: undefined, fileVer: undefined, severity: [], status: [] },
      // 项目名
      protName: undefined,
      // 哪个标签页生效
      activeName: 'first',
      // 标签页 位置
      tabPosition: 'top',
      // 添加版本 表单校验
      buildVerRules: {
        fileVer: [
          { required: true, message: '文件版本号不能为空', trigger: 'blur' }
        ],
        remark: [
          { min: 0, max: 100, message: '长度在 0 到 100 个字符', trigger: 'blur' }
        ]
      },
      // 表单校验
      rules: {
        item: [
          { required: true, message: 'Item 不能为空', trigger: 'blur' }
        ],
        problemDes: [
          { min: 0, max: 300, message: '长度超过 300 个字符', trigger: 'blur' }
        ],
        rootCause: [
          { min: 0, max: 300, message: '长度超过 300 个字符', trigger: 'blur' }
        ],
        action: [
          { min: 0, max: 300, message: '长度超过 300 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.$nextTick(function() {
      // https://blog.csdn.net/weixin_41725862/article/details/90439463
      // this.tableHeight = window.innerHeight - this.$refs.table1.$el.offsetTop - 60
      // this.tableHeight2 = window.innerHeight - this.$refs.table2.$el.offsetTop - 300
      // this.$refs.table2.$el.offsetTop 表格距离浏览器的高度 调整后发现，使用el-tabs时 不使用这个效果会更好
      this.tableHeight = window.innerHeight - 195
      this.tableHeight2 = window.innerHeight - 300
      // 监听窗口大小变化
      const self = this
      window.onresize = function() {
        // self.tableHeight = window.innerHeight - self.$refs.table1.$el.offsetTop - 60
        // self.tableHeight2 = window.innerHeight - self.$refs.table2.$el.offsetTop - 60
        self.tableHeight = window.innerHeight - 195
        self.tableHeight2 = window.innerHeight - 300
      }
    })
  },
  created() {
    this.getProtInfo()
    this.getPilotrunReportQueryRecord()
    this.getSeverityStatusTips()
    this.uploadHeader = { 'AuthorizationToken': getToken() }
  },
  methods: {
    /** 点击确定 上传 Excel 按钮 */
    submitUploadExcel: function() {
      // 触发组件的action
      this.$refs.uploadExcelFile.submit()
    },
    /** 导入excel的 url */
    uploadExcelUrl: function() {
      return process.env.VUE_APP_BASE_API + '/protPilotRunReport/upload'
    },
    /** 上传 excel 成功 */
    handleUploadExcelSuccess(response, file, fileList) {
      this.openUploadExcel = false
      this.$refs.uploadExcelFile.clearFiles()
      this.$message({ showClose: true, message: '上传成功', type: 'success' })
      this.getPilotrunReport('now')
    },
    /** beforeUploadExcel  */
    beforeUploadExcel(file) {
      const extension = file.name.split('.')[file.name.split('.').length - 1] === 'xls'
      const extension2 = file.name.split('.')[file.name.split('.').length - 1] === 'xlsx'
      if (!extension && !extension2) {
        this.$message({
          message: '上传文件只能是 xls、xlsx格式！！！',
          type: 'error'
        })
      }
      return extension || extension2
    },
    /** 点击  导入 按钮 */
    handleImport() {
      if (this.$refs.uploadExcelFile !== undefined) {
        this.$refs.uploadExcelFile.clearFiles()
      }
      this.title = '导入Excel'
      this.uploadExcelData.protId = this.protId
      this.uploadExcelData.mainProtId = this.mainProtId
      this.openUploadExcel = true
    },
    /** 下载 某一版本 Pilot Run Report */
    handleDownload(fileVer) {
      downloadPilotRunReport({ protId: this.protId, fileVer: fileVer }).then(response => {
        if (response.code === '100000') {
          if (response.data.split('.')[response.data.split('.').length - 1] === 'xls') {
            downloadXlsx(response.data)
          }
          if (response.data.split('.')[response.data.split('.').length - 1] === 'xlsx') {
            downloadXlsx(response.data)
          }
        }
      })
    },
    /** 恢复为 历史版本 */
    handleBackOldVer(row) {
      this.$confirm('此操作将永久恢复 Pilot Run Report 为 ' + row.fileVer + ' 的版本， 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        backBeforeVersion(this.mainProtId, { protId: this.protId, fileVer: row.fileVer }).then(response => {
          if (response.code === '100000') {
            this.$message({ showClose: true, message: '恢复成功', type: 'success' })
          }
        })
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消恢复' })
      })
    },
    /** 查看历史版本 数据 */
    handleViewVer(row) {
      this.loading = true
      selectPilotrunReport({ protId: this.protId, fileVer: row.fileVer }).then(response => {
        this.pilotrunReportList = response.data
        this.loading = false
      })
    },
    /** 生成版本 dialog 提交按钮 */
    submitBuildVerForm: function() {
      this.$refs['buildVerForm'].validate(valid => {
        if (valid) {
          addNewVersion(this.mainProtId, this.buildVerForm).then(response => {
            if (response.code === '100000') {
              if (response.data.msgFail !== undefined) {
                this.$message({ showClose: true, message: response.data.msgFail, type: 'error', duration: 5000 })
              } else {
                this.$message({ showClose: true, message: '生成版本成功', type: 'success' })
                this.openBuildVer = false
              }
            }
          })
        }
      })
    },
    /** 点击  生成版本 */
    handleBuildVersion() {
      this.buildVerForm = {
        protId: this.protId,
        fileVer: undefined,
        category: 'C',
        remark: undefined,
        approval: undefined
      }
      this.title = '生成新版本'
      this.openBuildVer = true
    },
    /** 点击 下载 所有 图片按钮  */
    handleDownloadAllPicture() {
      for (const item of this.pilotrunReportList) {
        if (item.picture !== undefined && item.picture.length > 0) {
          for (const onePic of item.picture) {
            this.handleDownloadPicture(onePic)
          }
        }
      }
    },
    /** 点击 下载图片按钮  */
    handleDownloadPicture(picture) {
      download(picture.id).then(res => {
        if (res.code === '100000') {
          if (res.data.split('.')[res.data.split('.').length - 1] === 'png') {
            downloadPng(res.data)
          }
          if (res.data.split('.')[res.data.split('.').length - 1] === 'jpg') {
            downloadJpg(res.data)
          }
        }
      })
    },
    /** 点击确定 上传图片 按钮 */
    submitUploadPicture: function() {
      // 触发组件的action
      this.$refs.uploadFile.submit()
    },
    /** 导入 picture 的 url */
    uploadPictureUrl: function() {
      return process.env.VUE_APP_BASE_API + '/protFile/upload'
    },
    /** 上传 Picture 成功  */
    handlePictureSuccess(response, file, fileList) {
      this.openUploadPicture = false
      this.$refs.uploadFile.clearFiles()
      this.$message({ showClose: true, message: '上传成功', type: 'success' })
      this.getPilotrunReport('now')
    },
    /** beforePictureUpload  */
    beforePictureUpload(file) {
      const extension = file.name.split('.')[file.name.split('.').length - 1] === 'jpg'
      const extension2 = file.name.split('.')[file.name.split('.').length - 1] === 'png'
      if (!extension && !extension2) {
        this.$message({ message: '上传文件只能是 jpg、png格式!', type: 'error' })
      }
      // this.upload.file = file
      return extension || extension2
    },
    /**  点击 传图 按钮 按钮操作 */
    handleImportPicture(row) {
      if (this.$refs.uploadFile !== undefined) {
        this.$refs.uploadFile.clearFiles()
      }
      this.title = '上传图片'
      this.uploadPicture.protId = row.id
      this.openUploadPicture = true
    },
    /**  点击 删除 按钮 按钮操作 */
    handleDelete(row) {
      this.$confirm('此操作将永久删除Item 为 ' + row.item + ' 的数据， 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteProtPilotRunReport(row.id, this.mainProtId).then(res => {
          if (res.code === '100000') {
            this.$message({ showClose: true, message: '删除成功', type: 'success' })
            this.getPilotrunReport('now')
          }
        })
      }).catch(() => { this.$message({ type: 'info', message: '已取消删除' }) })
    },
    /**  点击 修改按钮 按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.title = '修改数据'
      this.form.id = row.id
      this.form.item = row.item
      this.form.problemDes = row.problemDes
      this.form.dateTime = row.dateTime
      this.form.failRate = row.failRate
      this.form.severity = row.severity
      this.form.issuer = row.issuer
      this.form.owner = row.owner
      this.form.design = row.design
      this.form.work = row.work
      this.form.material = row.material
      this.form.rootCause = row.rootCause
      this.form.action = row.action
      this.form.dueDate = row.dueDate
      this.form.status = row.status
      this.openAddEditDialog = true
      // 获取 7 个 提示
      selectPRRAddEditTips().then(res => {
        this.allTipsForAddEdit = res.data
      })
    },
    /** 点击 新增 或 修改 操作 */
    submitAddOrUpdate: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            editProtPilotRunReport(this.mainProtId, this.form).then(response => {
              if (response.code === '100000') {
                if (response.data.msgFail !== undefined) {
                  this.$message({ showClose: true, message: response.data.msgFail, type: 'error', duration: 5000 })
                } else {
                  this.openAddEditDialog = false
                  this.$message({ showClose: true, message: '修改成功', type: 'success' })
                  this.getPilotrunReport('now')
                }
              }
            })
          } else {
            addProtPilotRunReport(this.mainProtId, this.form).then(response => {
              if (response.code === '100000') {
                if (response.data.msgFail !== undefined) {
                  this.$message({ showClose: true, message: response.data.msgFail, type: 'error', duration: 5000 })
                } else {
                  this.openAddEditDialog = false
                  this.$message({ showClose: true, message: '添加成功', type: 'success' })
                  this.getPilotrunReport('now')
                }
              }
            })
          }
        }
      })
    },
    /** 点击 新增数据 按钮 操作 */
    handleAddData() {
      this.reset()
      this.title = '新增数据'
      this.openAddEditDialog = true
      // 获取 7 个 提示
      selectPRRAddEditTips().then(res => {
        this.allTipsForAddEdit = res.data
      })
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        fileVer: 'now',
        protId: this.protId,
        item: undefined,
        problemDes: undefined,
        dateTime: undefined,
        failRate: undefined,
        severity: undefined,
        issuer: undefined,
        owner: undefined,
        design: undefined,
        work: undefined,
        material: undefined,
        rootCause: undefined,
        action: undefined,
        dueDate: undefined,
        status: undefined
      }
      this.resetForm('form')
    },
    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields()
      }
    },
    /** 获取PilotrunReport Severity & Status 提示 */
    handleChangeQueryRecord() {
      this.getPilotrunReport('now')
      savePRRQueryRecord(this.queryParams)
    },
    /** 获取PilotrunReport Severity & Status 提示 */
    getSeverityStatusTips() {
      selectPRRSeverityStatusTips().then(response => {
        this.severityQueryOptions = response.data.severity
        this.statusQueryOptions = response.data.status
      })
    },
    /** 获取PilotrunReportQueryRecord 数据库中 用户 最后 一次的查询 参数 */
    getPilotrunReportQueryRecord() {
      selectPRRQueryRecord().then(response => {
        if (response.code === '100000') {
          if (response.data !== undefined) {
            this.queryParams.severity = response.data.severity
            this.queryParams.status = response.data.status
          }
          this.getPilotrunReport('now')
        }
      })
    },
    /** 获取 特定 版本 的  PilotrunReport 数据 */
    getPilotrunReport(fileVer) {
      this.loading = true
      this.queryParams.protId = this.protId
      this.queryParams.fileVer = fileVer
      selectPilotrunReport(this.queryParams).then(reponse => {
        this.pilotrunReportList = reponse.data
        this.loading = false
      })
    },
    /** 点击 tab  */
    handleTabClick(tab, event) {
      if (tab.name === 'first') {
        this.pilotrunReportList = []
        this.getPilotrunReportQueryRecord()
      }
      if (tab.name === 'second') {
        this.pilotrunReportList = []
        selectProtFileVer({ protId: this.protId, category: 'C' }).then(response => {
          this.pilotrunReportAllFileVer = response.data
        })
      }
    },
    /** 获取 项目 信息  */
    getProtInfo() {
      this.protId = this.$route.query.protId
      selectProtByID(this.protId).then(response => {
        if (response.code === '100000') {
          if (response.data.parentId !== '0') {
            this.mainProtId = response.data.parentId
            selectProtByID(response.data.parentId).then(mainRes => {
              if (mainRes.code === '100000') {
                this.protName = mainRes.data.name + ' / ' + response.data.name
              }
            })
          } else {
            this.mainProtId = this.protId
            this.protName = response.data.name
          }
        }
      })
    },
    /** 带输入建议  Action Tips */
    queryAction(queryString, cb) {
      const dataTips = this.allTipsForAddEdit.action
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  Root Cause Tips */
    queryRootCause(queryString, cb) {
      const dataTips = this.allTipsForAddEdit.rootCause
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  Status Tips */
    queryStatus(queryString, cb) {
      const dataTips = this.allTipsForAddEdit.status
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  Owner Tips */
    queryOwner(queryString, cb) {
      const dataTips = this.allTipsForAddEdit.owner
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  Issuer Tips */
    queryIssuer(queryString, cb) {
      const dataTips = this.allTipsForAddEdit.issuer
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  Severity Tips */
    querySeverity(queryString, cb) {
      const dataTips = this.allTipsForAddEdit.severity
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  Problem Des Tips */
    queryProblemDes(queryString, cb) {
      const dataTips = this.allTipsForAddEdit.problemDes
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) >= 0)
      }
    }
  }
}
</script>

<style scoped>
  .app-container {
    padding: 20px;
  }
</style>
