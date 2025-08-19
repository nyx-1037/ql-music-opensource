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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import com.musicmanager.common.annotation.Log;
import com.musicmanager.common.core.controller.BaseController;
import com.musicmanager.common.core.domain.AjaxResult;
import com.musicmanager.common.enums.BusinessType;
import com.musicmanager.system.domain.QlMusic;
import com.musicmanager.system.domain.QlUser;
import com.musicmanager.system.service.IQlMusicService;
import com.musicmanager.system.service.IQlUserService;
import com.musicmanager.common.utils.poi.ExcelUtil;
import com.musicmanager.common.core.page.TableDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 音乐Controller
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@RestController
@RequestMapping("/system/music")
public class QlMusicController extends BaseController
{
    private static final Logger logger = LoggerFactory.getLogger(QlMusicController.class);
    
    @Autowired
    private IQlMusicService qlMusicService;
    
    @Autowired
    private IQlUserService qlUserService;

    /**
     * 查询音乐列表
     */
    @PreAuthorize("@ss.hasPermi('system:music:list')")
    @GetMapping("/list")
    public TableDataInfo list(QlMusic qlMusic)
    {
        startPage();
        List<QlMusic> list = qlMusicService.selectQlMusicList(qlMusic);
        return getDataTable(list);
    }

    /**
     * 导出音乐列表
     */
    @PreAuthorize("@ss.hasPermi('system:music:export')")
    @Log(title = "音乐", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QlMusic qlMusic)
    {
        List<QlMusic> list = qlMusicService.selectQlMusicList(qlMusic);
        ExcelUtil<QlMusic> util = new ExcelUtil<QlMusic>(QlMusic.class);
        util.exportExcel(response, list, "音乐数据");
    }

    /**
     * 获取音乐详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:music:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(qlMusicService.selectQlMusicById(id));
    }

    /**
     * 新增音乐
     */
    @PreAuthorize("@ss.hasPermi('system:music:add')")
    @Log(title = "音乐", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QlMusic qlMusic)
    {
        return toAjax(qlMusicService.insertQlMusic(qlMusic));
    }

    /**
     * 修改音乐
     */
    @PreAuthorize("@ss.hasPermi('system:music:edit')")
    @Log(title = "音乐", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QlMusic qlMusic)
    {
        return toAjax(qlMusicService.updateQlMusic(qlMusic));
    }

    /**
     * 删除音乐
     */
    @PreAuthorize("@ss.hasPermi('system:music:remove')")
    @Log(title = "音乐", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qlMusicService.deleteQlMusicByIds(ids));
    }

    /**
     * 上传音乐文件
     */
    @PreAuthorize("@ss.hasPermi('system:music:add')")
    @Log(title = "音乐上传", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public AjaxResult uploadMusic(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("artist") String artist,
            @RequestParam(value = "album", required = false) String album,
            @RequestParam(value = "cover", required = false) MultipartFile cover,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "releaseYear", required = false) String releaseYear,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "isPublic", defaultValue = "1") Integer isPublic,
            @RequestParam(value = "allowDownload", defaultValue = "1") Integer allowDownload,
            @RequestParam(value = "allowComment", defaultValue = "1") Integer allowComment
    ) {
        try {
            logger.info("开始上传音乐文件，标题: {}, 艺术家: {}", title, artist);
            
            // 参数验证
            if (file == null || file.isEmpty()) {
                logger.warn("上传失败：未选择音乐文件");
                return AjaxResult.error("请选择要上传的音乐文件");
            }
            
            if (!StringUtils.hasText(title)) {
                logger.warn("上传失败：音乐标题为空");
                return AjaxResult.error("音乐标题不能为空");
            }
            
            if (!StringUtils.hasText(artist)) {
                logger.warn("上传失败：艺术家为空");
                return AjaxResult.error("艺术家不能为空");
            }
            
            if (title.length() > 100) {
                logger.warn("上传失败：音乐标题长度超限，长度: {}", title.length());
                return AjaxResult.error("音乐标题长度不能超过100个字符");
            }
            
            if (artist.length() > 50) {
                logger.warn("上传失败：艺术家名称长度超限，长度: {}", artist.length());
                return AjaxResult.error("艺术家名称长度不能超过50个字符");
            }
            
            logger.info("文件信息 - 名称: {}, 大小: {} bytes, 类型: {}", 
                    file.getOriginalFilename(), file.getSize(), file.getContentType());
            
            if (cover != null && !cover.isEmpty()) {
                logger.info("封面文件信息 - 名称: {}, 大小: {} bytes, 类型: {}", 
                        cover.getOriginalFilename(), cover.getSize(), cover.getContentType());
            }
            
            // 获取当前登录用户对应的ql_user ID
            String currentUsername = getUsername();
            QlUser qlUser = qlUserService.selectQlUserByUsername(currentUsername);
            if (qlUser == null) {
                logger.error("未找到对应的ql_user记录，用户名: {}", currentUsername);
                return AjaxResult.error("用户信息异常，请联系管理员");
            }
            
            // 调用服务层处理上传
            QlMusic music = qlMusicService.uploadMusic(file, title, artist, album, cover, 
                    genre, releaseYear, description, tags, isPublic, allowDownload, allowComment, qlUser.getId());
            
            logger.info("音乐上传成功，音乐ID: {}", music.getId());
            return AjaxResult.success("音乐上传成功", music);
            
         // 移除重复的MultipartException处理，让全局异常处理器统一处理
        } catch (Exception e) {
            logger.error("音乐上传失败，错误信息: {}", e.getMessage(), e);
            // 检查是否是业务异常，如果是则直接返回异常信息
            String errorMessage = e.getMessage();
            if (errorMessage != null && (errorMessage.contains("文件格式") || errorMessage.contains("文件大小") || errorMessage.contains("OSS"))) {
                return AjaxResult.error(errorMessage);
            }
            return AjaxResult.error("音乐上传失败，请稍后重试");
        }
    }
}
