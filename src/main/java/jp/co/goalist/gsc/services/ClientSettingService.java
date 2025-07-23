package jp.co.goalist.gsc.services;

import static jp.co.goalist.gsc.mappers.InterviewCategoryMapper.INTERVIEW_CATEGORY_MAPPER;
import static jp.co.goalist.gsc.mappers.MasterMediaMapper.MASTER_MEDIA_MAPPER;
import static jp.co.goalist.gsc.mappers.MasterStatusMapper.MASTER_STATUS_MAPPER;
import static jp.co.goalist.gsc.mappers.MonthlyCostMapper.MONTHLY_COST_MAPPER;
import static jp.co.goalist.gsc.mappers.SelectionStatusMapper.SELECTION_STATUS_MAPPER;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import jp.co.goalist.gsc.dtos.InterviewCategoryDto;
import jp.co.goalist.gsc.dtos.MasterDataStatusDto;
import jp.co.goalist.gsc.dtos.SelectionStatusDto;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.services.criteriaBuilder.MasterDataCriteriaBuilder;
import org.springframework.data.domain.Page;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.enums.StatusType;
import jp.co.goalist.gsc.enums.SubRole;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.repositories.InterviewCategoryRepository;
import jp.co.goalist.gsc.repositories.MasterMediaRepository;
import jp.co.goalist.gsc.repositories.MasterStatusRepository;
import jp.co.goalist.gsc.repositories.MediaReportDisplayRepository;
import jp.co.goalist.gsc.repositories.MonthlyCostRepository;
import jp.co.goalist.gsc.repositories.SelectionStatusRepository;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientSettingService {

    private final SelectionStatusRepository selectionStatusRepo;
    private final InterviewCategoryRepository interviewCategoryRepo;
    private final MasterMediaRepository masterMediaRepo;
    private final MediaReportDisplayRepository mediaReportDisplayRepo;
    private final MonthlyCostRepository monthlyCostRepo;
    private final MasterDataCriteriaBuilder masterDataCriteriaBuilder;
    private final UtilService utilService;
    private final MasterStatusRepository masterStatusRepository;

    private MasterMedia getMasterMediaById(String id, String parentId) {
        Optional<MasterMedia> mediaOp = masterMediaRepo.findByIdAndParentId(id, parentId);
        if (mediaOp.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.MASTER_MEDIA.getTargetName(),
                            id))
                    .fieldError("id")
                    .build());
        }

        return mediaOp.get();
    }

    @Transactional
    public void upsertSelectionStatuses(
            List<MasterSelectionStatusUpsertDto> masterSelectionStatusUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        List<SelectionStatus> removedList = selectionStatusRepo.getRemovedSelectionStatuses(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                masterSelectionStatusUpsertDto.stream().map(MasterSelectionStatusUpsertDto::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet())
                );

        List<SelectionStatus> newSelectionStatuses = masterSelectionStatusUpsertDto.stream()
                .map(item -> {
                    SelectionStatus selectionStatus = SELECTION_STATUS_MAPPER.toEntity(item);
                    selectionStatus.setParentId(parentInfo.getLeft());
                    selectionStatus.setOemGroupId(parentInfo.getRight());
                    return selectionStatus;
                }).toList();

        selectionStatusRepo.saveAll(newSelectionStatuses);
        selectionStatusRepo.deleteAll(removedList);
    }

    @Transactional
    public void upsertMasterDataStatuses(
            String statusType,
            List<MasterDataStatusUpsertDto> masterProjectStatusUpsertDto) {
        StatusType type = StatusType.fromId(statusType);
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        List<MasterStatus> removedList = masterStatusRepository.getRemovedMasterStatuses(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                type.getId(),
                masterProjectStatusUpsertDto.stream().map(MasterDataStatusUpsertDto::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet())
        );

        Set<MasterStatus> newProjectStatus = masterProjectStatusUpsertDto.stream()
                .map(item -> MASTER_STATUS_MAPPER.mapToMasterStatus(
                        item,
                        parentInfo.getLeft(),
                        parentInfo.getRight(),
                        type.getId())
                ).collect(Collectors.toSet());
        masterStatusRepository.saveAllAndFlush(newProjectStatus);
        masterStatusRepository.deleteAll(removedList);
    }

    @Transactional
    public MasterMediaDetailsDto getMasterMediaDetails(String id) {
        Account account = GeneralUtils.getCurrentUser();
        MasterMedia media = getMasterMediaById(id, utilService.getParentIdAndGroupId(account).getLeft());
        return MASTER_MEDIA_MAPPER.toMasterMediaDetailsDto(media);
    }

    @Transactional
    public MasterMediaListDto getMasterMedias(Integer pageNumber, Integer pageSize, String searchInput) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        Page<MasterMedia> mediaPage = masterMediaRepo.findAllBy(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                GeneralUtils.wrapToLike(searchInput),
                GeneralUtils.getPagination(pageNumber, pageSize));

        return MasterMediaListDto.builder()
                .page(mediaPage.getNumber() + 1)
                .limit(mediaPage.getSize())
                .total(mediaPage.getTotalElements())
                .items(mediaPage.getContent().stream()
                        .map(MASTER_MEDIA_MAPPER::toMasterMediaItemsDto)
                        .toList())
                .build();
    }

    @Transactional
    public MasterMediaReportListDto getMediaReportSettings() {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        List<MediaReportDisplay> mediaReportSettings = mediaReportDisplayRepo.getMediaReportSetting(
                parentInfo.getLeft(),
                parentInfo.getRight());

        List<Boolean> settings;
        if (mediaReportSettings.isEmpty()) {
            settings = List.of(true, true, true, true);
        } else {
            settings = mediaReportSettings.stream().map(MediaReportDisplay::getIsEnabled).toList();
        }

        MasterMediaReportListDto masterMediaReportListDto = new MasterMediaReportListDto();
        masterMediaReportListDto.setSettings(settings);
        return masterMediaReportListDto;
    }

    @Transactional
    public List<MasterMonthlyCostItemsDto> getMonthlyCost(String selectedDate) {
        LocalDate parsedDate = validateYYYYMM(selectedDate);
        Account account = GeneralUtils.getCurrentUser();

        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        List<MonthlyCost> monthlyCosts = monthlyCostRepo.findMonthlyCostSetting(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                parsedDate);

        return monthlyCosts.stream()
                .map(MONTHLY_COST_MAPPER::toItemsDto)
                .toList();
    }

    @Transactional
    public List<MasterDataStatusItemsDto> getMasterDataStatuses(String statusType) {
        Account account = GeneralUtils.getCurrentUser();
        StatusType type = StatusType.fromId(statusType);
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        List<MasterDataStatusDto> statuses = masterDataCriteriaBuilder.getMasterDataStatuses(
                type,
                parentInfo.getLeft(),
                parentInfo.getRight()
        );

        return statuses.stream()
                .map(MASTER_STATUS_MAPPER::toItemsDto)
                .toList();
    };

    @Transactional
    public List<MasterSelectionStatusItemsDto> getSelectionStatuses() {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        List<SelectionStatusDto> selectionStatuses;

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                selectionStatuses = selectionStatusRepo.findAllOperatorStatuses(
                        parentInfo.getLeft());
            }
            case SubRole.OEM -> {
                selectionStatuses = selectionStatusRepo.findAllOemStatuses(
                        parentInfo.getLeft(),
                        parentInfo.getRight());
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }

        return selectionStatuses.stream()
                .map(SELECTION_STATUS_MAPPER::toSelectionStatusItemsDto)
                .toList();
    }

    @Transactional
    public void upsertInterviewCategories(
            List<MasterInterviewCategoryUpsertDto> masterInterviewCategoryUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        List<InterviewCategory> removedList = interviewCategoryRepo.getRemovedInterviewCategory(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                masterInterviewCategoryUpsertDto.stream().map(MasterInterviewCategoryUpsertDto::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet())
        );

        Set<InterviewCategory> newInterviewCategories = masterInterviewCategoryUpsertDto.stream()
                .map(item -> INTERVIEW_CATEGORY_MAPPER.mapToInterviewCategory(
                        item,
                        parentInfo.getLeft(),
                        parentInfo.getRight())
                ).collect(Collectors.toSet());
        interviewCategoryRepo.saveAllAndFlush(newInterviewCategories);
        interviewCategoryRepo.deleteAll(removedList);
    }

    @Transactional
    public List<MasterInterviewCategoryItemsDto> getInterviewCategories() {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        List<InterviewCategoryDto> interviewCategories;

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                interviewCategories  = interviewCategoryRepo.getOperatorInterviewCategories(parentInfo.getLeft());
            }
            case SubRole.OEM -> {
                interviewCategories  = interviewCategoryRepo.getOemInterviewCategories(parentInfo.getLeft(), parentInfo.getRight());
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }

        return interviewCategories.stream()
                .map(INTERVIEW_CATEGORY_MAPPER::toInterviewCategoryItem)
                .toList();
    }

    @Transactional
    public void createMasterMedia(MasterMediaUpsertDto masterMediaUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        // Check if media name already exists
        validateMasterMediaNameUniqueness(
                masterMediaUpsertDto.getName(),
                null,
                parentInfo.getLeft(),
                parentInfo.getRight()
        );

        MasterMedia media = MASTER_MEDIA_MAPPER.toEntity(masterMediaUpsertDto);
        media.setParentId(parentInfo.getLeft());
        media.setOemGroupId(parentInfo.getRight());
        masterMediaRepo.save(media);
    }

    @Transactional
    public void upsertMediaReportSettings(MasterMediaReportUpsertDto masterMediaReportUpsertDto) {
        validateMasterMediaReportUpsertDto(masterMediaReportUpsertDto);
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        List<MediaReportDisplay> mediaReportSetting = mediaReportDisplayRepo.getMediaReportSetting(
                parentInfo.getLeft(),
                parentInfo.getRight());

        List<MediaReportDisplay> newSettings = new ArrayList<>();
        for (int i = 0; i < masterMediaReportUpsertDto.getSettings().size(); i++) {
            if (mediaReportSetting.isEmpty()) {
                newSettings.add(new MediaReportDisplay(
                        null,
                        i + 1,
                        masterMediaReportUpsertDto.getSettings().get(i),
                        parentInfo.getLeft(),
                        parentInfo.getRight()
                ));
            } else {
                newSettings.add(new MediaReportDisplay(
                        mediaReportSetting.get(i).getId(),
                        mediaReportSetting.get(i).getFlowTypeId(),
                        masterMediaReportUpsertDto.getSettings().get(i),
                        mediaReportSetting.get(i).getParentId(),
                        mediaReportSetting.get(i).getOemGroupId()
                ));
            }
        }

        if (!newSettings.isEmpty()) {
            mediaReportDisplayRepo.saveAllAndFlush(newSettings);
        }
    }

    @Transactional
    public void editMasterMedia(String id, MasterMediaUpsertDto masterMediaUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        
        MasterMedia media = getMasterMediaById(id, parentInfo.getLeft());
        
        // Only validate name uniqueness if the name has changed
        if (!media.getMediaName().equals(masterMediaUpsertDto.getName())) {
            validateMasterMediaNameUniqueness(
                masterMediaUpsertDto.getName(),
                media.getId(),
                parentInfo.getLeft(), 
                parentInfo.getRight()
            );
        }
        
        MASTER_MEDIA_MAPPER.updateEntity(masterMediaUpsertDto, media);
        masterMediaRepo.save(media);
    }

    @Transactional
    public void upsertMonthlyCost(String selectedDate, List<MasterMonthlyCostUpsertDto> masterMonthlyCostUpsertDto) {
        LocalDate parsedDate = validateYYYYMM(selectedDate);
        Account account = GeneralUtils.getCurrentUser();

        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        List<MonthlyCost> removedList = monthlyCostRepo.getRemovedMonthlyCostSetting(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                parsedDate,
                masterMonthlyCostUpsertDto.stream().map(MasterMonthlyCostUpsertDto::getId)
                        .filter(Objects::nonNull).collect(Collectors.toSet())
        );

        List<MasterMedia> masterMediaList = masterMediaRepo.findAllByIDs(
                masterMonthlyCostUpsertDto.stream().map(MasterMonthlyCostUpsertDto::getMediaId).toList(),
                parentInfo.getLeft(),
                parentInfo.getRight()
        );
        Map<String, MasterMedia> masterMediaMapById = masterMediaList.stream().collect(Collectors.toMap(MasterMedia::getId, m -> m));

        List<MonthlyCost> newSettings = masterMonthlyCostUpsertDto.stream().map(i -> {
            MonthlyCost monthlyCost = MONTHLY_COST_MAPPER.toMonthlyCost(i, parentInfo.getLeft(), parentInfo.getRight());
            if (!masterMediaMapById.containsKey(i.getMediaId())) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message(String.format(ErrorMessage.INVALID_DATA.getMessage(), TargetName.MASTER_MEDIA))
                        .fieldError("mediaId")
                        .build());
            }

            MasterMedia masterMedia = masterMediaMapById.get(i.getMediaId());
            monthlyCost.setMedia(masterMedia);
            monthlyCost.setStartMonth(parsedDate);
            return monthlyCost;
        }).toList();

        monthlyCostRepo.saveAllAndFlush(newSettings);
        monthlyCostRepo.deleteAll(removedList);
    }

    /**
     * Validates that the media name is unique within the same parent and client scope
     *
     * @param mediaName name of the media to check
     * @param parentId parent ID (operator_client_account.id or oem_client_account.id)
     * @param oemGroupId client ID (null for operator, not null for OEM)
     */
    private void validateMasterMediaNameUniqueness(String mediaName, String mediaId, String parentId, String oemGroupId) {
        Optional<MasterMedia> existingMedia = masterMediaRepo.findByMediaName(mediaName, parentId, oemGroupId);
        if (existingMedia.isEmpty()) {
            return;
        }

        if (mediaId != null && existingMedia.get().getId().equals(mediaId)) {
            return;
        }


        throw new BadValidationException(ErrorResponse.builder()
                .statusCode(ErrorMessage.DUPLICATE_DATA.getStatusCode())
                .message(String.format(
                        ErrorMessage.DUPLICATE_DATA.getMessage(),
                        TargetName.MASTER_MEDIA.getTargetName(),
                        mediaName))
                .fieldError("name")
                .build());
    }

    private void validateMasterMediaReportUpsertDto(MasterMediaReportUpsertDto masterMediaReportUpsertDto) {
        if (masterMediaReportUpsertDto.getSettings() == null) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message(ErrorMessage.INVALID_DATA.getMessage())
                    .build());
        }

        if (masterMediaReportUpsertDto.getSettings().size() != 4) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message(String.format(ErrorMessage.INVALID_DATA
                                    .getMessage(),
                            masterMediaReportUpsertDto.getSettings().size()))
                    .build());
        }
    }

    private LocalDate validateYYYYMM(String yearMonth) {
        String append = yearMonth + "/01";
        try {
            return LocalDate.parse(append, Constants.dateFormatter);
        } catch (Exception e) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                            "selectedDate",
                            yearMonth))
                    .build());
        }
    }
}
