package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.entities.OperatorAccount;
import jp.co.goalist.gsc.entities.OperatorTeam;
import jp.co.goalist.gsc.gen.dtos.StaffOperatorAccountDetailsDto;
import jp.co.goalist.gsc.gen.dtos.StaffOperatorAccountItemsDto;
import jp.co.goalist.gsc.gen.dtos.StaffOperatorAccountUpserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface StaffMapper extends BaseMapper {

    StaffMapper STAFF_MAPPER = Mappers.getMapper(StaffMapper.class);

    OperatorAccount toStaffAccount(StaffOperatorAccountUpserDto staffAccountUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateToStaffOemAccount(StaffOperatorAccountUpserDto form, @MappingTarget OperatorAccount operatorAccount);

    @Mapping(target = "email", expression = "java(operatorAccount.getAccount().getEmail())")
    @Mapping(target = "teamIds", expression = "java(getTeamIds(operatorAccount))")
    StaffOperatorAccountDetailsDto toOperatorAccountDetailsDto(OperatorAccount operatorAccount);

    @Mapping(target = "email", expression = "java(operatorAccount.getAccount().getEmail())")
    @Mapping(target = "teams", expression = "java(getOperatorTeams(operatorAccount))")
    @Mapping(target = "isExpired", expression = "java(checkIsExpired(operatorAccount))")
    StaffOperatorAccountItemsDto toOperatorAccountItemsDto(OperatorAccount operatorAccount);

    default boolean checkIsExpired(OperatorAccount operatorAccount) {
        return Objects.nonNull(operatorAccount.getAccount().getResetTokenString()) &&
                Objects.nonNull(operatorAccount.getAccount().getTokenExpirationDate()) &&
                operatorAccount.getAccount().getTokenExpirationDate().isBefore(LocalDateTime.now());
    }

    default List<String> getTeamIds(OperatorAccount operatorAccount) {
        return Objects.nonNull(operatorAccount.getTeams()) ?
                operatorAccount.getTeams().stream().map(OperatorTeam::getId).collect(Collectors.toList()) : null;
    }
}
