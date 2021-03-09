<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item>
        <el-tag v-text="'项 目 : '+ protName">主项目名 / 子项目名</el-tag>&#12288;&#12288;&#12288;&#12288;&#12288;
        <!--        <el-button-->
        <!--          type="primary"-->
        <!--          size="mini"-->
        <!--          @click="getAllFile"-->
        <!--        >显示所有</el-button>-->
        <el-button
          type="primary"
          size="mini"
          @click="getNewFile"
        >只显示最新版</el-button>
      </el-form-item>
      <el-form-item label="历史版本查询" prop="status">
        <el-select
          v-model="queryParams.category"
          placeholder="历史版本查询"
          size="small"
          style="width: 240px"
          @change="handleChangeCategory"
        >
          <el-option
            v-for="dict in categoryOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <el-table
      :data="protFileList"
      border
      stripe
      :row-style="{height:'20px'}"
      :cell-style="{padding:'0px'}"
      :height="tableHeight1"
      style="width: 100%;font-size: 10px"
      :header-cell-style="{background:'#87CEEB',color:'#606266'}"
    >
      <el-table-column width="200" prop="fileType" label="文件类型" sortable />
      <el-table-column width="400" prop="name" label="文件名" sortable />
      <el-table-column align="center" label="上传" width="80" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.isUpload==='true'"
            size="mini"
            type="text"
            icon="el-icon-upload2"
            @click="handleUpload(scope.row)"
          >上传</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="下载" width="80" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            :disabled="disabledDownload(scope.row)"
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownload(scope.row)"
          >下载</el-button>
        </template>
      </el-table-column>
      <el-table-column width="120" align="center" prop="createTime" label="上传日期" sortable />
      <el-table-column width="120" align="center" prop="createUser" label="上传人" sortable />
      <el-table-column prop="remark" label="备注" />
    </el-table>
    <!-- 上传文件 对话框-->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="upload" :model="upload" :rules="uploadRules" label-width="100px" class="demo-ruleForm">
        <el-form-item label="文件" prop="file">
          <el-upload
            ref="uploadFile"
            class="upload-demo"
            drag
            :data="upload"
            :limit="1"
            :headers="uploadHeader"
            :auto-upload="false"
            :before-upload="beforeUpload"
            :on-success="handleFileSuccess"
            accept=".xlsx, .xls, .ppt, .pptx, .doc, .docx, .pdf"
            :action="uploadUrl()"
          >
            <i class="el-icon-upload" />
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div slot="tip" class="el-upload__tip" style="color:red">提示：仅允许导入“xls”、“xlsx”、“ppt”、“pptx”、“doc”、“docx”、“pdf”结尾的文件！</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="upload.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">取 消</el-button>
        <el-button type="primary" @click="submitUpload">导 入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { selectProtByID } from '@/api/prot'
import { selectNewestFile, downloadFile, selectAllFile } from '@/api/document'
import { getToken } from '@/utils/auth'
import { downloadPdf, downloadPptx, downloadXlsx, downloadPpt, downloadDoc, downloadDocx } from '@/utils/download'
export default {
  name: 'Document',
  data() {
    return {
      tableHeight1: 600,
      // dialog title
      title: '',
      // open dialog
      open: false,
      // 类别数据字典
      categoryOptions: [{ 'dictValue': 'subProtFile', 'dictLabel': 'All' }, { 'dictValue': 'subProtFile_A', 'dictLabel': 'ME Test Report' }, { 'dictValue': 'subProtFile_B', 'dictLabel': 'Dfx Guidline' },
        { 'dictValue': 'subProtFile_C', 'dictLabel': 'Feasibility Study Report' }, { 'dictValue': 'subProtFile_D', 'dictLabel': 'Structure Analysis Report' },
        { 'dictValue': 'subProtFile_E', 'dictLabel': 'Themal Analysis Report' }, { 'dictValue': 'subProtFile_F', 'dictLabel': 'Tooling DFM Reports' },
        { 'dictValue': 'subProtFile_G', 'dictLabel': 'DFMEA' }, { 'dictValue': 'subProtFile_H', 'dictLabel': 'Tolerance Analysis Report' },
        { 'dictValue': 'subProtFile_I', 'dictLabel': 'Assembly Notice' }],
      // 表单参数
      form: {},
      // 项目文件格数据
      protFileList: [],
      uploadHeader: {},
      // 上传 文件 参数
      upload: {
        // 子项目id
        protId: undefined,
        // 文件备注
        remark: undefined,
        // 文件类别
        category: ''
      },
      // 查询参数
      queryParams: {
        protId: undefined,
        category: undefined
      },
      // 上传文件表单校验
      uploadRules: {
        remark: [
          { min: 0, max: 150, message: '长度在 0 到 150 个字符', trigger: 'blur' }
        ]
      },
      // 子项目信息
      subProt: {},
      // 主项目信息
      mainProt: {},
      // 子项目id
      sonProtId: undefined,
      // 项目名
      protName: undefined,
      // 查找类型
      findType: 'new'
    }
  },
  mounted() {
    this.$nextTick(function() {
      this.tableHeight1 = window.innerHeight - 90
      // 监听窗口大小变化
      const self = this
      window.onresize = function() {
        self.tableHeight1 = window.innerHeight - 90
      }
    })
  },
  created() {
    this.getProtInfo()
    this.getNewFile()
    this.uploadHeader = { 'AuthorizationToken': getToken() }
  },
  methods: {
    /** 下载按钮操作 */
    handleDownload(row) {
      if (row.name !== undefined && row.id.length > 2) {
        downloadFile(row.id).then(res => {
          if (res.code === '100000') {
            if (res.data.split('.')[res.data.split('.').length - 1] === 'xls') {
              downloadXlsx(res.data)
            } else if (res.data.split('.')[res.data.split('.').length - 1] === 'xlsx') {
              downloadXlsx(res.data)
            } else if (res.data.split('.')[res.data.split('.').length - 1] === 'pdf') {
              downloadPdf(res.data)
            } else if (res.data.split('.')[res.data.split('.').length - 1] === 'pptx') {
              downloadPptx(res.data)
            } else if (res.data.split('.')[res.data.split('.').length - 1] === 'ppt') {
              downloadPpt(res.data)
            } else if (res.data.split('.')[res.data.split('.').length - 1] === 'doc') {
              downloadDoc(res.data)
            } else if (res.data.split('.')[res.data.split('.').length - 1] === 'docx') {
              downloadDocx(res.data)
            }
          }
        })
      }
    },
    /** 点击导入 按钮 */
    submitUpload: function() {
      this.$refs['upload'].validate(valid => {
        if (valid) {
          // 触发组件的action
          this.$refs.uploadFile.submit()
        }
      })
    },
    /** 导入excel的 url */
    uploadUrl: function() {
      return process.env.VUE_APP_BASE_API + '/protFile/uploadForDocument'
    },
    /** 上传 成功 */
    handleFileSuccess(response, file, fileList) {
      this.$refs.uploadFile.clearFiles()
      if (response.message === '无数据权限') {
        this.$message({ showClose: true, message: '无数据权限！！！', type: 'error' })
      } else if (response.message === '操作成功') {
        this.open = false
        this.$message({ showClose: true, message: '上传成功', type: 'success' })
      } else {
        this.$message({ showClose: true, message: '上传失败！！！', type: 'error' })
      }
      // if (this.findType === 'all') {
      //   this.getAllFile()
      // } else
      if (this.findType === 'category') {
        this.handleChangeCategory()
      } else if (this.findType === 'new') {
        this.getNewFile()
      } else {
        this.getNewFile()
      }
    },
    /** beforeUpload  */
    beforeUpload(file) {
      const extension1 = file.name.split('.')[file.name.split('.').length - 1] === 'xls'
      const extension2 = file.name.split('.')[file.name.split('.').length - 1] === 'xlsx'
      const extension3 = file.name.split('.')[file.name.split('.').length - 1] === 'ppt'
      const extension4 = file.name.split('.')[file.name.split('.').length - 1] === 'pptx'
      const extension5 = file.name.split('.')[file.name.split('.').length - 1] === 'doc'
      const extension6 = file.name.split('.')[file.name.split('.').length - 1] === 'docx'
      const extension7 = file.name.split('.')[file.name.split('.').length - 1] === 'pdf'
      if (!extension1 && !extension2 && !extension3 && !extension4 && !extension5 && !extension6 && !extension7) {
        this.$message({
          message: '上传的文件只能是 xls、xlsx、ppt、pptx、doc、docx、pdf 结尾的文件!',
          type: 'error'
        })
      }
      return extension1 || extension2 || extension3 || extension4 || extension5 || extension6 || extension7
    },
    /** 点击 上传 按钮 */
    handleUpload(row) {
      if (this.$refs.uploadFile !== undefined) {
        this.$refs.uploadFile.clearFiles()
      }
      if (row.category !== undefined && this.sonProtId !== undefined) {
        this.title = '上传 ' + row.fileType + ' 类型的数据'
        this.upload.protId = this.sonProtId
        this.upload.category = row.category
        this.upload.remark = ''
        this.open = true
      }
    },
    /** 下载按钮 是否可用 */
    disabledDownload(row) {
      return row.name === undefined
    },
    handleChangeCategory() {
      this.findType = 'category'
      if (this.queryParams.protId !== undefined) {
        selectAllFile(this.queryParams).then(response => {
          this.protFileList = response.data
        })
      }
    },
    /** 获取 所有  文件 */
    // getAllFile() {
    //   this.findType = 'all'
    //   this.queryParams.category = undefined
    //   if (this.queryParams.protId !== undefined) {
    //     selectAllFile(this.queryParams).then(response => {
    //       this.protFileList = response.data
    //     })
    //   }
    // },
    /** 获取 项目 最新 文件 */
    getNewFile() {
      this.findType = 'new'
      this.queryParams.category = undefined
      selectNewestFile(this.sonProtId).then(response => {
        this.protFileList = response.data
      })
    },
    /** 获取 项目 信息 */
    getProtInfo() {
      this.sonProtId = this.$route.query.sonProtId
      this.queryParams.protId = this.sonProtId
      selectProtByID(this.sonProtId).then(response => {
        if (response.code === '100000') {
          this.subProt = response.data
          selectProtByID(this.subProt.parentId).then(mainRes => {
            this.mainProt = mainRes.data
            this.protName = this.mainProt.name + ' / ' + this.subProt.name
          })
        }
      })
    }
  }
}
</script>

<style scoped>
  .app-container {
    padding: 20px;
  }
</style>
