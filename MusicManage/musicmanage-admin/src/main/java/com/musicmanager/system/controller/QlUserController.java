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
import com.musicmanager.system.domain.QlUser;
import com.musicmanager.system.service.IQlUserService;
import com.musicmanager.common.utils.poi.ExcelUtil;
import com.musicmanager.common.core.page.TableDataInfo;

/**
 * 用户Controller
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@RestController
@RequestMapping("/system/qluser")
public class QlUserController extends BaseController
{
    @Autowired
    private IQlUserService qlUserService;

    /**
     * 查询用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:qluser:list')")
    @GetMapping("/list")
    public TableDataInfo list(QlUser qlUser)
    {
        startPage();
        List<QlUser> list = qlUserService.selectQlUserList(qlUser);
        return getDataTable(list);
    }

    /**
     * 导出用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:qluser:export')")
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QlUser qlUser)
    {
        List<QlUser> list = qlUserService.selectQlUserList(qlUser);
        ExcelUtil<QlUser> util = new ExcelUtil<QlUser>(QlUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    /**
     * 获取用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:qluser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(qlUserService.selectQlUserById(id));
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:qluser:add')")
    @Log(title = "用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QlUser qlUser)
    {
        return toAjax(qlUserService.insertQlUser(qlUser));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:qluser:edit')")
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QlUser qlUser)
    {
        return toAjax(qlUserService.updateQlUser(qlUser));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:qluser:remove')")
    @Log(title = "用户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qlUserService.deleteQlUserByIds(ids));
    }
}
