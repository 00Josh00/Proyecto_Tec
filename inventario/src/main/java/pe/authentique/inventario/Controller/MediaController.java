package pe.authentique.inventario.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.authentique.inventario.service.FileSystemStorateService;

import java.nio.file.Files;

@Slf4j
@Controller
@RequestMapping("/media")
public class MediaController {
    @Autowired
    private FileSystemStorateService fileSystemStorateService;

    @GetMapping("/{nombreArchivo}")
    ResponseEntity<Resource> getMedia(@PathVariable String nombreArchivo) throws Exception {
        Resource resource = fileSystemStorateService.loadAsResource(nombreArchivo);
        String contentType = Files.probeContentType(resource.getFile().toPath());

        return ResponseEntity
                .ok()
                .header("Content-Type", contentType)
                .body(resource);
    }
}
