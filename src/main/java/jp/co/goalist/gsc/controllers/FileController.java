package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.gen.apis.FilesApi;
import jp.co.goalist.gsc.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileController implements FilesApi {

    private final FileService fileService;

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> importCSVFile(String screenName, MultipartFile multipartFile) {
        log.info("importCSVFile, screenName: {}", screenName);
        fileService.importCSVFile(screenName, multipartFile);
        return ResponseEntity.ok().build();
    }
}
