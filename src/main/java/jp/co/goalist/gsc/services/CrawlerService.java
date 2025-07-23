package jp.co.goalist.gsc.services;

import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.MasterMedia;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.repositories.MasterMediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.co.goalist.gsc.gen.dtos.CrawlerMediasDto;
import jp.co.goalist.gsc.services.bulkImport.MediaApplicantBulkImportService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static jp.co.goalist.gsc.mappers.MasterMediaMapper.MASTER_MEDIA_MAPPER;

@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final MediaApplicantBulkImportService mediaApplicantBulkImportService;
    private final MasterMediaRepository masterMediaRepository;

    public void importMediaCsv(String mediaId, MultipartFile multipartFile) {
        mediaApplicantBulkImportService.setMediaId(mediaId);
        mediaApplicantBulkImportService.execute(multipartFile);
    }

    public CrawlerMediasDto getCrawlerMediasDto(String mediaId) {
        if (mediaId == null || mediaId.isEmpty()) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.REQUIRED_FIELD.getStatusCode())
                    .message(String.format(
                            ErrorMessage.REQUIRED_FIELD.getMessage(),
                            TargetName.MASTER_MEDIA.getTargetName()))
                    .fieldError("mediaId")
                    .build());
        }

        Optional<MasterMedia> masterMediaOptional = masterMediaRepository.findById(mediaId);
        if (masterMediaOptional.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.MASTER_MEDIA.getTargetName()))
                    .fieldError("mediaId")
                    .build());
        }

        return MASTER_MEDIA_MAPPER.toCrawlerMediasDto(masterMediaOptional.get());
    }
}
