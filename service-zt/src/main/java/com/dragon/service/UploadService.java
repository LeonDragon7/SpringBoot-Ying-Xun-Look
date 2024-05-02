package com.dragon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    /**
     * 分片上传
     * @param fileName
     * @param fileChunk
     * @param chunkFilename
     * @return
     */
    void uploadFileChunk(String fileName, byte[] fileChunk, String chunkFilename);

    /**
     * 合并分片
     * @param fileName
     * @return
     */
    void mergeFileChunk(String fileName);

}
