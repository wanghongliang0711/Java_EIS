<template>
  <div class="app-container">
    <template>
      <el-tabs v-model="activeName" :tab-position="tabPosition" @tab-click="handleTabClick">
        <el-tab-pane label="Tooling Tracking List" name="first">
          <el-form :inline="true">
            <el-tag type="danger" v-text="'Project Name :   '+ protName">主项目名 / 子项目名</el-tag>&nbsp;&nbsp;&nbsp;&nbsp;
            <el-button
              type="primary"
              size="mini"
              @click="syncToolPlan"
            >Sync Tooling Plan</el-button>
            <el-button
              v-if="isOpen === true"
              type="primary"
              size="mini"
              @click="expandAll"
            >Issue呈列</el-button>
            <el-button
              v-if="isOpen === false"
              type="primary"
              size="mini"
              @click="collapseAll"
            >Issue隐藏</el-button>
            <el-button
              type="info"
              size="mini"
              @click="handleUploadExcel"
            >导入</el-button>
            <el-button
              type="primary"
              size="mini"
              @click="handleDownload"
            >下载</el-button>
            <h3 align="center"> TOOLING TRACKING LIST </h3>
            <el-tag type="danger" v-text="'UPDATE DATE:   '+lastTimeAndUser.updateTime">UPDATE DATE</el-tag>&nbsp;&nbsp;&nbsp;&nbsp;
            <el-tag v-text="'UPDATE BY:   '+lastTimeAndUser.updateUser">UPDATE BY</el-tag>&nbsp;&nbsp;
          </el-form>
          <el-table
            ref="topicTable"
            v-loading="loading"
            :data="toolTrackListData"
            :row-style="{height:'20px'}"
            :cell-style="{padding:'0px'}"
            border
            stripe
            :height="tableHeight1"
            row-key="id"
            style="width: 100%;font-size: 10px"
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
          >
            <el-table-column fixed width="130" prop="partName" label="Part Name" />
            <el-table-column fixed align="center" width="120" prop="partNo" label="Part No" />
            <el-table-column fixed align="center" width="40" prop="tx" label="Tx">
              <template slot-scope="scope">
                <p v-if="scope.row.tx !== undefined">T{{ scope.row.tx }}</p>
              </template>
            </el-table-column>
            <el-table-column fixed align="center" width="60" prop="item" label="Item" />
            <el-table-column fixed width="200" prop="issueDescription" label="Tooling Issue Description" />
            <el-table-column width="190" prop="rootCause" label="Root Cause" />
            <el-table-column width="190" prop="action" label="Action" />
            <el-table-column prop="issuePriority" label="Priority" />
            <el-table-column prop="status" label="Status" />
            <el-table-column width="250" prop="remark" label="Remark" />
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
            <el-table-column width="225" fixed="right" label="操作" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  v-if="scope.row.parentId === '0'"
                  size="mini"
                  type="text"
                  icon="el-icon-plus"
                  @click="handleInsertIssue(scope.row)"
                >新增Issue</el-button>
                <el-button
                  v-if="scope.row.parentId !== '0'"
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdateIssue(scope.row)"
                >修改</el-button>
                <el-button
                  v-if="scope.row.parentId !== '0'"
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDeleteIssue(scope.row)"
                >删除</el-button>
                <el-button
                  v-if="scope.row.parentId !== '0'"
                  size="mini"
                  type="text"
                  icon="el-icon-upload2"
                  @click="handleImportPicture(scope.row)"
                >传图</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="试模管理" name="second">
          <h3 align="center"> Latest Tx </h3>
          <el-table
            :data="latestTxListData"
            border
            stripe
            height="200"
            :row-style="{height:'20px'}"
            :cell-style="{padding:'0px'}"
            style="width: 1000px;font-size: 10px"
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
          >
            <el-table-column align="center" width="200" prop="partName" label="Part Name" />
            <el-table-column align="center" width="150" prop="partNo" label="Part No." />
            <el-table-column align="center" width="40" prop="tx" label="Tx">
              <template slot-scope="scope">
                T{{ scope.row.tx }}
              </template>
            </el-table-column>
            <el-table-column align="center" width="180" prop="datePlan" label="DATE_Plan" />
            <el-table-column align="center" width="180" prop="dateAct" label="DATE_Act" />
            <el-table-column align="center" width="240" label="操作" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-plus"
                  @click="handleCreateNewTx(scope.row)"
                >Create new Tx</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleEditLatestTx(scope.row)"
                >修改</el-button>
              </template>
            </el-table-column>
          </el-table>

          <h3 align="center"> Tx History </h3>
          <el-table
            :data="txHistoryListData"
            border
            stripe
            :row-style="{height:'20px'}"
            :cell-style="{padding:'0px'}"
            :height="tableHeight2"
            style="width: 1010px;font-size: 10px"
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
          >
            <el-table-column align="center" width="200" prop="partName" label="Part Name" />
            <el-table-column align="center" width="150" prop="partNo" label="Part No." />
            <el-table-column align="center" width="50" prop="tx" label="Tx">
              <template slot-scope="scope">
                T{{ scope.row.tx }}
              </template>
            </el-table-column>
            <el-table-column align="center" width="300" prop="datePlan" label="DATE_Plan" />
            <el-table-column align="center" width="300" prop="dateAct" label="DATE_Act" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="Check Issue Report" name="third">
          <el-form :inline="true">
            <el-tag type="danger" v-text="'Project Name :   '+ protName">主项目名 / 子项目名</el-tag>&nbsp;&nbsp;&nbsp;&nbsp;
            <el-button
              v-if="isOpenIssueReport === true"
              type="primary"
              size="mini"
              @click="expandAllIssueReport"
            >Issue呈列</el-button>
            <el-button
              v-if="isOpenIssueReport === false"
              type="primary"
              size="mini"
              @click="collapseAllIssueReport"
            >Issue隐藏</el-button>
          </el-form>
          <h3 align="center"> TOOLING TRACKING Report </h3>
          <el-table
            ref="issueReportTable"
            v-loading="loading"
            :data="issueReportData"
            border
            stripe
            :height="tableHeight3"
            row-key="id"
            style="width: 100%;font-size: 10px"
            :row-style="{height:'20px'}"
            :cell-style="cellStyle"
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
            @row-dblclick="handleRowDbClick"
          >
            <el-table-column fixed width="180" prop="partName" label="Part Name" />
            <el-table-column fixed align="center" width="120" prop="partNo" label="Part No." />
            <el-table-column fixed align="center" width="65" prop="item" label="Item" />
            <el-table-column fixed prop="issueDescription" width="255" label="Tooling Issue Description" />
            <el-table-column prop="rootCause" width="255" label="Root Cause" />
            <el-table-column v-for="(item1, index1) in issueReportHeader" :key="index1" width="60" align="center" :label="item1">
              <template slot-scope="scope">
                <span>{{ scope.row.txList[index1] }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" width="90" label="操作" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  v-if="scope.row.status === 'CLOSE' || scope.row.status === 'WAIVE'"
                  size="mini"
                  type="text"
                  @click="handleReOpen(scope.row)"
                >ReOpen</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

      </el-tabs>
    </template>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="openAddIssue" width="900px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="190px">
        <el-row>
          <el-col v-if="title === '修改Issue'" :span="24">
            <el-form-item label="Item" prop="item">
              <el-input-number v-model="form.item" :min="1" label="请输入Item" :precision="0" :step="1" />
              <!--              <el-input v-model="form.item" placeholder="请输入Item" />-->
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Tooling Issue Description" prop="issueDescription">
              <el-input v-model="form.issueDescription" placeholder="请输入Tooling Issue Description" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Root Cause" prop="rootCause">
              <!--              <el-input v-model="form.rootCause" placeholder="请输入Root Cause" />-->
              <el-autocomplete
                v-model="form.rootCause"
                class="inline-input"
                :fetch-suggestions="queryRootCause"
                placeholder="请输入Root Cause"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Action" prop="action">
              <el-input v-model="form.action" placeholder="请输入Action" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Priority" prop="issuePriority">
              <el-select
                v-model="form.issuePriority"
                placeholder="Priority"
                size="small"
                style="width: 240px"
              >
                <el-option
                  v-for="dict in priorityOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Status" prop="status">
              <el-select
                v-model="form.status"
                placeholder="Status"
                size="small"
                style="width: 240px"
              >
                <el-option
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Remark" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入Remark" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openAddIssue = false">取 消</el-button>
        <el-button type="primary" @click="submitAddIssue">确 定</el-button>
      </div>
    </el-dialog>

    <!-- create new tx 对话框 -->
    <el-dialog :title="title" :visible.sync="openCreateNewTx" width="600px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="Part Name" prop="partName">
              <el-input v-model="form.partName" :disabled="true" placeholder="请输入Part Name" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Part No." prop="partNo">
              <el-input v-model="form.partNo" :disabled="true" placeholder="请输入Part No." />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Tx" prop="tx">
              <el-input v-model="form.tx" :disabled="true" placeholder="请输入Tx" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="DATE_Plan" prop="datePlan">
              <el-date-picker
                v-model="form.datePlan"
                type="date"
                placeholder="选择 DATE_Plan"
                format="yyyy/MM/dd"
                value-format="yyyy/MM/dd"
                @change="changeDatePlan"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="DATE_Act" prop="dateAct">
              <el-date-picker
                v-model="form.dateAct"
                type="date"
                placeholder="选择 DATE_Act"
                format="yyyy/MM/dd"
                value-format="yyyy/MM/dd"
              />
            </el-form-item>
          </el-col>

        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openCreateNewTx = false">取 消</el-button>
        <el-button type="primary" @click="submitAddIssue">确 定</el-button>
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

    <!--  IssueTracking 数据显示  -->
    <el-dialog :title="title" :visible.sync="openIssueTracking" width="1300px" :close-on-click-modal="false">
      <el-table
        border
        stripe
        height="400"
        row-key="id"
        :data="openIssueTrackingData"
        style="width: 100%"
        :header-cell-style="{background:'#87CEEB',color:'#606266'}"
      >
        <el-table-column prop="partName" label="Part Name" width="180" />
        <el-table-column align="center" width="40" prop="tx" label="Tx">
          <template slot-scope="scope">
            <p v-if="scope.row.tx !== undefined">T{{ scope.row.tx }}</p>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="item" label="Item" width="60" />
        <el-table-column width="260" prop="issueDescription" label="Tooling Issue Description" />
        <el-table-column width="235" prop="rootCause" label="Root Cause" />
        <el-table-column width="233" prop="action" label="Action" />
        <el-table-column width="243" prop="remark" label="Remark" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openIssueTracking = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import { download, selectProtByID } from '@/api/prot'
import { selectBySonProtId, reopenIssue, syncToolPlanData, selectIssueTracking, selectIssueReportBySonProtId, downloadToolTrackList, selectTxHistory, selectLatestTx, selectNewTimeAndUser, deleteToolTrackListIssue, updateToolTrackListNewIssue, addToolTrackListNewIssue, selectAllRootCauseTips } from '@/api/toolingTrackingList'
import { downloadPng, downloadJpg, downloadXlsx } from '@/utils/download'

export default {
  name: 'ToolingTrackingList',
  data() {
    return {
      tableHeight1: 600,
      tableHeight2: 500,
      tableHeight3: 500,
      // issueReportData issue Report数据
      issueReportData: [],
      // issue Report 表头
      issueReportHeader: [],
      // 最新的 更新时间和 更新人
      lastTimeAndUser: { 'updateTime': ' ', 'updateUser': ' ' },
      // 存放 rootCause 提示数据
      rootCauseTips: [],
      // 遮罩层
      loading: true,
      // Priority Options 数据字典
      priorityOptions: [{ 'dictValue': 'Serious', 'dictLabel': 'Serious' }, { 'dictValue': 'Medium', 'dictLabel': 'Medium' }, { 'dictValue': 'Low', 'dictLabel': 'Low' }],
      // Status 数据字典
      statusOptions: [{ 'dictValue': 'OPEN', 'dictLabel': 'OPEN' }, { 'dictValue': 'CLOSE', 'dictLabel': 'CLOSE' }, { 'dictValue': 'WAIVE', 'dictLabel': 'WAIVE' }],
      // 弹出层标题
      title: '',
      // 子项目信息
      subProt: {},
      // 主项目信息
      mainProt: {},
      // 表单参数
      form: {},
      // 哪个标签页生效
      activeName: 'first',
      // 标签页 位置
      tabPosition: 'top',
      // 表格数据
      toolTrackListData: [],
      // 获取 最新的 Latest Tx 数据信息
      latestTxListData: [],
      // 获取 Tx History 数据信息
      txHistoryListData: [],
      // 项目名
      protName: undefined,
      // 子项目id
      sonProtId: undefined,
      // dialog 新增issue dialog
      openAddIssue: false,
      // dialog create new tx
      openCreateNewTx: false,
      // dialog 传图
      openUploadPicture: false,
      // dialog 传Excel
      openUploadExcel: false,
      // dialog Issue Tracking
      openIssueTracking: false,
      // dialog Issue Tracking Data
      openIssueTrackingData: [],
      // 上传 Excel picture  的头
      uploadHeader: {},
      // 传图 参数
      uploadPicture: { protId: undefined, category: 'P1', remark: 'ToolTrackList Issue Picture' },
      // 传Excel 参数
      uploadExcel: { sonProtId: undefined },
      // 是否展开 树 Tool Tracking List
      isOpen: true,
      // 是否展开 树 check issue report
      isOpenIssueReport: false
    }
  },
  mounted() {
    this.$nextTick(function() {
      this.tableHeight1 = window.innerHeight - 190
      this.tableHeight2 = window.innerHeight - 380
      this.tableHeight3 = window.innerHeight - 160
      // 监听窗口大小变化
      const self = this
      window.onresize = function() {
        self.tableHeight1 = window.innerHeight - 190
        self.tableHeight2 = window.innerHeight - 380
        self.tableHeight3 = window.innerHeight - 160
      }
    })
  },
  created() {
    this.getProtInfo()
    this.getToolTrackList()
    this.getNewTimeAndUser()
    this.uploadHeader = { 'AuthorizationToken': getToken() }
  },
  methods: {
    /** issue Report 中 Re open 按钮 */
    handleReOpen(row) {
      this.$confirm('此操作将 ReOpen Item 为 ' + row.item + '的数据， 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        reopenIssue(this.sonProtId, row.id).then(res => {
          if (res.code === '100000') {
            this.$message({ showClose: true, message: 'ReOpen 成功 !', type: 'success' })
            this.getIssueReportData()
          }
        })
      }).catch(() => { this.$message({ type: 'info', message: '已取消 ReOpen' }) })
    },
    /** issue Report 中 数据被双击 */
    handleRowDbClick(row, event, column) {
      if (row.item !== undefined) {
        selectIssueTracking(this.sonProtId, row.id).then(response => {
          this.openIssueTrackingData = response.data
          this.openIssueTracking = true
        })
      }
    },
    /** 点击 sync tool plan 按钮 */
    syncToolPlan() {
      syncToolPlanData(this.sonProtId).then(response => {
        if (response.code === '100000') {
          this.$message({ showClose: true, message: 'Sync 成功！', type: 'success' })
          this.getToolTrackList()
          this.getNewTimeAndUser()
        }
      })
    },
    /** 上传 Excel 按钮 */
    handleUploadExcel() {
      this.title = '导入Excel'
      this.openUploadExcel = true
      this.uploadExcel.sonProtId = this.sonProtId
    },
    /** 点击 下载 按钮  */
    handleDownload() {
      downloadToolTrackList(this.sonProtId).then(res => {
        if (res.code === '100000') {
          if (res.data.split('.')[res.data.split('.').length - 1] === 'xls') {
            downloadXlsx(res.data)
          }
          if (res.data.split('.')[res.data.split('.').length - 1] === 'xlsx') {
            downloadXlsx(res.data)
          }
        }
      })
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
    /** 上传 Picture 成功  */
    handlePictureSuccess(response, file, fileList) {
      this.openUploadPicture = false
      this.$refs.uploadFile.clearFiles()
      this.$message({ showClose: true, message: '上传成功', type: 'success' })
      this.getToolTrackList()
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
    /** 导入 picture 的 url */
    uploadPictureUrl: function() {
      return process.env.VUE_APP_BASE_API + '/protFile/upload'
    },
    /** 改变  DATE_Plan 是 联动改变 DATE_Act */
    changeDatePlan() {
      this.form.dateAct = this.form.datePlan
    },
    /** 点击  修改 LatestTx 按钮 */
    handleEditLatestTx(row) {
      this.reset()
      this.title = '修改 Latest Tx'
      this.form.id = row.id
      this.form.protId = row.protId
      this.form.partName = row.partName
      this.form.partNo = row.partNo
      this.form.datePlan = row.datePlan
      this.form.dateAct = row.dateAct
      this.form.parentId = row.parentId
      this.form.tx = row.tx
      this.form.isShow = row.isShow
      this.openCreateNewTx = true
    },
    /** 点击  Create new Tx 按钮 */
    handleCreateNewTx(row) {
      this.reset()
      this.title = 'Create new Tx'
      this.form.protId = row.protId
      this.form.partNo = row.partNo
      this.form.tx = row.tx + 1
      this.form.parentId = 0
      this.form.isShow = row.isShow
      this.form.partName = row.partName
      const date = new Date()
      this.form.dateAct = date.toLocaleDateString()
      this.form.datePlan = date.toLocaleDateString()
      this.openCreateNewTx = true
    },
    /** 获取 最新的更新时间  更新人 */
    getNewTimeAndUser() {
      selectNewTimeAndUser(this.$route.query.sonProtId).then(response => {
        if (response.data.lastTime !== undefined && response.data.lastUser !== undefined) {
          this.lastTimeAndUser.updateTime = response.data.lastTime
          this.lastTimeAndUser.updateUser = response.data.lastUser
        }
      })
    },
    handleImportPicture(row) {
      this.title = '上传图片'
      this.openUploadPicture = true
      this.uploadPicture.protId = row.id
    },
    /** 删除 issue 按钮操作 */
    handleDeleteIssue(row) {
      this.$confirm('此操作将永久删除 Item 为 ' + row.item + '的数据， 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteToolTrackListIssue(row.id, this.subProt.parentId).then(res => {
          if (res.code === '100000') {
            if (res.data.msgFail !== undefined) {
              this.$message({ showClose: true, message: res.data.msgFail, type: 'error' })
            } else {
              this.$message({ showClose: true, message: '删除成功', type: 'success' })
              this.getToolTrackList()
              this.getNewTimeAndUser()
            }
          }
        })
      }).catch(() => { this.$message({ type: 'info', message: '已取消删除' }) })
    },
    /** 更新 issue 按钮操作 */
    handleUpdateIssue(row) {
      this.reset()
      this.getNewRootCause()
      this.title = '修改Issue'
      this.form.id = row.id
      this.form.parentId = row.parentId
      this.form.protId = row.protId
      this.form.item = row.item
      this.form.issueDescription = row.issueDescription
      this.form.action = row.action
      this.form.issuePriority = row.issuePriority
      this.form.status = row.status
      this.form.remark = row.remark
      this.form.rootCause = row.rootCause
      this.openAddIssue = true
    },
    /** 新增issue 按钮操作 */
    handleInsertIssue(row) {
      this.reset()
      this.getNewRootCause()
      this.title = '新增Issue'
      this.form.parentId = row.id
      this.form.protId = row.protId
      this.form.partNo = row.partNo
      this.form.tx = row.tx
      this.form.issuePriority = 'Serious'
      this.form.status = 'OPEN'
      this.form.isFirst = 'true'
      this.openAddIssue = true
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        protId: undefined,
        partNo: undefined,
        partName: undefined,
        tx: undefined,
        item: undefined,
        isFirst: undefined,
        isShow: undefined,
        issueDescription: undefined,
        action: undefined,
        rootCause: undefined,
        issuePriority: undefined,
        status: undefined,
        datePlan: undefined,
        dateAct: undefined,
        remark: undefined,
        parentId: undefined
      }
    },
    /** 获取 ToolingTrackingList */
    getToolTrackList() {
      this.loading = true
      selectBySonProtId(this.$route.query.sonProtId).then(response => {
        this.toolTrackListData = response.data
        this.loading = false
      })
    },
    /** 获取 最新的 Latest Tx */
    getLatestTxBySonProtId() {
      selectLatestTx(this.sonProtId).then(response => {
        this.latestTxListData = response.data
      })
    },
    /** 获取 Tx History */
    getTxHistoryBySonProtId() {
      selectTxHistory(this.sonProtId).then(response => {
        this.txHistoryListData = response.data
      })
    },
    /** 获取 first second third */
    handleTabClick(tab, event) {
      if (tab.name === 'first') {
        this.getToolTrackList()
        this.getNewTimeAndUser()
      }
      if (tab.name === 'second') {
        this.getLatestTxBySonProtId()
        this.getTxHistoryBySonProtId()
      }
      if (tab.name === 'third') {
        this.getIssueReportData()
        this.isOpenIssueReport = false
        setTimeout(() => {
          this.expandAllIssueReport()
        }, 555)
      }
    },
    /** TOOLING TRACKING Report 数据 */
    getIssueReportData() {
      this.loading = true
      selectIssueReportBySonProtId(this.sonProtId).then(response => {
        this.issueReportData = response.data.tableData
        this.issueReportHeader = response.data.tableHeader
        setTimeout(() => {
          this.$refs.issueReportTable.doLayout()
        }, 180)
        this.loading = false
      })
    },
    /** 添加或修改对话框 提交按钮 */
    submitAddIssue: function() {
      if (this.form.id !== undefined) { // 修改
        updateToolTrackListNewIssue(this.form).then(response => {
          if (response.code === '100000') {
            this.$message({ showClose: true, message: '修改成功', type: 'success' })
            if (this.title === '修改Issue') {
              this.openAddIssue = false
              this.getToolTrackList()
              this.getNewTimeAndUser()
            } else if (this.title === '修改 Latest Tx') {
              this.getLatestTxBySonProtId()
              this.getTxHistoryBySonProtId()
              this.openCreateNewTx = false
            }
          }
        })
      } else { // 添加
        addToolTrackListNewIssue(this.form).then(response => {
          if (response.code === '100000') {
            if (response.data.msgFail !== undefined) {
              this.$message({ showClose: true, message: response.data.msgFail, type: 'error' })
            } else {
              if (this.title === '新增Issue') {
                this.$message({ showClose: true, message: '添加成功!!!', type: 'success' })
                this.openAddIssue = false
                this.getToolTrackList()
                this.getNewTimeAndUser()
              } else if (this.title === 'Create new Tx') {
                this.$message({ showClose: true, message: 'Create new Tx 成功!!!', type: 'success' })
                this.getLatestTxBySonProtId()
                this.getTxHistoryBySonProtId()
                this.openCreateNewTx = false
              }
            }
          } else {
            this.$message({ showClose: true, message: '操作失败', type: 'error' })
          }
        })
      }
    },
    /** 获取 项目 信息 */
    getProtInfo() {
      this.sonProtId = this.$route.query.sonProtId
      selectProtByID(this.sonProtId).then(response => {
        if (response.code === '100000') {
          this.subProt = response.data
          selectProtByID(this.subProt.parentId).then(mainRes => {
            this.mainProt = mainRes.data
            this.protName = this.mainProt.name + ' / ' + this.subProt.name
          })
        }
      })
    },
    // 展开 关闭 功能 Tool tracking list
    forArr(arr, isExpand) {
      arr.forEach(i => {
        this.$refs.topicTable.toggleRowExpansion(i, isExpand)
        if (i.children) {
          this.forArr(i.children, isExpand)
        }
      })
    },
    // 展开 关闭 功能 issue report
    forArrIssueReport(arr, isExpand) {
      arr.forEach(i => {
        this.$refs.issueReportTable.toggleRowExpansion(i, isExpand)
        if (i.children) {
          this.forArr(i.children, isExpand)
        }
      })
    },
    expandAll() { // 默认展开全部的话，首次加载需要在$nextTick下进行
      this.forArr(this.toolTrackListData, true)
      this.isOpen = false
    },
    expandAllIssueReport() {
      this.forArrIssueReport(this.issueReportData, true)
      this.isOpenIssueReport = false
    },
    collapseAll() {
      this.forArr(this.toolTrackListData, false)
      this.isOpen = true
    },
    collapseAllIssueReport() {
      this.forArrIssueReport(this.issueReportData, false)
      this.isOpenIssueReport = true
    },
    /** 获取最新 rootCauseTips rootCause提示信息 */
    getNewRootCause() {
      selectAllRootCauseTips().then(response => {
        this.rootCauseTips = response.data
      })
    },
    /** 带输入建议  rootCauseTips */
    queryRootCause(queryString, cb) {
      const partNumberTips = this.rootCauseTips
      const results = queryString ? partNumberTips.filter(this.createFilter(queryString)) : partNumberTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) >= 0)
      }
    },
    /** 获取表格中 对应的 背景色 */
    cellStyle({ row, column, rowIndex, columnIndex }) {
      let cellStyle = ''
      if (columnIndex > 4) {
        if (row.txList[columnIndex - 5] === 'X') {
          const isTx = this.issueReportHeader.indexOf(column.label)
          if (isTx > -1) {
            cellStyle = 'background: red;color:white;padding:0px'
            return cellStyle
          } else {
            cellStyle = 'padding:0px'
            return cellStyle
          }
        } else if (row.txList[columnIndex - 5] === 'O') {
          const isTx = this.issueReportHeader.indexOf(column.label)
          if (isTx > -1) {
            cellStyle = 'background: green;color:white;padding:0px'
            return cellStyle
          } else {
            cellStyle = 'padding:0px'
            return cellStyle
          }
        } else {
          cellStyle = 'padding:0px'
          return cellStyle
        }
      } else {
        cellStyle = 'padding:0px'
        return cellStyle
      }
    }
  }
}
</script>

<style scoped>
  .app-container {
    padding: 20px;
  }
  .inline-input {
    width: 666px;
  }
</style>
