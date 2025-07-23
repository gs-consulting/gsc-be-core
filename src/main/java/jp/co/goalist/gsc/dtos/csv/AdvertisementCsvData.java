package jp.co.goalist.gsc.dtos.csv;

import jp.co.goalist.gsc.utils.validation.CsvColumn;

import static jp.co.goalist.gsc.common.Constants.*;

public class AdvertisementCsvData {

    @CsvColumn(name = "広告名", required = true)
    public String advertisementName;

    @CsvColumn(name = "広告媒体ID", required = true)
    public String mediaId;

    @CsvColumn(name = "区分", required = true)
    public String category;

    @CsvColumn(name = "掲載開始日", patterns = CSV_DATE_RULE, required = true)
    public String publishingStartDate;

    @CsvColumn(name = "掲載終了日", patterns = CSV_DATE_RULE)
    public String publishingEndDate;

    @CsvColumn(name = "金額", patterns = CSV_NUMERIC_RULE)
    public Integer amount;

    @CsvColumn(name = "メモ")
    public String memo;
}
