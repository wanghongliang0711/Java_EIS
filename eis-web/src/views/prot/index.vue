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

    <el-table
      v-loading="loading"
      :data="protList"
      row-key="id"
      height="600"
      default-expand-all
      border
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
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
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-if="scope.row.parentId==0"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
          >新增子项目</el-button>
          <el-button
            v-if="(scope.row.parentId==0 && checkPermission(['ROOT'])) || scope.row.parentId!=0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
          <el-button
            v-if="scope.row.parentId==0"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="assignProjectMember(scope.row)"
          >添加成员</el-button>
          <el-button
            v-if="scope.row.parentId==0"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="selectProjectMember(scope.row)"
          >查看成员</el-button>
          <el-button
            v-if="scope.row.parentId!=0"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="viewSubprojects(scope.row)"
          >查看子项目</el-button>

        </template>
      </el-table-column>
    </el-table>

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
            <el-form-item v-if="this.title=='添加主项目' || this.title=='修改主项目'" label="项目code" prop="code">
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

    <!-- 查看成员对话框 -->
    <el-dialog :title="title" :visible.sync="openSelectProjectMember" width="600px" :close-on-click-modal="false">
      <template>
        <el-table
          :data="projectMember"
          height="250"
          border
          style="width: 100%"
        >
          <el-table-column
            prop="username"
            label="用户名"
            width="180"
          />
          <el-table-column
            prop="jobNum"
            label="工号"
            width="180"
          />
          <el-table-column
            prop="deptName"
            label="部门"
          />
          <el-table-column label="操作" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDeleteProjectMember(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>

        </el-table>
      </template>
    </el-dialog>

  </div>
</template>

<script>
import { deleteRootProtById, deleteSonProt, addRootProt, addSonProt, listProt, updateProt, findByProtId, deleteByUserIdProtId } from '@/api/prot'
import checkPermission from '@/utils/permission'

export default {
  name: 'Index',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 表格数据
      protList: [],
      // 表格数据
      projectMember: [],
      // 是否显示弹出层
      open: false,
      // 是否显示 查看成员 弹出层
      openSelectProjectMember: false,
      // 弹出层标题
      title: '',
      // 状态数据字典
      statusOptions: [{ 'dictValue': 0, 'dictLabel': '待完成' }, { 'dictValue': 1, 'dictLabel': '已完成' }],
      // 表单参数
      form: {},
      // 表单参数
      formProjectMember: {},
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
      },
      // 查询参数
      queryParams: {
        protName: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    checkPermission,
    /** 查看子项目详情 */
    viewSubprojects(row) {
      // this.$router.push({ name: 'subProt', params: { subProt: row }})
      // this.$router.push({ name: 'subProt', query: { id: row.id }})
      // 8/21 参数在路径里，看能否解决 退出后参数丢失的问题 https://www.cnblogs.com/liuliu-/p/13050545.html
      // 可以尝试localStorage，不删除就永久保存，不同于 cookie
      // 点击预设项，会跑到404，修改 router 应该可以解决 修改模具种类 的 path 为 index
      const routeData = this.$router.resolve({ name: 'subProt', query: { id: row.id }})
      window.open(routeData.href, '_blank')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 查询部门列表 */
    getList() {
      this.loading = true
      listProt(this.queryParams).then(response => {
        // console.log(response)
        this.protList = response.data
        this.loading = false
      })
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      if (row.id === undefined) {
        this.title = '添加主项目'
      } else {
        this.title = '添加子项目'
        this.form.parentId = row.id
      }
      this.open = true

      /** 发送请求 */
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      // eslint-disable-next-line eqeqeq
      if (row.parentId == 0) {
        this.title = '修改主项目'
      } else {
        this.title = '修改子项目'
      }
      this.form.id = row.id
      this.form.name = row.name
      this.form.code = row.code
      this.form.remark = row.remark
      this.form.parentId = row.parentId
      this.form.status = row.status
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
            // eslint-disable-next-line eqeqeq
            if (this.form.parentId == 0) {
              addRootProt(this.form).then(response => {
                if (response.code === '100000') {
                  this.$message({ showClose: true, message: '新增主项目成功', type: 'success' })
                  this.open = false
                  this.getList()
                }
              })
            } else {
              addSonProt(this.form).then(response => {
                if (response.code === '100000') {
                  this.$message({ showClose: true, message: '新增子项目成功', type: 'success' })
                  this.open = false
                  this.getList()
                }
              })
            }
          }
        }
      })
    },
    /** 添加成员 */
    assignProjectMember(row) {
      console.log('添加成员')
    },
    /** 查看成员 */
    selectProjectMember(row) {
      this.formProjectMember = { protId: undefined, userId: undefined }
      this.title = '项目 ' + row.name + '的成员'
      this.formProjectMember.protId = row.id
      this.openSelectProjectMember = true
      this.selectProjectMemberData()
    },
    selectProjectMemberData() {
      findByProtId(this.formProjectMember.protId).then(response => {
        this.projectMember = response.data
      })
    },
    /** 删除项目成员操作 */
    handleDeleteProjectMember(row) {
      this.$confirm('此操作将 ' + row.username + ' 从项目中删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.formProjectMember.userId = row.id
        deleteByUserIdProtId(this.formProjectMember).then(response => {
          if (response.code === '100000') {
            this.selectProjectMemberData()
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除名称为" ' + row.name + ' "的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        // eslint-disable-next-line eqeqeq
        if (row.parentId == 0) {
          return deleteRootProtById(row.id)
        } else {
          return deleteSonProt(row.id, row.parentId)
        }
        // console.log(row)
        // return delProt(row.id)
      }).then(() => {
        this.getList()
        this.$message({ showClose: true, message: '删除成功', type: 'success' })
      }).catch(function() {})
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
    }
  }
}
</script>

<style scoped>
  .app-container {
    padding: 20px;
  }

</style>
