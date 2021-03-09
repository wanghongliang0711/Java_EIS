<template>
  <div class="app-container">

    <el-form :inline="true">
      <el-form-item label="部门名称">
        <el-input
          v-model="queryParams.deptName"
          placeholder="请输入部门名称"
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
      :data="deptList"
      row-key="id"
      height="600"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="name" label="部门名称" width="260" />

      <el-table-column v-if="checkPermission(['ROOT','ADMIN'])" label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="checkPermission(['ROOT','ADMIN'])"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-if="checkPermission(['ROOT','ADMIN'])"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
          >新增</el-button>
          <el-button
            v-if="scope.row.parentId !== '0' && checkPermission(['ROOT'])"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col v-if="form.parentId !== '0'" :span="18">
            <el-form-item label="上级部门" prop="parentId">
              <treeselect v-model="form.parentId" :options="deptOptions" :normalizer="normalizer" placeholder="选择上级部门" />
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="部门名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入部门名称" />
            </el-form-item>
          </el-col>

        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listDept, addDept, delDept, getDept, updateDept } from '@/api/dept'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import checkPermission from '@/utils/permission'

export default {
  name: 'Index',
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 表格树数据
      deptList: [],
      // 部门树选项
      deptOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        deptName: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        parentId: [
          { required: true, message: '上级部门不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '部门名称不能为空', trigger: 'blur' }
        ]
      }

    }
  },
  created() {
    this.getList()
  },
  methods: {
    checkPermission,
    /** 查询部门列表 */
    getList() {
      this.loading = true
      // console.log("查询部门列表")
      listDept(this.queryParams).then(response => {
        // console.log(response.data)
        // this.deptList = response.data
        this.deptList = this.handleTree(response.data, 'id', 'parentId', 'children', '0')
        this.loading = false
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      getDept(row.id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改部门'
      })
      listDept().then(response => {
        for (let i = 0; i < response.data.length; i++) {
          // eslint-disable-next-line eqeqeq
          if (response.data[i].id === this.form.id) {
            response.data.splice(i, 1)
          }
        }
        this.deptOptions = this.handleTree(response.data, 'id', 'parentId', 'children', '0')
      })
    },

    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        parentId: undefined,
        name: undefined
      }
      this.resetForm('form')
    },

    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields()
      }
    },

    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      // eslint-disable-next-line eqeqeq
      if (row !== undefined) {
        this.form.parentId = row.id
      }
      this.open = true
      this.title = '添加部门'
      /** 发送请求 */
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, 'id', 'parentId', 'children', '0')
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },

    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },

    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          // eslint-disable-next-line eqeqeq
          if (this.form.id !== undefined) {
            updateDept(this.form).then(response => {
              if (response.code === '100000') {
                this.$message({ showClose: true, message: '修改成功', type: 'success' })
                this.open = false
                this.getList()
              }
            })
          } else {
            addDept(this.form).then(response => {
              if (response.code === '100000') {
                this.$message({ showClose: true, message: '新增成功', type: 'success' })
                this.open = false
                this.getList()
              }
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除名称为"' + row.name + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delDept(row.id)
      }).then(() => {
        this.getList()
        this.$message({ showClose: true, message: '删除成功', type: 'success' })
      }).catch(function() {})
    },

    /**
         * 构造树型结构数据
         * @param {*} data 数据源
         * @param {*} id id字段 默认 'id'
         * @param {*} parentId 父节点字段 默认 'parentId'
         * @param {*} children 孩子节点字段 默认 'children'
         * @param {*} rootId 根Id 默认 0
         */
    handleTree(data, id, parentId, children, rootId) {
      id = id || 'id'
      parentId = parentId || 'parentId'
      children = children || 'children'
      rootId = rootId || '0'
      // 对源数据深度克隆
      const cloneData = JSON.parse(JSON.stringify(data))
      const treeData = cloneData.filter(father => {
        const branchArr = cloneData.filter(child => {
          // 返回每一项的子级数组
          return father[id] === child[parentId]
        })
        branchArr.length > 0 ? father.children = branchArr : ''
        // 返回第一层
        return father[parentId] === rootId
      })
      // eslint-disable-next-line eqeqeq
      return treeData !== '' ? treeData : data
    },
    /** 转换部门数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        // 去掉children=[]的children属性
        delete node.children
      }
      return {
        id: node.id,
        // 将name转换成必填的label键
        label: node.name,
        children: node.children
      }
    }

  }

}
</script>

<!-- 局部样式 -->
<style scoped>
  .app-container {
    padding: 20px;
  }
  .filter-item {
    display: inline-block;
    vertical-align: middle;
    margin-bottom: 10px;
  }

</style>
