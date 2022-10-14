package com.hello.upload.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {
    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String file() {
        return "upload-form";
    }

    @PostMapping("/upload")
//    public String saveFileV1(@RequestParam String itemName,
//                             @RequestParam MultipartFile file,
//                             HttpServletRequest request) throws IOException {
    public String saveFileV1(@ModelAttribute SaveFileV1Request saveFileV1Request,
                             HttpServletRequest request) throws IOException {
        String itemName = saveFileV1Request.getItemName();
        MultipartFile file = saveFileV1Request.getFile();
        log.info("request={}", request);
        log.info("itemName={}", itemName);
        log.info("multipartFile={}", file);

        if (!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("파일 저장 fullPat={}", fullPath);
            file.transferTo(new File(fullPath));
        }
        return "upload-form";
    }

    @Getter
    static class SaveFileV1Request {
        private String itemName;
        private MultipartFile file;

        public SaveFileV1Request(String itemName, MultipartFile file) {
            this.itemName = itemName;
            this.file = file;
        }
    }
}
