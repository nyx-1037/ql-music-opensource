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
import com.musicmanager.system.domain.QlUserFollow;
import com.musicmanager.system.service.IQlUserFollowService;
import com.musicmanager.common.utils.poi.ExcelUtil;
import com.musicmanager.common.core.page.TableDataInfo;

/**
 * 用户关注关系Controller
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@RestController
@RequestMapping("/system/follow")
public class QlUserFollowController extends BaseController
{
    @Autowired
    private IQlUserFollowService qlUserFollowService;

    /**
     * 查询用户关注关系列表
     */
    @PreAuthorize("@ss.hasPermi('system:follow:list')")
    @GetMapping("/list")
    public TableDataInfo list(QlUserFollow qlUserFollow)
    {
        startPage();
        List<QlUserFollow> list = qlUserFollowService.selectQlUserFollowList(qlUserFollow);
        return getDataTable(list);
    }

    /**
     * 导出用户关注关系列表
     */
    @PreAuthorize("@ss.hasPermi('system:follow:export')")
    @Log(title = "用户关注关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QlUserFollow qlUserFollow)
    {
        List<QlUserFollow> list = qlUserFollowService.selectQlUserFollowList(qlUserFollow);
        ExcelUtil<QlUserFollow> util = new ExcelUtil<QlUserFollow>(QlUserFollow.class);
        util.exportExcel(response, list, "用户关注关系数据");
    }

    /**
     * 获取用户关注关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:follow:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(qlUserFollowService.selectQlUserFollowById(id));
    }

    /**
     * 新增用户关注关系
     */
    @PreAuthorize("@ss.hasPermi('system:follow:add')")
    @Log(title = "用户关注关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QlUserFollow qlUserFollow)
    {
        return toAjax(qlUserFollowService.insertQlUserFollow(qlUserFollow));
    }

    /**
     * 修改用户关注关系
     */
    @PreAuthorize("@ss.hasPermi('system:follow:edit')")
    @Log(title = "用户关注关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QlUserFollow qlUserFollow)
    {
        return toAjax(qlUserFollowService.updateQlUserFollow(qlUserFollow));
    }

    /**
     * 删除用户关注关系
     */
    @PreAuthorize("@ss.hasPermi('system:follow:remove')")
    @Log(title = "用户关注关系", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qlUserFollowService.deleteQlUserFollowByIds(ids));
    }
}
