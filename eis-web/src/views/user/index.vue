<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="deptName"
            placeholder="请输入部门名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            ref="tree"
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>

      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
          <el-form-item label="用户名称" prop="userName">
            <el-input
              v-model="queryParams.username"
              placeholder="请输入用户名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="用户状态"
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
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          </el-form-item>

        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              v-if="checkPermission(['ROOT','ADMIN'])"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-if="checkPermission(['ROOT'])"
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="batchDelete"
            >删除</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="userList" border stripe height="500" @selection-change="handleSelectionChange">
          <el-table-column v-if="checkPermission(['ROOT','ADMIN'])" type="selection" width="50" align="center" />
          <el-table-column label="用户名称" align="center" prop="username" :show-overflow-tooltip="true" />
          <el-table-column label="工号" width="120" align="center" prop="jobNum" :show-overflow-tooltip="true" />
          <el-table-column label="邮箱" align="center" prop="email" :show-overflow-tooltip="true" />
          <el-table-column label="部门" width="140" align="center" prop="deptName" :show-overflow-tooltip="true" />
          <el-table-column label="状态" width="90" align="center" prop="status">
            <template slot-scope="scope">
              <p v-if="scope.row.status=='0'">正常</p>
              <p v-if="scope.row.status=='1'">停用</p>
            </template>
          </el-table-column>
          <el-table-column
            v-if="checkPermission(['ROOT','ADMIN'])"
            label="操作"
            width="220"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                v-if="scope.row.id !== '0' && checkPermission(['ROOT','ADMIN'])"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
              >修改</el-button>
              <el-button
                v-if="scope.row.id !== '0' && checkPermission(['ROOT','ADMIN'])"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdatePower(scope.row)"
              >权限</el-button>
              <el-button
                v-if="scope.row.id !== '0' && checkPermission(['ROOT'])"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
              <el-button
                v-if="scope.row.id !== '0' && checkPermission(['ROOT','ADMIN'])"
                size="mini"
                type="text"
                icon="el-icon-key"
                @click="handleResetPwd(scope.row)"
              >重置</el-button>
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
      </el-col>
    </el-row>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归属部门" prop="deptId">
              <treeselect v-model="form.deptId" :options="deptOptions" :disable-branch-nodes="true" :show-count="true" placeholder="请选择归属部门" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="工号" prop="jobNum">
              <el-input v-model="form.jobNum" placeholder="请输入工号" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态">
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
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="openRole" width="600px" :close-on-click-modal="false">
      <el-form ref="ruleForm" :model="ruleForm" :rules="rulesRole" label-width="100px" class="demo-ruleForm">
        <el-form-item label="权限修改" prop="type">
          <el-checkbox-group v-model="ruleForm.type">
            <el-checkbox
              v-for="item in powerRoleOptions"
              :key="item.id"
              :label="item.name"
              name="type"
            />
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormRole">确 定</el-button>
        <!--        <el-button @click="cancel">取 消</el-button>-->
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { treeselect } from '@/api/dept'
import { listUser, addUser, delUser, batchDeleteUser, resetUserPwd, updateUser, updateUserRole } from '@/api/user'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import checkPermission from '@/utils/permission'
export default {
  name: 'Index',
  components: { Treeselect },
  data() {
    return {
      values: [],
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 非多个禁用
      multiple: true,
      // 选中数组
      ids: [],
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 是否显示修改权限弹出层
      openRole: false,
      // 部门名称
      deptName: undefined,
      // 表单参数
      form: {},
      // 权限表单参数
      formRole: { 'id': undefined, 'roleIds': undefined },
      ruleForm: { type: [] },
      // 弹出层标题
      title: '',
      // 状态数据字典
      statusOptions: [{ 'dictValue': 0, 'dictLabel': '正常' }, { 'dictValue': 1, 'dictLabel': '停用' }],
      // 用户角色字典
      powerRoleOptions: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: undefined,
        status: 0,
        deptId: undefined
      },
      // 用户表格数据
      userList: null,
      rulesRole: {
        type: [
          { type: 'array', required: true, message: '请至少选择一个权限', trigger: 'change' }
        ]
      },
      // 表单校验
      rules: {
        username: [
          { required: true, message: '用户名称不能为空', trigger: 'blur' }
        ],
        deptId: [
          { required: true, message: '归属部门不能为空', trigger: 'blur' }
        ],
        jobNum: [
          { required: true, message: '用户工号不能为空', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '邮箱地址可以为空', trigger: 'blur' },
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change']
          }
        ]
      }
    }
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getList()
    this.getTreeselect()
    this.getAllRole()
  },
  methods: {
    checkPermission,
    /** 将用户角色 写死， */
    getAllRole() {
      this.powerRoleOptions = [{ id: '1', code: 'USER', name: '用户' }, { id: '2', code: 'ADMIN', name: '管理员' }]
      // getAllRole().then(response => {
      //   this.powerRoleOptions = response.data
      // })
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then(response => {
        this.deptOptions = response.data
      })
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
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.getTreeselect()
      this.open = true
      this.title = '添加用户'
    },
    /** 提交用户角色按钮 */
    submitFormRole: function() {
      this.$refs['ruleForm'].validate(valid => {
        if (valid) {
          // eslint-disable-next-line no-undef
          const roleOptions = {}
          const role_ids = []
          // eslint-disable-next-line no-empty
          for (let i = 0; i < this.powerRoleOptions.length; i++) {
            roleOptions[this.powerRoleOptions[i].id] = this.powerRoleOptions[i].name
          }
          const findKey = (value, compare = (a, b) => a === b) => {
            return Object.keys(roleOptions).find(k => compare(roleOptions[k], value))
          }
          for (let j = 0; j < this.ruleForm.type.length; j++) {
            role_ids.push(findKey(this.ruleForm.type[j]))
          }
          updateUserRole({ 'id': this.ruleForm.id, 'roleIds': role_ids }).then(res => {
            if (res.code === '100000') {
              this.$message({ showClose: true, message: '修改用户权限成功', type: 'success' })
              this.openRole = false
              this.getList()
            }
          })
        }
      })
      // this.getList()  要判断 res.code === '100000' 后在进行查询，不然后时间差，查出来的数据可能没有及时更新
      // console.log('111')
      // this.$router.push('/user/index')
      // this.getList()
      // this.$router.go(0)
      // this.$message({ showClose: true, message: '修改用户权限成功', type: 'success' })
      // this.getList()
      // console.log('122')
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          // eslint-disable-next-line eqeqeq
          if (this.form.id != undefined) {
            updateUser(this.form).then(response => {
              if (response.code === '100000') {
                this.$message({ showClose: true, message: '修改用户信息成功', type: 'success' })
                this.open = false
                this.getList()
              }
            })
          } else {
            addUser(this.form).then(response => {
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
        deptId: undefined,
        username: undefined,
        email: undefined,
        jobNum: undefined,
        status: 0
      }
      this.resetForm('form')
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields()
      }
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$confirm('是否确认重置用户"' + row.username + '"的密码为工号?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return resetUserPwd(row.id)
      }).then(() => {
        this.$message({ showClose: true, message: '修改成功', type: 'success' })
      }).catch(function() {})
    },
    /** 修改权限按钮操作 */
    handleUpdatePower(row) {
      this.ruleForm.type = []
      this.ruleForm.id = row.id
      console.log(row.roleIds)
      const roleOptions = {}
      for (let i = 0; i < this.powerRoleOptions.length; i++) {
        roleOptions[this.powerRoleOptions[i].id] = this.powerRoleOptions[i].name
      }
      for (let i = 0; i < row.roleIds.length; i++) {
        this.ruleForm.type.push(roleOptions[row.roleIds[i]])
      }
      this.openRole = true
      this.title = '修改' + row.username + '用户的权限'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getTreeselect()
      this.form.id = row.id
      this.form.deptId = row.deptId
      this.form.username = row.username
      this.form.jobNum = row.jobNum
      this.form.status = row.status
      this.form.email = row.email
      this.open = true
      this.title = '修改用户'
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const userId = row.id
      this.$confirm('是否确认删除用户"' + row.username + '"?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        const params = { 'userId': userId }
        return delUser(params)
      }).then(() => {
        this.getList()
        this.$message({ showClose: true, message: '删除成功', type: 'success' })
      }).catch(function() {})
    },
    /** 批量删除按钮操作 */
    batchDelete() {
      const userIds = this.ids
      this.$confirm('是否确认删除所选用户', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return batchDeleteUser(userIds)
      }).then(() => {
        this.getList()
        this.$message({ showClose: true, message: '删除成功', type: 'success' })
      }).catch(function() {})
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id
      this.getList()
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.deptId = undefined
      this.getList()
    },
    /** 查询用户列表 */
    getList() {
      this.loading = true
      listUser(this.queryParams).then(response => {
        this.userList = response.data.list
        this.total = parseInt(response.data.total)
        this.loading = false
      })
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.multiple = !selection.length
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
