<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <div v-text="'子项目: '+this.subProt.name" />
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
        >导入</el-button>
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="subProtList"
      row-key="id"
      default-expand-all
      border
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column
        prop="name"
        label="项目名"
        width="200"
        sortable
      />
      <el-table-column
        prop="parts"
        label="部件"
        width="150"
        sortable
      />
      <el-table-column
        prop="describe"
        width="250"
        label="备注"
      />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
        </template>
      </el-table-column>
    </el-table>

    <br>
    <br>
    <br>
    <el-table
      :data="subProtExcelList"
      row-key="id"
      default-expand-all
      border
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column
        prop="name"
        label="文件名"
        width="200"
        sortable
      />
      <el-table-column
        prop="size"
        label="文件大小(KB)"
        width="150"
        sortable
      />
      <el-table-column
        prop="remark"
        width="250"
        label="备注"
      />
      <el-table-column label="类别" prop="category" width="150">
        <template slot-scope="scope">
          <p v-if="scope.row.category==='A'">ME Test Report</p>
          <p v-if="scope.row.category==='B'">Dfx Guidline</p>
          <p v-if="scope.row.category==='C'">Feasibility Study Report</p>
          <p v-if="scope.row.category==='D'">Structure Analysis Report</p>
          <p v-if="scope.row.category==='E'">Themal Analysis Report</p>
          <p v-if="scope.row.category==='F'">Tooling DFM Reports</p>
          <p v-if="scope.row.category==='G'">Tolerance Analysis Report</p>
          <p v-if="scope.row.category==='H'">Assembly Notice</p>
        </template>
      </el-table-column>
      <el-table-column label="上传时间" prop="createTime" width="200" sortable>
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownload(scope.row)"
          >下载</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改 对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col :span="18">
            <el-form-item label="项目名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="部件" prop="parts">
              <el-input v-model="form.parts" placeholder="请输入部件" />
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="备注" prop="describe">
              <el-input v-model="form.describe" type="textarea" />
            </el-form-item>
          </el-col>

        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form')">重置</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 上传文件 对话框-->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="600px" append-to-body>
      <el-form ref="upload" :model="upload" :rules="uploadRules" label-width="100px" class="demo-ruleForm">
        <el-form-item label="备注" prop="remark">
          <el-input v-model="upload.remark" type="textarea" />
        </el-form-item>
        <el-form-item label="类别" prop="category">
          <el-select
            v-model="upload.category"
            placeholder="文件类别"
            clearable
            size="small"
            style="width: 240px"
          >
            <el-option
              v-for="dict in uploadOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
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
            accept=".xlsx, .xls, .pdf, .pptx"
            :action="uploadUrl()"
            multiple
          >
            <i class="el-icon-upload" />
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div slot="tip" class="el-upload__tip" style="color:red">提示：仅允许导入“xls”或“xlsx”或“pdf”或“pptx”格式文件！</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('upload')">重置</el-button>
        <el-button type="primary" @click="submitUpload">导入</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listTest, selectProtByID, updateTest, getSubProtExcel, download } from '@/api/prot'
import { downloadXlsx, downloadPdf, downloadPptx } from '@/utils/download'
import { getToken } from '@/utils/auth'

export default {
  name: 'SubProt',
  data() {
    return {
      // 子项目信息
      subProt: {},
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 遮罩层
      loading: true,
      // 表格数据
      subProtList: [],
      // 子项目Excel数据
      subProtExcelList: [],
      // 子项目id
      subProtId: '',
      // 导入 excel 参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: '',
        // 子项目id
        protId: undefined,
        // file
        file: undefined,
        // 文件备注
        remark: '',
        // 文件类别
        category: 'A'
      },
      uploadHeader: {},
      // 上传文件表单校验
      uploadRules: {
        category: [
          { required: true, message: '文件类型不能为空', trigger: 'blur' }
        ],
        remark: [
          { min: 0, max: 150, message: '长度在 0 到 150 个字符', trigger: 'blur' }
        ]
      },
      uploadOptions: [{ 'dictValue': 'A', 'dictLabel': 'ME Test Report' },
        { 'dictValue': 'B', 'dictLabel': 'Dfx Guidline' },
        { 'dictValue': 'C', 'dictLabel': 'Feasibility Study Report' },
        { 'dictValue': 'D', 'dictLabel': 'Structure Analysis Report' },
        { 'dictValue': 'E', 'dictLabel': 'Themal Analysis Report' },
        { 'dictValue': 'F', 'dictLabel': 'Tooling DFM Reports' },
        { 'dictValue': 'G', 'dictLabel': 'Tolerance Analysis Report' },
        { 'dictValue': 'H', 'dictLabel': 'Assembly Notice' }],
      // 表单校验
      rules: {
        name: [
          { required: true, message: '项目名称不能为空', trigger: 'blur' }
        ],
        parts: [
          { required: true, message: '部件不能为空', trigger: 'blur' }
        ],
        describe: [
          { min: 0, max: 150, message: '长度在 0 到 150 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getSubProt()
    this.getList()
    this.getExcelList()
    this.uploadHeader = { 'AuthorizationToken': getToken() }
  },
  methods: {
    getList() {
      // this.subProt = this.$route.params.subProt
      listTest(this.subProtId).then(response => {
        this.subProtList = response.data
        this.loading = false
      })
    },
    /** 点击导入按钮操作 */
    handleImport() {
      this.upload.title = '导入Excel'
      this.upload.remark = ''
      this.upload.open = true
      this.upload.file = undefined
      this.upload.category = 'A'
      this.upload.protId = this.subProtId
    },
    /** beforeUpload  */
    beforeUpload(file) {
      const extension = file.name.split('.')[file.name.split('.').length - 1] === 'xls'
      const extension2 = file.name.split('.')[file.name.split('.').length - 1] === 'xlsx'
      const extension3 = file.name.split('.')[file.name.split('.').length - 1] === 'pdf'
      const extension4 = file.name.split('.')[file.name.split('.').length - 1] === 'pptx'
      if (!extension && !extension2 && !extension3 && !extension4) {
        this.$message({
          message: '上传文件只能是 xls、xlsx、pdf、pptx格式!',
          type: 'error'
        })
      }
      this.upload.file = file
      return extension || extension2 || extension3 || extension4
    },
    /** 导入excel的 url */
    uploadUrl: function() {
      return process.env.VUE_APP_BASE_API + '/protFile/upload'
    },
    /** 点击导入 url --》 uploadUrl */
    submitUpload: function() {
      this.$refs['upload'].validate(valid => {
        if (valid) {
          // 触发组件的action
          this.$refs.uploadFile.submit()
        }
      })
    },
    /** 上传excel 成功 */
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false
      this.$refs.uploadFile.clearFiles()
      this.$message({ showClose: true, message: '上传成功', type: 'success' })
      this.getExcelList()
    },
    /** 上传的文件 成功 */
    getExcelList() {
      getSubProtExcel({ 'protId': this.subProtId }).then(response => {
        this.subProtExcelList = response.data
      })
    },
    getSubProt() {
      // this.subProtId = this.$route.query.id
      this.subProtId = this.$route.query.id
      selectProtByID(this.subProtId).then(response => {
        this.subProt = response.data
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateTest(this.form).then(response => {
              if (response.code === '100000') {
                this.$message({ showClose: true, message: '修改成功', type: 'success' })
                this.open = false
                this.getList()
              }
            })
          } else {
            console.log('this.form.id == undefined')
          }
        }
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.title = '修改'
      this.form.id = row.id
      this.form.name = row.name
      this.form.parts = row.parts
      this.form.describe = row.describe
      this.open = true
    },
    /** 下载按钮操作 */
    handleDownload(row) {
      download(row.id).then(res => {
        console.log(res)
        if (res.code === '100000') {
          if (res.data.split('.')[res.data.split('.').length - 1] === 'xls') {
            downloadXlsx(res.data)
          }
          if (res.data.split('.')[res.data.split('.').length - 1] === 'xlsx') {
            downloadXlsx(res.data)
          }
          if (res.data.split('.')[res.data.split('.').length - 1] === 'pdf') {
            downloadPdf(res.data)
          }
          if (res.data.split('.')[res.data.split('.').length - 1] === 'pptx') {
            downloadPptx(res.data)
          }
        }
      })
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        parts: undefined,
        describe: undefined,
        rootProtId: this.subProt.parentId
      }
      this.resetForm('form')
    },
    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields()
      }
    }
  }
}
</script>

<style scoped>
  .app-container {
    padding: 20px;
  }
  .mb8 {
    margin-bottom: 8px;
  }

</style>
