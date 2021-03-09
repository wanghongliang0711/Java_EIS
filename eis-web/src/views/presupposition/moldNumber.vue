<template>
  <div class="app-container" style="width: 70%">
    <el-form :inline="true">
      <el-form-item label="模穴数">
        <el-input
          v-model="queryParams.moldNumber"
          placeholder="请输入模穴数"
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
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="moldNumList" row-key="id">
      <el-table-column label="模穴数" prop="moldNumber" />
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
    <el-dialog :title="title" :visible.sync="open" width="600px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col :span="18">
            <el-form-item label="模穴数" prop="moldNumber">
              <el-input v-model="form.moldNumber" placeholder="请输入模穴数" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import { listMoldNum, addMoldNum, updateMoldNum, deleteMoldNum } from '@/api/presupposition'
export default {
  name: 'MoldNumber',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 表格数据
      moldNumList: [],
      // 是否显示弹出层
      open: false,
      // 弹出层标题
      title: '',
      // 总条数
      total: 0,
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        moldNumber: undefined
      },
      // 表单校验
      rules: {
        moldNumber: [
          { required: true, message: '模穴数不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    checkPermission,
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除 "' + row.moldNumber + '" 的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return deleteMoldNum(row.id)
      }).then(() => {
        this.getList()
        this.$message({ showClose: true, message: '删除成功', type: 'success' })
      }).catch(function() {})
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.title = '修改模穴数'
      this.form.id = row.id
      this.form.moldNumber = row.moldNumber
      this.open = true
    },
    /** dialog 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateMoldNum(this.form).then(response => {
              if (response.code === '100000') {
                this.$message({ showClose: true, message: '修改成功', type: 'success' })
                this.open = false
                this.getList()
              }
            })
          } else {
            addMoldNum(this.form).then(res => {
              if (res.code === '100000') {
                this.$message({ showClose: true, message: '新增成功', type: 'success' })
                this.open = false
                this.getList()
              }
            })
          }
        }
      })
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加模穴数'
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        moldNumber: undefined
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
    /** 查询 模穴数 列表 */
    getList() {
      this.loading = true
      listMoldNum(this.queryParams).then(res => {
        this.moldNumList = res.data.list
        this.total = parseInt(res.data.total)
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
