package com.musicmanager.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.musicmanager.common.core.controller.BaseController;
import com.musicmanager.common.core.domain.AjaxResult;
import com.musicmanager.system.domain.QlBanner;
import com.musicmanager.system.service.IQlBannerService;

/**
 * Banner前台接口Controller
 * 
 * @author musicmanager
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/api/banner")
public class BannerController extends BaseController
{
    @Autowired
    private IQlBannerService qlBannerService;

    /**
     * 查询激活状态的Banner列表（前台接口，无需权限）
     */
    @GetMapping("/active")
    public AjaxResult getActiveBanners()
    {
        List<QlBanner> list = qlBannerService.selectActiveBannerList();
        return AjaxResult.success(list);
    }
}