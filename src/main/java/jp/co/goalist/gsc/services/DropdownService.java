package jp.co.goalist.gsc.services;

import com.fasterxml.jackson.databind.json.JsonMapper;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.common.Prefecture;
import jp.co.goalist.gsc.dtos.ZipCode;
import jp.co.goalist.gsc.dtos.ZipCodeResponse;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.StatusType;
import jp.co.goalist.gsc.enums.SubRole;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.gen.dtos.MDropdownListDto;
import jp.co.goalist.gsc.gen.dtos.MPostCodeDto;
import jp.co.goalist.gsc.gen.dtos.MTPrefectureItemsDto;
import jp.co.goalist.gsc.gen.dtos.MediaDropdownListDto;
import jp.co.goalist.gsc.repositories.*;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static jp.co.goalist.gsc.common.Constants.ZIPCODE_GET_FIXED_URL;
import static jp.co.goalist.gsc.mappers.DropdownMapper.DROPDOWN_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class DropdownService {

    private final PrefectureRepository prefectureRepo;
    private final MasterStatusRepository masterStatusRepo;
    private final MasterMediaRepository masterMediaRepo;
    private final OemProjectRepository oemProjectRepo;
    private final OperatorProjectRepository operatorProjectRepo;
    private final UtilService utilService;

    public List<MTPrefectureItemsDto> getMasterPrefectures() {
        return prefectureRepo.getPrefecturesOrderById().stream()
                .map(DROPDOWN_MAPPER::mapToPrefectureDTO).collect(Collectors.toList());
    }

    public MPostCodeDto getAddressByPostCode(String postCode) {
        try {
            MPostCodeDto address = new MPostCodeDto();
            ZipCode zipCode = getAddressByPostCodeFromExternalAPI(postCode);
            log.info("getAddressByPostCode: {}", zipCode);

            String prefectureId = Prefecture.prefectures.get(zipCode.getAddress1());
            String cityId = Prefecture.cities.get(zipCode.getAddress2());

            address.setAddress1(Objects.requireNonNullElse(prefectureId, ""));
            address.setAddress2(Objects.requireNonNullElse(cityId, ""));
            address.setAddressName2(zipCode.getAddress2());
            return address;
        } catch (Exception e) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.POST_CODE_INVALID.getStatusCode())
                    .message(ErrorMessage.POST_CODE_INVALID.getMessage())
                    .build());
        }
    }

    private ZipCode getAddressByPostCodeFromExternalAPI(String postCode) {
        if (Objects.isNull(postCode)) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.REQUIRED_FIELD.getStatusCode())
                    .message(String.format(ErrorMessage.REQUIRED_FIELD.getMessage(),
                            "postCode"))
                    .fieldError("postCode")
                    .build());
        }

        String path = ZIPCODE_GET_FIXED_URL.replace("${zipcode}", postCode);
        try {
            URL url = URI.create(path).toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            JsonMapper mapper = new JsonMapper();
            ZipCodeResponse item;
            item = mapper.readValue(content.toString(), ZipCodeResponse.class);
            if (item.getResults() == null || item.getResults().isEmpty()) {
                throw new NotFoundException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.POST_CODE_INVALID.getStatusCode())
                        .message(ErrorMessage.POST_CODE_INVALID.getMessage())
                        .build());
            }

            return item.getResults().get(0);
        } catch (Exception e) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.POST_CODE_INVALID.getStatusCode())
                    .message(ErrorMessage.POST_CODE_INVALID.getMessage())
                    .build());
        }
    }

    @Transactional
    public MDropdownListDto getClientStatusDropdown(String statusId) {
        StatusType statusType = StatusType.fromId(statusId);
        Account account = GeneralUtils.getCurrentUser();
        String parentId = utilService.getParentIdAndGroupId(account).getLeft();
        Pageable pageable = Pageable.unpaged(Sort.by("order"));

        Page<MasterStatus> masterStatuses = masterStatusRepo.findByParentIdAndType(
                parentId,
                statusType.getId(),
                pageable);
        return MDropdownListDto.builder()
                .page(masterStatuses.getNumber() + 1)
                .limit(masterStatuses.getSize())
                .total(masterStatuses.getTotalElements())
                .items(masterStatuses.getContent().stream()
                        .map(DROPDOWN_MAPPER::toMDropdownItemsDto)
                        .toList())
                .build();
    }

    @Transactional
    public MediaDropdownListDto getClientMediaDropdown(String projectId, String applicantId) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                List<MasterMedia> masterMedias = masterMediaRepo.findAllOperatorForDropdown(
                        parentInfo.getLeft(),
                        projectId,
                        applicantId
                );
                return MediaDropdownListDto.builder()
                        .items(masterMedias.stream()
                                .map(DROPDOWN_MAPPER::toMDropdownItemsDto)
                                .toList())
                        .build();
            }
            case SubRole.OEM -> {
                List<MasterMedia> masterMedias = masterMediaRepo.findAllOemForDropdown(
                        parentInfo.getLeft(),
                        parentInfo.getRight(),
                        projectId,
                        applicantId
                );
                return MediaDropdownListDto.builder()
                        .items(masterMedias.stream()
                                .map(DROPDOWN_MAPPER::toMDropdownItemsDto)
                                .toList())
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public MDropdownListDto getClientProjectsDropdown() {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                List<OperatorProject> projects = operatorProjectRepo.findAllProjectsByParentId(
                        parentInfo.getLeft()
                );
                return MDropdownListDto.builder()
                        .items(projects.stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                        .build();
            }
            case SubRole.OEM -> {
                List<OemProject> projects = oemProjectRepo.findAllProjectsByParentIdoemGroupId(
                        parentInfo.getLeft(),
                        parentInfo.getRight()
                );
                return MDropdownListDto.builder()
                        .items(projects.stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }
}
