package jp.co.goalist.gsc.services;

import java.time.temporal.ChronoUnit;
import jp.co.goalist.gsc.common.*;
import jp.co.goalist.gsc.dtos.mediaReport.*;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.*;
import jp.co.goalist.gsc.exceptions.*;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.*;
import jp.co.goalist.gsc.services.criteriaBuilder.*;
import jp.co.goalist.gsc.utils.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.apache.commons.lang3.tuple.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaReportService {

    private final UtilService utilService;
    private final MediaReportCriteriaBuilder mediaReportCriteriaBuilder;
    private final MediaReportDisplayRepository mediaReportDisplayRepository;
    private final String OTHER_NAME = "その他";
    private final String OTHER_COLOR = "#000000";
    private final Integer MAX_ITEMS = 10;
    private final String ONE_WEEK = "1週間";
    private final String ONE_MONTH = "1ヶ月";
    private final String TWO_MONTH = "2ヶ月";
    private final String THREE_MONTH = "3ヶ月";
    private final String SIX_MONTH = "6ヶ月";
    private final List<String> FIXED_BAR_CHART_AFTER_JOIN_LABELS = Arrays.asList(ONE_WEEK, ONE_MONTH, TWO_MONTH, THREE_MONTH, SIX_MONTH);
    private final List<Integer> SELECTION_TYPES = List.of(FlowType.APPLICATION.getId(), FlowType.INTERVIEW.getId(), FlowType.OFFER.getId(), FlowType.AGREEMENT.getId());

    private List<Integer> getMediaReportDisplay(String parentId, String oemGroupId, boolean isBeforeJoin) {
        List<MediaReportDisplay> settings = mediaReportDisplayRepository.getMediaReportSetting(parentId, oemGroupId);
        List<Integer> selectionTypes = new ArrayList<>();

        if (!settings.isEmpty()) {
            if (settings.get(0).getIsEnabled() && isBeforeJoin) {
                selectionTypes.add(FlowType.APPLICATION.getId());
            }
            if (settings.get(1).getIsEnabled() && isBeforeJoin) {
                selectionTypes.add(FlowType.INTERVIEW.getId());
            }
            if (settings.get(2).getIsEnabled() && isBeforeJoin) {
                selectionTypes.add(FlowType.OFFER.getId());
            }
            if (settings.get(3).getIsEnabled()) {
                selectionTypes.add(FlowType.AGREEMENT.getId());
            }
        } else {
            if (isBeforeJoin) {
                selectionTypes.addAll(List.of(
                        FlowType.APPLICATION.getId(),
                        FlowType.INTERVIEW.getId(),
                        FlowType.OFFER.getId(),
                        FlowType.AGREEMENT.getId())
                );
            } else {
                selectionTypes.add(FlowType.AGREEMENT.getId());
            }
        }

        return selectionTypes;
    }

    private Pair<Map<String, AdvertTopPersonCount>, List<MediaColorDto>> getColorsMap(String parentId, String oemGroupId,
                                                                                      MediaReportSearchBoxDto mediaReportSearchBoxDto,
                                                                                      DataType dataType, DateType dateType,
                                                                                      boolean isBeforeJoin) {
        List<AdvertTopPersonCount> colorData = mediaReportCriteriaBuilder.getColorsBasedOnTopPersonCount(
                parentId,
                oemGroupId,
                mediaReportSearchBoxDto.getStartDate(),
                mediaReportSearchBoxDto.getEndDate(),
                dataType,
                dateType,
                isBeforeJoin
        );

        List<MediaColorDto> colorMap = new ArrayList<>();
        Map<String, AdvertTopPersonCount> mapColorsById = colorData.stream()
                .collect(Collectors.toMap(AdvertTopPersonCount::getId, i -> {
                    if (Objects.isNull(i.getHexColor())) {
                        i.setHexColor(Constants.REPORT_DATA_COLORS.get(i.getIdx().intValue() - 1));
                    }
                    i.setIdx(i.getIdx());

                    colorMap.add(MediaColorDto.builder().name(i.getName()).color(i.getHexColor()).build());
                    return i;
                }));

        long largestIndex = mapColorsById.size();

        if (largestIndex > 10) {
            mapColorsById.put(OTHER_NAME,
                    AdvertTopPersonCount.builder()
                            .id(OTHER_NAME)
                            .name(OTHER_NAME)
                            .hexColor(OTHER_COLOR)
                            .idx(largestIndex + 1)
                            .build());

            colorMap.add(MediaColorDto.builder()
                    .color(OTHER_COLOR)
                    .name(OTHER_NAME)
                    .build());
        }
        return Pair.of(mapColorsById, colorMap);
    }

    private void checkDateRangeWithin6Months(MediaReportSearchBoxDto mediaReportSearchBoxDto) {
        long monthsBetween = ChronoUnit.MONTHS.between(mediaReportSearchBoxDto.getStartDate().withDayOfMonth(1),
                mediaReportSearchBoxDto.getEndDate().withDayOfMonth(1));
        if (Math.abs(monthsBetween) > 6) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.MEDIA_REPORT_DATE_RANGE.getStatusCode())
                    .message(ErrorMessage.MEDIA_REPORT_DATE_RANGE.getMessage())
                    .build());
        }
    }

    private Double calculateCountRatio(Long number1, Long number2) {
        if (Objects.isNull(number1) || Objects.isNull(number2) || number2 == 0 || number1 == 0) {
            return null;
        }
        return GeneralUtils.round(((double) number1 / number2) * 100, 1);
    }

    private Integer calculateCost(Integer amount, Long personCount) {
        if (Objects.isNull(amount) || personCount == 0) {
            return 0;
        }

        return Math.toIntExact((amount / personCount));
    }

    private MediaCostColumnDto buildCostColumn(Long personCount, Integer cost) {
        return MediaCostColumnDto.builder()
                .personCount(personCount)
                .cost(cost)
                .build();
    }

    private Double calculateCostRatio(Integer number1, Integer number2) {
        if (Objects.isNull(number1) || Objects.isNull(number2) || number2 == 0 || number1 == 0) {
            return null;
        }
        return GeneralUtils.round(((double) number1 / number2) * 100, 1);
    }

    private Integer calculateAndConvertToManYenCost(Integer amount, Long personCount) {
        if (Objects.isNull(amount)) {
            return 0;
        }

        if (personCount == 0) {
            return 0;
        }

        return Math.toIntExact((amount / personCount) / 10000);
    }

    private void getAvailableColor(List<MediaColorDto> colors, AdvertTopPersonCount colorMap) {
        colors.add(Objects.nonNull(colorMap) ?
                MediaColorDto.builder()
                        .name(colorMap.getName())
                        .color(colorMap.getHexColor())
                        .build() :
                MediaColorDto.builder()
                        .name(OTHER_NAME)
                        .color(OTHER_COLOR)
                        .build());
    }

    private Map<String, Integer> beforeAmountByIdMap(List<AdvertReportBeforeJoinDto> reportData, DataType dataType, List<Integer> selectionStatuses) {
        if (Objects.equals(dataType, DataType.INTERVIEWER)) {
            return Collections.emptyMap(); // Return empty if not allowed to compute
        }

        return reportData.stream()
            .filter(dto -> Objects.nonNull(dto.getAmount()))
            .filter(dto -> selectionStatuses.contains(dto.getStatusType()))
            .collect(Collectors.toMap(
                    dto -> dto.getId() + "|" + dto.getAdvertisementId(),
                    dto -> dto,
                    (dto1, dto2) -> dto1 // keep the first one if duplicate key
            ))
            .values().stream()
            .collect(Collectors.groupingBy(
                    AdvertReportBeforeJoinDto::getId,
                    Collectors.summingInt(dto -> Objects.isNull(dto.getAmount()) ? 0 : dto.getAmount())
            ));
    }

    private Map<String, Integer> beforeAmountByIdAndMonthMap(List<AdvertReportBeforeJoinDto> reportData, DataType dataType, List<Integer> selectionStatuses, Integer month) {
        if (Objects.equals(dataType, DataType.INTERVIEWER) || Objects.equals(dataType, DataType.EDUCATION_OFFICER)) {
            return null;
        }

        return reportData.stream()
            .filter(dto -> Objects.nonNull(dto.getAmount()))
            .filter(dto -> selectionStatuses.contains(dto.getStatusType()))
            .filter(dto -> Objects.equals(dto.getMonth(), month))
            .collect(Collectors.toMap(
                    dto -> dto.getId() + "|" + dto.getAdvertisementId(),
                    dto -> dto,
                    (dto1, dto2) -> dto1 // keep the first one if duplicate key
            ))
            .values().stream()
            .collect(Collectors.groupingBy(
                    AdvertReportBeforeJoinDto::getId,
                    Collectors.summingInt(dto -> Objects.isNull(dto.getAmount()) ? 0 : dto.getAmount())
            ));
    }

    private Map<String, Integer> afterAmountByIdMap(List<AdvertReportAfterJoinDto> reportData, DataType dataType) {
        if (Objects.equals(dataType, DataType.EDUCATION_OFFICER)) {
            return Collections.emptyMap(); // Return empty if not allowed to compute
        }

        return reportData.stream()
            .filter(dto -> Objects.nonNull(dto.getAmount()))
            .collect(Collectors.toMap(
                    dto -> dto.getId() + "|" + dto.getAdvertisementId(),
                    dto -> dto,
                    (dto1, dto2) -> dto1 // keep the first one if duplicate key
            ))
            .values().stream()
            .collect(Collectors.groupingBy(
                    AdvertReportAfterJoinDto::getId,
                    Collectors.summingInt(dto -> Objects.isNull(dto.getAmount()) ? 0 : dto.getAmount())
            ));
    }

    private Map<String, Long> beforePersonCountByIdMap(List<AdvertReportBeforeJoinDto> reportData, List<Integer> selectionStatuses) {
        return reportData.stream()
            .filter(dto -> Objects.nonNull(dto.getPersonCount()))
            .filter(dto -> selectionStatuses.contains(dto.getStatusType()))
            .collect(Collectors.toMap(
                    dto -> dto.getId() + "|" + dto.getStatusType() + "|" + dto.getApplicantId(),
                    dto -> dto,
                    (dto1, dto2) -> dto1 // keep the first one if duplicate key
            ))
            .values().stream()
            .collect(Collectors.groupingBy(
                    AdvertReportBeforeJoinDto::getId,
                    Collectors.summingLong(dto -> Objects.isNull(dto.getPersonCount()) ? 0 : dto.getPersonCount())
            ));
    }

    private Map<String, Long> beforePersonCountByIdAndMonthMap(List<AdvertReportBeforeJoinDto> reportData, List<Integer> selectionStatuses, Integer month) {
        return reportData.stream()
            .filter(dto -> Objects.nonNull(dto.getPersonCount()))
            .filter(dto -> selectionStatuses.contains(dto.getStatusType()))
            .filter(dto -> Objects.equals(dto.getMonth(), month))
            .collect(Collectors.toMap(
                    dto -> dto.getId() + "|" + dto.getStatusType() + "|" + dto.getApplicantId(),
                    dto -> dto,
                    (dto1, dto2) -> dto1 // keep the first one if duplicate key
            ))
            .values().stream()
            .collect(Collectors.groupingBy(
                    AdvertReportBeforeJoinDto::getId,
                    Collectors.summingLong(dto -> Objects.isNull(dto.getPersonCount()) ? 0 : dto.getPersonCount())
            ));
    }

    private Map<String, Long> afterPersonCountByIdMap(List<AdvertReportAfterJoinDto> reportData, String period) {
        Function<AdvertReportAfterJoinDto, Long> countExtractor = switch (period) {
            case ONE_WEEK -> AdvertReportAfterJoinDto::getOneWeekCount;
            case ONE_MONTH -> AdvertReportAfterJoinDto::getOneMonthCount;
            case TWO_MONTH -> AdvertReportAfterJoinDto::getTwoMonthCount;
            case THREE_MONTH -> AdvertReportAfterJoinDto::getThreeMonthCount;
            case SIX_MONTH -> AdvertReportAfterJoinDto::getSixMonthCount;
            default -> dto -> null;
        };

        return reportData.stream()
            .filter(dto -> Objects.nonNull(countExtractor.apply(dto)))
            .collect(Collectors.toMap(
                    dto -> dto.getId() + "|" + dto.getApplicantId(),
                    dto -> dto,
                    (dto1, dto2) -> dto1
            ))
            .values().stream()
            .collect(Collectors.groupingBy(
                    AdvertReportAfterJoinDto::getId,
                    Collectors.summingLong(dto -> {
                        Long value = countExtractor.apply(dto);
                        return value == null ? 0L : value;
                    })
            ));
    }

    private MediaCostBeforeJoinDto calculateBeforeTableRow_Total(List<Integer> selectionTypes,
                                                                 Map<String, Integer> amountById,
                                                                 Map<String, Long> personCountByIdApply,
                                                                 Map<String, Long> personCountByIdInterview,
                                                                 Map<String, Long> personCountByIdOffer,
                                                                 Map<String, Long> personCountByIdAgreement) {

        int totalAmount = amountById.values().stream().mapToInt(Integer::intValue).sum();
        long applyPersonCount = personCountByIdApply.values().stream().mapToLong(Long::longValue).sum();
        long interviewPersonCount = personCountByIdInterview.values().stream().mapToLong(Long::longValue).sum();
        long offerPersonCount = personCountByIdOffer.values().stream().mapToLong(Long::longValue).sum();
        long agreementPersonCount = personCountByIdAgreement.values().stream().mapToLong(Long::longValue).sum();

        MediaCostBeforeJoinDto totalRow = MediaCostBeforeJoinDto.builder()
                .name(Constants.MEDIA_REPORT_TOTAL_TITLE)
                .amount(totalAmount)
                .build();

        if (selectionTypes.contains(FlowType.APPLICATION.getId())) {
            MediaCostColumnDto section = MediaCostColumnDto.builder()
                    .personCount(applyPersonCount)
                    .cost(calculateCost(totalAmount, applyPersonCount))
                    .build();
            totalRow.setApply(section);
        }
        if (selectionTypes.contains(FlowType.INTERVIEW.getId())) {
            MediaCostColumnDto section = MediaCostColumnDto.builder()
                    .personCount(interviewPersonCount)
                    .cost(calculateCost(totalAmount, interviewPersonCount))
                    .ratio(calculateCountRatio(interviewPersonCount, applyPersonCount))
                    .build();
            totalRow.setInterview(section);
        }
        if (selectionTypes.contains(FlowType.OFFER.getId())) {
            MediaCostColumnDto section = MediaCostColumnDto.builder()
                    .personCount(offerPersonCount)
                    .cost(calculateCost(totalAmount, offerPersonCount))
                    .ratio(calculateCountRatio(offerPersonCount, applyPersonCount))
                    .build();
            totalRow.setOffer(section);
        }
        if (selectionTypes.contains(FlowType.AGREEMENT.getId())) {
            MediaCostColumnDto section = MediaCostColumnDto.builder()
                    .personCount(agreementPersonCount)
                    .cost(calculateCost(totalAmount, agreementPersonCount))
                    .ratio(calculateCountRatio(agreementPersonCount, applyPersonCount))
                    .build();
            totalRow.setAgreement(section);
        }

        return totalRow;
    }
    // 媒体費のテーブル
    public MediaCostAfterJoinDto calculateAfterTableRow_Total(Map<String, Integer> amountById,
                                                              Map<String, Long> personCountByIdOneWeek,
                                                              Map<String, Long> personCountByIdOneMonth,
                                                              Map<String, Long> personCountByIdTwoMonth,
                                                              Map<String, Long> personCountByIdThreeMonth,
                                                              Map<String, Long> personCountByIdSixMonth) {

        int totalAmount = amountById.values().stream().mapToInt(Integer::intValue).sum();
        long oneWeekPersonCount = personCountByIdOneWeek.values().stream().mapToLong(Long::longValue).sum();
        long oneMonthPersonCount = personCountByIdOneMonth.values().stream().mapToLong(Long::longValue).sum();
        long twoMonthPersonCount = personCountByIdTwoMonth.values().stream().mapToLong(Long::longValue).sum();
        long threeMonthPersonCount = personCountByIdThreeMonth.values().stream().mapToLong(Long::longValue).sum();
        long sixMonthPersonCount = personCountByIdSixMonth.values().stream().mapToLong(Long::longValue).sum();

        MediaCostColumnDto totalOneWeek = MediaCostColumnDto.builder()
                .personCount(oneWeekPersonCount)
                .cost(calculateCost(totalAmount, oneWeekPersonCount))
                .ratio(null)
                .build();

        MediaCostColumnDto totalOneMonth = MediaCostColumnDto.builder()
                .personCount(oneMonthPersonCount)
                .cost(calculateCost(totalAmount, oneMonthPersonCount))
                .ratio(null)
                .build();

        MediaCostColumnDto totalTwoMonth = MediaCostColumnDto.builder()
                .personCount(twoMonthPersonCount)
                .cost(calculateCost(totalAmount, twoMonthPersonCount))
                .ratio(null)
                .build();

        MediaCostColumnDto totalThreeMonth = MediaCostColumnDto.builder()
                .personCount(threeMonthPersonCount)
                .cost(calculateCost(totalAmount, threeMonthPersonCount))
                .ratio(null)
                .build();

        MediaCostColumnDto totalSixMonth = MediaCostColumnDto.builder()
                .personCount(sixMonthPersonCount)
                .cost(calculateCost(totalAmount, sixMonthPersonCount))
                .ratio(null)
                .build();

        return MediaCostAfterJoinDto.builder()
                .name(Constants.MEDIA_REPORT_TOTAL_TITLE)
                .amount(totalAmount)
                .oneWeek(totalOneWeek)
                .oneMonth(totalOneMonth)
                .twoMonth(totalTwoMonth)
                .threeMonth(totalThreeMonth)
                .sixMonth(totalSixMonth)
                .build();
    }

    private List<MediaCostBeforeJoinDto> calculateBeforeTableRow_Items(Map<String, String> dataNameMapById,
                                                                       DataType dataType, List<Integer> selectionTypes,
                                                                       Map<String, Integer> amountById,
                                                                       Map<String, Long> personCountByIdApply,
                                                                       Map<String, Long> personCountByIdInterview,
                                                                       Map<String, Long> personCountByIdOffer,
                                                                       Map<String, Long> personCountByIdAgreement) {

        List<MediaCostBeforeJoinDto> rowItems = new ArrayList<>();

        dataNameMapById.forEach((id, data) -> {
            MediaCostColumnDto apply = null;
            MediaCostColumnDto interview = null;
            MediaCostColumnDto offer = null;
            MediaCostColumnDto agreement = null;

            int amount = Objects.equals(dataType, DataType.INTERVIEWER) || Objects.isNull(amountById.get(id)) ? 0 : amountById.get(id);
            long applyPersonCount = Objects.isNull(personCountByIdApply.get(id)) ? 0 : personCountByIdApply.get(id);
            long interviewPersonCount = Objects.isNull(personCountByIdInterview.get(id)) ? 0 : personCountByIdInterview.get(id);
            long offerPersonCount = Objects.isNull(personCountByIdOffer.get(id)) ? 0 : personCountByIdOffer.get(id);
            long agreementPersonCount = Objects.isNull(personCountByIdAgreement.get(id)) ? 0 : personCountByIdAgreement.get(id);

            if (selectionTypes.contains(FlowType.APPLICATION.getId())) {
                apply = buildCostColumn(applyPersonCount, calculateCost(amount, applyPersonCount));
            }

            if (selectionTypes.contains(FlowType.INTERVIEW.getId())) {
                interview = buildCostColumn(interviewPersonCount, calculateCost(amount, interviewPersonCount));
                interview.setRatio(calculateCountRatio(interviewPersonCount, applyPersonCount));
            }

            if (selectionTypes.contains(FlowType.OFFER.getId())) {
                offer = buildCostColumn(offerPersonCount, calculateCost(amount, offerPersonCount));
                offer.setRatio(calculateCountRatio(offerPersonCount, applyPersonCount));
            }

            if (selectionTypes.contains(FlowType.AGREEMENT.getId())) {
                agreement = buildCostColumn(agreementPersonCount, calculateCost(amount, agreementPersonCount));
                agreement.setRatio(calculateCountRatio(agreementPersonCount, applyPersonCount));
            }

            String name = dataNameMapById.get(id);

            MediaCostBeforeJoinDto row = MediaCostBeforeJoinDto.builder()
                    .name(name)
                    .amount(amount)
                    .apply(apply)
                    .interview(interview)
                    .offer(offer)
                    .agreement(agreement)
                    .build();

            rowItems.add(row);
        });

        return rowItems;
    }

    private List<MediaCostAfterJoinDto> calculateAfterTableRow_Items(Map<String, String> dataNameMapById,
                                                                     Map<String, Integer> amountById,
                                                                     Map<String, Long> personCountByIdOneWeek,
                                                                     Map<String, Long> personCountByIdOneMonth,
                                                                     Map<String, Long> personCountByIdTwoMonth,
                                                                     Map<String, Long> personCountByIdThreeMonth,
                                                                     Map<String, Long> personCountByIdSixMonth) {

        List<MediaCostAfterJoinDto> rowItems = new ArrayList<>();

        dataNameMapById.forEach((id, data) -> {

            int amount = Objects.isNull(amountById.get(id)) ? 0 : amountById.get(id);
            long oneWeekPersonCount = Objects.isNull(personCountByIdOneWeek.get(id)) ? 0 : personCountByIdOneWeek.get(id);
            long oneMonthPersonCount = Objects.isNull(personCountByIdOneMonth.get(id)) ? 0 : personCountByIdOneMonth.get(id);
            long twoMonthPersonCount = Objects.isNull(personCountByIdTwoMonth.get(id)) ? 0 : personCountByIdTwoMonth.get(id);
            long threeMonthPersonCount = Objects.isNull(personCountByIdThreeMonth.get(id)) ? 0 : personCountByIdThreeMonth.get(id);
            long sixMonthPersonCount = Objects.isNull(personCountByIdSixMonth.get(id)) ? 0 : personCountByIdSixMonth.get(id);

            MediaCostColumnDto oneWeek = buildCostColumn(oneWeekPersonCount, calculateCost(amount, oneWeekPersonCount));
            MediaCostColumnDto oneMonth = buildCostColumn(oneMonthPersonCount, calculateCost(amount, oneMonthPersonCount));
            MediaCostColumnDto twoMonth = buildCostColumn(twoMonthPersonCount, calculateCost(amount, twoMonthPersonCount));
            MediaCostColumnDto threeMonth = buildCostColumn(threeMonthPersonCount, calculateCost(amount, threeMonthPersonCount));
            MediaCostColumnDto sixMonth = buildCostColumn(sixMonthPersonCount, calculateCost(amount, sixMonthPersonCount));

            String name = dataNameMapById.get(id);

            MediaCostAfterJoinDto row = MediaCostAfterJoinDto.builder()
                    .name(name)
                    .amount(amount)
                    .oneWeek(oneWeek)
                    .oneMonth(oneMonth)
                    .twoMonth(twoMonth)
                    .threeMonth(threeMonth)
                    .sixMonth(sixMonth)
                    .build();

            rowItems.add(row);
        });

        return rowItems;
    }

    private List<MediaPieChartSectionDto> calculateShareCostPieChartBeforeJoin(Map<String, String> dataNameMapById,
                                                                              DataType dataType, Map<String, Integer> amountById,
                                                                              Map<String, AdvertTopPersonCount> mapColorsById) {

        List<MediaPieChartSectionDto> sections = new ArrayList<>();
        List<Map.Entry<String, Integer>> sortedAmountById = new ArrayList<>(amountById.entrySet());
        sortedAmountById.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        List<String> sortedIdList = new ArrayList<>(sortedAmountById.stream()
                .map(Map.Entry::getKey)
                .toList());

        if (!Objects.equals(dataType, DataType.INTERVIEWER)) {
            int totalAmount = amountById.values().stream().mapToInt(Integer::intValue).sum();

            for (int i = 0; i < MAX_ITEMS; i++) {

                if (sortedIdList.isEmpty()) {
                    break;
                }

                String firstDataId = sortedIdList.removeFirst();
                int firstDataAmount = Objects.isNull(amountById.get(firstDataId)) ? 0 : amountById.get(firstDataId);
                String firstDataName = dataNameMapById.get(firstDataId);

                AdvertTopPersonCount colorMap = mapColorsById.get(firstDataId);

                MediaPieChartSectionDto sectionItem = MediaPieChartSectionDto.builder()
                        .name(firstDataName)
                        .cost(firstDataAmount)
                        .personCount(0L)
                        .ratio(calculateCostRatio(firstDataAmount, totalAmount))
                        .color(Objects.nonNull(colorMap) ? colorMap.getHexColor() : OTHER_COLOR)
                        .build();

                sections.add(sectionItem);
            }

            if (!sortedIdList.isEmpty()) {
                int otherCost =  sortedIdList.stream()
                        .mapToInt(key -> amountById.getOrDefault(key, 0))
                        .sum();

                sections.add(MediaPieChartSectionDto.builder()
                        .name(OTHER_NAME)
                        .personCount(0L)
                        .cost(otherCost)
                        .color(OTHER_COLOR)
                        .ratio(calculateCostRatio(otherCost, totalAmount))
                        .build());
            }
        }

        return sections;
    }

    private List<MediaPieChartSectionDto> calculatePieChartBeforeJoin(Map<String, String> dataNameMapById,
                                                                      Integer selectionType,
                                                                      List<Integer> selectionTypes,
                                                                      Map<String, Integer> amountById,
                                                                      Map<String, Long> personCountByIdApply,
                                                                      Map<String, Long> personCountByIdInterview,
                                                                      Map<String, Long> personCountByIdOffer,
                                                                      Map<String, Long> personCountByIdAgreement,
                                                                      Map<String, AdvertTopPersonCount> mapColorsById) {
        Map<String, Long> personCountById = switch (FlowType.fromId(selectionType)) {
            case APPLICATION -> personCountByIdApply;
            case INTERVIEW -> personCountByIdInterview;
            case OFFER -> personCountByIdOffer;
            default -> personCountByIdAgreement;
        };

        long totalPersonCount = personCountById.values().stream().mapToLong(Long::longValue).sum();

        if (!selectionTypes.contains(selectionType)) {
            return null;
        }

        List<MediaPieChartSectionDto> sections = new ArrayList<>();
        List<Map.Entry<String, Long>> sortedPersonCountById = new ArrayList<>(personCountById.entrySet());
        sortedPersonCountById.sort(Map.Entry.<String, Long>comparingByValue().reversed());

        List<String> sortedIdList = new ArrayList<>(sortedPersonCountById.stream()
                .map(Map.Entry::getKey)
                .toList());

        AdvertTopPersonCount otherColorMap = mapColorsById.get(OTHER_NAME);

        for (int i = 0; i < MAX_ITEMS; i++) {

            if (sortedIdList.isEmpty()) {
                break;
            }

            String firstDataId = sortedIdList.removeFirst();
            long firstDataPersonCount = personCountById.get(firstDataId);
            String firstDataName = dataNameMapById.get(firstDataId);
            int amount = Objects.isNull(amountById.get(firstDataId)) ? 0 : amountById.get(firstDataId);

            AdvertTopPersonCount colorMap = mapColorsById.get(firstDataId);

            MediaPieChartSectionDto sectionItem = MediaPieChartSectionDto.builder()
                    .name(firstDataName)
                    .personCount(firstDataPersonCount)
                    .cost(amount)
                    .color(Objects.nonNull(colorMap) ? colorMap.getHexColor() : otherColorMap.getHexColor())
                    .ratio(calculateCountRatio(firstDataPersonCount, totalPersonCount))
                    .build();

            if (firstDataPersonCount > 0) {
                sections.add(sectionItem);
            }
        }

        if (!sortedIdList.isEmpty()) {
            long otherPersonCount = sortedIdList.stream()
                    .mapToLong(key -> personCountById.getOrDefault(key, 0L))
                    .sum();

            if (otherPersonCount > 0) {
                sections.add(MediaPieChartSectionDto.builder()
                        .name(OTHER_NAME)
                        .personCount(otherPersonCount)
                        .cost(0)
                        .color(OTHER_COLOR) // or pick a default color
                        .ratio(calculateCountRatio(otherPersonCount, totalPersonCount))
                        .build());
            }
        }

        return sections;
    }

    private List<MediaPieChartSectionDto> calculatePieChartAfterJoin(Map<String, String> dataNameMapById,
                                                                     Map<String, Integer> amountById,
                                                                     DataType dataType, String period,
                                                                     Map<String, Long> personCountByIdOneWeek,
                                                                     Map<String, Long> personCountByIdOneMonth,
                                                                     Map<String, Long> personCountByIdTwoMonth,
                                                                     Map<String, Long> personCountByIdThreeMonth,
                                                                     Map<String, Long> personCountByIdSixMonth,
                                                                     Map<String, AdvertTopPersonCount> mapColorsById) {
        Map<String, Long> personCountById = switch (period) {
            case ONE_WEEK -> personCountByIdOneWeek;
            case ONE_MONTH -> personCountByIdOneMonth;
            case TWO_MONTH -> personCountByIdTwoMonth;
            case THREE_MONTH -> personCountByIdThreeMonth;
            default -> personCountByIdSixMonth;
        };

        int totalAmount = amountById.values().stream().mapToInt(Integer::intValue).sum();

        if (Objects.equals(DataType.EDUCATION_OFFICER, dataType)) {
            return new ArrayList<>();
        }

        List<MediaPieChartSectionDto> sections = new ArrayList<>();
        List<Map.Entry<String, Integer>> sortedAmountById = new ArrayList<>(amountById.entrySet());
        sortedAmountById.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        List<String> sortedIdList = new ArrayList<>(sortedAmountById.stream()
                .map(Map.Entry::getKey)
                .toList());

        AdvertTopPersonCount otherColorMap = mapColorsById.get(OTHER_NAME);

        for (int i = 0; i < MAX_ITEMS; i++) {

            if (sortedIdList.isEmpty()) {
                break;
            }

            String firstDataId = sortedIdList.removeFirst();
            String firstDataName = dataNameMapById.get(firstDataId);
            long personCount = personCountById.get(firstDataId);
            int amount = Objects.isNull(amountById.get(firstDataId)) ? 0 : (amountById.get(firstDataId));

            AdvertTopPersonCount colorMap = mapColorsById.get(firstDataId);

            MediaPieChartSectionDto sectionItem = MediaPieChartSectionDto.builder()
                    .name(firstDataName)
                    .personCount(null)
                    .cost(calculateCost(amount, personCount))
                    .color(Objects.nonNull(colorMap) ? colorMap.getHexColor() : otherColorMap.getHexColor())
                    .ratio(calculateCostRatio(calculateCost(amount, personCount), totalAmount))
                    .build();

            if (personCount > 0) {
                sections.add(sectionItem);
            }
        }

        if (!sortedIdList.isEmpty()) {
            long otherPersonCount =  sortedIdList.stream()
                    .mapToInt(key -> amountById.getOrDefault(key, 0))
                    .sum();
            int otherAmount =  sortedIdList.stream()
                    .mapToInt(key -> amountById.getOrDefault(key, 0))
                    .sum();

            if (otherPersonCount > 0) {
                sections.add(MediaPieChartSectionDto.builder()
                        .name(OTHER_NAME)
                        .personCount(null)
                        .cost(calculateCost(otherAmount, otherPersonCount))
                        .color(OTHER_COLOR) // or pick a default color
                        .ratio(calculateCostRatio(calculateCost(otherAmount, otherPersonCount), totalAmount))
                        .build());
            }
        }

        return sections;
    }

    private MediaBarChartTotalDto calculateBarChartTotalBeforeJoin(Map<String, String> dataNameMapById,
                                                                   Integer selectionType,
                                                                   List<Integer> selectionTypes,
                                                                   Map<String, Integer> amountById,
                                                                   Map<String, Long> personCountByIdApply,
                                                                   Map<String, Long> personCountByIdInterview,
                                                                   Map<String, Long> personCountByIdOffer,
                                                                   Map<String, Long> personCountByIdAgreement,
                                                                   Map<String, AdvertTopPersonCount> mapColorsById) {
        Map<String, Long> personCountById = switch (FlowType.fromId(selectionType)) {
            case APPLICATION -> personCountByIdApply;
            case INTERVIEW -> personCountByIdInterview;
            case OFFER -> personCountByIdOffer;
            default -> personCountByIdAgreement;
        };

        int totalAmount = amountById.values().stream().mapToInt(Integer::intValue).sum();
        long totalPersonCount = personCountById.values().stream().mapToLong(Long::longValue).sum();

        if (!selectionTypes.contains(selectionType)) {
            return MediaBarChartTotalDto.builder()
                .items(new ArrayList<>())
                .colors(new ArrayList<>())
                .build();
        }

        List<MediaBarChartSectionDto> totalBar = new ArrayList<>();
        List<MediaColorDto> colors = new ArrayList<>();
        List<Map.Entry<String, Long>> sortedPersonCountById = new ArrayList<>(personCountById.entrySet());
        sortedPersonCountById.sort(Map.Entry.<String, Long>comparingByValue().reversed());

        List<String> sortedIdList = new ArrayList<>(sortedPersonCountById.stream()
                .map(Map.Entry::getKey)
                .toList());
        AdvertTopPersonCount otherColorMap = mapColorsById.get(OTHER_NAME);

        for (int i = 0; i < MAX_ITEMS; i++) {

            if (sortedIdList.isEmpty()) {
                break;
            }

            String firstDataId = sortedIdList.removeFirst();
            AdvertTopPersonCount colorMap = mapColorsById.get(firstDataId);
            String firstDataName = dataNameMapById.get(firstDataId);
            long firstDataPersonCount = personCountById.get(firstDataId);
            int firstDataAmount = Objects.isNull(amountById.get(firstDataId)) ? 0 : (amountById.get(firstDataId));

            getAvailableColor(colors, colorMap);

            totalBar.add(MediaBarChartSectionDto.builder()
                    .name(firstDataName)
                    .color(Objects.nonNull(colorMap) ? colorMap.getHexColor() : otherColorMap.getHexColor())
                    .amount(calculateAndConvertToManYenCost(firstDataAmount, firstDataPersonCount))
                    .personCount(firstDataPersonCount)
                    .ratio(calculateCountRatio(firstDataPersonCount, totalPersonCount))
                    .amountRatio(calculateCostRatio(firstDataAmount, totalAmount))
                    .build());
        }

        if (!sortedIdList.isEmpty()) {
            int otherCost =  sortedIdList.stream()
                    .mapToInt(key -> amountById.getOrDefault(key, 0))
                    .sum();
            long otherPersonCount = sortedIdList.stream()
                    .mapToLong(key -> personCountById.getOrDefault(key, 0L))
                    .sum();

            getAvailableColor(colors, null);
            totalBar.add(MediaBarChartSectionDto.builder()
                    .name(otherColorMap.getName())
                    .color(otherColorMap.getHexColor())
                    .amount(calculateAndConvertToManYenCost(otherCost, otherPersonCount))
                    .personCount(otherPersonCount)
                    .ratio(calculateCountRatio(otherPersonCount, totalPersonCount))
                    .amountRatio(calculateCostRatio(otherCost, totalAmount))
                    .build());
        }

        return MediaBarChartTotalDto.builder()
                .items(totalBar)
                .colors(colors)
                .build();
    }

    private List<MediaBarChartItemGroupDto> calculateBarChartItemsBeforeJoin(Map<String, String> dataNameMapById,
                                                                             List<AdvertReportBeforeJoinDto> reportData,
                                                                             DataType dataType, Map<Integer, String> monthMap,
                                                                             Integer selectionType, List<Integer> selectionTypes,
                                                                             Map<String, AdvertTopPersonCount> mapColorsById) {

        List<MediaBarChartItemGroupDto> eachMonthData = new ArrayList<>();

        if (!selectionTypes.contains(selectionType)) {
            return eachMonthData;
        }

        List<Integer> selectionStatuses = switch (FlowType.fromId(selectionType)) {
            case APPLICATION -> SELECTION_TYPES;
            case INTERVIEW -> SELECTION_TYPES.subList(1,4);
            case OFFER -> SELECTION_TYPES.subList(2,4);
            default -> SELECTION_TYPES.subList(3,4);
        };

        for (Map.Entry<Integer, String> entry : monthMap.entrySet()) {

            MediaBarChartItemGroupDto group = MediaBarChartItemGroupDto.builder()
                        .month(entry.getValue())
                        .items(new ArrayList<>())
                        .colors(new ArrayList<>())
                        .build();
            List<MediaBarChartSectionDto> groupItems = new ArrayList<>();
            List<MediaColorDto> colors = new ArrayList<>();

            Map<String, Long> itemPersonCountById = beforePersonCountByIdAndMonthMap(reportData, selectionStatuses, entry.getKey());
            Map<String, Integer> itemAmountById = beforeAmountByIdAndMonthMap(reportData, dataType, selectionStatuses, entry.getKey());

            int totalAmount = Objects.isNull(itemAmountById) ? 0 : itemAmountById.values().stream().mapToInt(Integer::intValue).sum();
            long totalPersonCount = itemPersonCountById.values().stream().mapToLong(Long::longValue).sum();

            List<Map.Entry<String, Long>> sortedPersonCountById = new ArrayList<>(itemPersonCountById.entrySet());
            sortedPersonCountById.sort(Map.Entry.<String, Long>comparingByValue().reversed());

            List<String> sortedIdList = new ArrayList<>(sortedPersonCountById.stream()
                    .map(Map.Entry::getKey)
                    .toList());
            AdvertTopPersonCount otherColorMap = mapColorsById.get(OTHER_NAME);

            for (int i = 0; i < MAX_ITEMS; i++) {

                if (sortedIdList.isEmpty()) {
                    break;
                }

                String firstDataId = sortedIdList.removeFirst();
                AdvertTopPersonCount colorMap = mapColorsById.get(firstDataId);
                String firstDataName = dataNameMapById.get(firstDataId);
                long firstDataPersonCount = itemPersonCountById.get(firstDataId);
                int firstDataAmount = Objects.isNull(itemAmountById) ? 0 : Objects.isNull(itemAmountById.get(firstDataId)) ? 0 : itemAmountById.get(firstDataId);

                getAvailableColor(colors, colorMap);

                groupItems.add(MediaBarChartSectionDto.builder()
                        .name(firstDataName)
                        .color(Objects.nonNull(colorMap) ? colorMap.getHexColor() : otherColorMap.getHexColor())
                        .amount(calculateCost(firstDataAmount, firstDataPersonCount))
                        .personCount(firstDataPersonCount)
                        .ratio(calculateCountRatio(firstDataPersonCount, totalPersonCount))
                        .amountRatio(calculateCostRatio(firstDataAmount, totalAmount))
                        .build());
            }

            if (!sortedIdList.isEmpty()) {
                int otherCost =  sortedIdList.stream()
                        .mapToInt(key -> Objects.isNull(itemAmountById) ? 0 : itemAmountById.getOrDefault(key, 0))
                        .sum();
                long otherPersonCount = sortedIdList.stream()
                        .mapToLong(key -> itemPersonCountById.getOrDefault(key, 0L))
                        .sum();
                int combineDataAmount = calculateCost(otherCost, otherPersonCount);

                getAvailableColor(colors, null);

                groupItems.add(MediaBarChartSectionDto.builder()
                        .name(otherColorMap.getName())
                        .color(otherColorMap.getHexColor())
                        .amount(combineDataAmount)
                        .personCount(otherPersonCount)
                        .ratio(calculateCountRatio(otherPersonCount, totalPersonCount))
                        .amountRatio(calculateCostRatio(combineDataAmount, totalAmount))
                        .build());
            }

            group.setItems(groupItems);
            group.setColors(colors);
            eachMonthData.add(group);
        }

        return eachMonthData;
    }

    private List<MediaBarChartItemGroupDto> calculateBarChartItemsAfterJoin(Map<String, String> dataNameMapById,
                                                                            Map<String, Integer> amountById,
                                                                            Map<String, Long> personCountByIdOneWeek,
                                                                            Map<String, Long> personCountByIdOneMonth,
                                                                            Map<String, Long> personCountByIdTwoMonth,
                                                                            Map<String, Long> personCountByIdThreeMonth,
                                                                            Map<String, Long> personCountByIdSixMonth,
                                                                            Map<String, AdvertTopPersonCount> mapColorsById) {

        List<MediaBarChartItemGroupDto> groupItems = new ArrayList<>();

        for (String item : FIXED_BAR_CHART_AFTER_JOIN_LABELS) {

            List<MediaBarChartSectionDto> items = new ArrayList<>();

            int totalAmount = Objects.isNull(amountById) ? 0 : amountById.values().stream().mapToInt(Integer::intValue).sum();
            long totalPersonCount;
            Map<String, Long> itemPersonCountById;

            switch (item) {
                case ONE_WEEK -> {
                    totalPersonCount = personCountByIdOneWeek.values().stream().mapToLong(Long::longValue).sum();
                    itemPersonCountById = personCountByIdOneWeek;
                }
                case ONE_MONTH -> {
                    totalPersonCount = personCountByIdOneMonth.values().stream().mapToLong(Long::longValue).sum();
                    itemPersonCountById = personCountByIdOneMonth;
                }
                case TWO_MONTH -> {
                    totalPersonCount = personCountByIdTwoMonth.values().stream().mapToLong(Long::longValue).sum();
                    itemPersonCountById = personCountByIdTwoMonth;
                }
                case THREE_MONTH -> {
                    totalPersonCount = personCountByIdThreeMonth.values().stream().mapToLong(Long::longValue).sum();
                    itemPersonCountById = personCountByIdThreeMonth;
                }
                default -> {
                    totalPersonCount = personCountByIdSixMonth.values().stream().mapToLong(Long::longValue).sum();
                    itemPersonCountById = personCountByIdSixMonth;
                }
            }

            MediaBarChartItemGroupDto group = MediaBarChartItemGroupDto.builder()
                        .month(item)
                        .items(new ArrayList<>())
                        .colors(new ArrayList<>())
                        .build();
            List<MediaColorDto> colors = new ArrayList<>();

            List<Map.Entry<String, Long>> sortedPersonCountById = new ArrayList<>(itemPersonCountById.entrySet());
            sortedPersonCountById.sort(Map.Entry.<String, Long>comparingByValue().reversed());

            List<String> sortedIdList = new ArrayList<>(sortedPersonCountById.stream()
                    .map(Map.Entry::getKey)
                    .toList());
            AdvertTopPersonCount otherColorMap = mapColorsById.get(OTHER_NAME);

            for (int i = 0; i < MAX_ITEMS; i++) {

                if (sortedIdList.isEmpty()) {
                    break;
                }

                String firstDataId = sortedIdList.removeFirst();
                AdvertTopPersonCount colorMap = mapColorsById.get(firstDataId);
                String firstDataName = dataNameMapById.get(firstDataId);
                long firstDataPersonCount = itemPersonCountById.get(firstDataId);
                int dataAmount = Objects.isNull(amountById) ? 0 : Objects.isNull(amountById.get(firstDataId)) ? 0 : amountById.get(firstDataId);
                int firstDataAmount = calculateCost(dataAmount, firstDataPersonCount);

                getAvailableColor(colors, colorMap);

                items.add(MediaBarChartSectionDto.builder()
                        .name(firstDataName)
                        .color(Objects.nonNull(colorMap) ? colorMap.getHexColor() : otherColorMap.getHexColor())
                        .amount(firstDataAmount)
                        .personCount(firstDataPersonCount)
                        .ratio(calculateCountRatio(firstDataPersonCount, totalPersonCount))
                        .amountRatio(calculateCostRatio(firstDataAmount, totalAmount))
                        .build());
            }

            if (!sortedIdList.isEmpty()) {
                int otherCost =  sortedIdList.stream()
                        .mapToInt(key -> Objects.isNull(amountById) ? 0 : amountById.getOrDefault(key, 0))
                        .sum();
                long otherPersonCount = sortedIdList.stream()
                        .mapToLong(key -> itemPersonCountById.getOrDefault(key, 0L))
                        .sum();
                int combineDataAmount = calculateCost(otherCost, otherPersonCount);

                getAvailableColor(colors, null);

                items.add(MediaBarChartSectionDto.builder()
                        .name(otherColorMap.getName())
                        .color(otherColorMap.getHexColor())
                        .amount(combineDataAmount)
                        .personCount(otherPersonCount)
                        .ratio(calculateCountRatio(otherPersonCount, totalPersonCount))
                        .amountRatio(calculateCostRatio(combineDataAmount, totalAmount))
                        .build());
            }

            group.setItems(items);
            group.setColors(colors);
            groupItems.add(group);
        }

        return groupItems;
    }

    public MediaReportBeforeJoinDto getMediaCostBeforeJoin(MediaReportSearchBoxDto mediaReportSearchBoxDto) {
        checkDateRangeWithin6Months(mediaReportSearchBoxDto);

        DataType dataType = DataType.fromId(mediaReportSearchBoxDto.getDataType());
        DateType dateType = DateType.fromId(mediaReportSearchBoxDto.getDateType());

        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        List<Integer> selectionTypes = getMediaReportDisplay(parentInfo.getLeft(), parentInfo.getRight(), true);

        Pair<Map<String, AdvertTopPersonCount>, List<MediaColorDto>> colorMap = getColorsMap(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                mediaReportSearchBoxDto,
                dataType,
                dateType,
                true);

        Map<Integer, String> monthMap = Stream.iterate(mediaReportSearchBoxDto.getStartDate(),
                        date -> !date.isAfter(mediaReportSearchBoxDto.getEndDate()),
                        date -> date.plusMonths(1))
                .collect(Collectors.toMap(
                        LocalDate::getMonthValue,
                        date -> date.format(DateTimeFormatter.ofPattern("yyyy/MM")),
                        (existing, replacement) -> existing, // handle duplicate keys (same month)
                        LinkedHashMap::new));

        List<AdvertReportBeforeJoinDto> reportData = mediaReportCriteriaBuilder.getMediaCostReportBeforeJoin(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                mediaReportSearchBoxDto.getStartDate(),
                mediaReportSearchBoxDto.getEndDate(),
                dataType, dateType);

        Map<String, String> dataNameMapById = reportData.stream()
                .collect(Collectors.toMap(AdvertReportBeforeJoinDto::getId, AdvertReportBeforeJoinDto::getName,(name1, name2) -> name1));

        Map<String, Integer> amountById = beforeAmountByIdMap(reportData, dataType, SELECTION_TYPES);
        Map<String, Long> personCountByIdApply = beforePersonCountByIdMap(reportData, SELECTION_TYPES);
        Map<String, Long> personCountByIdInterview = beforePersonCountByIdMap(reportData, SELECTION_TYPES.subList(1,4)); // remove application
        Map<String, Long> personCountByIdOffer = beforePersonCountByIdMap(reportData, SELECTION_TYPES.subList(2,4)); // remove application, interview
        Map<String, Long> personCountByIdAgreement = beforePersonCountByIdMap(reportData, SELECTION_TYPES.subList(3,4)); // remove application, interview, offer

        // create API response
        MediaReportBeforeJoinDto resp = MediaReportBeforeJoinDto.builder().build();

        // handle report table beforeJoin
        resp.setCostTable(MediaCostListBeforeJoinDto.builder()
                .total(calculateBeforeTableRow_Total(
                        selectionTypes, amountById, personCountByIdApply,
                        personCountByIdInterview, personCountByIdOffer, personCountByIdAgreement))
                .items(calculateBeforeTableRow_Items(
                        dataNameMapById, dataType, selectionTypes, amountById, personCountByIdApply,
                        personCountByIdInterview, personCountByIdOffer, personCountByIdAgreement))
                .build());

        // handle report pieChart beforeJoin
        resp.setPieChart(MediaPieChartBeforeJoinDto.builder()
                .total(calculateShareCostPieChartBeforeJoin(dataNameMapById, dataType, amountById, colorMap.getLeft()))
                .apply(calculatePieChartBeforeJoin(dataNameMapById, FlowType.APPLICATION.getId(), selectionTypes, amountById,
                        personCountByIdApply, personCountByIdInterview, personCountByIdOffer, personCountByIdAgreement, colorMap.getLeft()))
                .interview(calculatePieChartBeforeJoin(dataNameMapById, FlowType.INTERVIEW.getId(), selectionTypes, amountById,
                        personCountByIdApply, personCountByIdInterview, personCountByIdOffer, personCountByIdAgreement, colorMap.getLeft()))
                .offer(calculatePieChartBeforeJoin(dataNameMapById, FlowType.OFFER.getId(), selectionTypes, amountById,
                        personCountByIdApply, personCountByIdInterview, personCountByIdOffer, personCountByIdAgreement, colorMap.getLeft()))
                .agreement(calculatePieChartBeforeJoin(dataNameMapById, FlowType.AGREEMENT.getId(), selectionTypes, amountById,
                        personCountByIdApply, personCountByIdInterview, personCountByIdOffer, personCountByIdAgreement, colorMap.getLeft()))
                .colors(colorMap.getRight())
                .build());

        // handle report barChart beforeJoin
        resp.setBarChart(MediaBarChartBeforeJoinDto.builder()
                .apply(selectionTypes.contains(FlowType.APPLICATION.getId())
                        ? MediaBarChartBeforeJoinItemsDto.builder()
                            .total(calculateBarChartTotalBeforeJoin(dataNameMapById, FlowType.APPLICATION.getId(), selectionTypes, amountById,
                                personCountByIdApply, personCountByIdInterview, personCountByIdOffer, personCountByIdAgreement,colorMap.getLeft()))
                            .groups(calculateBarChartItemsBeforeJoin(dataNameMapById, reportData, dataType, monthMap, FlowType.APPLICATION.getId(), selectionTypes, colorMap.getLeft()))
                            .build()
                        : null)
                .interview(selectionTypes.contains(FlowType.INTERVIEW.getId())
                        ? MediaBarChartBeforeJoinItemsDto.builder()
                            .total(calculateBarChartTotalBeforeJoin(dataNameMapById, FlowType.INTERVIEW.getId(), selectionTypes, amountById,
                                personCountByIdApply, personCountByIdInterview, personCountByIdOffer, personCountByIdAgreement,colorMap.getLeft()))
                            .groups(calculateBarChartItemsBeforeJoin(dataNameMapById, reportData, dataType, monthMap, FlowType.INTERVIEW.getId(), selectionTypes, colorMap.getLeft()))
                            .build()
                        : null)
                .offer(selectionTypes.contains(FlowType.OFFER.getId())
                        ? MediaBarChartBeforeJoinItemsDto.builder()
                            .total(calculateBarChartTotalBeforeJoin(dataNameMapById, FlowType.OFFER.getId(), selectionTypes, amountById,
                                personCountByIdApply, personCountByIdInterview, personCountByIdOffer, personCountByIdAgreement,colorMap.getLeft()))
                            .groups(calculateBarChartItemsBeforeJoin(dataNameMapById, reportData, dataType, monthMap, FlowType.OFFER.getId(), selectionTypes, colorMap.getLeft()))
                            .build()
                        : null)
                .agreement(selectionTypes.contains(FlowType.AGREEMENT.getId())
                        ? MediaBarChartBeforeJoinItemsDto.builder()
                            .total(calculateBarChartTotalBeforeJoin(dataNameMapById, FlowType.AGREEMENT.getId(), selectionTypes, amountById,
                                personCountByIdApply, personCountByIdInterview, personCountByIdOffer, personCountByIdAgreement,colorMap.getLeft()))
                            .groups(calculateBarChartItemsBeforeJoin(dataNameMapById, reportData, dataType, monthMap, FlowType.AGREEMENT.getId(), selectionTypes, colorMap.getLeft()))
                            .build()
                        : null)
                .colors(colorMap.getRight())
                .build());

        return resp;
    }

    public MediaReportAfterJoinDto getMediaCostAfterJoin(MediaReportSearchBoxDto mediaReportSearchBoxDto) {
        checkDateRangeWithin6Months(mediaReportSearchBoxDto);

        DataType dataType = DataType.fromId(mediaReportSearchBoxDto.getDataType());
        DateType dateType = DateType.fromId(mediaReportSearchBoxDto.getDateType());

        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        List<Integer> selectionTypes = getMediaReportDisplay(parentInfo.getLeft(), parentInfo.getRight(), false);

        Pair<Map<String, AdvertTopPersonCount>, List<MediaColorDto>> colorMap = getColorsMap(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                mediaReportSearchBoxDto,
                dataType,
                dateType,
                false);

        MediaReportAfterJoinDto resp = MediaReportAfterJoinDto.builder()
                .barChart(MediaBarChartAfterJoinDto.builder()
                        .groups(new ArrayList<>())
                        .colors(colorMap.getRight())
                        .build())
                .build();

        if (selectionTypes.isEmpty() ||
                (!Objects.equals(DataType.MEDIA_NAME, dataType) && !Objects.equals(DataType.EDUCATION_OFFICER, dataType))) {
            return resp;
        }

        List<AdvertReportAfterJoinDto> reportData = mediaReportCriteriaBuilder.getMediaCostReportAfterJoin(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                mediaReportSearchBoxDto.getStartDate(),
                mediaReportSearchBoxDto.getEndDate(),
                dataType, dateType);

        Map<String, String> dataNameMapById = reportData.stream()
                .collect(Collectors.toMap(AdvertReportAfterJoinDto::getId, AdvertReportAfterJoinDto::getName,(name1, name2) -> name1));

        Map<String, Integer> amountById = afterAmountByIdMap(reportData, dataType);
        Map<String, Long> personCountByIdOneWeek = afterPersonCountByIdMap(reportData, ONE_WEEK);
        Map<String, Long> personCountByIdOneMonth = afterPersonCountByIdMap(reportData, ONE_MONTH);
        Map<String, Long> personCountByIdTwoMonth = afterPersonCountByIdMap(reportData, TWO_MONTH);
        Map<String, Long> personCountByIdThreeMonth = afterPersonCountByIdMap(reportData, THREE_MONTH);
        Map<String, Long> personCountByIdSixMonth = afterPersonCountByIdMap(reportData, SIX_MONTH);

        // handle report table beforeJoin
        resp.setCostTable(MediaCostListAfterJoinDto.builder()
                .total(calculateAfterTableRow_Total(amountById, personCountByIdOneWeek, personCountByIdOneMonth,
                        personCountByIdTwoMonth, personCountByIdThreeMonth, personCountByIdSixMonth))
                .items(calculateAfterTableRow_Items(dataNameMapById, amountById, personCountByIdOneWeek,
                        personCountByIdOneMonth, personCountByIdTwoMonth, personCountByIdThreeMonth, personCountByIdSixMonth))
                .build());

        // handle report pieChart beforeJoin
        resp.setPieChart(MediaPieChartAfterJoinDto.builder()
                .oneWeek(calculatePieChartAfterJoin(dataNameMapById, amountById, dataType, ONE_WEEK,
                        personCountByIdOneWeek, personCountByIdOneMonth, personCountByIdTwoMonth,
                        personCountByIdThreeMonth, personCountByIdSixMonth, colorMap.getLeft()))
                .oneMonth(calculatePieChartAfterJoin(dataNameMapById, amountById, dataType, ONE_MONTH,
                        personCountByIdOneWeek, personCountByIdOneMonth, personCountByIdTwoMonth,
                        personCountByIdThreeMonth, personCountByIdSixMonth, colorMap.getLeft()))
                .twoMonth(calculatePieChartAfterJoin(dataNameMapById, amountById, dataType, TWO_MONTH,
                        personCountByIdOneWeek, personCountByIdOneMonth, personCountByIdTwoMonth,
                        personCountByIdThreeMonth, personCountByIdSixMonth, colorMap.getLeft()))
                .threeMonth(calculatePieChartAfterJoin(dataNameMapById, amountById, dataType, THREE_MONTH,
                        personCountByIdOneWeek, personCountByIdOneMonth, personCountByIdTwoMonth,
                        personCountByIdThreeMonth, personCountByIdSixMonth, colorMap.getLeft()))
                .sixMonth(calculatePieChartAfterJoin(dataNameMapById, amountById, dataType, SIX_MONTH,
                        personCountByIdOneWeek, personCountByIdOneMonth, personCountByIdTwoMonth,
                        personCountByIdThreeMonth, personCountByIdSixMonth, colorMap.getLeft()))
                .colors(colorMap.getRight())
                .build());

        // handle report barChart beforeJoin
        resp.setBarChart(MediaBarChartAfterJoinDto.builder()
                .groups(calculateBarChartItemsAfterJoin(dataNameMapById, amountById,
                        personCountByIdOneWeek, personCountByIdOneMonth, personCountByIdTwoMonth,
                        personCountByIdThreeMonth, personCountByIdSixMonth, colorMap.getLeft()))
                .colors(colorMap.getRight())
                .build());

        return resp;
    }
}
