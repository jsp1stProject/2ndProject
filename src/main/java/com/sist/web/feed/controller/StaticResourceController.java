package com.sist.web.feed.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@RequestMapping("/images/{filename:.+}")
@Controller
public class StaticResourceController {
	
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		System.out.println("이미지출력?");
        Path path = Paths.get("C:/download/").resolve(filename);
        Resource resource = new FileSystemResource(path);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
