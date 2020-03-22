package com.pixelfreak.service;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {

public List<String> getFiles() throws IOException {
    final GitHub github = GitHub.connect();
    final GHRepository repo = github.getRepository("https://github.com/CSSEGISandData/COVID-19");
    List<GHContent> files = repo.getDirectoryContent("csse_covid_19_data/csse_covid_19_daily_reports");
    return files.stream().map(e->e.getName()).collect(Collectors.toList());
}

}
