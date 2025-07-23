package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.entities.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface BaseMapper {

    default String getPrefecture(Prefecture prefecture) {
        return Objects.nonNull(prefecture) ? prefecture.getId().toString() : null;
    }

    default String getCity(City city) {
        return Objects.nonNull(city) ? city.getId().toString() : null;
    }

    default String getAddress(String postCode, Prefecture prefecture, City city) {
        StringBuilder address = new StringBuilder();

        if (Objects.nonNull(postCode) && !postCode.isBlank()) {
            address.append("〒").append(postCode, 0, 3).append("-").append(postCode.substring(3)).append(" ");
        }

        if (Objects.nonNull(prefecture)) {
            address.append(prefecture.getName());
        }

        if (Objects.nonNull(city)) {
            address.append(city.getName());
        }

        return address.toString();
    }

    default List<String> getOemTeams(OemAccount oemAccount) {
        return Objects.isNull(oemAccount.getTeams()) ? null :
                oemAccount.getTeams().stream().map(OemTeam::getName).collect(Collectors.toList());
    }

    default List<String> getOperatorTeams(OperatorAccount operatorAccount) {
        return Objects.isNull(operatorAccount.getTeams()) ? null :
                operatorAccount.getTeams().stream().map(OperatorTeam::getName).collect(Collectors.toList());
    }
}
