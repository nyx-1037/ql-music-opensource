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
import com.musicmanager.system.domain.QlPlaylist;
import com.musicmanager.system.service.IQlPlaylistService;
import com.musicmanager.common.utils.poi.ExcelUtil;
import com.musicmanager.common.core.page.TableDataInfo;

/**
 * 歌单Controller
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@RestController
@RequestMapping("/system/playlist")
public class QlPlaylistController extends BaseController
{
    @Autowired
    private IQlPlaylistService qlPlaylistService;

    /**
     * 查询歌单列表
     */
    @PreAuthorize("@ss.hasPermi('system:playlist:list')")
    @GetMapping("/list")
    public TableDataInfo list(QlPlaylist qlPlaylist)
    {
        startPage();
        List<QlPlaylist> list = qlPlaylistService.selectQlPlaylistList(qlPlaylist);
        return getDataTable(list);
    }

    /**
     * 导出歌单列表
     */
    @PreAuthorize("@ss.hasPermi('system:playlist:export')")
    @Log(title = "歌单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QlPlaylist qlPlaylist)
    {
        List<QlPlaylist> list = qlPlaylistService.selectQlPlaylistList(qlPlaylist);
        ExcelUtil<QlPlaylist> util = new ExcelUtil<QlPlaylist>(QlPlaylist.class);
        util.exportExcel(response, list, "歌单数据");
    }

    /**
     * 获取歌单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:playlist:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(qlPlaylistService.selectQlPlaylistById(id));
    }

    /**
     * 新增歌单
     */
    @PreAuthorize("@ss.hasPermi('system:playlist:add')")
    @Log(title = "歌单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QlPlaylist qlPlaylist)
    {
        return toAjax(qlPlaylistService.insertQlPlaylist(qlPlaylist));
    }

    /**
     * 修改歌单
     */
    @PreAuthorize("@ss.hasPermi('system:playlist:edit')")
    @Log(title = "歌单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QlPlaylist qlPlaylist)
    {
        return toAjax(qlPlaylistService.updateQlPlaylist(qlPlaylist));
    }

    /**
     * 删除歌单
     */
    @PreAuthorize("@ss.hasPermi('system:playlist:remove')")
    @Log(title = "歌单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qlPlaylistService.deleteQlPlaylistByIds(ids));
    }
}
