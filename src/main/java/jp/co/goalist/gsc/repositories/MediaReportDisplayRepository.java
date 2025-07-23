package jp.co.goalist.gsc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.goalist.gsc.entities.MediaReportDisplay;

@Repository
public interface MediaReportDisplayRepository extends JpaRepository<MediaReportDisplay, String> {

    @Query(value = """
        FROM media_report_displays WHERE parentId = :parentId
        AND ((:oemGroupId IS NULL AND oemGroupId IS NULL) OR oemGroupId = :oemGroupId)
        ORDER BY flowTypeId ASC
        """)
    List<MediaReportDisplay> getMediaReportSetting(String parentId, String oemGroupId);

}
