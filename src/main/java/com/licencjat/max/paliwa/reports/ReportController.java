package com.licencjat.max.paliwa.reports;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private ReportManager reportManager;

    public ReportController(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @GetMapping("/all")
    public String getAllReports(Model model) {
        Iterable<Report> reports = reportManager.findAll();
        model.addAttribute("reports", reports);
        return "reports";
    }


}
