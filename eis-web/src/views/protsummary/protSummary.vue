<template>
  <div class="app-container">
    <h3 align="center"> Project Status (已关注)</h3>
    <el-table
      ref="topicTable"
      v-loading="loading"
      :data="protStatusData"
      :row-style="{height:'20px'}"
      :cell-style="{padding:'0px'}"
      border
      stripe
      :height="tableHeight1"
      row-key="id"
      default-expand-all
      style="width: 100%;font-size: 10px"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      :header-cell-style="{background:'#87CEEB',color:'#606266'}"
    >
      <el-table-column fixed width="160" prop="protDes" label="Project Description" />
      <el-table-column fixed align="center" width="80" prop="protCode" label="Project Code" />
      <el-table-column fixed width="160" prop="sonProtDes" label="Sub Project" />
      <el-table-column prop="phase" label="PHASE" align="center" />
      <el-table-column width="85" prop="planOrFact" label="Plan/Fact" align="center" />
      <el-table-column width="80" prop="protKickoffMeet" label="Kick-Off" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.protKickoffMeet !== undefined">{{ scope.row.protKickoffMeet.substring(5,10) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Industrial Design" align="center">
        <el-table-column width="70" prop="industrialDesignStart" label="Start" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.industrialDesignStart !== undefined">{{ scope.row.industrialDesignStart.substring(5,10) }}</span>
          </template>
        </el-table-column>
        <el-table-column width="70" prop="industrialDesignEnd" label="End" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.industrialDesignEnd !== undefined">{{ scope.row.industrialDesignEnd.substring(5,10) }}</span>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="Mechanical Design" align="center">
        <el-table-column width="78" prop="mechanicalDesignStart" label="Start" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.mechanicalDesignStart !== undefined">{{ scope.row.mechanicalDesignStart.substring(5,10) }}</span>
          </template>
        </el-table-column>
        <el-table-column width="78" prop="mechanicalDesignEnd" label="End" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.mechanicalDesignEnd !== undefined">{{ scope.row.mechanicalDesignEnd.substring(5,10) }}</span>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="Mockup" align="center">
        <el-table-column width="60" prop="cncSampleStart" label="Start" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.cncSampleStart !== undefined">{{ scope.row.cncSampleStart.substring(5,10) }}</span>
          </template>
        </el-table-column>
        <el-table-column width="60" prop="cncSampleEnd" label="End" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.cncSampleEnd !== undefined">{{ scope.row.cncSampleEnd.substring(5,10) }}</span>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column width="75" prop="tooling" label="Tooling Start" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.tooling !== undefined">{{ scope.row.tooling.substring(5,10) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-for="(item1, index1) in tableHeaderEVTSys" :key="index1" width="95" align="center" :label="item1">
        <template slot-scope="scope">
          <span v-if="scope.row.evtSysData[index1] !== undefined">{{ scope.row.evtSysData[index1].substring(5,10) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-for="(item3, index3) in tableHeaderDVTSys" :key="index3+200" width="100" align="center" :label="item3">
        <template slot-scope="scope">
          <span v-if="scope.row.dvtSysData[index3] !== undefined">{{ scope.row.dvtSysData[index3].substring(5,10) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-for="(item5, index5) in tableHeaderPVTSys" :key="index5+400" width="100" align="center" :label="item5">
        <template slot-scope="scope">
          <span v-if="scope.row.pvtSysData[index5] !== undefined">{{ scope.row.pvtSysData[index5].substring(5,10) }}</span>
        </template>
      </el-table-column>
      <el-table-column width="100" fixed="right" label="操作" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.parentId === '0'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >milestone</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改  dialog -->
    <el-dialog :title="title" :visible.sync="openProtStatus" fullscreen :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="180px">
        <el-row>
          <el-row>
            <el-col :span="4">
              <el-form-item>
                <span />
              </el-form-item>
            </el-col>
            <el-col :span="17">
              <el-form-item label="ProjectCode" prop="protCode">
                <el-input v-model="form.protCode" :disabled="true" placeholder="请输入ProjectCode" />
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
                <el-input v-model="form.protDes" :disabled="true" placeholder="请输入Project Description" />
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
                <el-input v-model="form.sonProtDes" :disabled="true" placeholder="" />
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
                <el-input v-model="form.phase" placeholder="请输入PHASE" />
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
                  v-model="form.protKickoffMeetPlan"
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
                  v-model="form.protKickoffMeetFact"
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
                  v-model="form.industrialDesignPlanStart"
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
                  v-model="form.industrialDesignPlanEnd"
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
                  v-model="form.industrialDesignFactStart"
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
                  v-model="form.industrialDesignFactEnd"
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
                  v-model="form.mechanicalDesignPlanStart"
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
                  v-model="form.mechanicalDesignPlanEnd"
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
                  v-model="form.mechanicalDesignFactStart"
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
                  v-model="form.mechanicalDesignFactEnd"
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
                  v-model="form.cncSamplePlanStart"
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
                  v-model="form.cncSamplePlanEnd"
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
                  v-model="form.cncSampleFactStart"
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
                  v-model="form.cncSampleFactEnd"
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
                  v-model="form.toolingPlan"
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
                  v-model="form.toolingFact"
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
                <el-select v-model="form.evtSysId" style="width: 150px" placeholder="请选择" size="small" @change="handleChangeEvtSysId">
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
                  v-model="form.evtSysPlanStart"
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
                  v-model="form.evtSysPlanEnd"
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
                  v-model="form.evtSysFactStart"
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
                  v-model="form.evtSysFactEnd"
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
                <el-select v-model="form.dvtSysId" style="width: 150px" placeholder="请选择" size="small" @change="handleChangeDvtSysId">
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
                  v-model="form.dvtSysPlanStart"
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
                  v-model="form.dvtSysPlanEnd"
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
                  v-model="form.dvtSysFactStart"
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
                  v-model="form.dvtSysFactEnd"
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
                <el-select v-model="form.pvtSysId" style="width: 150px" placeholder="请选择" size="small" @change="handleChangePvtSysId">
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
                  v-model="form.pvtSysPlanStart"
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
                  v-model="form.pvtSysPlanEnd"
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
                  v-model="form.pvtSysFactStart"
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
                  v-model="form.pvtSysFactEnd"
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
import { selectAllProtStatus, editProtStatus, selectProtStatusAuxById, addProtStatusAux, pvtOptions, evtOptions, dvtOptions } from '@/api/protSummary'

export default {
  name: 'ProtSummary',
  data() {
    return {
      tableHeight1: 600,
      // 遮罩层
      loading: true,
      // 表头
      tableHeaderDVTSys: [],
      tableHeaderEVTSys: [],
      tableHeaderPVTSys: [],
      // 下拉 提示 options DVTRev DVTSys EVTRev EVTSys PVTRev PVTSys
      optionsDVTSys: [],
      optionsEVTSys: [],
      optionsPVTSys: [],
      // 表格数据
      protStatusData: [],
      // open 修改 prot status
      openProtStatus: false,
      // 表单参数
      form: {},
      // 弹出层标题
      title: ''
    }
  },
  mounted() {
    this.$nextTick(function() {
      this.tableHeight1 = window.innerHeight - 165
      // 监听窗口大小变化
      const self = this
      window.onresize = function() {
        self.tableHeight1 = window.innerHeight - 165
      }
    })
  },
  created() {
    this.getAllData()
  },
  methods: {
    /** 修改 对话框 提交 */
    submitUpdate: function() {
      if (this.form.id !== undefined && this.form.sonId !== undefined) { // 修改
        editProtStatus(this.form).then(response => {
          if (response.code === '100000') {
            this.$message({ showClose: true, message: '修改成功', type: 'success' })
            this.openProtStatus = false
            this.getAllData()
          }
        })
      }
    },
    /** 点击 添加 PVT DVT EVT 按钮 */
    handleAddProtStatusAux(dataType) {
      // id 里 放的 是 主项目 id， 用于 验证是否是项目成员
      addProtStatusAux({ id: this.form.mainProtId, protStatusId: this.form.id, dataType: dataType }).then(response => {
        if (response.code === '100000') {
          this.$message({ showClose: true, message: '添加 ' + dataType + ' 成功!!!', type: 'success' })
          if (dataType === 'EVT') {
            this.getEVTOptions(this.form.id)
          } else if (dataType === 'DVT') {
            this.getDVTOptions(this.form.id)
          } else if (dataType === 'PVT') {
            this.getPVTOptions(this.form.id)
          }
        }
      })
    },
    /** evtSysId 改变 */
    handleChangeEvtSysId() {
      if (this.form.evtSysId !== undefined) {
        selectProtStatusAuxById(this.form.evtSysId).then(response => {
          this.form.evtSysPlanStart = response.data.planStart
          this.form.evtSysPlanEnd = response.data.planEnd
          this.form.evtSysFactStart = response.data.factStart
          this.form.evtSysFactEnd = response.data.factEnd
        })
      }
    },
    /** dvtSysId 改变 */
    handleChangeDvtSysId() {
      if (this.form.dvtSysId !== undefined) {
        selectProtStatusAuxById(this.form.dvtSysId).then(response => {
          this.form.dvtSysPlanStart = response.data.planStart
          this.form.dvtSysPlanEnd = response.data.planEnd
          this.form.dvtSysFactStart = response.data.factStart
          this.form.dvtSysFactEnd = response.data.factEnd
        })
      }
    },
    /** pvtSysId 改变 */
    handleChangePvtSysId() {
      if (this.form.pvtSysId !== undefined) {
        selectProtStatusAuxById(this.form.pvtSysId).then(response => {
          this.form.pvtSysPlanStart = response.data.planStart
          this.form.pvtSysPlanEnd = response.data.planEnd
          this.form.pvtSysFactStart = response.data.factStart
          this.form.pvtSysFactEnd = response.data.factEnd
        })
      }
    },
    /** 点击修改 按钮操作 */
    handleUpdate(row) {
      if (row.sonProtDes !== undefined) {
        this.title = 'Milestone ' + row.protDes + ' ' + row.sonProtDes
      } else {
        this.title = 'Milestone ' + row.protDes
      }
      this.reset()
      this.form.id = row.id
      this.form.sonId = row.children[0].id
      this.form.mainProtId = row.mainProtId
      this.form.protCode = row.protCode
      this.form.protDes = row.protDes
      this.form.sonProtDes = row.sonProtDes
      this.form.phase = row.phase
      this.form.ownedBy = row.ownedBy
      this.form.protType = row.protType
      this.form.period = row.period
      this.form.achievedMeCost = row.achievedMeCost
      this.form.meCostTarget = row.meCostTarget
      this.form.vender = row.vender
      this.form.section = row.section
      this.form.team = row.team
      this.form.taskLeader = row.taskLeader
      this.form.engineers = row.engineers
      this.form.protKickoffMeetPlan = row.protKickoffMeet
      this.form.protKickoffMeetFact = row.children[0].protKickoffMeet
      this.form.industrialDesignPlanStart = row.industrialDesignStart
      this.form.industrialDesignPlanEnd = row.industrialDesignEnd
      this.form.industrialDesignFactStart = row.children[0].industrialDesignStart
      this.form.industrialDesignFactEnd = row.children[0].industrialDesignEnd
      this.form.idDummySamplePlanStart = row.idDummySampleStart
      this.form.idDummySamplePlanEnd = row.idDummySampleEnd
      this.form.idDummySampleFactStart = row.children[0].idDummySampleStart
      this.form.idDummySampleFactEnd = row.children[0].idDummySampleEnd
      this.form.mechanicalDesignPlanStart = row.mechanicalDesignStart
      this.form.mechanicalDesignPlanEnd = row.mechanicalDesignEnd
      this.form.mechanicalDesignFactStart = row.children[0].mechanicalDesignStart
      this.form.mechanicalDesignFactEnd = row.children[0].mechanicalDesignEnd
      this.form.cncSamplePlanStart = row.cncSampleStart
      this.form.cncSamplePlanEnd = row.cncSampleEnd
      this.form.cncSampleFactStart = row.children[0].cncSampleStart
      this.form.cncSampleFactEnd = row.children[0].cncSampleEnd
      this.form.softToolPartPlanStart = row.softToolPartStart
      this.form.softToolPartPlanEnd = row.softToolPartEnd
      this.form.softToolPartFactStart = row.children[0].softToolPartStart
      this.form.softToolPartFactEnd = row.children[0].softToolPartEnd
      this.form.toolingPlan = row.tooling
      this.form.toolingFact = row.children[0].tooling
      this.getEVTOptions(row.id)
      this.getDVTOptions(row.id)
      this.getPVTOptions(row.id)
      this.openProtStatus = true
    },
    /** 获取 PVT Options 用于提示 */
    getPVTOptions(protStatusId) {
      pvtOptions(protStatusId).then(response => {
        this.optionsPVTSys = response.data.optionsPVTSys
        if (this.optionsPVTSys.length > 0) {
          this.form.pvtSysId = this.optionsPVTSys[this.optionsPVTSys.length - 1].value
          this.handleChangePvtSysId()
        }
      })
    },
    /** 获取 DVT Options 用于提示 */
    getDVTOptions(protStatusId) {
      dvtOptions(protStatusId).then(response => {
        this.optionsDVTSys = response.data.optionsDVTSys
        if (this.optionsDVTSys.length > 0) {
          this.form.dvtSysId = this.optionsDVTSys[this.optionsDVTSys.length - 1].value
          this.handleChangeDvtSysId()
        }
      })
    },
    /** 获取 EVT Options 用于提示 */
    getEVTOptions(protStatusId) {
      evtOptions(protStatusId).then(response => {
        this.optionsEVTSys = response.data.optionsEVTSys
        if (this.optionsEVTSys.length > 0) {
          this.form.evtSysId = this.optionsEVTSys[this.optionsEVTSys.length - 1].value
          this.handleChangeEvtSysId()
        }
      })
    },
    /** 表单重置 */
    reset() {
      this.form = {
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
        idDummySamplePlanStart: undefined,
        idDummySamplePlanEnd: undefined,
        idDummySampleFactStart: undefined,
        idDummySampleFactEnd: undefined,
        mechanicalDesignPlanStart: undefined,
        mechanicalDesignPlanEnd: undefined,
        mechanicalDesignFactStart: undefined,
        mechanicalDesignFactEnd: undefined,
        cncSamplePlanStart: undefined,
        cncSamplePlanEnd: undefined,
        cncSampleFactStart: undefined,
        cncSampleFactEnd: undefined,
        softToolPartPlanStart: undefined,
        softToolPartPlanEnd: undefined,
        softToolPartFactStart: undefined,
        softToolPartFactEnd: undefined,
        toolingPlan: undefined,
        toolingFact: undefined,
        evtSysId: undefined,
        evtSysPlanStart: undefined,
        evtSysPlanEnd: undefined,
        evtSysFactStart: undefined,
        evtSysFactEnd: undefined,
        evtRevId: undefined,
        evtRevPlanStart: undefined,
        evtRevFactStart: undefined,
        dvtSysId: undefined,
        dvtSysPlanStart: undefined,
        dvtSysPlanEnd: undefined,
        dvtSysFactStart: undefined,
        dvtSysFactEnd: undefined,
        dvtRevId: undefined,
        dvtRevPlanStart: undefined,
        dvtRevFactStart: undefined,
        pvtSysId: undefined,
        pvtSysPlanStart: undefined,
        pvtSysPlanEnd: undefined,
        pvtSysFactStart: undefined,
        pvtSysFactEnd: undefined,
        pvtRevId: undefined,
        pvtRevPlanStart: undefined,
        pvtRevFactStart: undefined
      }
    },
    /** PVT Sys Plan End */
    changeDatePvtSysPlanEnd() {
      this.form.pvtSysFactEnd = this.form.pvtSysPlanEnd
    },
    /** PVT Sys Plan Start */
    changeDatePvtSysPlanStart() {
      this.form.pvtSysFactStart = this.form.pvtSysPlanStart
    },
    /** DVT Sys Plan End */
    changeDateDvtSysPlanEnd() {
      this.form.dvtSysFactEnd = this.form.dvtSysPlanEnd
    },
    /** DVT Sys Plan Start */
    changeDateDvtSysPlanStart() {
      this.form.dvtSysFactStart = this.form.dvtSysPlanStart
    },
    /** evt Sys Plan End */
    changeDateEvtSysPlanEnd() {
      this.form.evtSysFactEnd = this.form.evtSysPlanEnd
    },
    /** evt Sys Plan Start */
    changeDateEvtSysPlanStart() {
      this.form.evtSysFactStart = this.form.evtSysPlanStart
    },
    /** Tooling Plan */
    changeDateToolingPlan() {
      this.form.toolingFact = this.form.toolingPlan
    },
    /** CNC Sample Plan End */
    changeDateCncSamplePlanEnd() {
      this.form.cncSampleFactEnd = this.form.cncSamplePlanEnd
    },
    /** CNC Sample Plan Start */
    changeDateCncSamplePlanStart() {
      this.form.cncSampleFactStart = this.form.cncSamplePlanStart
    },
    /** Mechanical Design Plan End */
    changeDateMechanicalDesignPlanEnd() {
      this.form.mechanicalDesignFactEnd = this.form.mechanicalDesignPlanEnd
    },
    /** Mechanical Design Plan Start */
    changeDateMechanicalDesignPlanStart() {
      this.form.mechanicalDesignFactStart = this.form.mechanicalDesignPlanStart
    },
    /** Industrial Design Plan End */
    changeDateIndustrialDesignPlanEnd() {
      this.form.industrialDesignFactEnd = this.form.industrialDesignPlanEnd
    },
    /** Industrial Design Plan Start */
    changeDateIndustrialDesignPlanStart() {
      this.form.industrialDesignFactStart = this.form.industrialDesignPlanStart
    },
    /** Project Kick-Off Meeting Plan */
    changeDateProtKickoffMeetPlan() {
      this.form.protKickoffMeetFact = this.form.protKickoffMeetPlan
    },
    getAllData() {
      this.loading = true
      selectAllProtStatus().then(response => {
        this.tableHeaderDVTSys = response.data.tableHeaderDVTSys
        this.tableHeaderEVTSys = response.data.tableHeaderEVTSys
        this.tableHeaderPVTSys = response.data.tableHeaderPVTSys
        this.protStatusData = response.data.resultData
        setTimeout(() => {
          this.$refs.topicTable.doLayout()
        }, 150)
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
