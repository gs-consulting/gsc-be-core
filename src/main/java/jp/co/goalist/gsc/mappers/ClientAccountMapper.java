package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.dtos.OemClientAccountItemsDto;
import jp.co.goalist.gsc.entities.OemClientAccount;
import jp.co.goalist.gsc.entities.OperatorClientAccount;
import jp.co.goalist.gsc.gen.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ClientAccountMapper extends BaseMapper {

    ClientAccountMapper CLIENT_ACCOUNT_MAPPER = Mappers.getMapper(ClientAccountMapper.class);

    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "managers", ignore = true)
    @Mapping(target = "oemGroupId", ignore = true)
    @Mapping(target = "employmentType", ignore = true)
    @Mapping(target = "isInterviewer", ignore = true)
    @Mapping(target = "isIniEducationStaff", ignore = true)
    @Mapping(target = "isDomainEnabled", expression = "java(checkDomainSetting(clientAccountUpsertDto.getDomainSetting()))")
    OemClientAccount toOemClientAccount(ClientAccountUpsertDto clientAccountUpsertDto);

    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "managers", ignore = true)
    @Mapping(target = "employmentType", ignore = true)
    @Mapping(target = "isInterviewer", ignore = true)
    @Mapping(target = "isIniEducationStaff", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDomainEnabled", expression = "java(checkDomainSetting(clientAccountUpsertDto.getDomainSetting()))")
    OperatorClientAccount toOperatorClientAccount(ClientAccountUpsertDto clientAccountUpsertDto);

    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "managers", ignore = true)
    @Mapping(target = "employmentType", ignore = true)
    @Mapping(target = "clientName", ignore = true)
    @Mapping(target = "furiganaName", ignore = true)
    OemClientAccount toOemClientAccount(ClientUserAccountUpsertDto clientAccountUpsertDto);

    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "managers", ignore = true)
    @Mapping(target = "employmentType", ignore = true)
    @Mapping(target = "clientName", ignore = true)
    @Mapping(target = "furiganaName", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    OperatorClientAccount toOperatorClientAccount(ClientUserAccountUpsertDto clientAccountUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "managers", ignore = true)
    @Mapping(target = "oemGroupId", ignore = true)
    @Mapping(target = "employmentType", ignore = true)
    @Mapping(target = "isInterviewer", ignore = true)
    @Mapping(target = "isIniEducationStaff", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "isDomainEnabled", expression = "java(checkDomainSetting(clientAccountUpsertDto.getDomainSetting()))")
    void updateOemClientAccount(@MappingTarget OemClientAccount oemClientAccount,
                                ClientAccountUpsertDto clientAccountUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "managers", ignore = true)
    @Mapping(target = "employmentType", ignore = true)
    @Mapping(target = "isInterviewer", ignore = true)
    @Mapping(target = "isIniEducationStaff", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDomainEnabled", expression = "java(checkDomainSetting(clientAccountUpsertDto.getDomainSetting()))")
    void updateOperatorClientAccount(@MappingTarget OperatorClientAccount operatorClientAccount,
                                     ClientAccountUpsertDto clientAccountUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "managers", ignore = true)
    @Mapping(target = "employmentType", ignore = true)
    @Mapping(target = "isInterviewer", ignore = true)
    @Mapping(target = "isIniEducationStaff", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "oemGroupId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDomainEnabled", expression = "java(checkDomainSetting(clientAccountUpsertDto.getDomainSetting()))")
    void updateOperatorClientAccountForOem(@MappingTarget OperatorClientAccount operatorClientAccount,
                                     ClientAccountUpsertDto clientAccountUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "managers", ignore = true)
    @Mapping(target = "employmentType", ignore = true)
    @Mapping(target = "isIniEducationStaff", source = "isEducationOfficer")
    @Mapping(target = "clientName", source = "fullName")
    void updateOemUserClientAccount(@MappingTarget OemClientAccount oemClientAccount,
                                    ClientUserAccountUpsertDto clientAccountUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "managers", ignore = true)
    @Mapping(target = "employmentType", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isIniEducationStaff", source = "isEducationOfficer")
    @Mapping(target = "clientName", source = "fullName")
    void updateOperatorUserClientAccount(@MappingTarget OperatorClientAccount operatorClientAccount,
                                         ClientUserAccountUpsertDto clientAccountUpsertDto);

    @Mapping(target = "prefecture", expression = "java(getPrefecture(operatorClientAccount.getPrefecture()))")
    @Mapping(target = "city", expression = "java(getCity(operatorClientAccount.getCity()))")
    @Mapping(target = "managerIds", expression = "java(getManagerIds(operatorClientAccount))")
    @Mapping(target = "branchIds", expression = "java(getBranchIds(operatorClientAccount))")
    @Mapping(target = "storeIds", expression = "java(getStoreIds(operatorClientAccount))")
    @Mapping(target = "isCreatedByOperator", constant = "true")
    ClientAccountDetailsDto toOperatorClientAccountDetailsDto(OperatorClientAccount operatorClientAccount);

    @Mapping(target = "prefecture", expression = "java(getPrefecture(oemClientAccount.getPrefecture()))")
    @Mapping(target = "city", expression = "java(getCity(oemClientAccount.getCity()))")
    @Mapping(target = "managerIds", expression = "java(getManagerIds(oemClientAccount))")
    @Mapping(target = "branchIds", expression = "java(getBranchIds(oemClientAccount))")
    @Mapping(target = "storeIds", expression = "java(getStoreIds(oemClientAccount))")
    ClientAccountDetailsDto toOemClientAccountDetailsDto(OemClientAccount oemClientAccount);

    @Mapping(target = "branchIds", expression = "java(getBranchIds(operatorClientAccount))")
    @Mapping(target = "storeIds", expression = "java(getStoreIds(operatorClientAccount))")
    @Mapping(target = "email", expression = "java(operatorClientAccount.getAccount().getEmail())")
    @Mapping(target = "isEducationOfficer", source = "isIniEducationStaff")
    @Mapping(target = "fullName", source = "clientName")
    @Mapping(target = "isRestricted", ignore = true)
    ClientUserAccountDetailsDto toOperatorClientUserAccountDetailsDto(OperatorClientAccount operatorClientAccount);

    @Mapping(target = "branchIds", expression = "java(getBranchIds(oemClientAccount))")
    @Mapping(target = "storeIds", expression = "java(getStoreIds(oemClientAccount))")
    @Mapping(target = "email", expression = "java(oemClientAccount.getAccount().getEmail())")
    @Mapping(target = "isEducationOfficer", source = "isIniEducationStaff")
    @Mapping(target = "fullName", source = "clientName")
    @Mapping(target = "isRestricted", ignore = true)
    ClientUserAccountDetailsDto toOemClientUserAccountDetailsDto(OemClientAccount oemClientAccount);

    @Mapping(target = "isExpired", expression = "java(checkIsExpired(operatorClientAccount))")
    @Mapping(target = "isMember", expression = "java(operatorClientAccount.getAccount().isEnabled())")
    @Mapping(target = "managers", expression = "java(getManagers(operatorClientAccount))")
    @Mapping(target = "branches", expression = "java(getBranches(operatorClientAccount))")
    @Mapping(target = "stores", expression = "java(getStores(operatorClientAccount))")
    @Mapping(target = "totalApplications", source = "total")
    @Mapping(target = "totalNotSupportedApplications", source = "notSupported")
    ClientAccountItemsDto toOperatorClientAccountItemsDto(OperatorClientAccount operatorClientAccount, long total, long notSupported);

    @Mapping(target = "isExpired", expression = "java(checkIsExpiredForOemRole(oemClientAccountItem))")
    @Mapping(target = "managers", expression = "java(getManagersForOemRole(oemClientAccountItem))")
    @Mapping(target = "branches", expression = "java(getBranchesForOemRole(oemClientAccountItem))")
    @Mapping(target = "stores", expression = "java(getStoresForOem(oemClientAccountItem))")
    @Mapping(target = "totalApplications", source = "total")
    @Mapping(target = "totalNotSupportedApplications", source = "notSupported")
    ClientAccountItemsDto toOemClientAccountItemsDto(OemClientAccountItemsDto oemClientAccountItem, long total, long notSupported);

    @Mapping(target = "email", expression = "java(operatorClientAccount.getAccount().getEmail())")
    @Mapping(target = "isExpired", expression = "java(checkIsExpired(operatorClientAccount))")
    @Mapping(target = "branches", expression = "java(getBranches(operatorClientAccount))")
    @Mapping(target = "stores", expression = "java(getStores(operatorClientAccount))")
    @Mapping(target = "fullName", source = "operatorClientAccount.clientName")
    @Mapping(target = "totalApplications", source = "total")
    @Mapping(target = "totalNotSupportedApplications", source = "notSupported")
    ClientUserAccountItemsDto toOperatorClientUserAccountItemsDto(OperatorClientAccount operatorClientAccount, long total, long notSupported);

    @Mapping(target = "email", expression = "java(oemClientAccount.getAccount().getEmail())")
    @Mapping(target = "isExpired", expression = "java(checkIsExpired(oemClientAccount))")
    @Mapping(target = "branches", expression = "java(getBranches(oemClientAccount))")
    @Mapping(target = "stores", expression = "java(getStores(oemClientAccount))")
    @Mapping(target = "fullName", source = "oemClientAccount.clientName")
    @Mapping(target = "totalApplications", source = "total")
    @Mapping(target = "totalNotSupportedApplications", source = "notSupported")
    ClientUserAccountItemsDto toOemClientUserAccountItemsDto(OemClientAccount oemClientAccount, long total, long notSupported);

    @Mapping(target = "name", source = "clientName")
    MDropdownItemsDto toMDropdownItemsDto(OperatorClientAccount account);

    @Mapping(target = "name", source = "clientName")
    MDropdownItemsDto toMDropdownItemsDto(OemClientAccount account);

    default boolean checkIsExpired(OperatorClientAccount operatorClientAccount) {
        return Objects.nonNull(operatorClientAccount.getAccount().getResetTokenString()) &&
                Objects.nonNull(operatorClientAccount.getAccount().getTokenExpirationDate()) &&
                operatorClientAccount.getAccount().getTokenExpirationDate().isBefore(LocalDateTime.now());
    }

    default boolean checkIsExpired(OemClientAccount oemClientAccount) {
        return Objects.nonNull(oemClientAccount.getAccount().getResetTokenString()) &&
                Objects.nonNull(oemClientAccount.getAccount().getTokenExpirationDate()) &&
                oemClientAccount.getAccount().getTokenExpirationDate().isBefore(LocalDateTime.now());
    }

    default List<String> getManagerIds(OperatorClientAccount operatorClientAccount) {
        return Objects.isNull(operatorClientAccount) ? null :
                operatorClientAccount.getManagers().stream().map(OperatorClientAccount::getId).collect(Collectors.toList());
    }

    default List<String> getManagerIds(OemClientAccount oemClientAccount) {
        return Objects.isNull(oemClientAccount) ? null :
                oemClientAccount.getManagers().stream().map(OemClientAccount::getId).collect(Collectors.toList());
    }

    default List<String> getManagers(OperatorClientAccount operatorClientAccount) {
        return Objects.isNull(operatorClientAccount) ? null :
                operatorClientAccount.getManagers().stream().map(OperatorClientAccount::getClientName).collect(Collectors.toList());
    }

    default boolean checkIsExpiredForOemRole(OemClientAccountItemsDto oemClientAccountItem) {
        return Objects.nonNull(oemClientAccountItem.getResetTokenString()) &&
                Objects.nonNull(oemClientAccountItem.getTokenExpirationDate()) &&
                oemClientAccountItem.getTokenExpirationDate().before(Timestamp.valueOf(LocalDateTime.now()));
    }

    default List<String> getManagersForOemRole(OemClientAccountItemsDto oemClientAccountItem) {
        return Objects.isNull(oemClientAccountItem.getManagerNames()) ? null :
                List.of(oemClientAccountItem.getManagerNames().split(","));
    }

    default List<String> getBranchesForOemRole(OemClientAccountItemsDto oemClientAccountItem) {
        return Objects.isNull(oemClientAccountItem.getBranchNames()) ? null :
                List.of(oemClientAccountItem.getBranchNames().split(","));
    }

    default List<String> getStoresForOem(OemClientAccountItemsDto oemClientAccount) {
        return Objects.isNull(oemClientAccount.getStoreNames()) ? null :
                List.of(oemClientAccount.getStoreNames().split(","));
    }

    default List<String> getBranches(OperatorClientAccount operatorClientAccount) {
        return Objects.isNull(operatorClientAccount) ||
                operatorClientAccount.getLocations().stream().noneMatch(i -> Objects.nonNull(i.getOperatorBranch())) ? null :
                operatorClientAccount.getLocations().stream()
                        .filter(i -> Objects.nonNull(i.getOperatorBranch()))
                        .map(i -> i.getOperatorBranch().getBranchName())
                        .distinct().collect(Collectors.toList());
    }

    default List<String> getBranches(OemClientAccount oemClientAccount) {
        return Objects.isNull(oemClientAccount) ||
                oemClientAccount.getLocations().stream().noneMatch(i -> Objects.nonNull(i.getOemBranch())) ? null :
                oemClientAccount.getLocations().stream()
                        .filter(i -> Objects.nonNull(i.getOemBranch()))
                        .map(i -> i.getOemBranch().getBranchName())
                        .distinct().collect(Collectors.toList());
    }

    default List<String> getStores(OperatorClientAccount operatorClientAccount) {
        return Objects.isNull(operatorClientAccount) ||
                operatorClientAccount.getLocations().stream().noneMatch(i -> Objects.nonNull(i.getOperatorStore())) ? null :
                operatorClientAccount.getLocations().stream()
                        .filter(i -> Objects.nonNull(i.getOperatorStore()))
                        .map(i -> i.getOperatorStore().getStoreName())
                        .distinct().collect(Collectors.toList());
    }

    default List<String> getStores(OemClientAccount oemClientAccount) {
        return Objects.isNull(oemClientAccount) ||
                oemClientAccount.getLocations().stream().noneMatch(i -> Objects.nonNull(i.getOemStore())) ? null :
                oemClientAccount.getLocations().stream()
                        .filter(i -> Objects.nonNull(i.getOemStore()))
                        .map(i -> i.getOemStore().getStoreName())
                        .distinct().collect(Collectors.toList());
    }

    default List<String> getBranchIds(OperatorClientAccount operatorClientAccount) {
        return Objects.isNull(operatorClientAccount) ||
                operatorClientAccount.getLocations().stream().noneMatch(i -> Objects.nonNull(i.getOperatorBranch())) ? null :
                operatorClientAccount.getLocations().stream()
                        .filter(i -> Objects.nonNull(i.getOperatorBranch()))
                        .map(i -> i.getOperatorBranch().getId())
                        .distinct().collect(Collectors.toList());
    }

    default List<String> getBranchIds(OemClientAccount oemClientAccount) {
        return Objects.isNull(oemClientAccount) ||
                oemClientAccount.getLocations().stream().noneMatch(i -> Objects.nonNull(i.getOemBranch())) ? null :
                oemClientAccount.getLocations().stream()
                        .filter(i -> Objects.nonNull(i.getOemBranch()))
                        .map(i -> i.getOemBranch().getId())
                        .distinct().collect(Collectors.toList());
    }

    default List<String> getStoreIds(OperatorClientAccount operatorClientAccount) {
        return Objects.isNull(operatorClientAccount) ||
                operatorClientAccount.getLocations().stream().noneMatch(i -> Objects.nonNull(i.getOperatorStore())) ? null :
                operatorClientAccount.getLocations().stream()
                        .filter(i -> Objects.nonNull(i.getOperatorStore()))
                        .map(i -> i.getOperatorStore().getId())
                        .distinct().collect(Collectors.toList());
    }

    default List<String> getStoreIds(OemClientAccount oemClientAccount) {
        return Objects.isNull(oemClientAccount) ||
                oemClientAccount.getLocations().stream().noneMatch(i -> Objects.nonNull(i.getOemStore())) ? null :
                oemClientAccount.getLocations().stream()
                        .filter(i -> Objects.nonNull(i.getOemStore()))
                        .map(i -> i.getOemStore().getId())
                        .distinct().collect(Collectors.toList());
    }

    default boolean checkDomainSetting(String domainSetting) {
        return Objects.nonNull(domainSetting);
    }
}
