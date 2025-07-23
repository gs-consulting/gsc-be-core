package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "oemApplicantCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_applicants")
public class OemApplicant extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @ManyToOne
    private OemClientAccount parent;

    @Column
    private String oemGroupId;

    @Column
    private String fullName;

    @Column
    private String furiganaName;

    @Column
    private LocalDate birthday;

    @Column
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    private OemProject project;

    @Column
    private String email;

    @Column
    private String tel;

    @Column
    private String postCode;

    @ManyToOne
    private Prefecture prefecture;

    @Column
    private String city;

    @Column
    private String homeAddress;

    @Column
    private String occupation;

    @ManyToOne
    @JoinColumn(name = "selection_status_id")
    private SelectionStatus selectionStatus;

    @Column(columnDefinition = "mediumtext")
    private String qualificationIds;

    @Column(columnDefinition = "mediumtext")
    private String experienceIds;

    @ManyToOne
    private MasterMedia media;

    @Column
    private String manuscriptUrl;

    @Column
    private LocalDate hiredDate;

    @ManyToOne
    private OemClientAccount pic;

    @Column
    private String memo;

    @Column(name = "blacklist1_id")
    private String blacklist1;

    @Column(name = "blacklist2_id")
    private String blacklist2;

    @Column
    private Boolean isMailDuplicate = false;

    @Column
    private Boolean isTelDuplicate = false;

    @Column
    private Boolean isCrawledData = false;

    @Column
    private Boolean isUnread = false;

    @Column
    private String mediaApplicantId;

    @Column
    private LocalDateTime lstStatusChangeDateTime;

    @Column
    private LocalDateTime lstContactDateTime;

    @OneToMany(mappedBy = "applicant", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("interviewStartDate ASC")
    @ToString.Exclude
    private List<OemApplicantInterview> interviews = new ArrayList<>();

    public void setInterviews(List<OemApplicantInterview> interviews) {
        this.interviews.clear();
        if (Objects.nonNull(interviews)) {
            this.interviews.addAll(interviews);
        }
    }

    public void addToInterviews(OemApplicantInterview interview) {
        interview.setApplicant(this);
        this.interviews.add(interview);
    }

    public void editInterview(OemApplicantInterview newInterview) {
        Optional<OemApplicantInterview> currentInterview = this.interviews.stream()
                .filter(i -> i.getId().equals(newInterview.getId()))
                .findFirst();

        currentInterview.ifPresent(interview -> {
            interview.setInterviewStartDate(newInterview.getInterviewStartDate());
            interview.setInterviewEndDate(newInterview.getInterviewEndDate());
            interview.setPicId(newInterview.getPicId());
            interview.setMemo(newInterview.getMemo());
            interview.setCategory(newInterview.getCategory());
        });
    }

    public void removeEmptyInterviewsById(List<String> ids) {
        List<OemApplicantInterview> removed = this.interviews.stream()
                .filter(i -> {
                    boolean exists = ids.contains(i.getId());
                    if (!exists) {
                        i.setApplicant(null);
                    }
                    return exists;
                })
                .toList();
        setInterviews(removed);
    }
}
