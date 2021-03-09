<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item label="项目名称">
        <el-input
          v-model="queryParams.protName"
          placeholder="请输入项目名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="项目状态"
          clearable
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
          type="primary"
          icon="el-icon-open"
          size="mini"
          @click="handleOnlyFollow"
        >显示关注</el-button>
      </el-form-item>
    </el-form>

    <el-table
      ref="table1"
      v-loading="loading"
      :data="protList"
      :row-style="{height:'20px'}"
      :cell-style="{padding:'0px'}"
      :height="tableHeight1"
      style="width: 100%;font-size: 10px"
      row-key="id"
      border
      :default-sort="{prop: 'code', order: 'descending'}"
      @row-dblclick="handleRowDBClick"
    >
      <el-table-column
        prop="name"
        label="项目名"
        width="300"
        sortable
      />
      <el-table-column
        prop="code"
        label="项目code"
        width="150"
        sortable
      />
      <el-table-column prop="status" label="状态" height="10" width="100">
        <template slot-scope="scope">
          <p v-if="scope.row.status=='0'">待完成</p>
          <p v-if="scope.row.status=='1'">已完成</p>
        </template>
      </el-table-column>
      <el-table-column label="关注" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.follow"
            active-value="0"
            inactive-value="1"
            @change="handleFollowChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="remark"
        width="250"
        label="备注"
      />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleRowClick(scope.row)"
          >子项目</el-button>
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
          <el-button
            v-if="checkPermission(['ROOT','ADMIN'])"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleEditProtMember(scope.row)"
          >编辑成员</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div v-if="isPage===true" id="mpage" class="block">
      <el-pagination
        v-show="total>0"
        :page-sizes="[20, 40, 60, 80]"
        :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 添加或修改项目对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col :span="18">
            <el-form-item label="项目名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="项目code" prop="code">
              <el-input v-model="form.code" placeholder="请输入项目code" />
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="项目备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" />
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="项目状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form')">重置</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import { listProt, addRootProt, updateProt, deleteRootProtById, addUserFollowProt, deleteUserFollowByUserIdProtId, selectFollowByUserId } from '@/api/prot'

export default {
  name: 'MainProt',
  data() {
    return {
      tableHeight1: 500,
      // 是否显示 页数
      isPage: true,
      // 遮罩层
      loading: true,
      // 主项目表格数据
      protList: [],
      // 总条数
      total: 0,
      // 弹出层标题
      title: '',
      // 状态数据字典
      statusOptions: [{ 'dictValue': 0, 'dictLabel': '待完成' }, { 'dictValue': 1, 'dictLabel': '已完成' }],
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        protName: undefined,
        status: undefined
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: '项目名称不能为空', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '项目code不能为空', trigger: 'blur' }
        ],
        remark: [
          { min: 0, max: 150, message: '长度在 0 到 150 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.$nextTick(function() {
      this.tableHeight1 = window.innerHeight - 210
      // 监听窗口大小变化
      const self = this
      window.onresize = function() {
        self.tableHeight1 = window.innerHeight - 210
      }
    })
  },
  created() {
    this.getList()
  },
  methods: {
    checkPermission,
    /** 关注 取消关注 操作 */
    handleFollowChange(row) {
      const text = row.follow === '0' ? '关注' : '取消关注'
      this.$confirm('确认要"' + text + '"项目"' + row.name + '"吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        if (row.follow === '0') {
          return addUserFollowProt(row.id)
        } else {
          return deleteUserFollowByUserIdProtId(row.id)
        }
      }).then(() => {
        if (this.isPage === false) {
          this.handleOnlyFollow()
        }
        this.$message({ showClose: true, message: text + '成功', type: 'success' })
      }).catch(function() {
        row.follow = row.follow === '0' ? '1' : '0'
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.title = '修改主项目'
      this.form.id = row.id
      this.form.name = row.name
      this.form.code = row.code
      this.form.remark = row.remark
      this.form.parentId = row.parentId
      this.form.status = row.status
      this.open = true
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.title = '添加主项目'
      this.open = true
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateProt(this.form).then(response => {
              if (response.code === '100000') {
                this.$message({ showClose: true, message: '修改成功', type: 'success' })
                this.open = false
                this.getList()
              }
            })
          } else {
            addRootProt(this.form).then(response => {
              if (response.code === '100000') {
                this.$message({ showClose: true, message: '新增主项目成功', type: 'success' })
                this.open = false
                this.getList()
              }
            })
          }
        }
      })
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        code: undefined,
        remark: undefined,
        parentId: 0,
        status: 0
      }
      this.resetForm('form')
    },
    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields()
      }
    },
    /** 编辑 项目 成员 */
    handleEditProtMember(row) {
      localStorage.setItem('memberMainProtId', row.id)
      localStorage.setItem('memberMainProtName', row.name)
      this.$router.push({ name: 'protMember' })
    },
    /** 双击 触发进入子项目 */
    handleRowDBClick(row, event, column) {
      localStorage.setItem('mainProtId', row.id)
      localStorage.setItem('mainProtName', row.name)
      this.$router.push({ name: 'subProt' })
    },
    /** 点击一行 触发进入子项目 */
    handleRowClick(row) {
      localStorage.setItem('mainProtId', row.id)
      localStorage.setItem('mainProtName', row.name)
      this.$router.push({ name: 'subProt' })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除名称为" ' + row.name + ' "的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return deleteRootProtById(row.id)
      }).then(() => {
        this.getList()
        this.$message({ showClose: true, message: '删除成功', type: 'success' })
      }).catch(function() {})
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 每页多少条 */
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 查询第几页 */
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    /** 查询 关注 项目 */
    handleOnlyFollow() {
      this.loading = true
      this.isPage = false
      selectFollowByUserId().then(response => {
        this.protList = response.data
        this.loading = false
      })
    },
    /** 查询项目列表 */
    getList() {
      this.isPage = true
      this.loading = true
      listProt(this.queryParams).then(response => {
        // console.log(response)
        this.protList = response.data.list
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
  .mb8 {
    margin-bottom: 8px;
  }
</style>
