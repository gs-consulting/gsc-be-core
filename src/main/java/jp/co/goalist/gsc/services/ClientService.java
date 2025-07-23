package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.OemGroup;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.gen.dtos.OemGroupItemsDto;
import jp.co.goalist.gsc.gen.dtos.OemUpsertDto;
import jp.co.goalist.gsc.repositories.OemGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jp.co.goalist.gsc.mappers.OemGroupMapper.OEM_GROUP_MAPPER;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final OemGroupRepository oemGroupRepository;
    private final OemGroupItemsDto OEM_GROUP_DEFAULT = OemGroupItemsDto.builder()
            .id("-1")
            .oemName("ジーエスコンサルティング")
            .build();

    public OemGroup getExistingById(String id) {
        Optional<OemGroup> optionalOne = oemGroupRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.CLIENT.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    @Transactional
    public void createNewOemGroup(OemUpsertDto oemUpsertDto) {
        Optional<OemGroup> existingOemGroup = oemGroupRepository.findByOemGroupName(oemUpsertDto.getOemName());
        if (existingOemGroup.isPresent()) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.DUPLICATE_DATA.getStatusCode())
                    .message(String.format(ErrorMessage.DUPLICATE_DATA.getMessage(),
                            TargetName.CLIENT.getTargetName(),
                            oemUpsertDto.getOemName()))
                    .build());
        }

        OemGroup group = new OemGroup();
        group.setOemGroupName(oemUpsertDto.getOemName());
        oemGroupRepository.saveAndFlush(group);
    }

    @Transactional
    public void editNewOemGroup(String id, OemUpsertDto oemUpsertDto) {
        OemGroup oemGroup = getExistingById(id);

        Optional<OemGroup> existing = oemGroupRepository.findByOemGroupName(oemUpsertDto.getOemName());
        if (existing.isPresent() && !existing.get().getId().equals(oemGroup.getId())) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.DUPLICATE_DATA.getStatusCode())
                    .message(String.format(ErrorMessage.DUPLICATE_DATA.getMessage(),
                            TargetName.CLIENT.getTargetName(),
                            oemUpsertDto.getOemName()))
                    .build());
        }

        oemGroup.setOemGroupName(oemUpsertDto.getOemName());
        oemGroupRepository.save(oemGroup);
    }

    public List<OemGroupItemsDto> getOemGroups(Boolean isClientEdit) {
        List<OemGroup> oemGroups = oemGroupRepository.findAll();
        List<OemGroupItemsDto> oemGroupItems = new ArrayList<>();
        if (isClientEdit) {
            oemGroupItems.add(OEM_GROUP_DEFAULT);
        }

        if (!oemGroups.isEmpty()) {
            oemGroupItems.addAll(oemGroups.stream().map(OEM_GROUP_MAPPER::toOemGroupNameItemsDto).toList());
        }

        return oemGroupItems;
    }
}
