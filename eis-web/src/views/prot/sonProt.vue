<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-tag type="success" v-text="'主项目名: '+ protName">主项目名</el-tag>&nbsp;&nbsp;&nbsp;
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleMilestone"
      >Milestone</el-button>
      <el-button
        type="primary"
        icon="el-icon-view"
        size="mini"
        @click="handlePilotrunReport"
      >PilotrunReport</el-button>
      <el-button
        v-if="checkPermission(['ROOT','ADMIN'])"
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd"
      >新增</el-button>
    </el-form>
    <br>

    <el-table
      ref="table1"
      :data="subProtList"
      :row-style="{height:'20px'}"
      :cell-style="{padding:'0px'}"
      :height="tableHeight1"
      style="width: 100%;font-size: 10px"
      row-key="id"
      border
      @row-dblclick="handleRowClick"
    >
      <el-table-column
        prop="name"
        label="子项目名"
        width="300"
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
        width="350"
        label="备注"
      />
      <el-table-column v-if="checkPermission(['ROOT','ADMIN'])" label="操作">
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
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改项目对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="18">
            <el-form-item label="子项目名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入子项目名称" />
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="子项目备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" />
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="子项目状态">
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

    <!-- 修改  prot status dialog -->
    <el-dialog :title="title" :visible.sync="openProtStatus" fullscreen :close-on-click-modal="false">
      <el-form ref="formProtStatus" :model="formProtStatus" label-width="180px">
        <el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item>
                <span />
              </el-form-item>
            </el-col>
            <el-col :span="17">
              <el-form-item label="ProjectCode" prop="protCode">
                <el-input v-model="formProtStatus.protCode" :disabled="true" placeholder="请输入ProjectCode" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item>
                <span />
              </el-form-item>
            </el-col>
            <el-col :span="17">
              <el-form-item label="Project Description" prop="protDes">
                <el-input v-model="formProtStatus.protDes" :disabled="true" placeholder="请输入Project Description" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item>
                <span />
              </el-form-item>
            </el-col>
            <el-col :span="17">
              <el-form-item label="Sub Project" prop="sonProtDes">
                <el-input v-model="formProtStatus.sonProtDes" :disabled="true" placeholder="" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item>
                <span />
              </el-form-item>
            </el-col>
            <el-col :span="17">
              <el-form-item label="PHASE" prop="phase">
                <el-input v-model="formProtStatus.phase" placeholder="请输入PHASE" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item>
                <span />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item>
                <el-button
                  align="center"
                  type="success"
                  style="width: 155px"
                  size="medium"
                >Plan Start</el-button>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item>
                <el-button
                  align="center"
                  type="success"
                  style="width: 155px"
                  size="medium"
                >Plan End</el-button>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item>
                <el-button
                  align="center"
                  type="info"
                  style="width: 155px"
                  size="medium"
                >Fact Start</el-button>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item>
                <el-button
                  align="center"
                  type="info"
                  style="width: 155px"
                  size="medium"
                >Fact End</el-button>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item>
                <span />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="Project Kick-Off Meeting" prop="protKickoffMeetPlan">
                <el-date-picker
                  v-model="formProtStatus.protKickoffMeetPlan"
                  type="date"
                  style="width: 155px"
                  placeholder="选择 Plan 时间"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDateProtKickoffMeetPlan"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item prop="protKickoffMeetFact">
                <el-date-picker
                  v-model="formProtStatus.protKickoffMeetFact"
                  type="date"
                  style="width: 155px"
                  placeholder="选择 Fact 时间"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item>
                <span />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="Industrial Design" prop="industrialDesignPlanStart">
                <el-date-picker
                  v-model="formProtStatus.industrialDesignPlanStart"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan Start"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDateIndustrialDesignPlanStart"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="industrialDesignPlanEnd">
                <el-date-picker
                  v-model="formProtStatus.industrialDesignPlanEnd"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan End"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDateIndustrialDesignPlanEnd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="industrialDesignFactStart">
                <el-date-picker
                  v-model="formProtStatus.industrialDesignFactStart"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact Start"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="industrialDesignFactEnd">
                <el-date-picker
                  v-model="formProtStatus.industrialDesignFactEnd"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact End"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item>
                <span />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="Mechanical Design" prop="mechanicalDesignPlanStart">
                <el-date-picker
                  v-model="formProtStatus.mechanicalDesignPlanStart"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan Start"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDateMechanicalDesignPlanStart"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="mechanicalDesignPlanEnd">
                <el-date-picker
                  v-model="formProtStatus.mechanicalDesignPlanEnd"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan End"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDateMechanicalDesignPlanEnd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="mechanicalDesignFactStart">
                <el-date-picker
                  v-model="formProtStatus.mechanicalDesignFactStart"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact Start"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="mechanicalDesignFactEnd">
                <el-date-picker
                  v-model="formProtStatus.mechanicalDesignFactEnd"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact End"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item>
                <span />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="Mockup" prop="cncSamplePlanStart">
                <el-date-picker
                  v-model="formProtStatus.cncSamplePlanStart"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan Start"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDateCncSamplePlanStart"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="cncSamplePlanEnd">
                <el-date-picker
                  v-model="formProtStatus.cncSamplePlanEnd"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan End"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDateCncSamplePlanEnd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="cncSampleFactStart">
                <el-date-picker
                  v-model="formProtStatus.cncSampleFactStart"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact Start"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="cncSampleFactEnd">
                <el-date-picker
                  v-model="formProtStatus.cncSampleFactEnd"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact End"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item>
                <span />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="Tooling Start" prop="toolingPlan">
                <el-date-picker
                  v-model="formProtStatus.toolingPlan"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan 时间"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDateToolingPlan"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item prop="toolingFact">
                <el-date-picker
                  v-model="formProtStatus.toolingFact"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact 时间"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item label="EVT System Assembly" prop="evtSysId">
                <el-select v-model="formProtStatus.evtSysId" style="width: 150px" placeholder="请选择" size="small" @change="handleChangeEvtSysId">
                  <el-option
                    v-for="item in optionsEVTSys"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="evtSysPlanStart">
                <el-date-picker
                  v-model="formProtStatus.evtSysPlanStart"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan Start"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDateEvtSysPlanStart"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="evtSysPlanEnd">
                <el-date-picker
                  v-model="formProtStatus.evtSysPlanEnd"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan End"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDateEvtSysPlanEnd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="evtSysFactStart">
                <el-date-picker
                  v-model="formProtStatus.evtSysFactStart"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact Start"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="evtSysFactEnd">
                <el-date-picker
                  v-model="formProtStatus.evtSysFactEnd"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact End"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label-width="90px">
                <el-button
                  align="center"
                  type="primary"
                  size="mini"
                  @click="handleAddProtStatusAux('EVT')"
                >添加EVT Stage</el-button>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item label="DVT System Assembly" prop="dvtSysId">
                <el-select v-model="formProtStatus.dvtSysId" style="width: 150px" placeholder="请选择" size="small" @change="handleChangeDvtSysId">
                  <el-option
                    v-for="item in optionsDVTSys"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="dvtSysPlanStart">
                <el-date-picker
                  v-model="formProtStatus.dvtSysPlanStart"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan Start"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDateDvtSysPlanStart"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="dvtSysPlanEnd">
                <el-date-picker
                  v-model="formProtStatus.dvtSysPlanEnd"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan End"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDateDvtSysPlanEnd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="dvtSysFactStart">
                <el-date-picker
                  v-model="formProtStatus.dvtSysFactStart"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact Start"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="dvtSysFactEnd">
                <el-date-picker
                  v-model="formProtStatus.dvtSysFactEnd"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact End"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label-width="90px">
                <el-button
                  align="center"
                  type="primary"
                  size="mini"
                  @click="handleAddProtStatusAux('DVT')"
                >添加DVT Stage</el-button>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item label="PVT System Assembly" prop="pvtSysId">
                <el-select v-model="formProtStatus.pvtSysId" style="width: 150px" placeholder="请选择" size="small" @change="handleChangePvtSysId">
                  <el-option
                    v-for="item in optionsPVTSys"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="pvtSysPlanStart">
                <el-date-picker
                  v-model="formProtStatus.pvtSysPlanStart"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan Start"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDatePvtSysPlanStart"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="pvtSysPlanEnd">
                <el-date-picker
                  v-model="formProtStatus.pvtSysPlanEnd"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Plan End"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  @change="changeDatePvtSysPlanEnd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="pvtSysFactStart">
                <el-date-picker
                  v-model="formProtStatus.pvtSysFactStart"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact Start"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="pvtSysFactEnd">
                <el-date-picker
                  v-model="formProtStatus.pvtSysFactEnd"
                  type="date"
                  style="width: 155px"
                  placeholder="选择Fact End"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label-width="90px">
                <el-button
                  align="center"
                  type="primary"
                  size="mini"
                  @click="handleAddProtStatusAux('PVT')"
                >添加PVT Stage</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openProtStatus = false">取 消</el-button>
        <el-button type="primary" @click="submitUpdate">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { selectSubProtByMainId, addSonProt, updateProt, deleteSonProt } from '@/api/prot'
import { selectByMainProtId, evtOptions, dvtOptions, pvtOptions, editProtStatus, selectProtStatusAuxById, addProtStatusAux } from '@/api/protSummary'
import checkPermission from '@/utils/permission'
export default {
  name: 'SonProt',
  data() {
    return {
      tableHeight1: 600,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // prot status 表单参数
      formProtStatus: {},
      // 下拉 提示 options  DVTSys  EVTSys  PVTSys
      optionsDVTSys: [],
      optionsEVTSys: [],
      optionsPVTSys: [],
      // open 修改 prot status
      openProtStatus: false,
      // 状态数据字典
      statusOptions: [{ 'dictValue': 0, 'dictLabel': '待完成' }, { 'dictValue': 1, 'dictLabel': '已完成' }],
      // 子项目表格数据
      subProtList: [],
      // 项目名
      protName: '',
      // 表单校验
      rules: {
        name: [
          { required: true, message: '项目名称不能为空', trigger: 'blur' }
        ],
        remark: [
          { min: 0, max: 150, message: '长度在 0 到 150 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.$nextTick(function() {
      this.tableHeight1 = window.innerHeight - 160
      // 监听窗口大小变化
      const self = this
      window.onresize = function() {
        self.tableHeight1 = window.innerHeight - 160
      }
    })
  },
  created() {
    this.getSubProt()
  },
  methods: {
    checkPermission,
    /** 点击 PilotrunReport */
    handlePilotrunReport() {
      const routeData = this.$router.resolve({ name: 'PilotrunReport', query: { protId: localStorage.getItem('mainProtId') }})
      window.open(routeData.href, '_blank')
    },
    /** 修改 对话框 提交 */
    submitUpdate: function() {
      if (this.formProtStatus.id !== undefined && this.formProtStatus.sonId !== undefined) { // 修改
        editProtStatus(this.formProtStatus).then(response => {
          if (response.code === '100000') {
            this.$message({ showClose: true, message: '修改成功', type: 'success' })
            this.openProtStatus = false
          }
        })
      }
    },
    /** 点击 添加 PVT DVT EVT 按钮 */
    handleAddProtStatusAux(dataType) {
      // id 里 放的 是 主项目 id， 用于 验证是否是项目成员
      if (this.formProtStatus.mainProtId !== undefined && this.formProtStatus.id !== undefined) {
        addProtStatusAux({ id: this.formProtStatus.mainProtId, protStatusId: this.formProtStatus.id, dataType: dataType }).then(response => {
          if (response.code === '100000') {
            this.$message({ showClose: true, message: '添加 ' + dataType + ' 成功!!!', type: 'success' })
            if (dataType === 'EVT') {
              this.getEVTOptions(this.formProtStatus.id)
            } else if (dataType === 'DVT') {
              this.getDVTOptions(this.formProtStatus.id)
            } else if (dataType === 'PVT') {
              this.getPVTOptions(this.formProtStatus.id)
            }
          }
        })
      }
    },
    /** 点击 Milestone，修改 prot status */
    handleMilestone() {
      selectByMainProtId(localStorage.getItem('mainProtId')).then(response => {
        this.title = 'Milestone  ' + this.protName
        this.resetProtStatus()
        this.formProtStatus.id = response.data.id
        this.formProtStatus.sonId = response.data.children[0].id
        this.formProtStatus.mainProtId = response.data.mainProtId
        this.formProtStatus.protCode = response.data.protCode
        this.formProtStatus.protDes = response.data.protDes
        this.formProtStatus.sonProtDes = response.data.sonProtDes
        this.formProtStatus.phase = response.data.phase
        this.formProtStatus.ownedBy = response.data.ownedBy
        this.formProtStatus.protType = response.data.protType
        this.formProtStatus.period = response.data.period
        this.formProtStatus.achievedMeCost = response.data.achievedMeCost
        this.formProtStatus.meCostTarget = response.data.meCostTarget
        this.formProtStatus.vender = response.data.vender
        this.formProtStatus.section = response.data.section
        this.formProtStatus.team = response.data.team
        this.formProtStatus.taskLeader = response.data.taskLeader
        this.formProtStatus.engineers = response.data.engineers
        this.formProtStatus.protKickoffMeetPlan = response.data.protKickoffMeet
        this.formProtStatus.protKickoffMeetFact = response.data.children[0].protKickoffMeet
        this.formProtStatus.industrialDesignPlanStart = response.data.industrialDesignStart
        this.formProtStatus.industrialDesignPlanEnd = response.data.industrialDesignEnd
        this.formProtStatus.industrialDesignFactStart = response.data.children[0].industrialDesignStart
        this.formProtStatus.industrialDesignFactEnd = response.data.children[0].industrialDesignEnd
        this.formProtStatus.mechanicalDesignPlanStart = response.data.mechanicalDesignStart
        this.formProtStatus.mechanicalDesignPlanEnd = response.data.mechanicalDesignEnd
        this.formProtStatus.mechanicalDesignFactStart = response.data.children[0].mechanicalDesignStart
        this.formProtStatus.mechanicalDesignFactEnd = response.data.children[0].mechanicalDesignEnd
        this.formProtStatus.cncSamplePlanStart = response.data.cncSampleStart
        this.formProtStatus.cncSamplePlanEnd = response.data.cncSampleEnd
        this.formProtStatus.cncSampleFactStart = response.data.children[0].cncSampleStart
        this.formProtStatus.cncSampleFactEnd = response.data.children[0].cncSampleEnd
        this.formProtStatus.toolingPlan = response.data.tooling
        this.formProtStatus.toolingFact = response.data.children[0].tooling
        if (this.formProtStatus.id !== undefined) {
          this.getEVTOptions(this.formProtStatus.id)
          this.getDVTOptions(this.formProtStatus.id)
          this.getPVTOptions(this.formProtStatus.id)
        }
        this.openProtStatus = true
      })
    },
    /** 获取 PVT Options 用于提示 */
    getPVTOptions(protStatusId) {
      pvtOptions(protStatusId).then(response => {
        this.optionsPVTSys = response.data.optionsPVTSys
        if (this.optionsPVTSys.length > 0) {
          this.formProtStatus.pvtSysId = this.optionsPVTSys[this.optionsPVTSys.length - 1].value
          this.handleChangePvtSysId()
        }
      })
    },
    /** 获取 DVT Options 用于提示 */
    getDVTOptions(protStatusId) {
      dvtOptions(protStatusId).then(response => {
        this.optionsDVTSys = response.data.optionsDVTSys
        if (this.optionsDVTSys.length > 0) {
          this.formProtStatus.dvtSysId = this.optionsDVTSys[this.optionsDVTSys.length - 1].value
          this.handleChangeDvtSysId()
        }
      })
    },
    /** 获取 EVT Options 用于提示 */
    getEVTOptions(protStatusId) {
      evtOptions(protStatusId).then(response => {
        this.optionsEVTSys = response.data.optionsEVTSys
        if (this.optionsEVTSys.length > 0) {
          this.formProtStatus.evtSysId = this.optionsEVTSys[this.optionsEVTSys.length - 1].value
          this.handleChangeEvtSysId()
        }
      })
    },
    /** pvtSysId 改变 */
    handleChangePvtSysId() {
      if (this.formProtStatus.pvtSysId !== undefined) {
        selectProtStatusAuxById(this.formProtStatus.pvtSysId).then(response => {
          this.formProtStatus.pvtSysPlanStart = response.data.planStart
          this.formProtStatus.pvtSysPlanEnd = response.data.planEnd
          this.formProtStatus.pvtSysFactStart = response.data.factStart
          this.formProtStatus.pvtSysFactEnd = response.data.factEnd
        })
      }
    },
    /** dvtSysId 改变 */
    handleChangeDvtSysId() {
      if (this.formProtStatus.dvtSysId !== undefined) {
        selectProtStatusAuxById(this.formProtStatus.dvtSysId).then(response => {
          this.formProtStatus.dvtSysPlanStart = response.data.planStart
          this.formProtStatus.dvtSysPlanEnd = response.data.planEnd
          this.formProtStatus.dvtSysFactStart = response.data.factStart
          this.formProtStatus.dvtSysFactEnd = response.data.factEnd
        })
      }
    },
    /** evtSysId 改变 */
    handleChangeEvtSysId() {
      if (this.formProtStatus.evtSysId !== undefined) {
        selectProtStatusAuxById(this.formProtStatus.evtSysId).then(response => {
          this.formProtStatus.evtSysPlanStart = response.data.planStart
          this.formProtStatus.evtSysPlanEnd = response.data.planEnd
          this.formProtStatus.evtSysFactStart = response.data.factStart
          this.formProtStatus.evtSysFactEnd = response.data.factEnd
        })
      }
    },
    resetProtStatus() {
      this.formProtStatus = {
        id: undefined,
        sonId: undefined,
        mainProtId: undefined,
        parentId: undefined,
        protCode: undefined,
        protDes: undefined,
        sonProtDes: undefined,
        phase: undefined,
        ownedBy: undefined,
        protType: undefined,
        period: undefined,
        achievedMeCost: undefined,
        meCostTarget: undefined,
        vender: undefined,
        section: undefined,
        team: undefined,
        taskLeader: undefined,
        engineers: undefined,
        protKickoffMeetPlan: undefined,
        protKickoffMeetFact: undefined,
        industrialDesignPlanStart: undefined,
        industrialDesignPlanEnd: undefined,
        industrialDesignFactStart: undefined,
        industrialDesignFactEnd: undefined,
        mechanicalDesignPlanStart: undefined,
        mechanicalDesignPlanEnd: undefined,
        mechanicalDesignFactStart: undefined,
        mechanicalDesignFactEnd: undefined,
        cncSamplePlanStart: undefined,
        cncSamplePlanEnd: undefined,
        cncSampleFactStart: undefined,
        cncSampleFactEnd: undefined,
        toolingPlan: undefined,
        toolingFact: undefined,
        evtSysId: undefined,
        evtSysPlanStart: undefined,
        evtSysPlanEnd: undefined,
        evtSysFactStart: undefined,
        evtSysFactEnd: undefined,
        dvtSysId: undefined,
        dvtSysPlanStart: undefined,
        dvtSysPlanEnd: undefined,
        dvtSysFactStart: undefined,
        dvtSysFactEnd: undefined,
        pvtSysId: undefined,
        pvtSysPlanStart: undefined,
        pvtSysPlanEnd: undefined,
        pvtSysFactStart: undefined,
        pvtSysFactEnd: undefined
      }
    },
    /** PVT Sys Plan End */
    changeDatePvtSysPlanEnd() {
      this.formProtStatus.pvtSysFactEnd = this.formProtStatus.pvtSysPlanEnd
    },
    /** PVT Sys Plan Start */
    changeDatePvtSysPlanStart() {
      this.formProtStatus.pvtSysFactStart = this.formProtStatus.pvtSysPlanStart
    },
    /** DVT Sys Plan End */
    changeDateDvtSysPlanEnd() {
      this.formProtStatus.dvtSysFactEnd = this.formProtStatus.dvtSysPlanEnd
    },
    /** DVT Sys Plan Start */
    changeDateDvtSysPlanStart() {
      this.formProtStatus.dvtSysFactStart = this.formProtStatus.dvtSysPlanStart
    },
    /** evt Sys Plan End */
    changeDateEvtSysPlanEnd() {
      this.formProtStatus.evtSysFactEnd = this.formProtStatus.evtSysPlanEnd
    },
    /** evt Sys Plan Start */
    changeDateEvtSysPlanStart() {
      this.formProtStatus.evtSysFactStart = this.formProtStatus.evtSysPlanStart
    },
    /** Tooling Plan */
    changeDateToolingPlan() {
      this.formProtStatus.toolingFact = this.formProtStatus.toolingPlan
    },
    /** CNC Sample Plan End */
    changeDateCncSamplePlanEnd() {
      this.formProtStatus.cncSampleFactEnd = this.formProtStatus.cncSamplePlanEnd
    },
    /** CNC Sample Plan Start */
    changeDateCncSamplePlanStart() {
      this.formProtStatus.cncSampleFactStart = this.formProtStatus.cncSamplePlanStart
    },
    /** Mechanical Design Plan End */
    changeDateMechanicalDesignPlanEnd() {
      this.formProtStatus.mechanicalDesignFactEnd = this.formProtStatus.mechanicalDesignPlanEnd
    },
    /** Mechanical Design Plan Start */
    changeDateMechanicalDesignPlanStart() {
      this.formProtStatus.mechanicalDesignFactStart = this.formProtStatus.mechanicalDesignPlanStart
    },
    /** Industrial Design Plan End */
    changeDateIndustrialDesignPlanEnd() {
      this.formProtStatus.industrialDesignFactEnd = this.formProtStatus.industrialDesignPlanEnd
    },
    /** Industrial Design Plan Start */
    changeDateIndustrialDesignPlanStart() {
      this.formProtStatus.industrialDesignFactStart = this.formProtStatus.industrialDesignPlanStart
    },
    /** Project Kick-Off Meeting Plan */
    changeDateProtKickoffMeetPlan() {
      this.formProtStatus.protKickoffMeetFact = this.formProtStatus.protKickoffMeetPlan
    },
    /** 点击一行 触发进子项目详情 */
    handleRowClick(row, event, column) {
      // 不用打开新页面的写法， 修改 路径跳转 还用 localStorage 方法 ， 这个还在这个框架中写，子项目详细的再打开新界面
      // const routeData = this.$router.resolve({ name: 'subProtCon', query: { sonProtId: row.id }})
      // window.open(routeData.href, '_blank')
      localStorage.setItem('sonProtId', row.id)
      localStorage.setItem('sonProtName', this.protName + ' / ' + row.name)
      this.$router.push({ name: 'subProtCon' })
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.title = '添加子项目'
      this.open = true
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除名称为" ' + row.name + ' "的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return deleteSonProt(row.id, localStorage.getItem('mainProtId'))
      }).then(() => {
        this.getSubProt()
        this.$message({ showClose: true, message: '删除成功', type: 'success' })
      }).catch(function() {})
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
                this.getSubProt()
              }
            })
          } else {
            addSonProt(this.form).then(response => {
              if (response.code === '100000') {
                this.$message({ showClose: true, message: '新增子项目成功', type: 'success' })
                this.open = false
                this.getSubProt()
              }
            })
          }
        }
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.title = '修改子项目'
      this.form.id = row.id
      this.form.name = row.name
      this.form.remark = row.remark
      this.form.parentId = row.parentId
      this.form.status = row.status
      this.open = true
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        remark: undefined,
        parentId: localStorage.getItem('mainProtId'),
        status: 0
      }
      this.resetForm('form')
    },
    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields()
      }
    },
    getSubProt() {
      this.protName = localStorage.getItem('mainProtName')
      selectSubProtByMainId(localStorage.getItem('mainProtId')).then(response => {
        this.subProtList = response.data
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
