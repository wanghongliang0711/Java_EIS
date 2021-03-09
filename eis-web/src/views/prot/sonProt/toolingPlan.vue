<template>
  <div class="app-container">
    <template>
      <el-tabs v-model="activeName" :tab-position="tabPosition" @tab-click="handleTabClick">
        <el-tab-pane label="Tooling Plan" name="first">
          <el-button
            type="primary"
            size="mini"
            @click="handleAddFromMePartList"
          >从Me Part List添加</el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleBuildVersion"
          >生成版本</el-button>
          <el-button
            type="info"
            size="mini"
            icon="el-icon-upload2"
          >导入</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-download"
            @click="handleDownload('now')"
          >下载</el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleEditPrNum"
          >修改PR NUMBER</el-button>
          <h3 align="center"> TOOLING PLAN </h3>
          <el-tag type="danger" v-text="'UPDATE DATE:   '+lastTimeAndUser.updateTime">UPDATE DATE</el-tag>&nbsp;&nbsp;&nbsp;&nbsp;
          <el-tag v-text="'UPDATE BY:   '+lastTimeAndUser.updateUser">UPDATE BY</el-tag>&nbsp;&nbsp;
          <el-tag type="danger" v-text="'Project Name :   '+ protName">主项目名 / 子项目名</el-tag>&nbsp;&nbsp;&nbsp;&nbsp;
          <el-tag v-text="'Approval :   '+ approvalName">Approval</el-tag>&nbsp;&nbsp;
          <el-button
            type="primary"
            size="mini"
            @click="handleEditApproval"
          >修改Approval</el-button>
          <el-table
            ref="topicTable"
            v-loading="loading"
            :data="toolPlanData"
            :row-style="{height:'20px'}"
            :cell-style="{padding:'0px'}"
            border
            stripe
            :height="tableHeight1"
            row-key="id"
            style="width: 100%;font-size: 10px"
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
            :header-cell-style="{background:'#87CEEB',color:'#606266'}"
          >
            <el-table-column fixed align="center" width="70" prop="no" label="No." />
            <el-table-column fixed align="center" width="130" prop="partNo" label="PART NUMBER" />
            <el-table-column fixed align="center" width="160" prop="partDes" label="PART DESCRIPTION" />
            <el-table-column align="center" width="160" prop="proEFileName" label="ProE FILE NAME" />
            <el-table-column align="center" width="88" prop="version" label="VERSION" />
            <el-table-column align="center" width="80" prop="weight" label="WEIGHT" />
            <el-table-column align="center" width="80" prop="chineseName" label="中文品名" />
            <el-table-column align="center" width="80" prop="cav" label="CAV." />
            <el-table-column align="center" width="160" prop="material" label="MATERIAL" />
            <el-table-column align="center" width="160" prop="colorNo" label="COLOR No." />
            <el-table-column align="center" width="170" prop="paintingColorNo" label="PAINTING COLOR No." />
            <el-table-column align="center" width="170" prop="printingColorNo" label="PRINTING COLOR No." />
            <el-table-column align="center" width="180" prop="coatingCategory" label="COATING  CATEGORY" />
            <el-table-column align="center" width="180" prop="textureCategory" label="TEXTURE  CATEGORY">
              <template slot-scope="scope">
                <span>{{ scope.row.textureCategory.join("\n") }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" width="165" prop="insertNutSpec" label="INSERT NUT SPEC.">
              <template slot-scope="scope">
                <span>{{ scope.row.insertNutSpec.join("\n") }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" width="88" prop="unitPrice" label="UNIT PRICE(USD)" />
            <el-table-column align="center" width="60" prop="qty" label="Q'TY" />
            <el-table-column align="center" width="160" prop="toolingVender" label="TOOLING VENDER" />
            <el-table-column align="center" width="110" prop="toolingPrCost" label="TOOLING PR COST(USD)" />
            <el-table-column align="center" width="160" prop="prNumber" label="PR NUMBER." />
            <el-table-column align="center" width="160" prop="suppliedVendor" label="SUPPLIED VENDOR" />
            <el-table-column align="center" width="190" prop="remark" label="REMARK" />
            <el-table-column width="400" prop="picture" label="APPEARANCE">
              <template slot-scope="scope">
                <el-button
                  v-for="(onePicture, index1) in scope.row.picture"
                  :key="index1"
                  size="mini"
                  type="text"
                  @click="handleDownloadPicture(onePicture)"
                >{{ onePicture.name }}</el-button>
              </template>
            </el-table-column>
            <el-table-column width="320" fixed="right" label="操作" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  v-if="scope.row.parentId === '0'"
                  size="mini"
                  type="text"
                  icon="el-icon-plus"
                  @click="handleInsertSonData(scope.row)"
                >新增子项</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                >删除</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-upload2"
                  @click="handleImportPicture(scope.row)"
                >传图</el-button>
              </template>
            </el-table-column>
          </el-table>

        </el-tab-pane>

        <el-tab-pane label="查看历史版本" name="second">
          <el-table
            :data="toolPlanAllFileVer"
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
          <h4 align="center"> TOOLING PLAN </h4>
          <el-table
            v-loading="loading"
            :data="toolPlanData"
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
            <el-table-column fixed align="center" width="70" prop="no" label="No." />
            <el-table-column fixed align="center" width="130" prop="partNo" label="PART NUMBER" />
            <el-table-column fixed align="center" width="160" prop="partDes" label="PART DESCRIPTION" />
            <el-table-column align="center" width="160" prop="proEFileName" label="ProE FILE NAME" />
            <el-table-column align="center" width="88" prop="version" label="VERSION" />
            <el-table-column align="center" width="80" prop="weight" label="WEIGHT" />
            <el-table-column align="center" width="80" prop="chineseName" label="中文品名" />
            <el-table-column align="center" width="80" prop="cav" label="CAV." />
            <el-table-column align="center" width="160" prop="material" label="MATERIAL" />
            <el-table-column align="center" width="160" prop="colorNo" label="COLOR No." />
            <el-table-column align="center" width="170" prop="paintingColorNo" label="PAINTING COLOR No." />
            <el-table-column align="center" width="170" prop="printingColorNo" label="PRINTING COLOR No." />
            <el-table-column align="center" width="175" prop="coatingCategory" label="COATING  CATEGORY" />
            <el-table-column align="center" width="175" prop="textureCategory" label="TEXTURE  CATEGORY">
              <template slot-scope="scope">
                <span>{{ scope.row.textureCategory.join("\n") }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" width="165" prop="insertNutSpec" label="INSERT NUT SPEC.">
              <template slot-scope="scope">
                <span>{{ scope.row.insertNutSpec.join("\n") }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" width="88" prop="unitPrice" label="UNIT PRICE(USD)" />
            <el-table-column align="center" width="60" prop="qty" label="Q'TY" />
            <el-table-column align="center" width="160" prop="toolingVender" label="TOOLING VENDER" />
            <el-table-column align="center" width="110" prop="toolingPrCost" label="TOOLING PR COST(USD)" />
            <el-table-column align="center" width="160" prop="prNumber" label="PR NUMBER." />
            <el-table-column align="center" width="160" prop="suppliedVendor" label="SUPPLIED VENDOR" />
            <el-table-column align="center" width="190" prop="remark" label="REMARK" />
            <el-table-column width="400" prop="picture" label="APPEARANCE">
              <template slot-scope="scope">
                <el-button
                  v-for="(onePicture, index1) in scope.row.picture"
                  :key="index1"
                  size="mini"
                  type="text"
                  @click="handleDownloadPicture(onePicture)"
                >{{ onePicture.name }}</el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" fixed="right" width="60" prop="fileVer" label="文件版本" />
          </el-table>

        </el-tab-pane>
      </el-tabs>
    </template>

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
            <el-form-item label="备注" prop="remark">
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

    <!-- 上传图片 对话框-->
    <el-dialog :title="title" :visible.sync="openUploadPicture" width="400px" :close-on-click-modal="false">
      <el-upload
        ref="uploadFile"
        class="upload-demo"
        drag
        :data="uploadPicture"
        :limit="1"
        :headers="uploadHeader"
        :auto-upload="false"
        :before-upload="beforePictureUpload"
        :on-success="handlePictureSuccess"
        accept=".jpg, .png"
        :action="uploadPictureUrl()"
      >
        <i class="el-icon-upload" />
        <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip" style="color:red">提示：仅允许导入“jpg”或“png”格式图片！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openUploadPicture = false">取 消</el-button>
        <el-button type="primary" @click="submitUploadPicture">导 入</el-button>
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

    <!-- 修改Pr Number -->
    <el-dialog :title="title" :visible.sync="openUpdatePrNum" :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="130px">
        <el-row>
          <el-col :span="20">
            <el-form-item label="PR NUMBER " prop="prNumber">
              <el-input v-model="form.prNumber" placeholder="请输入 PR NUMBER" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openUpdatePrNum = false">取 消</el-button>
        <el-button type="primary" @click="submitPrNumberForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="openAddToolPlan" width="1100px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="190px">
        <el-row>
          <el-col v-if="form.parentId === '0'" :span="12">
            <el-form-item label="No." prop="no">
              <el-input-number v-model="form.no" :min="0" label="请输入 No." :precision="0" :step="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="ProE FILE NAME" prop="proEFileName">
              <el-input v-model="form.proEFileName" placeholder="请输入ProE FILE NAME" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Version" prop="version">
              <el-autocomplete
                v-model="form.version"
                class="inline-input1"
                :fetch-suggestions="queryVersion"
                placeholder="请输入Version"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Weight" prop="weight">
              <el-input-number v-model="form.weight" :min="0" label="请输入Weight" :precision="1" :step="0.1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="中文品名" prop="chineseName">
              <el-input v-model="form.chineseName" placeholder="请输入 中文品名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="CAV." prop="cav">
              <el-input v-model="form.cav" placeholder="请输入 CAV." />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Material" prop="material">
              <el-autocomplete
                v-model="form.material"
                class="inline-input1"
                :fetch-suggestions="queryMaterial"
                placeholder="请输入 Material"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="COLOR No." prop="colorNo">
              <el-autocomplete
                v-model="form.colorNo"
                class="inline-input1"
                :fetch-suggestions="queryColorNo"
                placeholder="请输入 COLOR No."
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="PAINTING COLOR No." prop="paintingColorNo">
              <el-autocomplete
                v-model="form.paintingColorNo"
                class="inline-input1"
                :fetch-suggestions="queryPaintingColorNo"
                placeholder="请输入 PAINTING COLOR No."
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="PRINTING COLOR No." prop="printingColorNo">
              <el-autocomplete
                v-model="form.printingColorNo"
                class="inline-input1"
                :fetch-suggestions="queryPrintingColorNo"
                placeholder="请输入 PRINTING COLOR No."
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Coating Category" prop="coatingCategory">
              <el-autocomplete
                v-model="form.coatingCategory"
                class="inline-input1"
                :fetch-suggestions="queryCoatingCategory"
                placeholder="请输入 Coating Category"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="UNIT PRICE(USD)" prop="unitPrice">
              <el-input-number v-model="form.unitPrice" :min="0" label="请输入UNIT PRICE(USD)" :precision="3" :step="0.001" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Q'TY" prop="qty">
              <el-input-number v-model="form.qty" :min="0" label="请输入 Q'TY" :precision="0" :step="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="TOOLING PR COST(USD)" prop="toolingPrCost">
              <el-input-number v-model="form.toolingPrCost" :min="0" label="请输入TOOLING PR COST(USD)" :precision="0" :step="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="TEXTURE CATEGORY" prop="textureCategory">
              <el-select
                v-model="form.textureCategory"
                class="inline-input1"
                multiple
                filterable
                allow-create
                default-first-option
                placeholder="请选择TEXTURE CATEGORY"
              >
                <el-option
                  v-for="item in textureCategoryTipsData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="INSERT NUT SPEC" prop="insertNutSpec">
              <el-select
                v-model="form.insertNutSpec"
                class="inline-input1"
                multiple
                filterable
                allow-create
                default-first-option
                placeholder="请选择INSERT NUT SPEC"
              >
                <el-option
                  v-for="item in insertNutSpecTipsData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="TOOLING VENDER" prop="toolingVender">
              <el-autocomplete
                v-model="form.toolingVender"
                class="inline-input1"
                :fetch-suggestions="queryToolingVender"
                placeholder="请输入 TOOLING VENDER"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="SUPPLIED VENDOR" prop="suppliedVendor">
              <el-autocomplete
                v-model="form.suppliedVendor"
                class="inline-input1"
                :fetch-suggestions="querySuppliedVendor"
                placeholder="请输入 SUPPLIED VENDOR"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="PR NUMBER." prop="prNumber">
              <el-autocomplete
                v-model="form.prNumber"
                class="inline-input2"
                :fetch-suggestions="queryPrNumber"
                placeholder="请输入 PR NUMBER."
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Remark" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入 Remark" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openAddToolPlan = false">取 消</el-button>
        <el-button type="primary" @click="submitAddOrUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 从Me Part List 添加 对话框 -->
    <el-dialog :title="title" :visible.sync="openAddMePartList" width="800px" :close-on-click-modal="false">
      <el-table :data="addFromMePartListData" height="400" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="No." width="60" align="center" prop="no" />
        <el-table-column label="BOM Level" width="60" align="center" prop="bomLevel" />
        <el-table-column label="Part Number" width="130" align="center" prop="partNumber" />
        <el-table-column label="Part Description" prop="partDescription" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openAddMePartList = false">取 消</el-button>
        <el-button type="primary" :disabled="multiple" @click="submitAddMePartListData">添 加</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import { download, selectProtByID } from '@/api/prot'
import { selectByMePartList, downloadToolPlan, updatePrNum, backBeforeVersion, addVersion, selectNewTimeAndUser, deleteProtToolPlanById, editProtToolPlan, addToolPlanSon, selectAllSuppliedVendor, selectAllPrNumber, selectAllToolingVender, selectAllInsertNutSpec, selectAllTextureCategory, selectAllCoatingCategory, selectAllVersion, selectAllPrintingColorNo, selectAllPaintingColorNo, selectAllColorNo, selectAllMaterial, selectByProtIdAndVersion, batchAddByMePartList } from '@/api/toolingPlan'
import { editProtFileVer, selectProtFileVer } from '@/api/mePartList'
import { downloadJpg, downloadPng, downloadXlsx } from '@/utils/download'

export default {
  name: 'ToolingPlan',
  data() {
    return {
      tableHeight1: 600,
      tableHeight2: 500,
      // tool plan 所有文件版本
      toolPlanAllFileVer: [],
      // 表格数据
      toolPlanData: [],
      // approval 名
      approvalName: '',
      // 上传 文件 的头
      uploadHeader: {},
      // 子项目id
      sonProtId: undefined,
      // 子项目信息
      subProt: {},
      // 主项目信息
      mainProt: {},
      // 表单参数
      form: {},
      // 项目名
      protName: undefined,
      // 能从Me Part List添加的数据
      addFromMePartListData: [],
      // Approval 表单参数
      approvalForm: {},
      // 选中数组
      partNumbers: [],
      // 哪个标签页生效
      activeName: 'first',
      // 弹出层标题
      title: '',
      // 遮罩层
      loading: true,
      // 非多个禁用
      multiple: true,
      // 是否显示弹出层
      openAddMePartList: false,
      // dialog 传图
      openUploadPicture: false,
      // 是否显示 新增、修改 弹出层
      openAddToolPlan: false,
      // 所有 VERSION 用于提示
      versionTipsData: [],
      // 所有 MATERIAL 第三级 用于提示
      materialTipsData: [],
      // 所有 COLOR No 用于提示
      colorNoTipsData: [],
      // 所有 PAINTING COLOR No. 用于提示
      paintColorNoTipsData: [],
      // 所有 PRINTING COLOR No. 用于提示
      printColorNoTipsData: [],
      // 所有 COATING  CATEGORY 用于提示
      coatingCategoryTipsData: [],
      // 所有 TEXTURE  CATEGORY 用于提示
      textureCategoryTipsData: [],
      // 所有 INSERT NUT SPEC. 用于提示
      insertNutSpecTipsData: [],
      // 所有 TOOLING  VENDER 用于提示
      toolVenderTipsData: [],
      // 所有 PR NUMBER. 用于提示
      prNumberTipsData: [],
      // 是否显示 修改Approval弹出层
      openApproval: false,
      // 所有 SUPPLIED VENDOR 用于提示
      suppliedVendorTipsData: [],
      // 是否显示 buildVer 弹出层
      openBuildVer: false,
      // 是否显示 修改 pr num 弹出层
      openUpdatePrNum: false,
      // 生成版本 表单参数
      buildVerForm: {},
      // 传图 参数
      uploadPicture: { protId: undefined, category: 'P2', remark: 'Tool Plan Picture' },
      // 最新的 更新时间和 更新人
      lastTimeAndUser: { 'updateTime': ' ', 'updateUser': ' ' },
      // 标签页 位置
      tabPosition: 'top',
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
      this.tableHeight1 = window.innerHeight - 195
      this.tableHeight2 = window.innerHeight - 310
      // 监听窗口大小变化
      const self = this
      window.onresize = function() {
        self.tableHeight1 = window.innerHeight - 195
        self.tableHeight2 = window.innerHeight - 310
      }
    })
  },
  created() {
    this.getProtInfo()
    this.getToolingPlan('now')
    this.getNewTimeAndUser()
    this.getApproval()
    this.uploadHeader = { 'AuthorizationToken': getToken() }
  },
  methods: {
    /** 下载 Tool plan */
    handleDownload(fileVer) {
      downloadToolPlan({ protId: this.sonProtId, fileVer: fileVer }).then(res => {
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
    /** 恢复为 历史版本 */
    handleBackOldVer(row) {
      this.$confirm('此操作将永久恢复 Tool Plan 为 ' + row.fileVer + ' 的版本， 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        backBeforeVersion({ protId: this.sonProtId, fileVer: row.fileVer }).then(res => {
          if (res.code === '100000') {
            this.$message({ showClose: true, message: '已成功恢复，请返回Tooling Plan查看！', type: 'success' })
          }
        })
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消恢复' })
      })
    },
    /** 查看历史版本 数据 */
    handleViewVer(row) {
      this.getToolingPlan(row.fileVer)
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
        category: 'B',
        remark: undefined,
        approval: undefined
      }
      this.title = '生成新版本'
      this.openBuildVer = true
    },
    /** 点击 下载图片按钮  */
    handleDownloadPicture(picture) {
      download(picture.id).then(res => {
        if (res.code === '100000') {
          if (res.data.split('.')[res.data.split('.').length - 1] === 'png') {
            downloadPng(res.data)
          }
          if (res.data.split('.')[res.data.split('.').length - 1] === 'jpg') {
            downloadJpg(res.data)
          }
        }
      })
    },
    /** 点击确定 上传图片 按钮 */
    submitUploadPicture: function() {
      // 触发组件的action
      this.$refs.uploadFile.submit()
    },
    /** 导入 picture 的 url */
    uploadPictureUrl: function() {
      return process.env.VUE_APP_BASE_API + '/protFile/upload'
    },
    /** 上传 Picture 成功  */
    handlePictureSuccess(response, file, fileList) {
      this.openUploadPicture = false
      this.$refs.uploadFile.clearFiles()
      this.$message({ showClose: true, message: '上传成功', type: 'success' })
      this.getToolingPlan('now')
    },
    /** beforePictureUpload  */
    beforePictureUpload(file) {
      const extension = file.name.split('.')[file.name.split('.').length - 1] === 'jpg'
      const extension2 = file.name.split('.')[file.name.split('.').length - 1] === 'png'
      if (!extension && !extension2) {
        this.$message({ message: '上传文件只能是 jpg、png格式!', type: 'error' })
      }
      // this.upload.file = file
      return extension || extension2
    },
    /** 点击 传图 按钮 */
    handleImportPicture(row) {
      this.title = '上传图片'
      this.openUploadPicture = true
      this.uploadPicture.protId = row.id
    },
    /** 获取Approval */
    getApproval() {
      selectProtFileVer({ protId: this.sonProtId, fileVer: 'now', category: 'B' }).then(response => {
        if (response.code === '100000') {
          if (response.data.length > 0) {
            this.approvalName = response.data[0].approval
          } else {
            this.approvalName = ' '
          }
        }
      })
    },
    /** 修改 Approval dialog 提交按钮 */
    submitApprovalForm: function() {
      editProtFileVer(this.approvalForm).then(response => {
        if (response.code === '100000') {
          this.$message({ showClose: true, message: '修改成功', type: 'success' })
          this.openApproval = false
          this.getApproval()
        }
      })
    },
    submitPrNumberForm: function() {
      updatePrNum(this.form).then(response => {
        if (response.code === '100000') {
          this.$message({ showClose: true, message: '修改成功', type: 'success' })
          this.openUpdatePrNum = false
          this.getToolingPlan('now')
          this.getNewTimeAndUser()
        }
      })
    },
    /** 点击修改 PR NUMBER */
    handleEditPrNum() {
      this.reset()
      this.title = '修改 PR NUMBER'
      this.form.protId = this.sonProtId
      this.form.fileVer = 'now'
      this.form.textureCategory = undefined
      this.form.insertNutSpec = undefined
      this.openUpdatePrNum = true
    },
    /** 点击修改 Approval 按钮 */
    handleEditApproval() {
      this.approvalForm = {
        protId: this.sonProtId,
        fileVer: 'now',
        category: 'B',
        remark: 'ToolPlan当前版本',
        approval: undefined
      }
      this.openApproval = true
      this.title = '修改 Approval'
    },
    /** 获取 最新的更新时间  更新人 */
    getNewTimeAndUser() {
      selectNewTimeAndUser(this.$route.query.sonProtId).then(response => {
        if (response.data.lastTime !== undefined && response.data.lastUser !== undefined) {
          this.lastTimeAndUser.updateTime = response.data.lastTime
          this.lastTimeAndUser.updateUser = response.data.lastUser
        }
      })
    },
    /**  点击 删除 按钮 按钮操作 */
    handleDelete(row) {
      this.$confirm('此操作将永久删除这条数据， 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteProtToolPlanById(row.id, this.subProt.parentId, row.parentId).then(res => {
          if (res.code === '100000') {
            if (res.data.msgFail !== undefined) {
              this.$message({ showClose: true, message: res.data.msgFail, type: 'error' })
            } else {
              this.$message({ showClose: true, message: '删除成功', type: 'success' })
              this.getToolingPlan('now')
              this.getNewTimeAndUser()
            }
          }
        })
      }).catch(() => { this.$message({ type: 'info', message: '已取消删除' }) })
    },
    /**  点击 修改按钮 按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.title = '修改 Tool Plan 数据'
      this.form.id = row.id
      this.form.protId = row.protId
      this.form.fileVer = row.fileVer
      this.form.no = row.no
      this.form.partNo = row.partNo
      this.form.partDes = row.partDes
      this.form.proEFileName = row.proEFileName
      this.form.version = row.version
      this.form.weight = row.weight
      this.form.chineseName = row.chineseName
      this.form.cav = row.cav
      this.form.material = row.material
      this.form.colorNo = row.colorNo
      this.form.paintingColorNo = row.paintingColorNo
      this.form.printingColorNo = row.printingColorNo
      this.form.coatingCategory = row.coatingCategory
      this.form.unitPrice = row.unitPrice
      this.form.qty = row.qty
      this.form.toolingVender = row.toolingVender
      this.form.toolingPrCost = row.toolingPrCost
      this.form.prNumber = row.prNumber
      this.form.suppliedVendor = row.suppliedVendor
      this.form.remark = row.remark
      this.form.insertNutSpec = row.insertNutSpec
      this.form.textureCategory = row.textureCategory
      this.form.parentId = row.parentId
      this.openAddToolPlan = true
      this.getAllTips() // 获取11个提示
    },
    /**  新增子项， 修改数据  提交按钮 */
    submitAddOrUpdate: function() {
      if (this.form.weight !== '' && this.form.weight !== undefined) {
        this.form.weight = this.form.weight.toFixed(1)
      }
      if (this.form.unitPrice !== '' && this.form.unitPrice !== undefined) {
        this.form.unitPrice = this.form.unitPrice.toFixed(3)
      }
      if (this.form.qty !== '' && this.form.qty !== undefined) {
        this.form.qty = this.form.qty.toFixed(0)
      }
      if (this.form.toolingPrCost !== '' && this.form.toolingPrCost !== undefined) {
        this.form.toolingPrCost = this.form.toolingPrCost.toFixed(0)
      }
      if (this.form.id !== undefined) { // 修改
        editProtToolPlan(this.form).then(response => {
          if (response.code === '100000') {
            if (response.data.msgFail !== undefined) {
              this.$message({ showClose: true, message: response.data.msgFail, type: 'error', duration: 5000 })
            } else {
              this.openAddToolPlan = false
              this.$message({ showClose: true, message: '修改成功', type: 'success' })
              this.getToolingPlan('now')
              this.getNewTimeAndUser()
            }
          }
        })
      } else { // 添加
        addToolPlanSon(this.form).then(response => {
          if (response.code === '100000') {
            if (response.data.msgFail !== undefined) {
              this.$message({ showClose: true, message: response.data.msgFail, type: 'error', duration: 5000 })
            } else {
              this.openAddToolPlan = false
              this.$message({ showClose: true, message: '添加成功', type: 'success' })
              this.getToolingPlan('now')
              this.getNewTimeAndUser()
            }
          }
        })
      }
    },
    /** 点击 新增 子项 */
    handleInsertSonData(row) {
      this.reset()
      this.title = '新增子项'
      this.form.parentId = row.id
      this.form.protId = row.protId
      this.form.fileVer = 'now'
      this.openAddToolPlan = true
      this.getAllTips() // 获取11个提示
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        protId: undefined,
        fileVer: undefined,
        no: undefined,
        partNo: undefined,
        partDes: undefined,
        proEFileName: undefined,
        version: undefined,
        weight: undefined,
        chineseName: undefined,
        cav: undefined,
        material: undefined,
        colorNo: undefined,
        paintingColorNo: undefined,
        printingColorNo: undefined,
        coatingCategory: undefined,
        unitPrice: undefined,
        qty: undefined,
        toolingVender: undefined,
        toolingPrCost: undefined,
        prNumber: undefined,
        suppliedVendor: undefined,
        remark: undefined,
        insertNutSpec: [],
        textureCategory: [],
        parentId: undefined
      }
    },
    /** 获取 十一个 提示 修改和新增数据的 dialog 提示 */
    getAllTips() {
      selectAllVersion().then(res => { this.versionTipsData = res.data })
      selectAllMaterial().then(res => { this.materialTipsData = res.data })
      selectAllColorNo().then(res => { this.colorNoTipsData = res.data })
      selectAllPaintingColorNo().then(res => { this.paintColorNoTipsData = res.data })
      selectAllPrintingColorNo().then(res => { this.printColorNoTipsData = res.data })
      selectAllCoatingCategory().then(res => { this.coatingCategoryTipsData = res.data })
      selectAllTextureCategory().then(res => { this.textureCategoryTipsData = res.data })
      selectAllInsertNutSpec().then(res => { this.insertNutSpecTipsData = res.data })
      selectAllToolingVender().then(res => { this.toolVenderTipsData = res.data })
      selectAllPrNumber().then(res => { this.prNumberTipsData = res.data })
      selectAllSuppliedVendor().then(res => { this.suppliedVendorTipsData = res.data })
    },
    /** 获取 特定版本 Tooling Plan */
    getToolingPlan(fileVer) {
      this.loading = true
      selectByProtIdAndVersion({ protId: this.$route.query.sonProtId, fileVer: fileVer }).then(res => {
        this.toolPlanData = res.data
        this.loading = false
      })
    },
    /** 批量添加 Me Part List 提交按钮 */
    submitAddMePartListData() {
      const mePartListPartNums = this.partNumbers
      this.$confirm('是否确认添加所选数据', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchAddByMePartList(this.sonProtId, this.subProt.parentId, mePartListPartNums).then(res => {
          if (res.code === '100000') {
            this.getToolingPlan('now')
            this.getNewTimeAndUser()
            this.$message({ showClose: true, message: '添加成功', type: 'success' })
            this.openAddMePartList = false
          }
        })
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消添加' })
      })
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.partNumbers = selection.map(item => item.partNumber)
      this.multiple = !selection.length
    },
    /** 点击 从Me Part List添加 */
    handleAddFromMePartList() {
      this.addFromMePartListData = []
      this.title = '请选择Me Part List数据'
      selectByMePartList(this.sonProtId).then(response => {
        if (response.code === '100000') {
          this.addFromMePartListData = response.data
        }
      })
      this.openAddMePartList = true
    },
    /** 点击选择卡 */
    handleTabClick(tab, event) {
      if (tab.name === 'first') {
        this.getNewTimeAndUser()
        this.getToolingPlan('now')
        console.log('first')
      }
      if (tab.name === 'second') {
        console.log('second')
        this.toolPlanData = []
        selectProtFileVer({ protId: this.sonProtId, category: 'B' }).then(response => {
          this.toolPlanAllFileVer = response.data
        })
      }
    },
    /** 带输入建议 Pr Number Tips */
    queryPrNumber(queryString, cb) {
      const dataTips = this.prNumberTipsData
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议 Supplied Vendor Tips */
    querySuppliedVendor(queryString, cb) {
      const dataTips = this.suppliedVendorTipsData
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  TOOLING  VENDER Tips */
    queryToolingVender(queryString, cb) {
      const dataTips = this.toolVenderTipsData
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  Coating Category Tips */
    queryCoatingCategory(queryString, cb) {
      const dataTips = this.coatingCategoryTipsData
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  printColorNo Tips */
    queryPrintingColorNo(queryString, cb) {
      const dataTips = this.printColorNoTipsData
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  PaintingColorNo Tips */
    queryPaintingColorNo(queryString, cb) {
      const dataTips = this.paintColorNoTipsData
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  ColorNo Tips */
    queryColorNo(queryString, cb) {
      const dataTips = this.colorNoTipsData
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  Material Tips */
    queryMaterial(queryString, cb) {
      const dataTips = this.materialTipsData
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    /** 带输入建议  version Tips */
    queryVersion(queryString, cb) {
      const dataTips = this.versionTipsData
      const results = queryString ? dataTips.filter(this.createFilter(queryString)) : dataTips
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) >= 0)
      }
    },
    /** 获取 项目 信息 */
    getProtInfo() {
      this.sonProtId = this.$route.query.sonProtId
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

<style>
  .app-container {
    padding: 20px;
  }
  .inline-input1 {
    width: 350px;
  }
  .inline-input2 {
    width: 880px;
  }
  .el-table .cell {
    white-space: pre-wrap;
  }
</style>
