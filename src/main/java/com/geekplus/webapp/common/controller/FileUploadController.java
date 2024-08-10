package com.geekplus.webapp.common.controller;

import com.geekplus.common.config.WebAppConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/31/24 4:31 AM
 * description: 做什么的？
 */

@RestController
@RequestMapping("/plus/upload")
public class FileUploadController {

    private String uploadDirectory= WebAppConfig.getProfile();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final Map<String, UploadedFileInfo> fileInfoMap = new ConcurrentHashMap<>();
    // 文件分片信息类
    private static class UploadedFileInfo {
        String fileName;
        long fileSize;
        int chunkSize;
        int totalChunks;
        Set<Integer> uploadedChunks;
        public UploadedFileInfo(String fileName, long fileSize, int chunkSize) {
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.chunkSize = chunkSize;
            this.totalChunks = (int) Math.ceil((double) fileSize / chunkSize);
            this.uploadedChunks = Collections.newSetFromMap(new ConcurrentHashMap<>());
        }
    }

    /**
      * @Author geekplus
      * @Description //初始化上传
      * @Param fileName 文件名 fileSize 文件大小 chunkSize 分片部分大小
      * @Throws
      * @Return {@link }
      */
    @PostMapping("/init")
    public ResponseEntity<Map<String, Object>> initUpload(@RequestParam("fileName") String fileName,
                                                          @RequestParam("fileSize") long fileSize,
                                                          @RequestParam("chunkSize") int chunkSize) {
        String fileKey = generateFileKey(fileName); // 生成唯一文件标识
        fileInfoMap.put(fileKey, new UploadedFileInfo(fileName, fileSize, chunkSize));
        Map<String, Object> response = new HashMap<>();
        response.put("fileKey", fileKey);
        response.put("totalChunks", fileInfoMap.get(fileKey).totalChunks);
        return ResponseEntity.ok(response);
    }
    // 上传分片
    @PostMapping("/{fileKey}/{chunkNumber}")
    public ResponseEntity<String> uploadChunk(@PathVariable String fileKey,
                                              @PathVariable int chunkNumber,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        UploadedFileInfo fileInfo = fileInfoMap.get(fileKey);
        if (fileInfo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file key");
        }

        // 保存分片文件
        String chunkFileName = getChunkFileName(fileKey, chunkNumber);
        try {
            // 使用线程池处理每个分片上传
            executorService.execute(() -> {
                try {
                    File chunkFile = new File(uploadDirectory, chunkFileName);
                    try (InputStream inputStream = file.getInputStream();
                         OutputStream outputStream = new FileOutputStream(chunkFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                    fileInfo.uploadedChunks.add(chunkNumber);
                } catch (IOException e) {
                    // 处理异常，例如记录日志或返回错误信息
                    e.printStackTrace();
                }
            });

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Chunk uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading chunk.");
        }
//        return ResponseEntity.ok("Chunk uploaded successfully");
    }
    // 合并分片
    @PostMapping("/merge/{fileKey}")
    public ResponseEntity<String> mergeChunks(@PathVariable String fileKey) throws IOException {
        UploadedFileInfo fileInfo = fileInfoMap.get(fileKey);
        if (fileInfo == null || fileInfo.uploadedChunks.size() != fileInfo.totalChunks) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not all chunks uploaded");
        }
        // 合并分片文件
        String filePath = uploadDirectory + File.separator + fileInfo.fileName;
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            for (int i = 1; i <= fileInfo.totalChunks; i++) {
                String chunkFileName = getChunkFileName(fileKey, i);
                Path chunkFilePath = Paths.get(uploadDirectory, chunkFileName);
                byte[] chunkData = Files.readAllBytes(chunkFilePath);
                outputStream.write(chunkData);
                // 删除分片文件 (可选)
                Files.deleteIfExists(chunkFilePath);
            }
        }
        fileInfoMap.remove(fileKey); // 清理文件信息
        return ResponseEntity.ok("File merged successfully");
    }
    // 生成唯一文件标识
    private String generateFileKey(String fileName) {
        return UUID.randomUUID().toString() + "_" + fileName;
    }
    // 获取分片文件名
    private String getChunkFileName(String fileKey, int chunkNumber) {
        return fileKey + "_chunk_" + chunkNumber;
    }
}
