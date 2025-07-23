package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.entities.Blacklist;
import jp.co.goalist.gsc.gen.dtos.BlacklistCreateDto;
import jp.co.goalist.gsc.gen.dtos.BlacklistItemsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BlacklistMapper extends BaseMapper {

    BlacklistMapper BLACKLIST_MAPPER = Mappers.getMapper(BlacklistMapper.class);

    @Mapping(target = "isRestricted", constant = "false")
    @Mapping(target = "birthday", source = "birthday", dateFormat = Constants.DATE_FORMAT)
    @Mapping(target = "createdDateTime", source = "createdAt", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    BlacklistItemsDto toBlacklistItemDto(Blacklist blacklist);

    Blacklist toBlacklist(BlacklistCreateDto blacklistCreateDto);
}
