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
import com.musicmanager.system.domain.QlMusicCategoryRelation;
import com.musicmanager.system.service.IQlMusicCategoryRelationService;
import com.musicmanager.common.utils.poi.ExcelUtil;
import com.musicmanager.common.core.page.TableDataInfo;

/**
 * 音乐分类关联Controller
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@RestController
@RequestMapping("/system/relation")
public class QlMusicCategoryRelationController extends BaseController
{
    @Autowired
    private IQlMusicCategoryRelationService qlMusicCategoryRelationService;

    /**
     * 查询音乐分类关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:relation:list')")
    @GetMapping("/list")
    public TableDataInfo list(QlMusicCategoryRelation qlMusicCategoryRelation)
    {
        startPage();
        List<QlMusicCategoryRelation> list = qlMusicCategoryRelationService.selectQlMusicCategoryRelationList(qlMusicCategoryRelation);
        return getDataTable(list);
    }

    /**
     * 导出音乐分类关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:relation:export')")
    @Log(title = "音乐分类关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QlMusicCategoryRelation qlMusicCategoryRelation)
    {
        List<QlMusicCategoryRelation> list = qlMusicCategoryRelationService.selectQlMusicCategoryRelationList(qlMusicCategoryRelation);
        ExcelUtil<QlMusicCategoryRelation> util = new ExcelUtil<QlMusicCategoryRelation>(QlMusicCategoryRelation.class);
        util.exportExcel(response, list, "音乐分类关联数据");
    }

    /**
     * 获取音乐分类关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:relation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(qlMusicCategoryRelationService.selectQlMusicCategoryRelationById(id));
    }

    /**
     * 新增音乐分类关联
     */
    @PreAuthorize("@ss.hasPermi('system:relation:add')")
    @Log(title = "音乐分类关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QlMusicCategoryRelation qlMusicCategoryRelation)
    {
        return toAjax(qlMusicCategoryRelationService.insertQlMusicCategoryRelation(qlMusicCategoryRelation));
    }

    /**
     * 修改音乐分类关联
     */
    @PreAuthorize("@ss.hasPermi('system:relation:edit')")
    @Log(title = "音乐分类关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QlMusicCategoryRelation qlMusicCategoryRelation)
    {
        return toAjax(qlMusicCategoryRelationService.updateQlMusicCategoryRelation(qlMusicCategoryRelation));
    }

    /**
     * 删除音乐分类关联
     */
    @PreAuthorize("@ss.hasPermi('system:relation:remove')")
    @Log(title = "音乐分类关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qlMusicCategoryRelationService.deleteQlMusicCategoryRelationByIds(ids));
    }
}
