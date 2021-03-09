package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.dto.ProtDailyReportQueryRecordDto;
import com.mic.eis.domain.dto.ProtToolTrackListDto;
import com.mic.eis.domain.dto.ProtToolTrackListForDailyReportDto;
import com.mic.eis.domain.model.ProtToolTrackList;

import java.util.HashMap;
import java.util.List;

public interface ProtToolTrackListService extends IService<ProtToolTrackList> {

    /**
     * 通过 子项目id 查询 最新 的数据
     * @param sonProtId 子类 id
     * @return List<ProtToolTrackListDto
     */
    List<ProtToolTrackListDto> selectBySonProtId(Long sonProtId, String issuePriority);


    /**
     * 通过 项目id、子项目id、bug等级 查询 最新 的数据
     * @param protDailyReportQueryRecordDto ProtDailyReportQueryRecordDto
     * @return List<ProtToolTrackListForDailyReportDto>
     */
    List<ProtToolTrackListForDailyReportDto> findForDailyReport(ProtDailyReportQueryRecordDto protDailyReportQueryRecordDto);


    /**
     * 通过ParentId查询 子类
     * @param parentId 父类 id
     * @param issuePriority issuePriority
     * @return 这个父类 符合 issuePriority 的所有子类
     */
    List<ProtToolTrackList> selectByParentId(Long parentId, String issuePriority);


    /**
     * 通过 id 查找
     * @param id Long
     * @return  ProtToolTrackList
     */
    ProtToolTrackList selectToolTrackListById(Long id);

    /**
     * 添加 protToolTrackList 新 issue
     * @param protToolTrackList ProtToolTrackList 实体
     * @return {msg: }
     */
    HashMap<String, Object> addProtToolTrackListIssue(ProtToolTrackList protToolTrackList);


    /**
     * 修改 protToolTrackList issue
     * @param protToolTrackList 实体
     * @return true false
     */
    boolean editProtToolTrackListIssue(ProtToolTrackList protToolTrackList);


    /**
     * 通过 id 删除 -->  真实删除
     * @param id id
     */
    void deleteProtToolTrackListById(Long id);


    /**
     *  删除 tool plan 主数据时，
     *  tool track list 对应的part num issue 全部变为closed,并remark
     * @param partNo part num
     */
    void updateToolTrackListPartNumForClosed(String partNo, Long sonProtId);


    /**
     * tool plan 版本恢复 或者 从Me Part List 添加时
     * Tool track List 对应的 issue 是 closed 且 有 关键字的 全部 open ？
     * @param partNo
     * @param sonProtId
     */
    void updateToolTrackListPartNumForOpen(String partNo, Long sonProtId);


    /**
     * 获取所有的 root cause 用于提示
     * @return [{value: root cause }, ]
     */
    List<HashMap<String, String>> selectAllRootCauseTips();


    /**
     * 获取 最新的 更新时间  更新人
     * @return  [{time: 更新时间 }, {user: 更新人 } ]
     */
    HashMap<String, String> selectLastTimeUser(Long sonProtId);


    /**
     * 获取 最新的 Latest Tx
     * @param sonProtId 子项目 id
     * @return List<ProtToolTrackList>
     */
    List<ProtToolTrackList> selectLatestTxBySonProtId(Long sonProtId);

    /**
     * 获取 Tx History
     * @param sonProtId  子项目 id
     * @return  List<ProtToolTrackList>
     */
    List<ProtToolTrackList> selectTxHistoryBySonProtId(Long sonProtId);


    /**
     * 同步 tooling plan 数据
     * @param sonProtId 子项目 id
     */
    void syncToolPlanData(Long sonProtId);


    /**
     * 获取  TOOLING TRACKING Report
     * @param sonProtId 子项目 id
     * @return List<ProtToolTrackListIssueReportDto>
     */
    HashMap<String, Object> selectIssueReportBySonProtId(Long sonProtId);


    /**
     * Issue Tracking 这个 issue 的 所有 Tx 数据
     * @param sonProtId Long 子项目 id
     * @param issueId  issue id
     * @return List<ProtToolTrackList>
     */
    List<ProtToolTrackList> selectIssueTracking(Long sonProtId, Long issueId);


    /**
     * Issue Tracking / ReOpen issue
     * @param issueId issue id
     */
    void reOpenIssue(Long issueId);


    /**
     * 通过 子项目id 查询 最新 的数据
     * @param sonProtId 子项目 id
     * @return ExcelData
     */
    List<ProtToolTrackList> selectExcelData(Long sonProtId);
}
