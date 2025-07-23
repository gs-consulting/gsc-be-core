package jp.co.goalist.gsc.services;

import jp.co.goalist.gsc.entities.MasterStatus;
import jp.co.goalist.gsc.repositories.MasterStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MasterStatusService {

    private final MasterStatusRepository masterStatusRepository;

    public Map<String, MasterStatus> setStatusMap(List<String> statuses,
                                                  List<String> occupations,
                                                  List<String> employmentStatues,
                                                  List<String> workingPeriods,
                                                  List<String> interviewVenues,
                                                  List<String> qualifications,
                                                  List<String> experiences,
                                                  String parentId,
                                                  String oemGroupId) {

        List<MasterStatus> data = masterStatusRepository.findMasterStatusForCSV(statuses,
                occupations,
                employmentStatues,
                workingPeriods,
                interviewVenues,
                qualifications,
                experiences,
                parentId,
                oemGroupId);

        return data.stream()
                .collect(Collectors.toMap(i -> i.getId().toString(), status -> status));

    }
}
