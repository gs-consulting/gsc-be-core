package jp.co.goalist.gsc.services;

import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.repositories.CityRepository;
import jp.co.goalist.gsc.repositories.PrefectureRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrefectureService {

    private final CityRepository cityRepository;
    private final PrefectureRepository prefectureRepository;

    public City getExistingCityById(Integer id) {
        Optional<City> optionalOne = cityRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.CITY.getTargetName()))
                    .fieldError("city")
                    .build());
        }

        return optionalOne.get();
    }

    public Prefecture getExistingPrefectureById(Integer id) {
        Optional<Prefecture> optionalOne = prefectureRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.PREFECTURE.getTargetName()))
                    .fieldError("prefecture")
                    .build());
        }

        return optionalOne.get();
    }

    public Pair<Map<String, Prefecture>, Map<String, City>> getPrefecturesAndCitiesForCSV(List<String> prefectures,
                                                                                       List<String> cities) {

        List<Prefecture> prefectureList = prefectureRepository.getPrefecturesBy(prefectures);
        List<City> cityList = cityRepository.getCitiesBy(cities);

        return Pair.of(prefectureList.stream().collect(Collectors.toMap(i -> i.getId().toString(), f -> f)),
                cityList.stream().collect(Collectors.toMap(i -> i.getId().toString(), c -> c)));
    }
}
