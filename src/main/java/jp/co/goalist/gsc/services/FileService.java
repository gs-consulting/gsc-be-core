package jp.co.goalist.gsc.services;

import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.enums.UploadScreen;
import jp.co.goalist.gsc.services.bulkImport.AdvertisementBulkImportService;
import jp.co.goalist.gsc.services.bulkImport.ApplicantBulkImportService;
import jp.co.goalist.gsc.services.bulkImport.MediaApplicantBulkImportService;
import jp.co.goalist.gsc.services.bulkImport.ProjectBulkImportService;
import jp.co.goalist.gsc.utils.GeneralUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final ProjectBulkImportService projectBulkImportService;
    private final AdvertisementBulkImportService advertisementBulkImportService;
    private final ApplicantBulkImportService applicantBulkImportService;
    private final MediaApplicantBulkImportService mediaApplicantBulkImportService;
    private final UtilService utilService;

    public void importCSVFile(String screenName, MultipartFile multipartFile) {
        UploadScreen uploadScreen = UploadScreen.valueOf(screenName);
        Account account = GeneralUtils.getCurrentUser();
        Triple<String, String, String> parentInfo = utilService.getParentIdAndOemIdAndGroupId(account);

        switch (uploadScreen) {
            case PROJECT -> {
                projectBulkImportService.setParentInfo(parentInfo.getLeft(), parentInfo.getMiddle(), parentInfo.getRight());
                projectBulkImportService.execute(multipartFile);
            }
            case ADVERTISEMENT -> {
                advertisementBulkImportService.setParentInfo(parentInfo.getLeft(), parentInfo.getRight());
                advertisementBulkImportService.execute(multipartFile);
            }
            case APPLICANT -> {
                applicantBulkImportService.setParentInfo(parentInfo.getLeft(), parentInfo.getRight());
                applicantBulkImportService.execute(multipartFile);
            }
            case MEDIA_APPLICANT -> {
                mediaApplicantBulkImportService.setParentInfo(parentInfo.getLeft(), parentInfo.getRight());
                mediaApplicantBulkImportService.execute(multipartFile);
            }
            default -> throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "画面", screenName));
        }
    }
}
