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
import com.musicmanager.system.domain.QlMusicFavorite;
import com.musicmanager.system.service.IQlMusicFavoriteService;
import com.musicmanager.common.utils.poi.ExcelUtil;
import com.musicmanager.common.core.page.TableDataInfo;

/**
 * 音乐收藏Controller
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@RestController
@RequestMapping("/system/favorite")
public class QlMusicFavoriteController extends BaseController
{
    @Autowired
    private IQlMusicFavoriteService qlMusicFavoriteService;

    /**
     * 查询音乐收藏列表
     */
    @PreAuthorize("@ss.hasPermi('system:favorite:list')")
    @GetMapping("/list")
    public TableDataInfo list(QlMusicFavorite qlMusicFavorite)
    {
        startPage();
        List<QlMusicFavorite> list = qlMusicFavoriteService.selectQlMusicFavoriteList(qlMusicFavorite);
        return getDataTable(list);
    }

    /**
     * 导出音乐收藏列表
     */
    @PreAuthorize("@ss.hasPermi('system:favorite:export')")
    @Log(title = "音乐收藏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QlMusicFavorite qlMusicFavorite)
    {
        List<QlMusicFavorite> list = qlMusicFavoriteService.selectQlMusicFavoriteList(qlMusicFavorite);
        ExcelUtil<QlMusicFavorite> util = new ExcelUtil<QlMusicFavorite>(QlMusicFavorite.class);
        util.exportExcel(response, list, "音乐收藏数据");
    }

    /**
     * 获取音乐收藏详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:favorite:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(qlMusicFavoriteService.selectQlMusicFavoriteById(id));
    }

    /**
     * 新增音乐收藏
     */
    @PreAuthorize("@ss.hasPermi('system:favorite:add')")
    @Log(title = "音乐收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QlMusicFavorite qlMusicFavorite)
    {
        return toAjax(qlMusicFavoriteService.insertQlMusicFavorite(qlMusicFavorite));
    }

    /**
     * 修改音乐收藏
     */
    @PreAuthorize("@ss.hasPermi('system:favorite:edit')")
    @Log(title = "音乐收藏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QlMusicFavorite qlMusicFavorite)
    {
        return toAjax(qlMusicFavoriteService.updateQlMusicFavorite(qlMusicFavorite));
    }

    /**
     * 删除音乐收藏
     */
    @PreAuthorize("@ss.hasPermi('system:favorite:remove')")
    @Log(title = "音乐收藏", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qlMusicFavoriteService.deleteQlMusicFavoriteByIds(ids));
    }
}
