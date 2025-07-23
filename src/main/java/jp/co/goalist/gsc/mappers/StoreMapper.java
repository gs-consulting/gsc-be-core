package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.entities.OemStore;
import jp.co.goalist.gsc.entities.OperatorStore;
import jp.co.goalist.gsc.gen.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface StoreMapper extends BaseMapper {

    StoreMapper STORE_MAPPER = Mappers.getMapper(StoreMapper.class);

    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "storeName", source = "name")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    OperatorStore mapToOperatorStore(ClientStoreUpsertDto storeUpsertDto);

    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "storeName", source = "name")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    OemStore mapToOemStore(ClientStoreUpsertDto storeUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "storeName", source = "name")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateOemStore(@MappingTarget OemStore oemStore, ClientStoreUpsertDto StoreUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "storeName", source = "name")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateOperatorStore(@MappingTarget OperatorStore operatorStore, ClientStoreUpsertDto StoreUpsertDto);

    @Mapping(target = "name", source = "storeName")
    @Mapping(target = "branchId", expression = "java(oemStore.getBranch() != null ? oemStore.getBranch().getId() : null)")
    ClientStoreDetailsDto toClientStoreDetailsDto(OemStore oemStore);

    @Mapping(target = "name", source = "storeName")
    @Mapping(target = "branchId", expression = "java(operatorStore.getBranch() != null ? operatorStore.getBranch().getId() : null)")
    ClientStoreDetailsDto toClientStoreDetailsDto(OperatorStore operatorStore);

    @Mapping(target = "name", source = "storeName")
    @Mapping(target = "branch", expression = "java(getOemBranchName(oemStore))")
    @Mapping(target = "address", expression = "java(getAddress(oemStore.getPostCode(), oemStore.getPrefecture(), oemStore.getCity()))")
    ClientStoreItemsDto toClientStoreItemsDto(OemStore oemStore);

    @Mapping(target = "name", source = "storeName")
    @Mapping(target = "branch", expression = "java(getOperatorBranchName(operatorStore))")
    @Mapping(target = "address", expression = "java(getAddress(operatorStore.getPostCode(), operatorStore.getPrefecture(), operatorStore.getCity()))")
    ClientStoreItemsDto toClientStoreItemsDto(OperatorStore operatorStore);

    default String getOemBranchName(OemStore oemStore) {
        return Objects.isNull(oemStore.getBranch()) ? null :
                oemStore.getBranch().getBranchName();
    }

    default String getOperatorBranchName(OperatorStore operatorStore) {
        return Objects.isNull(operatorStore.getBranch()) ? null :
                operatorStore.getBranch().getBranchName();
    }
}
