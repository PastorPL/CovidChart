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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class GitHubService {

    private final Logger log = LoggerFactory.getLogger(GitHubService.class);

    private EntryRepository entryRepository;

    public GitHubService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public void updateEntriesFromGithub() {
        final Entry lastUpdateEntry = this.entryRepository.findFirstByOrderByLastUpdateDesc();
        if (lastUpdateEntry != null && lastUpdateEntry.getLastUpdate().isEqual(LocalDate.now())) {
            return;
        }
        this.entryRepository.deleteAll();
        this.getEntries();
    }

    private void getEntries() {
        try {
            final String baseURL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
            URL url = new URL(baseURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == 200) {
                try (InputStreamReader streamReader = new InputStreamReader(connection.getInputStream())) {
                    BufferedReader in = new BufferedReader(streamReader);
                    Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
                    for (CSVRecord record : records) {
                        this.processCSVGlobal(record);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage());
        }
    }

    private void processCSVGlobal(CSVRecord record) {
        final Map<String, String> valueMap = record.toMap();

        final List<String> standardHeaders = Arrays.asList("Province/State", "Country/Region", "Lat", "Long");
        final String province = valueMap.get(standardHeaders.get(0));
        final String country = valueMap.get(standardHeaders.get(1));
        final Double lat = Double.parseDouble(valueMap.get(standardHeaders.get(2)));
        final Double lon = Double.parseDouble(valueMap.get(standardHeaders.get(3)));

        final DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("M/d/y");

        List<Entry> entryList = new ArrayList<>();
        for (String key : valueMap.keySet()) {
            if (standardHeaders.contains(key)) {
                continue;
            }
            final Entry entry = new Entry();
            entry.setProvince(province);
            entry.setCountry(country);
            entry.setLat(lat);
            entry.setLon(lon);
            final LocalDate localDate = LocalDate.parse(key, localDateFormat);
            entry.setLastUpdate(localDate);
            entry.setConfirmed(Integer.parseInt(valueMap.get(key)));
            entryList.add(entry);
        }

        this.entryRepository.insert(entryList);
    }

}
