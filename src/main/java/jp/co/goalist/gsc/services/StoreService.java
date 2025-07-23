package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.Role;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.OemStoreRepository;
import jp.co.goalist.gsc.repositories.OperatorStoreRepository;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.*;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static jp.co.goalist.gsc.mappers.DropdownMapper.DROPDOWN_MAPPER;
import static jp.co.goalist.gsc.mappers.StoreMapper.STORE_MAPPER;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final OemStoreRepository oemStoreRepository;
    private final OperatorStoreRepository operatorStoreRepository;
    private final OemService oemService;
    private final UtilService utilService;

    public List<OemStore> getOemStoresByIdsAndBranchIds(List<String> ids, List<String> branchIds) {
        return oemStoreRepository.getOemStoresByIdsAndBranchIds(ids, branchIds);
    }

    public List<OperatorStore> getOperatorStoresByIdsAndBranchIds(List<String> ids, List<String> branchIds) {
        return operatorStoreRepository.getOperatorStoresByIdsAndBranchIds(ids, branchIds);
    }

    @Transactional
    public void createClientStore(ClientStoreUpsertDto storeUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        Triple<String, String, String> parentInfo = utilService.getParentIdAndOemIdAndGroupId(account);

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                OperatorStore newOperatorStore = STORE_MAPPER.mapToOperatorStore(storeUpsertDto);
                newOperatorStore.setParentId(parentInfo.getLeft());
                updateOperatorRelationships(
                        storeUpsertDto.getPrefecture(),
                        storeUpsertDto.getCity(),
                        storeUpsertDto.getBranchId(),
                        newOperatorStore
                );
                operatorStoreRepository.saveAndFlush(newOperatorStore);
            }
            case Role.OEM -> {
                OemStore newOemStore = STORE_MAPPER.mapToOemStore(storeUpsertDto);
                newOemStore.setOemGroupId(parentInfo.getRight());
                newOemStore.setOemParentId(parentInfo.getMiddle());
                newOemStore.setParentId(parentInfo.getLeft());
                updateOemRelationships(
                        storeUpsertDto.getPrefecture(),
                        storeUpsertDto.getCity(),
                        storeUpsertDto.getBranchId(),
                        newOemStore
                );
                oemStoreRepository.saveAndFlush(newOemStore);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void editClientStore(String id, ClientStoreUpsertDto storeUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                OperatorStore operatorStore = utilService.getExistingOperatorStoreById(id);
                STORE_MAPPER.updateOperatorStore(operatorStore, storeUpsertDto);
                updateOperatorRelationships(
                        storeUpsertDto.getPrefecture(),
                        storeUpsertDto.getCity(),
                        storeUpsertDto.getBranchId(),
                        operatorStore
                );
                operatorStoreRepository.saveAndFlush(operatorStore);
            }
            case Role.OEM -> {
                OemStore oemStore = utilService.getExistingOemStoreById(id);
                STORE_MAPPER.updateOemStore(oemStore, storeUpsertDto);
                updateOemRelationships(
                        storeUpsertDto.getPrefecture(),
                        storeUpsertDto.getCity(),
                        storeUpsertDto.getBranchId(),
                        oemStore
                );
                oemStoreRepository.saveAndFlush(oemStore);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public ClientStoreDetailsDto getClientStoreDetails(String id) {
        Account account = GeneralUtils.getCurrentUser();

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                OperatorStore operatorStore = utilService.getExistingOperatorStoreById(id);
                return STORE_MAPPER.toClientStoreDetailsDto(operatorStore);
            }
            case Role.OEM -> {
                OemStore oemStore = utilService.getExistingOemStoreById(id);
                return STORE_MAPPER.toClientStoreDetailsDto(oemStore);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public ClientStoreListDto getClientStores(String branchId, String searchInput, Integer pageNumber, Integer pageSize) {
        Account account = GeneralUtils.getCurrentUser();

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                OperatorClientAccount parent = utilService.getOperatorParent(account);
                Page<OperatorStore> stores = operatorStoreRepository.findOperatorStoresBy(
                        parent.getId(),
                        branchId,
                        GeneralUtils.wrapToLike(searchInput),
                        GeneralUtils.getPagination(pageNumber, pageSize)
                );

                return ClientStoreListDto.builder()
                        .total(stores.getTotalElements())
                        .page(stores.getNumber() + 1)
                        .limit(stores.getSize())
                        .items(stores.getContent().stream().map(STORE_MAPPER::toClientStoreItemsDto).collect(Collectors.toList()))
                        .build();
            }
            case Role.OEM -> {
                OemClientAccount parent = utilService.getOemParent(account);
                Page<OemStore> stores = oemStoreRepository.findOemClientStoresBy(
                        parent.getId(),
                        parent.getOemAccount().getId(),
                        parent.getOemGroupId(),
                        branchId,
                        GeneralUtils.wrapToLike(searchInput),
                        GeneralUtils.getPagination(pageNumber, pageSize)
                );

                return ClientStoreListDto.builder()
                        .total(stores.getTotalElements())
                        .page(stores.getNumber() + 1)
                        .limit(stores.getSize())
                        .items(stores.getContent().stream().map(STORE_MAPPER::toClientStoreItemsDto).collect(Collectors.toList()))
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public MDropdownListDto getOperatorStores(Integer pageNumber, Integer pageSize, List<String> branchIds, String clientAccountId, Integer isAll) {
        Page<OperatorStore> stores = operatorStoreRepository.findAllStoresDropdownByBranchIds(
                clientAccountId,
                isAll,
                branchIds,
                GeneralUtils.getPagination(pageNumber, pageSize)
        );

        return MDropdownListDto.builder()
                .total(stores.getTotalElements())
                .limit(stores.getSize())
                .page(stores.getNumber() + 1)
                .items(stores.getContent().stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    public MDropdownListDto getOemStores(Integer pageNumber, Integer pageSize, List<String> branchIds, String clientAccountId, Integer isAll) {
        Account account = GeneralUtils.getCurrentUser();
        OemAccount oemAccount = oemService.getExistingById(account.getId());

        Page<OemStore> stores = oemStoreRepository.findAllStoresDropdownByBranchId(
                Objects.nonNull(oemAccount.getParent()) ? oemAccount.getParent().getId() : oemAccount.getId(),
                clientAccountId,
                isAll,
                branchIds,
                GeneralUtils.getPagination(pageNumber, pageSize)
        );

        return MDropdownListDto.builder()
                .total(stores.getTotalElements())
                .limit(stores.getSize())
                .page(stores.getNumber() + 1)
                .items(stores.getContent().stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    public MDropdownListDto getClientStoresDropdown(List<String> branchIds) {
        Account account = GeneralUtils.getCurrentUser();

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                OperatorClientAccount operatorClientAccount = utilService.getExistingOperatorClientAccountById(account.getId());

                List<OperatorStore> stores = operatorStoreRepository.findClientStoresByBranchIds(
                        Objects.nonNull(operatorClientAccount.getParent()) ? operatorClientAccount.getParent().getId() : operatorClientAccount.getId(),
                        branchIds
                );

                return MDropdownListDto.builder()
                        .total((long) stores.size())
                        .limit(100)
                        .page(1)
                        .items(stores.stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                        .build();
            }
            case Role.OEM -> {
                OemClientAccount oemClientAccount = utilService.getExistingOemClientAccountById(account.getId());

                List<OemStore> stores = oemStoreRepository.findClientStoresDropdownByBranchIds(
                        utilService.getParentId(oemClientAccount),
                        oemClientAccount.getOemAccount().getId(),
                        branchIds
                );

                return MDropdownListDto.builder()
                        .total((long) stores.size())
                        .limit(100)
                        .page(1)
                        .items(stores.stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public Pair<Map<String, OperatorStore>, Map<String, OemStore>> getStoresForCSV(String parentId, String oemParentId, String oemGroupId, List<String> storeIds) {
        if (Objects.isNull(oemGroupId)) {
            List<OperatorStore> branches = operatorStoreRepository.findStoresBy(storeIds, parentId);
            return Pair.of(branches.stream().collect(Collectors.toMap(OperatorStore::getId, b -> b)), null);
        } else {
            List<OemStore> branches = oemStoreRepository.findStoresBy(storeIds, parentId, oemParentId, oemGroupId);
            return Pair.of(null, branches.stream().collect(Collectors.toMap(OemStore::getId, b -> b)));
        }
    }

    private void updateOperatorRelationships(String prefectureId, String cityId, String branchId, OperatorStore operatorStore) {
        Pair<Prefecture, City> prefectureCityPair = utilService.findPrefectureAndCity(
                prefectureId,
                cityId
        );

        operatorStore.setPrefecture(prefectureCityPair.getLeft());
        operatorStore.setCity(prefectureCityPair.getRight());

        if (Objects.isNull(branchId)) {
            operatorStore.setBranch(null);
        } else {
            OperatorBranch branch = utilService.getExistingOperatorBranchById(branchId);
            operatorStore.setBranch(branch);
        }
    }

    private void updateOemRelationships(String prefectureId, String cityId, String branchId, OemStore oemStore) {
        Pair<Prefecture, City> prefectureCityPair = utilService.findPrefectureAndCity(
                prefectureId,
                cityId
        );

        oemStore.setPrefecture(prefectureCityPair.getLeft());
        oemStore.setCity(prefectureCityPair.getRight());

        if (Objects.isNull(branchId)) {
            oemStore.setBranch(null);
        } else {
            OemBranch branch = utilService.getExistingOemBranchById(branchId);
            oemStore.setBranch(branch);
        }
    }

}
