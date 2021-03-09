<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item label="材料主种类">
        <el-input
          v-model="queryParams.material"
          placeholder="请输入材料主种类"
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
      :data="materialClassList"
      row-key="id"
      height="600"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column
        prop="material"
        label="材料种类"
        width="140"
      />
      <el-table-column
        prop="brand"
        label="品牌"
        width="300"
      />
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.parentId==='0' && checkPermission(['ROOT','ADMIN'])"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
          >新增子类</el-button>
          <el-button
            v-if="scope.row.code==='2' && checkPermission(['ROOT','ADMIN'])"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAddBrand(scope.row)"
          >新增品牌/型号</el-button>
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

    <!-- 添加或修改 模具种类 对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col v-if="form.code === '1' ||form.code === '2'" :span="24">
            <el-form-item label="材料类型" prop="material">
              <el-input v-model="form.material" placeholder="请输入材料类型" />
            </el-form-item>
          </el-col>
          <el-col v-if="form.code === '3'" :span="24">
            <el-form-item label="品牌/型号" prop="brand">
              <el-input v-model="form.brand" placeholder="请输入品牌/型号" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { addMaterial, listOneMaterial, addBrand, updateBrand, deleteMaterial, updateMaterial } from '@/api/presupposition'
import checkPermission from '@/utils/permission'

export default {
  name: 'MaterialClassification',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 表格数据
      materialClassList: [],
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 弹出层标题
      title: '',
      // 查询参数
      queryParams: {
        material: ''
      },
      // 表单校验
      rules: {
        material: [
          { required: true, message: '材料类型不能为空', trigger: 'blur' }
        ],
        brand: [
          { required: true, min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
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
      this.$confirm('是否确认删除该数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return deleteMaterial(row.id)
      }).then(() => {
        this.getList()
        this.$message({ showClose: true, message: '删除成功', type: 'success' })
      }).catch(function() {})
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      // eslint-disable-next-line eqeqeq
      if (row.code === '1') {
        this.title = '修改材料种类'
      } else if (row.code === '2') {
        this.title = '修改材料子类'
      } else if (row.code === '3') {
        this.title = '品牌/型号'
      }
      this.form.id = row.id
      this.form.material = row.material
      this.form.brand = row.brand
      this.form.code = row.code
      this.form.parentId = row.parentId
      this.open = true
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            if (this.form.code === '3') {
              updateBrand(this.form).then(response => {
                if (response.code === '100000') {
                  this.$message({ showClose: true, message: '修改成功', type: 'success' })
                  this.open = false
                  this.getList()
                }
              })
            } else if (this.form.code === '1' || this.form.code === '2') {
              updateMaterial(this.form).then(response => {
                if (response.code === '100000') {
                  this.$message({ showClose: true, message: '修改成功', type: 'success' })
                  this.open = false
                  this.getList()
                }
              })
            }
          } else {
            if (this.form.code === '3') {
              addBrand(this.form).then(response => {
                if (response.code === '100000') {
                  this.$message({ showClose: true, message: '新增成功', type: 'success' })
                  this.open = false
                  this.getList()
                }
              })
            } else if (this.form.code === '1' || this.form.code === '2') {
              addMaterial(this.form).then(response => {
                if (response.code === '100000') {
                  this.$message({ showClose: true, message: '新增成功', type: 'success' })
                  this.open = false
                  this.getList()
                }
              })
            }
          }
        }
      })
    },
    handleAddBrand(row) {
      this.reset()
      this.title = '添加品牌/型号'
      this.form.code = '3'
      this.form.parentId = row.id
      this.open = true
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      if (row.id === undefined) {
        this.title = '添加材料种类'
        this.form.code = '1'
      } else {
        this.title = '添加材料子类'
        this.form.parentId = row.id
        this.form.code = '2'
      }
      this.open = true
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        material: undefined,
        brand: undefined,
        code: undefined,
        parentId: 0
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 查询 材料种类 */
    getList() {
      this.loading = true
      listOneMaterial(this.queryParams).then(response => {
        this.materialClassList = response.data
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
</style>
