package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.entities.OemAccount;
import jp.co.goalist.gsc.entities.OemTeam;
import jp.co.goalist.gsc.gen.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface OemAccountMapper extends BaseMapper {

    OemAccountMapper OEM_ACCOUNT_MAPPER = Mappers.getMapper(OemAccountMapper.class);

    OemAccount toOemAccount(OemAccountUpsertDto form);

    OemAccount toStaffOemAccount(StaffOemAccountUpsertDto form);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateToOemAccount(OemAccountUpsertDto form, @MappingTarget OemAccount oemAccount);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateToStaffOemAccount(StaffOemAccountUpsertDto form, @MappingTarget OemAccount oemAccount);

    @Mapping(target = "email", expression = "java(oemAccount.getAccount().getEmail())")
    @Mapping(target = "teamIds", expression = "java(getTeamIds(oemAccount))")
    OemAccountDetailsDto toOemAccountDetailsDto(OemAccount oemAccount);

    @Mapping(target = "email", expression = "java(oemAccount.getAccount().getEmail())")
    @Mapping(target = "teamIds", expression = "java(getTeamIds(oemAccount))")
    StaffOemAccountDetailsDto toStaffOemAccountDetailsDto(OemAccount oemAccount);

    @Mapping(target = "isExpired", expression = "java(checkIsExpired(oemAccount))")
    @Mapping(target = "teams", expression = "java(getOemTeams(oemAccount))")
    OemAccountItemsDto toOemAccountItemsDto(OemAccount oemAccount);

    @Mapping(target = "isExpired", expression = "java(checkIsExpired(oemAccount))")
    @Mapping(target = "teams", expression = "java(getOemTeams(oemAccount))")
    StaffOemAccountItemsDto toStaffOemAccountItemsDto(OemAccount oemAccount);

    default boolean checkIsExpired(OemAccount oemAccount) {
        return Objects.nonNull(oemAccount.getAccount().getResetTokenString()) &&
                Objects.nonNull(oemAccount.getAccount().getTokenExpirationDate()) &&
                oemAccount.getAccount().getTokenExpirationDate().isBefore(LocalDateTime.now());
    }

    default List<String> getTeamIds(OemAccount oemAccount) {
        return Objects.nonNull(oemAccount.getTeams()) ?
                oemAccount.getTeams().stream().map(OemTeam::getId).collect(Collectors.toList()) : null;
    }
}
