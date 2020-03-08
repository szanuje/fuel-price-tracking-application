package com.licencjat.max.paliwa.reports;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportManager {

    ReportRepository reportRepository;

    public ReportManager(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void save(Report report) {
        reportRepository.save(report);
    }

    public void saveAll(List<Report> reports) {
        reportRepository.saveAll(reports);
    }

    public void deleteAll() {
        reportRepository.deleteAll();
    }

    public Iterable<Report> findAll() {
        return reportRepository.findAll();
    }
}
