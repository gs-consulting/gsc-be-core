package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.entities.OemBranch;
import jp.co.goalist.gsc.entities.OemStore;
import jp.co.goalist.gsc.entities.OperatorBranch;
import jp.co.goalist.gsc.entities.OperatorStore;
import jp.co.goalist.gsc.gen.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BranchMapper extends BaseMapper {

    BranchMapper BRANCH_MAPPER = Mappers.getMapper(BranchMapper.class);

    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "branchName", source = "name")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    OperatorBranch mapToOperatorBranch(ClientBranchUpsertDto branchUpsertDto);

    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "branchName", source = "name")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    OemBranch mapToOemBranch(ClientBranchUpsertDto branchUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "branchName", source = "name")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateOperatorBranch(@MappingTarget OperatorBranch operatorBranch, ClientBranchUpsertDto branchUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "branchName", source = "name")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateOemBranch(@MappingTarget OemBranch oemBranch, ClientBranchUpsertDto branchUpsertDto);

    @Mapping(target = "name", source = "branchName")
    @Mapping(target = "prefecture", expression = "java(getPrefecture(operatorBranch.getPrefecture()))")
    @Mapping(target = "city", expression = "java(getCity(operatorBranch.getCity()))")
    ClientBranchDetailsDto toClientOperatorBranchDetails(OperatorBranch operatorBranch);

    @Mapping(target = "name", source = "branchName")
    @Mapping(target = "prefecture", expression = "java(getPrefecture(oemBranch.getPrefecture()))")
    @Mapping(target = "city", expression = "java(getCity(oemBranch.getCity()))")
    ClientBranchDetailsDto toClientOemBranchDetails(OemBranch oemBranch);

    @Mapping(target = "name", source = "branchName")
    @Mapping(target = "address", expression = "java(getAddress(operatorBranch.getPostCode(), operatorBranch.getPrefecture(), operatorBranch.getCity()))")
    @Mapping(target = "storeNames", expression = "java(getOperatorStores(operatorBranch))")
    ClientBranchItemsDto toClientBranchItemsDto(OperatorBranch operatorBranch);

    @Mapping(target = "name", source = "branchName")
    @Mapping(target = "address", expression = "java(getAddress(oemBranch.getPostCode(), oemBranch.getPrefecture(), oemBranch.getCity()))")
    @Mapping(target = "storeNames", expression = "java(getOemStores(oemBranch))")
    ClientBranchItemsDto toClientBranchItemsDto(OemBranch oemBranch);

    default List<String> getOperatorStores(OperatorBranch operatorBranch) {
        return operatorBranch.getStores().isEmpty() ? null :
                operatorBranch.getStores().stream().map(OperatorStore::getStoreName).collect(Collectors.toList());
    }

    default List<String> getOemStores(OemBranch oemBranch) {
        return oemBranch.getStores().isEmpty() ? null :
                oemBranch.getStores().stream().map(OemStore::getStoreName).collect(Collectors.toList());
    }
}
