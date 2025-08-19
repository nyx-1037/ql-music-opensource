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
import com.musicmanager.system.domain.QlBanner;
import com.musicmanager.system.service.IQlBannerService;
import com.musicmanager.system.service.OssService;
import com.musicmanager.common.utils.poi.ExcelUtil;
import com.musicmanager.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BannerController
 * 
 * @author musicmanager
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/system/qlbanner")
public class QlBannerController extends BaseController
{
    private static final Logger logger = LoggerFactory.getLogger(QlBannerController.class);
    
    @Autowired
    private IQlBannerService qlBannerService;
    
    @Autowired
    private OssService ossService;

    /**
     * 查询Banner列表
     */
    @PreAuthorize("@ss.hasPermi('system:banner:list')")
    @GetMapping("/list")
    public TableDataInfo list(QlBanner qlBanner)
    {
        startPage();
        List<QlBanner> list = qlBannerService.selectQlBannerList(qlBanner);
        return getDataTable(list);
    }

    /**
     * 查询激活状态的Banner列表（前台接口，无需权限）
     */
    @GetMapping("/active")
    public AjaxResult getActiveBanners()
    {
        List<QlBanner> list = qlBannerService.selectActiveBannerList();
        return AjaxResult.success(list);
    }

    /**
     * 导出Banner列表
     */
    @PreAuthorize("@ss.hasPermi('system:banner:export')")
    @Log(title = "Banner", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QlBanner qlBanner)
    {
        List<QlBanner> list = qlBannerService.selectQlBannerList(qlBanner);
        ExcelUtil<QlBanner> util = new ExcelUtil<QlBanner>(QlBanner.class);
        util.exportExcel(response, list, "Banner数据");
    }

    /**
     * 获取Banner详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:banner:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(qlBannerService.selectQlBannerById(id));
    }

    /**
     * 新增Banner
     */
    @PreAuthorize("@ss.hasPermi('system:banner:add')")
    @Log(title = "Banner", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QlBanner qlBanner)
    {
        return toAjax(qlBannerService.insertQlBanner(qlBanner));
    }

    /**
     * 修改Banner
     */
    @PreAuthorize("@ss.hasPermi('system:banner:edit')")
    @Log(title = "Banner", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QlBanner qlBanner)
    {
        return toAjax(qlBannerService.updateQlBanner(qlBanner));
    }

    /**
     * 删除Banner
     */
    @PreAuthorize("@ss.hasPermi('system:banner:remove')")
    @Log(title = "Banner", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qlBannerService.deleteQlBannerByIds(ids));
    }
    
    /**
     * 修改Banner状态
     */
    @PreAuthorize("@ss.hasPermi('system:banner:edit')")
    @Log(title = "Banner状态", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody QlBanner qlBanner)
    {
        try {
            logger.info("修改Banner状态 - ID: {}, 状态: {}", qlBanner.getId(), qlBanner.getStatus());
            
            // 参数验证
            if (qlBanner.getId() == null) {
                return AjaxResult.error("Banner ID不能为空");
            }
            
            if (qlBanner.getStatus() == null) {
                return AjaxResult.error("状态不能为空");
            }
            
            // 验证状态值
            if (!"0".equals(qlBanner.getStatus()) && !"1".equals(qlBanner.getStatus())) {
                return AjaxResult.error("状态值只能为0(禁用)或1(启用)");
            }
            
            // 检查Banner是否存在
            QlBanner existBanner = qlBannerService.selectQlBannerById(qlBanner.getId());
            if (existBanner == null) {
                return AjaxResult.error("Banner不存在");
            }
            
            // 只更新状态字段
            existBanner.setStatus(qlBanner.getStatus());
            int result = qlBannerService.updateQlBanner(existBanner);
            
            if (result > 0) {
                String statusText = "1".equals(qlBanner.getStatus()) ? "启用" : "禁用";
                logger.info("Banner状态修改成功 - ID: {}, 新状态: {}", qlBanner.getId(), statusText);
                return AjaxResult.success("状态修改成功");
            } else {
                logger.warn("Banner状态修改失败 - ID: {}", qlBanner.getId());
                return AjaxResult.error("状态修改失败");
            }
            
        } catch (Exception e) {
            logger.error("修改Banner状态异常 - ID: {}, 错误信息: {}", qlBanner.getId(), e.getMessage(), e);
            return AjaxResult.error("状态修改失败，请稍后重试");
        }
    }
    
    /**
     * 上传Banner图片
     */
    @PreAuthorize("@ss.hasPermi('system:banner:add')")
    @Log(title = "Banner图片上传", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public AjaxResult uploadBannerImage(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("开始上传Banner图片文件: {}", file.getOriginalFilename());
            
            // 参数验证
            if (file == null || file.isEmpty()) {
                logger.warn("上传失败：未选择Banner图片文件");
                return AjaxResult.error("请选择要上传的Banner图片文件");
            }
            
            // 验证文件类型
            String originalFilename = file.getOriginalFilename();
            if (!StringUtils.hasText(originalFilename)) {
                logger.warn("上传失败：文件名为空");
                return AjaxResult.error("文件名不能为空");
            }
            
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            if (!".jpg".equals(fileExtension) && !".jpeg".equals(fileExtension) && 
                !".png".equals(fileExtension) && !".gif".equals(fileExtension) && 
                !".webp".equals(fileExtension)) {
                logger.warn("上传失败：不支持的文件格式: {}", fileExtension);
                return AjaxResult.error("只支持 jpg、jpeg、png、gif、webp 格式的图片文件");
            }
            
            // 验证文件大小（限制为10MB）
            long maxSize = 10 * 1024 * 1024; // 10MB
            if (file.getSize() > maxSize) {
                logger.warn("上传失败：文件大小超限，大小: {} bytes", file.getSize());
                return AjaxResult.error("图片文件大小不能超过10MB");
            }
            
            logger.info("Banner图片文件信息 - 名称: {}, 大小: {} bytes, 类型: {}", 
                    file.getOriginalFilename(), file.getSize(), file.getContentType());
            
            // 调用OSS服务上传文件
            String imageUrl = ossService.uploadBannerFile(file);
            
            if (StringUtils.hasText(imageUrl)) {
                logger.info("Banner图片上传成功，URL: {}", imageUrl);
                return AjaxResult.success("Banner图片上传成功", imageUrl);
            } else {
                logger.error("Banner图片上传失败：OSS服务返回空URL");
                return AjaxResult.error("Banner图片上传失败");
            }
            
        } catch (Exception e) {
            logger.error("Banner图片上传失败，错误信息: {}", e.getMessage(), e);
            // 检查是否是业务异常，如果是则直接返回异常信息
            String errorMessage = e.getMessage();
            if (errorMessage != null && (errorMessage.contains("文件格式") || errorMessage.contains("文件大小") || errorMessage.contains("OSS"))) {
                return AjaxResult.error(errorMessage);
            }
            return AjaxResult.error("Banner图片上传失败，请稍后重试");
        }
    }
}