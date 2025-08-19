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
import com.musicmanager.system.domain.QlPlaylistMusic;
import com.musicmanager.system.service.IQlPlaylistMusicService;
import com.musicmanager.common.utils.poi.ExcelUtil;
import com.musicmanager.common.core.page.TableDataInfo;

/**
 * 歌单音乐关联Controller
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@RestController
@RequestMapping("/system/playlistMusic")
public class QlPlaylistMusicController extends BaseController
{
    @Autowired
    private IQlPlaylistMusicService qlPlaylistMusicService;

    /**
     * 查询歌单音乐关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:playlistMusic:list')")
    @GetMapping("/list")
    public TableDataInfo list(QlPlaylistMusic qlPlaylistMusic)
    {
        startPage();
        List<QlPlaylistMusic> list = qlPlaylistMusicService.selectQlPlaylistMusicList(qlPlaylistMusic);
        return getDataTable(list);
    }

    /**
     * 导出歌单音乐关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:playlistMusic:export')")
    @Log(title = "歌单音乐关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QlPlaylistMusic qlPlaylistMusic)
    {
        List<QlPlaylistMusic> list = qlPlaylistMusicService.selectQlPlaylistMusicList(qlPlaylistMusic);
        ExcelUtil<QlPlaylistMusic> util = new ExcelUtil<QlPlaylistMusic>(QlPlaylistMusic.class);
        util.exportExcel(response, list, "歌单音乐关联数据");
    }

    /**
     * 获取歌单音乐关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:playlistMusic:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(qlPlaylistMusicService.selectQlPlaylistMusicById(id));
    }

    /**
     * 新增歌单音乐关联
     */
    @PreAuthorize("@ss.hasPermi('system:playlistMusic:add')")
    @Log(title = "歌单音乐关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QlPlaylistMusic qlPlaylistMusic)
    {
        return toAjax(qlPlaylistMusicService.insertQlPlaylistMusic(qlPlaylistMusic));
    }

    /**
     * 修改歌单音乐关联
     */
    @PreAuthorize("@ss.hasPermi('system:playlistMusic:edit')")
    @Log(title = "歌单音乐关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QlPlaylistMusic qlPlaylistMusic)
    {
        return toAjax(qlPlaylistMusicService.updateQlPlaylistMusic(qlPlaylistMusic));
    }

    /**
     * 删除歌单音乐关联
     */
    @PreAuthorize("@ss.hasPermi('system:playlistMusic:remove')")
    @Log(title = "歌单音乐关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qlPlaylistMusicService.deleteQlPlaylistMusicByIds(ids));
    }
}
