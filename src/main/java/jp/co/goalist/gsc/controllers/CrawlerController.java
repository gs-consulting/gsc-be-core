package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.gen.apis.CrawlerApi;
import jp.co.goalist.gsc.gen.dtos.CrawlerMediasDto;
import jp.co.goalist.gsc.services.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class CrawlerController implements CrawlerApi {

    private final CrawlerService crawlerService;

    @Override
    public ResponseEntity<CrawlerMediasDto> getCrawlerMedias(String mediaId) {
        CrawlerMediasDto mediasDto = crawlerService.getCrawlerMediasDto(mediaId);
        return ResponseEntity.ok(mediasDto);
    }

    @Override
    public ResponseEntity<Void> importMediaCsv(String mediaId, MultipartFile multipartFile) {
        crawlerService.importMediaCsv(mediaId, multipartFile);
        return ResponseEntity.accepted().build();
    }
}
