package com.hello.upload.controller;

import com.hello.upload.domain.Item;
import com.hello.upload.domain.ItemRepository;
import com.hello.upload.domain.UploadFile;
import com.hello.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        Item item = new Item();

        item.setId(form.getItemId());
        log.info("ItemId={}", item.getId());

        item.setItemName(form.getItemName());
        log.info("ItemName={}", item.getItemName());

        MultipartFile attachFile = form.getAttachFile();
        UploadFile uploadFile = fileStore.storeFile(attachFile);
        item.setAttachFile(uploadFile);
        log.info("attachFileName={}", attachFile.getName());

        List<MultipartFile> imageFiles = form.getImageFiles();
        List<UploadFile> uploadImageFiles = fileStore.storeFiles(imageFiles);
        item.setImageFiles(uploadImageFiles);
        for (UploadFile uploadImageFile : uploadImageFiles) {
            log.info("uploadImageFileName={}", uploadImageFile.getUploadFileName());
            log.info("storeImageFileName={}", uploadImageFile.getStoreFileName());
        }
        itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/items/{itemId}";
    }

    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }

    @ResponseBody
    @GetMapping(path = "/images/{filename}"
//            , produces = MediaType.IMAGE_JPEG_VALUE // HTTP 응답에서 반환되는 타입을 정해줘, 해석할 수 있도록
    )
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
        log.info("uploadFileName={}", uploadFileName);
        String encodingUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodingUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
