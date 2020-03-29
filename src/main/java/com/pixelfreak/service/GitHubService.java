package com.pixelfreak.service;

import com.pixelfreak.domain.Entry;
import com.pixelfreak.repository.EntryRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GitHubService {

    private final Logger log = LoggerFactory.getLogger(GitHubService.class);

    private EntryRepository entryRepository;

    public GitHubService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public void updateEntriesFromGithub()  {
        final Entry lastUpdateEntry = this.entryRepository.findFirstByOrderByLastUpdateDesc();
        this.log.debug("Updating...");
        if (lastUpdateEntry!=null && lastUpdateEntry.getLastUpdate().isEqual(LocalDate.now())) {
            return;
        }

        this.log.debug("Still Updating...");
        LocalDate startUpdateFrom;
        if (lastUpdateEntry == null) {
            startUpdateFrom = LocalDate.of(2020, Month.JANUARY, 22);
        } else {
            startUpdateFrom = lastUpdateEntry.getLastUpdate();
        }

        startUpdateFrom.datesUntil(LocalDate.now().plus(1, ChronoUnit.DAYS)).forEach(date -> this.getEntries(date).forEach(this.entryRepository::save));
    }

    private List<Entry> getEntries(LocalDate date) {
        this.log.debug("Updating for date: "+date.toString());
        try {
            final List<Entry> entryList = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            final String baseurl = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/" + date.format(formatter) + ".csv";
            URL url = new URL(baseurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == 200) {
                try (InputStreamReader streamReader = new InputStreamReader(connection.getInputStream())) {
                    BufferedReader in = new BufferedReader(streamReader);
                    Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
                    for (CSVRecord record : records) {
                        if(date.isEqual(LocalDate.of(2020, 3, 22))){
                            this.processCVSFileFrom2203(entryList, record);
                        } else if(date.isBefore(LocalDate.of(2020, 3, 22))) {
                            this.processCVSFileBefor2203(entryList, record);
                        } else {
                            this.processCVSFileAfter2203(entryList, record);
                        }

                    }
                }
            }
            return entryList;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    private void processCVSFileAfter2203(List<Entry> entryList, CSVRecord record) {
        final Map<String, String> valueMap = record.toMap();
        final Entry entry = new Entry();
        entry.setProvince(valueMap.get("Province_State"));
        entry.setCountry(valueMap.get("Country_Region"));
        entry.setConfirmed(Integer.parseInt(valueMap.get("Confirmed")));
        entry.setDeaths(Integer.parseInt(valueMap.get("Deaths")));
        entry.setRecovered(Integer.parseInt(valueMap.get("Recovered")));
        entry.setLat(Double.parseDouble(valueMap.get("Lat")));
        entry.setLon(Double.parseDouble(valueMap.get("Long_")));
        String lastUpdateString = valueMap.get("Last_Update");
        lastUpdateString = lastUpdateString.split(" ")[0];
        final DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate localDate = LocalDate.parse(lastUpdateString, localDateFormat);
        entry.setLastUpdate(localDate);
        entryList.add(entry);
    }

    private void processCVSFileFrom2203(List<Entry> entryList, CSVRecord record) {
        final Map<String, String> valueMap = record.toMap();
        final Entry entry = new Entry();
        entry.setProvince(valueMap.get("Province_State"));
        entry.setCountry(valueMap.get("Country_Region"));
        entry.setConfirmed(Integer.parseInt(valueMap.get("Confirmed")));
        entry.setDeaths(Integer.parseInt(valueMap.get("Deaths")));
        entry.setRecovered(Integer.parseInt(valueMap.get("Recovered")));
        entry.setLat(Double.parseDouble(valueMap.get("Lat")));
        entry.setLon(Double.parseDouble(valueMap.get("Long_")));
        String lastUpdateString = valueMap.get("Last_Update");
        lastUpdateString = lastUpdateString.split(" ")[0];
        final DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("MM/dd/yy");
        final LocalDate localDate = LocalDate.parse(lastUpdateString, localDateFormat);
        entry.setLastUpdate(localDate);
        entryList.add(entry);
    }

    private void processCVSFileBefor2203(List<Entry> entryList, CSVRecord record) {
        final Map<String, String> valueMap = record.toMap();
        final Entry entry = new Entry();
        entry.setProvince(valueMap.get("Province/State"));
        entry.setCountry(valueMap.get("Country/Region"));
        entry.setConfirmed(Integer.parseInt(valueMap.get("Confirmed")));
        entry.setDeaths(Integer.parseInt(valueMap.get("Deaths")));
        entry.setRecovered(Integer.parseInt(valueMap.get("Recovered")));
        entry.setLat(Double.parseDouble(valueMap.get("Latitude")));
        entry.setLon(Double.parseDouble(valueMap.get("Longitude")));
        String lastUpdateString = valueMap.get("Last Update");
        if (lastUpdateString.contains("T")) {
            lastUpdateString = lastUpdateString.split("T")[0];
            final DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-d");
            final LocalDate localDate = LocalDate.parse(lastUpdateString, localDateFormat);
            entry.setLastUpdate(localDate);
        } else {
            lastUpdateString = lastUpdateString.split(" ")[0];
            final DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            final LocalDate localDate = LocalDate.parse(lastUpdateString, localDateFormat);
            entry.setLastUpdate(localDate);
        }
        entryList.add(entry);
    }

}
