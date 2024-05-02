package com.dragon.controller.admin;

import com.dragon.result.Result;
import com.dragon.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fzt
 * @since 2024-05-01
 */
@Api(tags = "上传相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/upload")
@CrossOrigin
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 分片上传
     * @param fileName
     * @param fileChunk
     * @param chunkFilename
     * @return
     */
    @ApiOperation("分片上传")
    @PostMapping("/upload/{fileName}")
    public Result uploadChunk(
            @ApiParam(name = "fileName",value = "视频名",required = true)
            @PathVariable String fileName,
            @ApiParam(name = "fileChunk",value = "分片视频",required = true)
            @RequestBody byte[] fileChunk,
            @ApiParam(name = "fileChunkName",value = "分片视频名",required = true)
            @RequestParam String chunkFilename
    ){
        log.info("分片上传参数：{}",fileName,fileChunk,chunkFilename);
        uploadService.uploadFileChunk(fileName,fileChunk,chunkFilename);
        return Result.success();
    }

    /**
     * 合并分片
     * @param fileName
     * @return
     */
    @ApiOperation("合并分片")
    @GetMapping("/merge/{filename}")
    public Result mergeChunk(
            @ApiParam(name = "filename",value = "视频名",required = true)
            @PathVariable(value = "filename") String fileName
    ){
        log.info("分片视频名：{}",fileName);
        uploadService.mergeFileChunk(fileName);
        return Result.success();
    }
}
