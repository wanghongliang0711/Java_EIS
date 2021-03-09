<template>
  <div class="app-container">
    <template>
      <el-tabs v-model="activeName" :tab-position="tabPosition" @tab-click="handleTabClick">
        <el-tab-pane label="Milestones" name="first">
          <h3 align="center"> Milestones (已关注)</h3>
          <el-table
            v-loading="loading"
            :data="firstProtStatus"
            :row-style="{height:'20px'}"
            :cell-style="{padding:'0px'}"
            style="width: 985px;font-size: 10px"
            border
            stripe
            :height="tableHeight1"
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
          >
            <el-table-column align="center" width="190" prop="protDes" label="Project Description" />
            <el-table-column align="center" width="120" prop="protCode" label="Project Code" />
            <el-table-column align="center" width="190" prop="sonProtDes" label="Sub Project" />
            <el-table-column align="center" width="160" prop="toolStart" label="Tooling Start" />
            <el-table-column align="center" width="160" prop="dvt" label="DVT" />
            <el-table-column align="center" width="160" prop="pvt" label="PVT" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="Tooling Status" name="second">
          <el-form :inline="true">
            <el-form-item label="项目名称">
              <el-select
                v-model="queryParams.protId"
                filterable
                placeholder="请选择项目"
                @visible-change="dragGetAllProtOptions"
                @change="handleChangeProtId"
              >
                <el-option
                  v-for="item in protOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="子项目名称">
              <el-select
                v-model="queryParams.subProtId"
                filterable
                clearable
                placeholder="请选择子项目"
                @visible-change="dragGetAllSonProtOptions"
                @change="handleChangeSonProtId"
              >
                <el-option
                  v-for="item in sonProtOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="Bug严重度">
              <el-select
                v-model="queryParams.issuePriority"
                multiple
                placeholder="请选择 Bug严重度"
                @change="handleChangePriority"
              >
                <el-option
                  v-for="item in priorityOptions"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-form>
          <el-button
            v-if="isOpen === true"
            type="primary"
            size="mini"
            @click="expandAll"
          >全部展示</el-button>
          <el-button
            v-if="isOpen === false"
            type="primary"
            size="mini"
            @click="collapseAll"
          >全部隐藏</el-button>
          <h4 align="center"> TOOLING TRACKING LIST </h4>
          <el-table
            ref="topicTable"
            :data="secondToolingStatusData"
            border
            stripe
            :row-style="{height:'20px'}"
            :cell-style="{padding:'0px'}"
            :height="tableHeight2"
            row-key="id"
            style="width: 100%;font-size: 10px"
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
          >
            <el-table-column align="center" width="150" prop="sonProtName" label="子项目名" />
            <el-table-column align="center" width="180" prop="partName" label="Part Name" />
            <el-table-column align="center" width="120" prop="partNo" label="Part No" />
            <el-table-column align="center" width="120" prop="dateAct" label="DATE_Act" />
            <el-table-column align="center" width="40" prop="tx" label="Tx">
              <template slot-scope="scope">
                <p v-if="scope.row.tx !== undefined">T{{ scope.row.tx }}</p>
              </template>
            </el-table-column>

            <el-table-column align="center" width="60" prop="item" label="Item" />
            <el-table-column prop="issueDescription" label="Tooling Issue Description" />
            <el-table-column prop="action" label="Action" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="Pilot Run Report" name="third">
          third
        </el-tab-pane>

        <el-tab-pane label="To Do List" name="fourth">
          fourth
        </el-tab-pane>

      </el-tabs>
    </template>
  </div>
</template>

<script>
import { selectQueryRecord, selectProtStatus, updateDailyReportQueryRecord, selectToolTrackList } from '@/api/dashboard'
import { selectAllProtForTips, selectAllSonProtForTips } from '@/api/prot'

export default {
  name: 'Dashboard',
  data() {
    return {
      tableHeight1: 600,
      tableHeight2: 500,
      // Priority Options 数据字典
      priorityOptions: [{ 'dictValue': 'Serious', 'dictLabel': 'Serious' }, { 'dictValue': 'Medium', 'dictLabel': 'Medium' }, { 'dictValue': 'Low', 'dictLabel': 'Low' }],
      // 所有 主项目
      protOptions: [],
      // 所有 主项目 对应的 子项目
      sonProtOptions: [],
      // second 数据 Tooling status
      secondToolingStatusData: [],
      // first 数据 protStatus
      firstProtStatus: [],
      // 哪个标签页生效
      activeName: 'first',
      // 标签页 位置
      tabPosition: 'top',
      // 查询参数
      queryParams: { protId: undefined, subProtId: undefined, issuePriority: [] },
      // 是否展开 树
      isOpen: true,
      // 遮罩层
      loading: true
    }
  },
  mounted() {
    this.$nextTick(function() {
      this.tableHeight1 = window.innerHeight - 215
      this.tableHeight2 = window.innerHeight - 255
      // 监听窗口大小变化
      const self = this
      window.onresize = function() {
        self.tableHeight1 = window.innerHeight - 215
        self.tableHeight2 = window.innerHeight - 255
      }
    })
  },
  created() {
    this.getQueryRecord()
    this.getMilestonesData()
  },
  methods: {
    /** issuePriority 值改变时 需要做的 动作 */
    handleChangePriority() {
      this.updateQueryRecord()
      if (this.activeName === 'first') {
        console.log('issuePriority--first')
      } else if (this.activeName === 'second') {
        this.getToolStatusData()
      } else if (this.activeName === 'third') {
        console.log('issuePriority--third')
      } else if (this.activeName === 'fourth') {
        console.log('issuePriority--fourth')
      }
    },
    /** 子项目 值改变时 需要做的 动作 */
    handleChangeSonProtId() {
      this.updateQueryRecord()
      if (this.activeName === 'first') {
        console.log('子项目--first')
      } else if (this.activeName === 'second') {
        this.getToolStatusData()
      } else if (this.activeName === 'third') {
        console.log('子项目--third')
      } else if (this.activeName === 'fourth') {
        console.log('子项目--fourth')
      }
    },
    /** 主项目 值改变时 需要做的 动作 */
    handleChangeProtId() {
      this.queryParams.subProtId = undefined
      this.updateQueryRecord()
      if (this.activeName === 'first') {
        console.log('主项目--first')
      } else if (this.activeName === 'second') {
        this.getToolStatusData()
      } else if (this.activeName === 'third') {
        console.log('主项目--third')
      } else if (this.activeName === 'fourth') {
        console.log('主项目--fourth')
      }
    },
    /** 获取 first 的 prot Status 数据 */
    getMilestonesData() {
      this.loading = true
      selectProtStatus().then(response => {
        this.firstProtStatus = response.data
        this.loading = false
      })
    },
    /** 获取 second 的 Tooling Status 数据 */
    getToolStatusData() {
      if (this.queryParams.protId !== undefined && this.queryParams.protId !== '') {
        selectToolTrackList(this.queryParams).then(response => {
          this.secondToolingStatusData = response.data
        })
      }
    },
    /** 子项目 打开 select 时 获取所有的 项目 */
    dragGetAllSonProtOptions(flag) {
      if (flag === true) {
        this.getAllSonProtOptions()
      }
    },
    /** 主项目 打开 select 时 获取所有的 项目 */
    dragGetAllProtOptions(flag) {
      if (flag === true) {
        this.getAllProtOptions()
      }
    },
    /** 获取所有的 项目 用于提示 */
    getAllProtOptions() {
      selectAllProtForTips().then(response => {
        this.protOptions = response.data
      })
    },
    /** 获取所有的 这个项目 对应的子项目 用于提示 */
    getAllSonProtOptions() {
      if (this.queryParams.protId !== undefined && this.queryParams.protId !== '') {
        selectAllSonProtForTips(this.queryParams.protId).then(res => {
          this.sonProtOptions = res.data
        })
      }
    },
    /** 更新 用户 查询 参数 */
    updateQueryRecord() {
      updateDailyReportQueryRecord(this.queryParams)
    },
    /** 获取数据库中 用户 最后 一次的查询 参数 */
    getQueryRecord() {
      selectQueryRecord().then(response => {
        if (response.code === '100000') {
          this.getAllProtOptions()
          if (response.data !== undefined) {
            this.queryParams.protId = response.data.protId
            this.queryParams.subProtId = response.data.subProtId
            this.queryParams.issuePriority = response.data.issuePriority
            this.getAllSonProtOptions()
          }
        }
      })
    },
    /** 点击选择卡 */
    handleTabClick(tab, event) {
      if (tab.name === 'first') {
        this.getMilestonesData()
      } else if (tab.name === 'second') {
        this.getToolStatusData()
      } else if (tab.name === 'third') {
        console.log(this.activeName)
      } else if (tab.name === 'fourth') {
        console.log(this.activeName)
      }
    },
    // 展开 关闭 功能
    forArr(arr, isExpand) {
      arr.forEach(i => {
        this.$refs.topicTable.toggleRowExpansion(i, isExpand)
        if (i.children) {
          this.forArr(i.children, isExpand)
        }
      })
    },
    // 展开按钮
    expandAll() {
      this.forArr(this.secondToolingStatusData, true)
      this.isOpen = false
    },
    // 隐藏按钮
    collapseAll() {
      this.forArr(this.secondToolingStatusData, false)
      this.isOpen = true
    }
  }
}
</script>

<style scoped>
  .app-container {
    padding: 20px;
  }

</style>
