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
import com.musicmanager.system.domain.QlMusicCategory;
import com.musicmanager.system.service.IQlMusicCategoryService;
import com.musicmanager.common.utils.poi.ExcelUtil;
import com.musicmanager.common.core.page.TableDataInfo;

/**
 * 音乐分类Controller
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@RestController
@RequestMapping("/system/category")
public class QlMusicCategoryController extends BaseController
{
    @Autowired
    private IQlMusicCategoryService qlMusicCategoryService;

    /**
     * 查询音乐分类列表
     */
    @PreAuthorize("@ss.hasPermi('system:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(QlMusicCategory qlMusicCategory)
    {
        startPage();
        List<QlMusicCategory> list = qlMusicCategoryService.selectQlMusicCategoryList(qlMusicCategory);
        return getDataTable(list);
    }

    /**
     * 导出音乐分类列表
     */
    @PreAuthorize("@ss.hasPermi('system:category:export')")
    @Log(title = "音乐分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QlMusicCategory qlMusicCategory)
    {
        List<QlMusicCategory> list = qlMusicCategoryService.selectQlMusicCategoryList(qlMusicCategory);
        ExcelUtil<QlMusicCategory> util = new ExcelUtil<QlMusicCategory>(QlMusicCategory.class);
        util.exportExcel(response, list, "音乐分类数据");
    }

    /**
     * 获取音乐分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(qlMusicCategoryService.selectQlMusicCategoryById(id));
    }

    /**
     * 新增音乐分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:add')")
    @Log(title = "音乐分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QlMusicCategory qlMusicCategory)
    {
        return toAjax(qlMusicCategoryService.insertQlMusicCategory(qlMusicCategory));
    }

    /**
     * 修改音乐分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:edit')")
    @Log(title = "音乐分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QlMusicCategory qlMusicCategory)
    {
        return toAjax(qlMusicCategoryService.updateQlMusicCategory(qlMusicCategory));
    }

    /**
     * 删除音乐分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:remove')")
    @Log(title = "音乐分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qlMusicCategoryService.deleteQlMusicCategoryByIds(ids));
    }
}
