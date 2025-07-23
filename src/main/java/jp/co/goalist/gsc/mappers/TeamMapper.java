package jp.co.goalist.gsc.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import jp.co.goalist.gsc.entities.OemTeam;
import jp.co.goalist.gsc.entities.OperatorTeam;
import jp.co.goalist.gsc.gen.dtos.TeamDetailsDto;
import jp.co.goalist.gsc.gen.dtos.TeamItemsDto;
import jp.co.goalist.gsc.gen.dtos.TeamUpsertDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TeamMapper extends BaseMapper {

    TeamMapper TEAM_MAPPER = Mappers.getMapper(TeamMapper.class);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    OperatorTeam mapToOperatorTeam(TeamUpsertDto teamUpsertDto);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    OemTeam mapToOemTeam(TeamUpsertDto teamUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateOemTeam(@MappingTarget OemTeam oemTeam, TeamUpsertDto teamUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateOperatorTeam(@MappingTarget OperatorTeam operatorTeam, TeamUpsertDto teamUpsertDto);

    TeamDetailsDto toOemTeamDetailsDto(OemTeam oemTeam);

    TeamDetailsDto toOperatorTeamDetailsDto(OperatorTeam operatorTeam);

    @Mapping(target = "teamName", source = "name")
    TeamItemsDto toOemTeamItemsDto(OemTeam oemTeam);

    @Mapping(target = "teamName", source = "name")
    TeamItemsDto toOperatorTeamItemsDto(OperatorTeam operatorTeam);

}
