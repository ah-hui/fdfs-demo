package com.example.fdfsdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private FdfsService fdfsService;

    @GetMapping
    String test() {
        return "test";
    }

    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        String upload = fdfsService.upload(file);
        return upload;
    }

    @GetMapping("/download")
    public String download(String path, HttpServletResponse response) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 从FastDFS获取文件字节流
            fdfsService.downloadFile(path, baos);
            int size = baos.size();
            // 将流写到response
            response.setHeader("Content-Length", String.valueOf(size));
            response.setHeader("Content-Encoding", "UTF-8");
            baos.writeTo(response.getOutputStream());
            baos.flush();
            return "成功";
        } catch (Exception e) {
            e.printStackTrace();
            log.error("读取文件失败！path=[{}], errorMsg=[{}]", path, e.getMessage());
            return "读取文件失败！";
        }
    }
}
