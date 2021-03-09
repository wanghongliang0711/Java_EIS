<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item label="Part Number">
        <el-input
          v-model="queryParams.partNumber"
          placeholder="请输入Part Number"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />

      </el-form-item>
      <el-form-item label="Part Description">
        <el-input
          v-model="queryParams.partDescription"
          placeholder="请输入Part Description"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索</el-button>
        <el-button
          v-if="checkPermission(['ROOT','ADMIN'])"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
        <el-button
          v-if="checkPermission(['ROOT','ADMIN'])"
          type="info"
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
        >导入</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" style="width: 100%" :data="partNumList" row-key="id">
      <el-table-column width="170" label="Part Number" prop="partNumber" />
      <el-table-column label="Part Description" prop="partDescription" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            v-if="checkPermission(['ROOT','ADMIN'])"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-if="checkPermission(['ROOT'])"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div id="mpage" class="block">
      <el-pagination
        v-show="total>0"
        :page-sizes="[10, 20, 40, 80]"
        :page-size="10"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col :span="18">
            <el-form-item label="零件号" prop="partNumber">
              <el-input v-model="form.partNumber" placeholder="请输入Part Number" />
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="零件描述" prop="partDescription">
              <el-input v-model="form.partDescription" type="textarea" />
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
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" :close-on-click-modal="false">
      <el-upload
        ref="uploadFile"
        class="upload-demo"
        drag
        :limit="1"
        :headers="uploadHeader"
        :auto-upload="false"
        :before-upload="beforeUpload"
        :on-success="handleFileSuccess"
        accept=".xlsx, .xls"
        :action="uploadUrl()"
      >
        <i class="el-icon-upload" />
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip" style="color:red">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="upload.open = false">取 消</el-button>
        <el-button type="primary" @click="submitUpload">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 重复的数据对话框 -->
    <el-dialog :title="repeatData.title" :visible.sync="repeatData.open" width="600px" :close-on-click-modal="false">
      <template>
        <el-table
          :data="upload.repeatPartNum"
          height="350"
          border
          style="width: 100%"
        >
          <el-table-column
            prop="partNumber"
            label="Part Number"
          />
          <el-table-column
            prop="partDescription"
            label="Part Description"
          />
        </el-table>
      </template>
    </el-dialog>

  </div>
</template>

<script>
import { deletePartNum, listPartNum, addPartNum, updatePartNum } from '@/api/presupposition'
import checkPermission from '@/utils/permission'
import { getToken } from '@/utils/auth'
export default {
  name: 'PartNumber',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 表格数据
      partNumList: [],
      // 是否显示弹出层
      open: false,
      // 弹出层标题
      title: '',
      // 表单参数
      form: {},
      // 总条数
      total: 0,
      // 导入 excel 参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: '',
        // file
        file: undefined,
        // 重复的 PartNum
        repeatPartNum: []
      },
      repeatData: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: ''
      },
      // 导入 excel Header
      uploadHeader: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        partNumber: undefined,
        partDescription: undefined
      },
      // 表单校验
      rules: {
        partNumber: [
          { required: true, message: 'Part Number不能为空', trigger: 'blur' }
        ],
        partDescription: [
          { required: true, min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.uploadHeader = { 'AuthorizationToken': getToken() }
  },
  methods: {
    checkPermission,
    /** 点击导入按钮操作 */
    handleImport() {
      this.upload.title = '导入Excel'
      this.upload.open = true
      this.upload.file = undefined
      this.upload.repeatPartNum = []
    },
    /** beforeUpload  */
    beforeUpload(file) {
      const extension = file.name.split('.')[file.name.split('.').length - 1] === 'xls'
      const extension2 = file.name.split('.')[file.name.split('.').length - 1] === 'xlsx'
      if (!extension && !extension2) {
        this.$message({
          message: '上传文件只能是 xls、xlsx格式!',
          type: 'error'
        })
      }
      return extension || extension2
    },
    /** 上传excel 成功 */
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false
      this.$refs.uploadFile.clearFiles()
      this.$message({ showClose: true, message: '上传成功', type: 'success' })
      this.upload.repeatPartNum = response.data
      if (this.upload.repeatPartNum.length > 0) {
        this.repeatData.open = true
        this.repeatData.title = '有 ' + this.upload.repeatPartNum.length + ' 条Part Number重复的数据项:'
      }
      this.getList()
    },
    /** 导入excel的 url  */
    uploadUrl: function() {
      return process.env.VUE_APP_BASE_API + '/partNum/uploadPartNum'
    },
    /** 点击确定上传按钮 */
    submitUpload: function() {
      // 触发组件的action
      this.$refs.uploadFile.submit()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加Part Number'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.title = '修改Part Number'
      this.form.id = row.id
      this.form.partNumber = row.partNumber
      this.form.partDescription = row.partDescription
      this.open = true
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updatePartNum(this.form).then(response => {
              if (response.code === '100000') {
                this.$message({ showClose: true, message: '修改成功', type: 'success' })
                this.open = false
                this.getList()
              }
            })
          } else {
            addPartNum(this.form).then(response => {
              if (response.code === '100000') {
                this.$message({ showClose: true, message: '新增成功', type: 'success' })
                this.open = false
                this.getList()
              } else {
                this.$message({ showClose: true, message: response.message, type: 'error' })
                this.getList()
              }
            })
          }
        }
      })
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        partNumber: undefined,
        partDescription: ''
      }
      this.resetForm('form')
    },
    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields()
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 查询第几页 */
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    /** 每页多少条 */
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除 "' + row.partNumber + '" 的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return deletePartNum(row.id)
      }).then(() => {
        this.getList()
        this.$message({ showClose: true, message: '删除成功', type: 'success' })
      }).catch(function() {})
    },
    /** 查询部门列表 */
    getList() {
      this.loading = true
      listPartNum(this.queryParams).then(response => {
        this.partNumList = response.data.list
        this.total = parseInt(response.data.total)
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
  .app-container {
    padding: 20px;
  }
  #mpage{
    margin: 0 auto;
    text-align: right;
  }

</style>
