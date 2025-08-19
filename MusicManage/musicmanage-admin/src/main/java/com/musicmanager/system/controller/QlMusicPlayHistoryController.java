package com.musicmanager.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.musicmanager.common.annotation.Log;
import com.musicmanager.common.core.controller.BaseController;
import com.musicmanager.common.core.domain.AjaxResult;
import com.musicmanager.common.enums.BusinessType;
import com.musicmanager.system.domain.QlMusicPlayHistory;
import com.musicmanager.system.service.IQlMusicPlayHistoryService;
import com.musicmanager.common.utils.poi.ExcelUtil;
import com.musicmanager.common.core.page.TableDataInfo;

/**
 * 音乐播放历史Controller
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@RestController
@RequestMapping("/system/history")
public class QlMusicPlayHistoryController extends BaseController
{
    @Autowired
    private IQlMusicPlayHistoryService qlMusicPlayHistoryService;

    /**
     * 查询音乐播放历史列表
     */
    @PreAuthorize("@ss.hasPermi('system:history:list')")
    @GetMapping("/list")
    public TableDataInfo list(QlMusicPlayHistory qlMusicPlayHistory)
    {
        startPage();
        List<QlMusicPlayHistory> list = qlMusicPlayHistoryService.selectQlMusicPlayHistoryList(qlMusicPlayHistory);
        return getDataTable(list);
    }

    /**
     * 导出音乐播放历史列表
     */
    @PreAuthorize("@ss.hasPermi('system:history:export')")
    @Log(title = "音乐播放历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QlMusicPlayHistory qlMusicPlayHistory)
    {
        List<QlMusicPlayHistory> list = qlMusicPlayHistoryService.selectQlMusicPlayHistoryList(qlMusicPlayHistory);
        ExcelUtil<QlMusicPlayHistory> util = new ExcelUtil<QlMusicPlayHistory>(QlMusicPlayHistory.class);
        util.exportExcel(response, list, "音乐播放历史数据");
    }

    /**
     * 获取音乐播放历史详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:history:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(qlMusicPlayHistoryService.selectQlMusicPlayHistoryById(id));
    }

    /**
     * 新增音乐播放历史
     */
    @PreAuthorize("@ss.hasPermi('system:history:add')")
    @Log(title = "音乐播放历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QlMusicPlayHistory qlMusicPlayHistory)
    {
        return toAjax(qlMusicPlayHistoryService.insertQlMusicPlayHistory(qlMusicPlayHistory));
    }

    /**
     * 修改音乐播放历史
     */
    @PreAuthorize("@ss.hasPermi('system:history:edit')")
    @Log(title = "音乐播放历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QlMusicPlayHistory qlMusicPlayHistory)
    {
        return toAjax(qlMusicPlayHistoryService.updateQlMusicPlayHistory(qlMusicPlayHistory));
    }

    /**
     * 删除音乐播放历史
     */
    @PreAuthorize("@ss.hasPermi('system:history:remove')")
    @Log(title = "音乐播放历史", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qlMusicPlayHistoryService.deleteQlMusicPlayHistoryByIds(ids));
    }
}
