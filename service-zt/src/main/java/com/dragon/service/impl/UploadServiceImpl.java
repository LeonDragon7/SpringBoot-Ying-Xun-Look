package com.dragon.service.impl;

import com.dragon.constant.FileConstant;
import com.dragon.constant.MessageConstant;
import com.dragon.exception.RuntimeExceptionCustom;
import com.dragon.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class UploadServiceImpl implements UploadService {

    //存储分片临时目录
    @Value("${upload.temp}")
    private String TEMP_DIR;

    //合并视频目录
    @Value("${upload.directory}")
    private String UPLOAD_DIR;

    //创建一个重入锁
    Lock lock = new ReentrantLock();



    /**
     * 分片上传
     * @param fileName
     * @param fileChunk
     * @param chunkFilename
     * @return
     */
    @Override
    public void uploadFileChunk(String fileName, byte[] fileChunk, String chunkFilename) {
        //1.拼接切片文件夹目录
        File chunkDIR = new File(TEMP_DIR, fileName);
        //2.加锁 等待目录创建
        lock.lock();
        //3.判断目录是否存在
        try {
            if(!chunkDIR.exists()){
                //不存在 创建目录
                try {
                    Files.createDirectory(chunkDIR.toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }finally {
            // 解锁
            lock.unlock();
        }

        //4.拼接完整切片文件
        Path chunkPath = Paths.get(chunkDIR.getAbsolutePath(), chunkFilename);

        //5.将切片视频输出
        try(OutputStream out = Files.newOutputStream(chunkPath)) {
            out.write(fileChunk);
        } catch (Exception e) {
            throw new RuntimeException(MessageConstant.UPLOAD_FAIL.getMessage(),e);
        }
    }

    /**
     * 合并分片
     * @param fileName
     * @return
     */
    @Override
    public void mergeFileChunk(String fileName) {
        //1.拼接切片文件夹目录
        File chunkDIR = new File(TEMP_DIR, fileName);

        //2.读取切片文件夹里的文件路径
        String[] chunks = chunkDIR.list();

        //3.判断文件名是否存在
        if(chunks == null ||chunks.length == 0) throw new RuntimeExceptionCustom(MessageConstant.FILE_NO);

        //4.根据切片的索引排序
        Arrays.stream(chunks).sorted(Comparator.comparing(a -> Integer.parseInt(a.split("-")[0].trim())));

        //5.合并的文件名路径
        String mergePath = Paths.get(UPLOAD_DIR, fileName).toString();

        //6.写入文件的异步任务
        try(FileOutputStream out = new FileOutputStream(mergePath)){
            for (String chunk : chunks) {
                //将逐个切片文件对象合并写入到指定路径
                File chunkFile = new File(chunkDIR, chunk);
                try (FileInputStream in = new FileInputStream(chunkFile)){
                    byte[] buffer = new byte[FileConstant.CHUNK_SIZE];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1){
                        out.write(buffer,0,bytesRead);
                    }
                }
            }
            //7.合并完成后，删除非空切片文件夹
            //方法一：
//            Files.walk(chunkDIR.toPath())
//                    .sorted((p1,p2) -> -p1.compareTo(p2))
//                    .forEach(p -> {
//                        try {
//                            Files.deleteIfExists(p);
//                        } catch (Exception e) {
//                            throw new RuntimeExceptionCustom(MessageConstant.DELETE_FAIL);
//                        }
//                    });
            //方法二
            FileUtils.deleteDirectory(chunkDIR);
        }catch (Exception e){
            throw new RuntimeExceptionCustom(MessageConstant.MERGE_FAIL);
        }
    }
}
