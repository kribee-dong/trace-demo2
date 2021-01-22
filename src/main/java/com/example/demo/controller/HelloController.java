package com.example.demo.controller;

import com.example.demo.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试Controller
 *
 * @author Dongjia
 * @date 2020/7/29
 */
@RestController
@Slf4j
public class HelloController {


    @Value("${uri}")
    private String uri;


    @GetMapping("/hello")
    public Object get(HttpServletRequest request) {
        // 从请求头中获取traceId
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        try {
            HttpUtils.get(uri, headers);
        } catch (URISyntaxException e) {
            log.error("request foreign resource error, reason: ", e);
            return new ResponseEntity<>("failed to call [" + uri + "] reason: " + e, HttpStatus.OK);
        }
        return new ResponseEntity<>("call [" + uri + "] successfully!", HttpStatus.OK);
    }

    @GetMapping("/echo")
    public ResponseEntity<Integer> echo(Integer arg) {
        return new ResponseEntity<>(arg, HttpStatus.OK);
    }

    @GetMapping("/print")
    public ResponseEntity<String> print(String msg) {
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/display")
    public ResponseEntity<String> display(String msg) {
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/show")
    public ResponseEntity<String> show(String msg) {
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @RequestMapping("/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
        log.info("进入下载方法。。。。");
        String fileName = "CentOS-7-x86_64-Minimal-1810.iso";// 设置文件名，根据业务需要替换成要下载的文件名
        if (fileName != null) {
            //设置文件路径
            String realPath = "D:\\tmp\\";
            File file = new File(realPath, fileName);
            if (file.exists()) {
                response.setContentType("application/octet-stream");//
                response.setHeader("content-type", "application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile(String fileName)
        throws IOException {
        log.info("进入下载方法...");
        String filePath = "D:/tmp/" + fileName + ".iso";
        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentLength(file.contentLength())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(new InputStreamResource(file.getInputStream()));
    }

}
