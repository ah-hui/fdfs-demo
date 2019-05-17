package com.example.fdfsdemo;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 功能描述：FastDFS工具类.
 *
 * @author liuguanghui
 * @since 2019-05-15
 */
@Component
@Slf4j
public class FdfsService {

    @Autowired
    private DefaultFastFileStorageClient defaultFastFileStorageClient;

    public String upload(MultipartFile file) {
        try {
            StorePath storePath = defaultFastFileStorageClient.uploadFile(file.getInputStream(),
                    file.getSize(), getFileExtension(file.getOriginalFilename()), null);
            return storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("FastDFS上传失败！");
            throw new FdfsFailException("FastDFS上传失败！");
        }
    }

    public String upload(File file) {
        try {
            StorePath storePath = defaultFastFileStorageClient.uploadFile(new FileInputStream(file),
                    file.length(), getFileExtension(file.getName()), null);
            return storePath.getFullPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("FastDFS上传失败！");
            throw new FdfsFailException("FastDFS上传失败！");
        }
    }

    public String uploadStream(InputStream is, long size, String filename) {
        try {
            StorePath storePath = defaultFastFileStorageClient.uploadFile(is,
                    size, getFileExtension(filename), null);
            return storePath.getFullPath();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("FastDFS上传失败！");
            throw new FdfsFailException("FastDFS上传失败！");
        }
    }

    public void delete(String filePath) {
        if (!StringUtils.hasLength(filePath)) {
            return;
        }
        try {
            defaultFastFileStorageClient.deleteFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("FastDFS删除失败！filePath=[{}]", filePath);
            throw new FdfsFailException(String.format("FastDFS删除失败！filePath=[{}]", filePath));
        }
    }

    public byte[] downloadFile(String path) {
        try {
            StorePath storePath = StorePath.parseFromUrl(path);
            byte[] bytes = defaultFastFileStorageClient.downloadFile(storePath.getGroup(),
                    storePath.getPath(), new DownloadByteArray());
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("FastDFS下载失败！path=[{}]", path);
            throw new FdfsFailException(String.format("FastDFS下载失败！path=[{}]", path));
        }
    }

    public void downloadFile(String path, OutputStream os) throws IOException {
        try {
            os.write(downloadFile(path));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("FastDFS下载失败！path=[{}]", path);
            throw new FdfsFailException(String.format("FastDFS下载失败！path=[{}]", path));
        }
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
