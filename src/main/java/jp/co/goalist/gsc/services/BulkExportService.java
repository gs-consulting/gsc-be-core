package jp.co.goalist.gsc.services;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.dtos.csv.ApplicantCSVItemsDto;
import jp.co.goalist.gsc.dtos.csv.ProjectCSVItemsDto;
import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.CSVException;
import jp.co.goalist.gsc.gen.dtos.ApplicantSearchDto;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.gen.dtos.ProjectSearchBoxDto;
import jp.co.goalist.gsc.services.criteriaBuilder.ApplicantCriteriaBuilder;
import jp.co.goalist.gsc.services.criteriaBuilder.ProjectCriteriaBuilder;
import jp.co.goalist.gsc.utils.CSVUtils;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static jp.co.goalist.gsc.common.Constants.*;
import static jp.co.goalist.gsc.mappers.ApplicantMapper.APPLICANT_MAPPER;
import static jp.co.goalist.gsc.mappers.ProjectMapper.PROJECT_MAPPER;

@Slf4j
@Service
@AllArgsConstructor
public class BulkExportService {

    private final UtilService utilService;
    private final ProjectCriteriaBuilder projectCriteriaBuilder;
    private final ApplicantCriteriaBuilder applicantCriteriaBuilder;

    public static Writer getWriter(ServletOutputStream outputStream) throws IOException {
        // first create an array for the Byte Order Mark
        final byte[] bom = new byte[] { (byte) 239, (byte) 187, (byte) 191 };
        outputStream.write(bom);
        return new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }

    public static void downloadProjects(Stream<ProjectCSVItemsDto> projectList, Writer writer) throws IOException {
        writer.write(BOM_CHARACTER); // Write BOM
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setEscape('\\')
                .setQuoteMode(QuoteMode.NONE)
                .build();
        CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);
        csvPrinter.printRecord(Arrays.stream(PROJECT_EXPORT_HEADERS_LIST).toList()); // Header Record
        final AtomicInteger[] i = {new AtomicInteger()};
        // Print records
        projectList.forEach(record -> {
            try {
                List<String> workingDays = null;

                if (Objects.nonNull(record.getWorkingDays())) {
                    workingDays = GeneralUtils.fromJsonToList(record.getWorkingDays(), String.class);
                }
                String[] wh1 = Objects.nonNull(record.getWorkingHours1())
                        ? record.getWorkingHours1().split(Constants.WORKING_HOUR_SEPARATOR)
                        : null;
                String[] wh2 = Objects.nonNull(record.getWorkingHours2())
                        ? record.getWorkingHours2().split(Constants.WORKING_HOUR_SEPARATOR)
                        : null;
                String[] wh3 = Objects.nonNull(record.getWorkingHours3())
                        ? record.getWorkingHours3().split(Constants.WORKING_HOUR_SEPARATOR)
                        : null;
                csvPrinter.print(CSVUtils.addQuote(record.getProjectNumber()));
                csvPrinter.print(CSVUtils.addQuote(record.getProjectName()));
                csvPrinter.print(record.getStatusId());
                csvPrinter.print(record.getPrefectureId());
                csvPrinter.print(record.getCityId());
                csvPrinter.print(CSVUtils.addQuote(record.getWard()));
                csvPrinter.print(CSVUtils.addQuote(record.getBranchId()));
                csvPrinter.print(CSVUtils.addQuote(record.getStoreId()));
                csvPrinter.print(Objects.nonNull(record.getDeadline()) 
                        ? CSVUtils.addQuote(dateStrFormatter.format(record.getDeadline()))
                        : null);
                csvPrinter.print(Objects.nonNull(wh1) ? CSVUtils.addQuote(wh1[0]) : null);
                csvPrinter.print(Objects.nonNull(wh1) ? CSVUtils.addQuote(wh1[1]) : null);
                csvPrinter.print(Objects.nonNull(wh2) ? CSVUtils.addQuote(wh2[0]) : null);
                csvPrinter.print(Objects.nonNull(wh2) ? CSVUtils.addQuote(wh2[1]) : null);
                csvPrinter.print(Objects.nonNull(wh3) ? CSVUtils.addQuote(wh3[0]) : null);
                csvPrinter.print(Objects.nonNull(wh3) ? CSVUtils.addQuote(wh3[1]) : null);
                String workingDate = Objects.nonNull(workingDays) ? String.join("|", workingDays) : null;
                csvPrinter.print(CSVUtils.addQuote(workingDate));
                csvPrinter.print(record.getOccupationId());
                csvPrinter.print(CSVUtils.addQuote(record.getSalaryType()));
                csvPrinter.print(record.getSalaryAmount());
                csvPrinter.print(CSVUtils.addQuote(record.getSalaryNotes()));
                csvPrinter.print(record.getEmploymentTypeId());
                csvPrinter.print(record.getRecruitingNumber());
                csvPrinter.print(CSVUtils.addQuote(record.getGenderRestriction()));
                csvPrinter.print(record.getMinAge());
                csvPrinter.print(record.getMaxAge());
                csvPrinter.print(CSVUtils.addQuote(record.getNotHiringCondition()));
                csvPrinter.print(record.getWorkingPeriodId());
                csvPrinter.print(Objects.nonNull(record.getDesiredStartDate())
                        ? CSVUtils.addQuote(dateStrFormatter.format(record.getDesiredStartDate()))
                        : null);
                csvPrinter.print(CSVUtils.addQuote(record.getJobDescription()));
                csvPrinter.print(CSVUtils.addQuote(record.getRemarks()));
                csvPrinter.print(record.getInterviewVenueId());
                csvPrinter.print(Objects.nonNull(record.getEmploymentPeriodStart())
                        ? CSVUtils.addQuote(dateStrFormatter.format(record.getEmploymentPeriodStart()))
                        : null);
                csvPrinter.print(Objects.nonNull(record.getEmploymentPeriodEnd())
                        ? CSVUtils.addQuote(dateStrFormatter.format(record.getEmploymentPeriodEnd()))
                        : null);
                csvPrinter.print(Objects.nonNull(record.getIsShiftSystem()) 
                        ? (record.getIsShiftSystem() ? 1 : 0)
                        : null);
                csvPrinter.print(CSVUtils.addQuote(record.getExperienceStatus()));
                csvPrinter.print(CSVUtils.addQuote(record.getPortraits()));
                String qualificationIds = Objects.nonNull(record.getQualifications())
                        ? record.getQualifications().replace(",", "|")
                        : null;
                csvPrinter.print(CSVUtils.addQuote(qualificationIds));
                csvPrinter.print(CSVUtils.addQuote(record.getQualificationNotes()));
                csvPrinter.print(CSVUtils.addQuote(record.getWorkingHoursNotes()));
                csvPrinter.print(CSVUtils.addQuote(record.getHolidays()));
                csvPrinter.print(CSVUtils.addQuote(record.getBenefits()));
                csvPrinter.print(CSVUtils.addQuote(record.getAbAdjustment()));
                csvPrinter.print(CSVUtils.addQuote(record.getMemo()));
                csvPrinter.print(record.getGoalApply());
                csvPrinter.print(record.getGoalInterview());
                csvPrinter.print(record.getGoalOffer());
                csvPrinter.print(record.getGoalAgreement());
                csvPrinter.println();
                i[0].addAndGet(1);
            } catch (IOException e) {
                log.debug("Exception " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                log.debug("Exception " + e.getMessage());
                throw new RuntimeException(e);
            }
        });

        log.info("Total data rows downloaded: " + i[0]);
        csvPrinter.flush();
        csvPrinter.close();
        writer.flush();
        writer.close();
    }

    public static void downloadApplicants(Stream<ApplicantCSVItemsDto> applicantList, Writer writer) throws IOException {
        writer.write(BOM_CHARACTER); // Write BOM

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecord(Arrays.stream(APPLICANT_EXPORT_HEADERS_LIST).toList()); // Header Record
        final AtomicInteger[] i = {new AtomicInteger()};
        // Print records
        applicantList.forEach(record -> {
            try {
                csvPrinter.print(record.getId());
                csvPrinter.print(record.getFullName());
                csvPrinter.print(record.getFuriganaName());
                csvPrinter.print(Objects.nonNull(record.getBirthday()) ? dateStrFormatter.format(record.getBirthday()) : null);
                csvPrinter.print(record.getGender());
                csvPrinter.print(record.getProjectId());
                csvPrinter.print(record.getEmail());
                csvPrinter.print(record.getTel());
                csvPrinter.print(record.getPostCode());
                csvPrinter.print(record.getPrefecture());
                csvPrinter.print(record.getCity());
                csvPrinter.print(record.getHomeAddress());
                csvPrinter.print(record.getOccupation());
                csvPrinter.print(record.getSelectionStatusId());
                csvPrinter.print(Objects.nonNull(record.getQualificationIds()) ? record.getQualificationIds().replace(",", "|") : null);
                csvPrinter.print(Objects.nonNull(record.getExperienceIds()) ? record.getExperienceIds().replace(",", "|") : null);
                csvPrinter.print(record.getPicId());
                csvPrinter.print(Objects.nonNull(record.getJoinedDate()) ? dateStrFormatter.format(record.getJoinedDate()) : null);
                csvPrinter.print(record.getMemo());
                csvPrinter.print(record.getMediaId());
                csvPrinter.print(record.getIsCrawledData() ? "有" : "無");
                csvPrinter.println();
                i[0].addAndGet(1);
            } catch (IOException e) {
                log.debug("Exception " + e.getMessage());
                e.printStackTrace();
            }
        });

        log.info("Total data rows downloaded: " + i[0]);
        csvPrinter.flush();
        csvPrinter.close();
        writer.flush();
        writer.close();
    }

    @Transactional
    public void downloadProjectCSV(ProjectSearchBoxDto projectSearchBoxDto, HttpServletResponse response) {
        try {
            Account account = GeneralUtils.getCurrentUser();

            Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
            downloadProjects(projectCriteriaBuilder.findAllProjectsForCSV(
                            parentInfo.getLeft(),
                            parentInfo.getRight(),
                            PROJECT_MAPPER.toProjectSearchBoxRequest(projectSearchBoxDto)),
                    getWriter(response.getOutputStream()));
        } catch (IOException e) {
            throw new CSVException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.CSV_EXPORT_ERROR.getStatusCode())
                    .message(String.format(ErrorMessage.CSV_EXPORT_ERROR.getMessage(), TargetName.PROJECT))
                    .build());
        }
    }

    @Transactional
    public void downloadApplicantCSV(ApplicantSearchDto applicantSearchDto, HttpServletResponse response) {
        try {
            Account account = GeneralUtils.getCurrentUser();

            Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
            downloadApplicants(applicantCriteriaBuilder.findAllApplicantsForCSV(
                            parentInfo.getLeft(),
                            parentInfo.getRight(),
                            APPLICANT_MAPPER.toApplicantSearchBoxRequest(applicantSearchDto)
                    ),
                    getWriter(response.getOutputStream()));
        } catch (IOException e) {
            throw new CSVException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.CSV_EXPORT_ERROR.getStatusCode())
                    .message(String.format(ErrorMessage.CSV_EXPORT_ERROR.getMessage(), TargetName.PROJECT))
                    .build());
        }
    }
}
