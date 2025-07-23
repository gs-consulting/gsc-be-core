package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import jp.co.goalist.gsc.utils.JsonColumnConverter;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Cacheable
@NoArgsConstructor
@AllArgsConstructor
@Cache(region = "operatorProjectCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_projects")
public class OperatorProject extends BaseEntity  {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column
    private String projectNumber;

    @Column
    private String projectName;

    @ManyToOne
    private MasterStatus status;

    @ManyToOne
    private OperatorBranch branch;

    @ManyToOne
    private OperatorStore store;

    @Column
    private LocalDate deadline;

    @Column
    private String workingHours1;

    @Column
    private String workingHours2;

    @Column
    private String workingHours3;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonColumnConverter.class)
    private List<String> workingDays;

    @ManyToOne
    private MasterStatus occupation;

    @Column
    private String salaryType;

    @Column
    private Integer salaryAmount;

    @Column(columnDefinition = "text")
    private String salaryNotes;

    @ManyToOne
    private MasterStatus employmentType;

    @Column
    private Integer recruitingNumber;

    @Column
    private String genderRestriction;

    @Column
    private Integer minAge;

    @Column
    private Integer maxAge;

    @Column
    private String notHiringCondition;

    @ManyToOne
    private MasterStatus workingPeriod;

    @Column
    private LocalDate desiredStartDate;

    @Column(columnDefinition = "text")
    private String jobDescription;

    @Column(columnDefinition = "text")
    private String remarks;

    @ManyToOne
    private MasterStatus interviewVenue;

    @Column
    private LocalDate employmentPeriodStart;

    @Column
    private LocalDate employmentPeriodEnd;

    @Column
    private Boolean isShiftSystem;

    @Column
    private String experienceStatus;

    @Column(columnDefinition = "text")
    private String portraits;

    private String qualifications;

    private String qualificationNotes;

    @ManyToOne
    private Prefecture prefecture;

    @ManyToOne
    private City city;

    @Column
    private String ward;

    @Column(columnDefinition = "text")
    private String workingHoursNotes;

    private String holidays;

    @Column(columnDefinition = "text")
    private String benefits;

    @Column(columnDefinition = "text")
    private String abAdjustment;

    @Column
    private Integer goalApply;

    @Column
    private Integer goalInterview;

    @Column
    private Integer goalOffer;

    @Column
    private Integer goalAgreement;

    @Column(columnDefinition = "text")
    private String memo;

    @Column
    private String groupedBy;

    @Column
    private boolean isSummarized = false;

    @Column
    private String parentId;

    @Column
    private String permission;

    @ManyToMany
    @JoinTable(
            name = "operator_advertisement_linkings",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "advertisement_id"))
    private List<OperatorAdvertisement> advertisements = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "operator_project_linkings",
            joinColumns = @JoinColumn(name = "project1_id"),
            inverseJoinColumns = @JoinColumn(name = "project2_id"))
    private List<OperatorProject> linkingProjects = new ArrayList<>();

    public void setLinkingProjects(List<OperatorProject> linkingProjects) {
        if ( this.getLinkingProjects() != null ) {
            if ( linkingProjects != null ) {
                this.getLinkingProjects().clear();
                this.linkingProjects = linkingProjects;
            }
            else {
                this.linkingProjects = new ArrayList<>();
            }
        }
        else {
            if ( linkingProjects != null ) {
                this.linkingProjects = linkingProjects;
            }
        }
    }
}
