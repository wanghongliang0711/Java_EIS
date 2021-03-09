<template>
  <div class="app-container">
    <template>
      <el-tabs v-model="activeName" :tab-position="tabPosition" @tab-click="handleTabClick">
        <el-tab-pane label="Me Part List" name="first">
          <el-form :inline="true">
            <el-button
              type="primary"
              size="mini"
            >计重</el-button>
            <el-button
              type="primary"
              size="mini"
              @click="handleBuildVersion"
            >生成版本</el-button>
            <el-button
              type="primary"
              size="mini"
              @click="matchPartNum"
            >匹配Part Number</el-button>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-upload2"
              @click="handleImport"
            >导入</el-button>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-download"
              @click="handleDownload('now')"
            >下载</el-button>
            <h3 align="center"> Mechanical Parts List </h3>
            <el-tag type="danger" v-text="'Project Name :   '+ protName">主项目名 / 子项目名</el-tag>&nbsp;&nbsp;&nbsp;&nbsp;
            <el-tag v-text="'Approval :   '+ approvalName">Approval</el-tag>&nbsp;&nbsp;
            <el-button
              type="primary"
              size="mini"
              @click="handleEditApproval"
            >修改Approval</el-button>
            <el-button
              type="primary"
              size="mini"
              @click="handleAdd"
            >新增数据</el-button>
          </el-form>
          <br>
          <el-table
            v-loading="loading"
            :data="mePartList"
            :row-style="{height:'20px'}"
            :cell-style="{padding:'0px'}"
            style="width: 100%;font-size: 10px"
            border
            stripe
            :height="tableHeight1"
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
          >
            <el-table-column align="center" width="60" prop="no" label="No." />
            <el-table-column align="center" width="60" prop="bomLevel" label="BOM Level" />
            <el-table-column align="center" width="130" prop="partNumber" label="Part Number" />
            <el-table-column width="300" prop="partDescription" label="Part Description" />
            <el-table-column prop="meterial" label="Meterial" />
            <el-table-column align="center" width="70" prop="weight" label="Weight(g)" />
            <el-table-column align="center" width="60" prop="qty" label="Q'ty" />
            <el-table-column width="80" prop="vendor" label="Vendor" />
            <el-table-column align="center" width="80" prop="unitPrice" label="Unit Price(USD)" />
            <el-table-column align="center" width="80" prop="totalPrice" label="Total Price(USD)" />
            <el-table-column prop="remark" label="Remark" />
            <el-table-column width="240" label="操作" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                >修改</el-button>
                <el-button
                  v-if="scope.row.no !== undefined"
                  size="mini"
                  type="text"
                  icon="el-icon-plus"
                  @click="handleInsert(scope.row)"
                >插入</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

        </el-tab-pane>

        <el-tab-pane label="查看历史版本" name="second">

          <el-table
            :data="mePartListAllFileVer"
            border
            stripe
            height="200"
            :row-style="{height:'20px'}"
            :cell-style="{padding:'0px'}"
            style="width: 855px;font-size: 10px"
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
            :default-sort="{prop: 'fileVer', order: 'descending'}"
          >
            <el-table-column
              width="110"
              prop="fileVer"
              label="文件版本"
              sortable
            />
            <el-table-column
              width="350"
              prop="remark"
              label="备注"
            />
            <el-table-column
              width="140"
              prop="approval"
              label="Approval"
            />
            <el-table-column label="操作" width="240" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleViewVer(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-refresh-left"
                  @click="handleBackOldVer(scope.row)"
                >恢复</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-download"
                  @click="handleDownload(scope.row.fileVer)"
                >下载</el-button>
              </template>
            </el-table-column>
          </el-table>
          <h4 align="center"> Mechanical Parts List </h4>
          <el-table
            v-loading="loading"
            :data="mePartList"
            :row-style="{height:'20px'}"
            :cell-style="{padding:'0px'}"
            border
            stripe
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
            :height="tableHeight2"
            style="width: 100%;font-size: 10px"
          >
            <el-table-column align="center" width="60" prop="no" label="No." />
            <el-table-column align="center" width="60" prop="bomLevel" label="BOM Level" />
            <el-table-column align="center" width="130" prop="partNumber" label="Part Number" />
            <el-table-column width="251" prop="partDescription" label="Part Description" />
            <el-table-column prop="meterial" label="Meterial" />
            <el-table-column align="center" width="70" prop="weight" label="Weight(g)" />
            <el-table-column align="center" width="60" prop="qty" label="Q'ty" />
            <el-table-column width="80" prop="vendor" label="Vendor" />
            <el-table-column align="center" width="80" prop="unitPrice" label="Unit Price(USD)" />
            <el-table-column align="center" width="80" prop="totalPrice" label="Total Price(USD)" />
            <el-table-column prop="remark" label="Remark" />
            <el-table-column width="100" prop="fileVer" label="文件版本" />
          </el-table>

        </el-tab-pane>
      </el-tabs>
    </template>

    <!-- 添加或修改或插入对话框 -->
    <el-dialog :title="title" :visible.sync="openMePartList" width="600px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="130px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="No." prop="no">
              <!--              <el-input v-model.number="form.no" type="number" placeholder="请输入No." />-->
              <el-input-number v-model="form.no" size="small" :precision="0" :step="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="BOM Level" prop="bomLevel">
              <el-input v-model="form.bomLevel" placeholder="请输入BOM Level" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Part Number" prop="partNumber">
              <!--              <el-input v-model="form.partNumber" placeholder="请输入Part Number" />-->
              <el-autocomplete
                v-model="form.partNumber"
                class="inline-input"
                :fetch-suggestions="querySearch"
                placeholder="请输入Part Number"
                @select="handleSelect"
                @blur="handleBlur"
              >
                <template slot-scope="{ item }">
                  <div class="name">{{ item.value }}</div>
                  <span class="addr">{{ item.partDescription }}</span>
                </template>
              </el-autocomplete>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Part Description" prop="partDescription">
              <el-input v-model="form.partDescription" placeholder="请输入Part Description" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Meterial" prop="meterial">
              <!--              <el-input v-model="form.meterial" placeholder="请输入Meterial" />-->
              <!--              <el-autocomplete-->
              <!--                v-model="form.meterial"-->
              <!--                :fetch-suggestions="querySearchMeterialTips"-->
              <!--                placeholder="请输入Meterial"-->
              <!--              />-->
              <treeselect v-model="form.meterial" :options="meterialTips" :normalizer="normalizer" placeholder="请输入Meterial" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Weight(g)" prop="weight">
              <el-input v-model="form.weight" placeholder="请输入Weight(g)" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Q'ty" prop="qty">
              <el-input v-model="form.qty" placeholder="请输入Q'ty" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Vendor" prop="vendor">
              <el-input v-model="form.vendor" placeholder="请输入Vendor" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Unit Price(USD)" prop="unitPrice">
              <el-input v-model="form.unitPrice" placeholder="Unit Price(USD)" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Total Price(USD)" prop="totalPrice">
              <el-input v-model="form.totalPrice" placeholder="Total Price(USD)" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Remark" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入Remark" />
            </el-form-item>
          </el-col>

        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form')">重置</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改 Approval -->
    <el-dialog :title="title" :visible.sync="openApproval" :close-on-click-modal="false">
      <el-form ref="form" :model="approvalForm" label-width="130px">
        <el-row>
          <el-col :span="18">
            <el-form-item label="Approval " prop="approval">
              <el-input v-model="approvalForm.approval" placeholder="请输入Approval " />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openApproval = false">取 消</el-button>
        <el-button type="primary" @click="submitApprovalForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 生成 新版本 -->
    <el-dialog :title="title" :visible.sync="openBuildVer" :close-on-click-modal="false">
      <el-form ref="buildVerForm" :model="buildVerForm" :rules="rules" label-width="110px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="文件版本号" prop="fileVer">
              <el-input v-model="buildVerForm.fileVer" placeholder="请输入文件版本号" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Approval " prop="approval">
              <el-input v-model="buildVerForm.approval" placeholder="请输入Approval " />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="approval">
              <el-input v-model="buildVerForm.remark" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openBuildVer = false">取 消</el-button>
        <el-button type="primary" @click="submitBuildVerForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 上传文件 对话框-->
    <el-dialog :title="title" :visible.sync="openUpload" width="400px" :close-on-click-modal="false">
      <el-upload
        ref="uploadFile"
        class="upload-demo"
        drag
        :data="upload"
        :limit="1"
        :headers="uploadHeader"
        :auto-upload="false"
        :before-upload="beforeUpload"
        :on-success="handleFileSuccess"
        accept=".xlsx, .xls"
        :action="uploadUrl()"
      >
        <i class="el-icon-upload" />
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip" style="color:red">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openUpload = false">取 消</el-button>
        <el-button type="primary" @click="submitUpload">导入</el-button>
      </div>
    </el-dialog>

    <!-- part num 或 Mete不在数据库中 numMeteNotInDB 对话框-->
    <el-dialog :title="numMeteNotInDBTitle" :visible.sync="openNumMeteNotInDB" width="1200px" :close-on-click-modal="false">
      <el-table
        :data="numMeteNotInDBMePartList"
        border
        stripe
        height="450"
        style="width: 100%"
      >
        <el-table-column width="60" prop="no" label="No." />
        <el-table-column width="60" prop="bomLevel" label="BOM Level" />
        <el-table-column prop="partNumber" label="Part Number" />
        <el-table-column width="251" prop="partDescription" label="Part Description" />
        <el-table-column prop="meterial" label="Meterial" />
        <el-table-column width="70" prop="weight" label="Weight(g)" />
        <el-table-column width="60" prop="qty" label="Q'ty" />
        <el-table-column width="80" prop="vendor" label="Vendor" />
        <el-table-column width="80" prop="unitPrice" label="Unit Price(USD)" />
        <el-table-column width="80" prop="totalPrice" label="Total Price(USD)" />
        <el-table-column prop="remark" label="Remark" />
      </el-table>
    </el-dialog>

    <!-- 上传Excel 的 重复数据 -->
    <el-dialog :title="repeatDataTitle" :visible.sync="openRepeatData" width="1200px" :close-on-click-modal="false">
      <el-table
        :data="repeatDataList"
        border
        stripe
        height="450"
        style="width: 100%"
      >
        <el-table-column width="60" prop="no" label="No." />
        <el-table-column width="60" prop="bomLevel" label="BOM Level" />
        <el-table-column prop="partNumber" label="Part Number" />
        <el-table-column width="251" prop="partDescription" label="Part Description" />
        <el-table-column prop="meterial" label="Meterial" />
        <el-table-column width="70" prop="weight" label="Weight(g)" />
        <el-table-column width="60" prop="qty" label="Q'ty" />
        <el-table-column width="80" prop="vendor" label="Vendor" />
        <el-table-column width="80" prop="unitPrice" label="Unit Price(USD)" />
        <el-table-column width="80" prop="totalPrice" label="Total Price(USD)" />
        <el-table-column prop="remark" label="Remark" />
      </el-table>
    </el-dialog>

    <!-- part num 与 part des 不一致-->
    <el-dialog :title="numDesDiffDataTitle" :visible.sync="openNumDesDiffData" width="1200px" :close-on-click-modal="false">
      <el-table
        :data="numDesDiffDataList"
        border
        stripe
        height="350"
        style="width: 100%"
      >
        <el-table-column width="60" prop="no" label="No." />
        <el-table-column width="60" prop="bomLevel" label="BOM Level" />
        <el-table-column prop="partNumber" label="Part Number" />
        <el-table-column width="251" prop="partDescription" label="Part Description" />
        <el-table-column prop="meterial" label="Meterial" />
        <el-table-column width="70" prop="weight" label="Weight(g)" />
        <el-table-column width="60" prop="qty" label="Q'ty" />
        <el-table-column width="80" prop="vendor" label="Vendor" />
        <el-table-column width="80" prop="unitPrice" label="Unit Price(USD)" />
        <el-table-column width="80" prop="totalPrice" label="Total Price(USD)" />
        <el-table-column prop="remark" label="Remark" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openNumDesDiffData = false">取 消</el-button>
        <el-button type="primary" @click="overDesc">按预设项覆盖Desc</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { selectProtByID } from '@/api/prot'
import { downloadMePartlist, selectAllPartNumberTips, overDescription, backBeforeVersion, matchPartNumber, addVersion, selectProtFileVer, editProtFileVer, selectBySonProtIdAndVersion, addMePartList, editProtMePartList, deleteProtMePartList } from '@/api/mePartList'
import { getToken } from '@/utils/auth'
import { listMaterial } from '@/api/presupposition'
import { downloadXlsx } from '@/utils/download'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'MePartList',
  components: { Treeselect },
  data() {
    return {
      tableHeight1: 600,
      tableHeight2: 500,
      // el-autocomplete form.partNumber
      // state1: '',
      // 存放 partNumber 提示数据
      partNumberTips: [],
      // 存放 meterial 提示数据
      meterialTips: [],
      // 遮罩层
      loading: true,
      // 标签页 位置
      tabPosition: 'top',
      // 哪个标签页生效
      activeName: 'first',
      // 子项目信息
      subProt: {},
      // 主项目信息
      mainProt: {},
      // 表单参数
      form: {},
      // Approval 表单参数
      approvalForm: {},
      // 生成版本 表单参数
      buildVerForm: {},
      // 是否显示Approval弹出层
      openApproval: false,
      // 是否显示 buildVer 弹出层
      openBuildVer: false,
      // 是否显示 上传Excel 文件 弹出层
      openUpload: false,
      openNumMeteNotInDB: false,
      openNumDesDiffData: false,
      openRepeatData: false,
      // 上传 Excel 的头
      uploadHeader: {},
      // me part list
      mePartList: [],
      numMeteNotInDBMePartList: [],
      numDesDiffDataList: [],
      repeatDataList: [],
      // me part list 所有文件版本
      mePartListAllFileVer: [],
      // 子项目id
      sonProtId: undefined,
      // 项目名
      protName: undefined,
      // approval 名
      approvalName: undefined,
      // mePartList 新增 修改 表单参数
      mePartListForm: {},
      // 弹出层标题
      title: '',
      numMeteNotInDBTitle: '',
      numDesDiffDataTitle: '',
      repeatDataTitle: '',
      // 是否显示弹出层
      openMePartList: false,
      // 查询 me part list 参数
      queryParams: {
        protId: undefined,
        fileVer: undefined
      },
      // 导入 excel 参数
      upload: {
        // file: undefined,
        sonProtId: undefined
      },
      // 表单校验
      rules: {
        fileVer: [
          { required: true, message: '文件版本号不能为空', trigger: 'blur' }
        ],
        remark: [
          { min: 0, max: 100, message: '长度在 0 到 100 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.$nextTick(function() {
      this.tableHeight1 = window.innerHeight - 215
      this.tableHeight2 = window.innerHeight - 310
      // 监听窗口大小变化
      const self = this
      window.onresize = function() {
        self.tableHeight1 = window.innerHeight - 215
        self.tableHeight2 = window.innerHeight - 310
      }
    })
  },
  created() {
    this.getProtInfo()
    this.uploadHeader = { 'AuthorizationToken': getToken() }
  },
  methods: {
    /** 上传按钮 */
    handleImport() {
      this.title = '导入Excel'
      this.openUpload = true
      // this.upload.file = undefined
      this.upload.sonProtId = this.sonProtId
    },
    /** 点击确定 上传 按钮 */
    submitUpload: function() {
      // 触发组件的action
      this.$refs.uploadFile.submit()
    },
    overDesc: function() {
      overDescription(this.numDesDiffDataList).then(response => {
        if (response.code === '100000') {
          this.openNumDesDiffData = false
          this.$message({ showClose: true, message: '覆盖Desc并插入成功！', type: 'success' })
          this.getMePartList('now')
        }
      })
    },
    /** 上传excel 成功 */
    handleFileSuccess(response, file, fileList) {
      this.openUpload = false
      this.$refs.uploadFile.clearFiles()
      this.$message({ showClose: true, message: '上传成功', type: 'success' })
      this.getMePartList('now')
      this.numMeteNotInDBMePartList = []
      this.numDesDiffDataList = []
      if (response.data.numMeteNotInDB !== undefined) {
        if (response.data.numMeteNotInDB.length > 0) {
          this.numMeteNotInDBTitle = 'Part Number 或 Meterial 与预设项不符，无法导入 !'
          this.numMeteNotInDBMePartList = response.data.numMeteNotInDB
          this.openNumMeteNotInDB = true
        }
      }
      if (response.data.numDesDiffData !== undefined) {
        if (response.data.numDesDiffData.length > 0) {
          this.numDesDiffDataTitle = 'Part Number对应的Part Description 与预设项中的Part Description不一致!'
          this.numDesDiffDataList = response.data.numDesDiffData
          this.openNumDesDiffData = true
        }
      }
      if (response.data.repeatData !== undefined) {
        if (response.data.repeatData.length > 0) {
          this.repeatDataTitle = '重复的Part Number 和 Part Description不能插入！'
          this.repeatDataList = response.data.repeatData
          this.openRepeatData = true
        }
      }
    },
    /** 导入excel的 url */
    uploadUrl: function() {
      return process.env.VUE_APP_BASE_API + '/protMePartList/upload'
    },
    /** beforeUpload  */
    beforeUpload(file) {
      const extension = file.name.split('.')[file.name.split('.').length - 1] === 'xls'
      const extension2 = file.name.split('.')[file.name.split('.').length - 1] === 'xlsx'
      if (!extension && !extension2) {
        this.$message({
          message: '上传文件只能是 xls、xlsx格式!',
          type: 'error'
        })
      }
      // this.upload.file = file
      return extension || extension2
    },
    /** 恢复为 历史版本 */
    handleBackOldVer(row) {
      this.$confirm('此操作将永久恢复 Me Part List 为 ' + row.fileVer + ' 的版本， 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        backBeforeVersion({ protId: this.sonProtId, fileVer: row.fileVer }).then(res => {
          if (res.code === '100000') {
            this.$message({ showClose: true, message: '已成功恢复，请返回Me Part List查看！', type: 'success' })
          }
        })
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消恢复' })
      })
    },
    /** 查看历史版本 */
    handleViewVer(row) {
      this.getMePartList(row.fileVer)
    },
    /** 匹配Part Number 按钮 */
    matchPartNum() {
      matchPartNumber(this.sonProtId, this.subProt.parentId).then(response => {
        if (response.code === '100000') {
          this.$message({ showClose: true, message: '有 ' + response.data.fillPartNumCount + ' 条数据成功匹配了 Part Number。', type: 'success' })
          setTimeout(() => {
            this.$message({ showClose: true, message: '有 ' + response.data.modifyPartDesCount + ' 条数据的Part Description 被修改了。', type: 'success' })
          }, 2000)
          this.getMePartList('now')
        }
      })
    },
    /** 生成版本 dialog 提交按钮 */
    submitBuildVerForm: function() {
      this.$refs['buildVerForm'].validate(valid => {
        if (valid) {
          addVersion(this.buildVerForm).then(response => {
            if (response.code === '100000') {
              if (response.data.msgFail !== undefined) {
                this.$message({ showClose: true, message: response.data.msgFail, type: 'error', duration: 5000 })
              } else {
                this.$message({ showClose: true, message: '生成版本成功', type: 'success' })
                this.openBuildVer = false
              }
            }
          })
        }
      })
    },
    /** 点击  生成版本 */
    handleBuildVersion() {
      this.buildVerForm = {
        protId: this.sonProtId,
        fileVer: undefined,
        category: 'A',
        remark: undefined,
        approval: undefined
      }
      this.title = '生成新版本'
      this.openBuildVer = true
    },
    /** 获取Approval */
    getApproval() {
      selectProtFileVer({ protId: this.sonProtId, fileVer: 'now', category: 'A' }).then(response => {
        if (response.code === '100000') {
          if (response.data.length > 0) {
            this.approvalName = response.data[0].approval
          } else {
            this.approvalName = ' '
          }
        }
      })
    },
    /** 修改 Approval */
    handleEditApproval() {
      this.approvalForm = {
        protId: this.sonProtId,
        fileVer: 'now',
        category: 'A',
        remark: '当前版本',
        approval: undefined
      }
      this.openApproval = true
      this.title = '修改 Approval'
    },
    /** 修改 Approval 提交按钮 */
    submitApprovalForm: function() {
      editProtFileVer(this.approvalForm).then(response => {
        if (response.code === '100000') {
          this.$message({ showClose: true, message: '修改成功', type: 'success' })
          this.openApproval = false
          this.getApproval()
        }
      })
    },
    /** 带输入建议  partNumberTips */
    querySearch(queryString, cb) {
      const partNumberTips = this.partNumberTips
      const results = queryString ? partNumberTips.filter(this.createFilter(queryString)) : partNumberTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) >= 0)
      }
    },
    handleBlur() {
      if (this.form.partNumber !== undefined) {
        const partNumberTips = this.partNumberTips
        const queryString = this.form.partNumber
        for (let i = 0; i < partNumberTips.length; i++) {
          if (partNumberTips[i].value === queryString) {
            if (partNumberTips[i].partDescription !== '') {
              this.form.partDescription = partNumberTips[i].partDescription
              break
            }
          }
        }
      }
    },
    handleSelect(item) {
      if (item.partDescription !== '') {
        this.form.partDescription = item.partDescription
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.getNewPartNumberTips()
      this.getNewMeterialTips()
      this.title = '添加Me Part List数据'
      this.openMePartList = true
    },
    /** 获取最新 partNumberTips partNumber提示信息 */
    getNewPartNumberTips() {
      selectAllPartNumberTips().then(response => {
        this.partNumberTips = response.data
      })
    },
    /** 获取最新 meterialTips Meterial提示信息 */
    getNewMeterialTips() {
      listMaterial({ material: '' }).then(response => {
        this.meterialTips = response.data
      })
    },
    /** 转换部门数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        // 去掉children=[]的children属性
        delete node.children
      }
      return {
        id: node.material,
        // 将name转换成必填的label键
        label: node.material,
        children: node.children
      }
    },
    /** 添加或修改或插入对话框 提交按钮 */
    submitForm: function() { // 修改
      if (this.form.id !== undefined) {
        editProtMePartList(this.form).then(response => {
          if (response.code === '100000') {
            if (response.data.msgFail !== undefined) {
              this.$message({ showClose: true, message: response.data.msgFail, type: 'error', duration: 5000 })
            } else {
              this.$message({ showClose: true, message: '修改成功', type: 'success' })
              this.openMePartList = false
              this.getMePartList('now')
            }
          }
        })
      } else { // 添加 插入
        addMePartList(this.form).then(response => {
          if (response.code === '100000') {
            if (response.data.msgFail !== undefined) {
              this.$message({ showClose: true, message: response.data.msgFail, type: 'error', duration: 5000 })
            } else {
              this.$message({ showClose: true, message: '添加成功', type: 'success' })
              this.openMePartList = false
              this.getMePartList('now')
            }
          }
        })
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('此操作将永久删除No.为 ' + row.no + '的数据， 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteProtMePartList(row.id, this.subProt.parentId).then(res => {
          if (res.code === '100000') {
            this.$message({ showClose: true, message: '删除成功', type: 'success' })
            this.getMePartList('now')
          }
        })
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消删除' })
      })
    },
    /** 插入按钮操作 */
    handleInsert(row) {
      this.reset()
      this.title = '插入 Me Part List 数据'
      this.getNewPartNumberTips()
      this.getNewMeterialTips()
      this.form.no = row.no + 1
      this.openMePartList = true
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getNewPartNumberTips()
      this.getNewMeterialTips()
      this.title = '修改 Me Part List 数据'
      this.form.id = row.id
      this.form.no = row.no
      this.form.bomLevel = row.bomLevel
      this.form.partNumber = row.partNumber
      this.form.partDescription = row.partDescription
      this.form.meterial = row.meterial
      this.form.weight = row.weight
      this.form.qty = row.qty
      this.form.vendor = row.vendor
      this.form.unitPrice = row.unitPrice
      this.form.totalPrice = row.totalPrice
      this.form.remark = row.remark
      this.openMePartList = true
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        fileVer: 'now',
        protId: this.sonProtId,
        no: undefined,
        bomLevel: undefined,
        partNumber: undefined,
        partDescription: undefined,
        meterial: undefined,
        weight: undefined,
        qty: undefined,
        vendor: undefined,
        unitPrice: undefined,
        totalPrice: undefined,
        remark: undefined
      }
      this.resetForm('form')
    },
    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields()
      }
    },
    /** 下载  Me Part List */
    handleDownload(fileVer) {
      downloadMePartlist({ protId: this.sonProtId, fileVer: fileVer }).then(res => {
        if (res.code === '100000') {
          if (res.data.split('.')[res.data.split('.').length - 1] === 'xls') {
            downloadXlsx(res.data)
          }
          if (res.data.split('.')[res.data.split('.').length - 1] === 'xlsx') {
            downloadXlsx(res.data)
          }
        }
      })
    },
    /** 获取  Me Part List */
    getMePartList(fileVer) {
      this.loading = true
      this.queryParams.protId = this.sonProtId
      this.queryParams.fileVer = fileVer
      selectBySonProtIdAndVersion(this.queryParams).then(response => {
        this.mePartList = response.data
        this.loading = false
      })
    },
    handleTabClick(tab, event) {
      if (tab.name === 'first') {
        this.getProtInfo()
      }
      if (tab.name === 'second') {
        this.mePartList = []
        selectProtFileVer({ protId: this.sonProtId, category: 'A' }).then(response => {
          this.mePartListAllFileVer = response.data
        })
      }
    },
    /** 获取 项目 信息 */
    getProtInfo() {
      this.sonProtId = this.$route.query.sonProtId
      this.getMePartList('now')
      this.getApproval()
      selectProtByID(this.sonProtId).then(response => {
        if (response.code === '100000') {
          this.subProt = response.data
          selectProtByID(this.subProt.parentId).then(mainRes => {
            this.mainProt = mainRes.data
            this.protName = this.mainProt.name + ' / ' + this.subProt.name
          })
        }
      })
    }
  }
}
</script>

<style scoped>
  .app-container {
    padding: 20px;
  }
  .inline-input {
    width: 425px;
  }
  .name {
    text-overflow: ellipsis;
    overflow: hidden;
  }
  .addr {
    font-size: 12px;
    color: #b4b4b4;
  }

</style>
