<template>
  <div class="app-container">
    <el-tag type="success" v-text="'主项目名: '+ protName">主项目名</el-tag>
    <br>
    <br>
    <template>
      <el-tabs v-model="activeName" :tab-position="tabPosition" @tab-click="handleClick">
        <el-tab-pane label="项目成员" name="first">
          <el-table
            v-loading="loading"
            :data="projectMember"
            border
            stripe
            style="width: 100%"
            :default-sort="{prop: 'member', order: 'descending'}"
          >
            <el-table-column
              prop="username"
              label="用户名"
              width="180"
              sortable
            />
            <el-table-column
              prop="jobNum"
              label="工号"
              width="180"
              sortable
            />
            <el-table-column
              prop="deptName"
              label="部门"
            />
            <el-table-column sortable prop="member" label="身份">
              <template slot-scope="scope">
                <p v-if="scope.row.member=='0'">普通成员</p>
                <p v-if="scope.row.member=='1'">项目管理员</p>
              </template>
            </el-table-column>
            <el-table-column label="操作" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  v-if="scope.row.member=='0'"
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDeleteProjectMember(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>

          </el-table>
        </el-tab-pane>

        <el-tab-pane label="添加项目成员" name="second">
          <el-table
            v-loading="loading"
            :data="addMemList"
            border
            stripe
            style="width: 100%"
            :default-sort="{prop: 'deptName', order: 'descending'}"
          >
            <el-table-column
              prop="username"
              label="用户名"
              width="180"
              sortable
            />
            <el-table-column
              prop="jobNum"
              label="工号"
              width="180"
              sortable
            />
            <el-table-column
              prop="deptName"
              label="部门"
              sortable
            />
            <el-table-column label="操作" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-plus"
                  @click="handleAddProjectMember(scope.row)"
                >添加</el-button>
              </template>
            </el-table-column>

          </el-table>
        </el-tab-pane>

        <el-tab-pane label="改变项目管理员" name="third">
          <el-table
            v-loading="loading"
            :data="changeOwnerList"
            border
            stripe
            style="width: 100%"
            :default-sort="{prop: 'deptName', order: 'descending'}"
          >
            <el-table-column
              prop="username"
              label="用户名"
              width="180"
              sortable
            />
            <el-table-column
              prop="jobNum"
              label="工号"
              width="180"
              sortable
            />
            <el-table-column
              prop="deptName"
              label="部门"
              sortable
            />
            <el-table-column label="操作" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-warning"
                  @click="handleChangeProjectAdmin(scope.row)"
                >更改</el-button>
              </template>
            </el-table-column>

          </el-table>

        </el-tab-pane>
      </el-tabs>
    </template>
  </div>
</template>

<script>
import { deleteByUserIdProtId, findByProtId, addMemList, addMember, changeOwnerList, changeOwner } from '@/api/prot'
export default {
  name: 'ProtMember',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 项目名
      protName: '',
      // 标签页 位置
      tabPosition: 'top',
      // 哪个标签页生效
      activeName: 'first',
      // 项目的成员
      projectMember: [],
      // 可以添加的成员列表
      addMemList: [],
      // 改变项目管理员 列表
      changeOwnerList: [],
      // 删除 项目成员 的参数
      deleteProjectMember: {},
      // 添加 项目成员 的参数
      addProjectMember: {},
      // 更改 项目管理员 的参数
      changeProjectAdmin: {}
    }
  },
  created() {
    this.getProtMember()
  },
  methods: {
    /** 更改 项目管理员 操作 */
    handleChangeProjectAdmin(row) {
      this.$confirm('此操作将 ' + row.username + ' 变更为项目管理员, 你将被从项目中移除， 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.changeProjectAdmin = { protId: undefined, userId: undefined }
        this.changeProjectAdmin.userId = row.id
        this.changeProjectAdmin.protId = localStorage.getItem('memberMainProtId')
        changeOwner(this.changeProjectAdmin).then(response => {
          if (response.code === '100000') {
            this.getChangeOwnerList()
            this.$message({ type: 'success', message: '更改成功!' })
          }
        })
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消更改' })
      })
    },
    /** 添加 项目成员操作 */
    handleAddProjectMember(row) {
      this.$confirm('此操作将 ' + row.username + ' 添加到项目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.addProjectMember = { protId: undefined, userId: undefined }
        this.addProjectMember.userId = row.id
        this.addProjectMember.protId = localStorage.getItem('memberMainProtId')
        addMember(this.addProjectMember).then(response => {
          if (response.code === '100000') {
            this.addProtMemberList()
            this.$message({ type: 'success', message: '添加成功!' })
          }
        })
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消添加' })
      })
    },
    /** 删除项目成员操作 */
    handleDeleteProjectMember(row) {
      this.$confirm('此操作将 ' + row.username + ' 从项目中删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteProjectMember = { protId: undefined, userId: undefined }
        this.deleteProjectMember.userId = row.id
        this.deleteProjectMember.protId = localStorage.getItem('memberMainProtId')
        deleteByUserIdProtId(this.deleteProjectMember).then(response => {
          if (response.code === '100000') {
            this.getProtMember()
            this.$message({ type: 'success', message: '删除成功!' })
          }
        })
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消删除' })
      })
    },
    /** 获取项目成员列表 */
    getProtMember() {
      this.loading = true
      this.protName = localStorage.getItem('memberMainProtName')
      findByProtId(localStorage.getItem('memberMainProtId')).then(response => {
        this.projectMember = response.data
        this.loading = false
      })
    },
    /** 获取 添加 项目成员列表 */
    addProtMemberList() {
      this.loading = true
      addMemList(localStorage.getItem('memberMainProtId')).then(response => {
        this.addMemList = response.data
        this.loading = false
      })
    },
    /** 获取 改变项目管理员 列表 */
    getChangeOwnerList() {
      this.loading = true
      changeOwnerList(localStorage.getItem('memberMainProtId')).then(response => {
        this.changeOwnerList = response.data
        this.loading = false
      })
    },
    handleClick(tab, event) {
      if (tab.name === 'first') {
        this.getProtMember()
      }
      if (tab.name === 'second') {
        this.addProtMemberList()
      }
      if (tab.name === 'third') {
        this.getChangeOwnerList()
      }
    }
  }
}
</script>

<style scoped>

</style>
