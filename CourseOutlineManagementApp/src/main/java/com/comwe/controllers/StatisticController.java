/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.comwe.controllers;

import com.comwe.services.OutlineReportService;
import com.comwe.services.StatisticService;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author lahon
 */
@Controller
public class StatisticController {
    @Autowired
    private OutlineReportService outlineReportService;
    
    @Autowired
    private StatisticService statisticService;
    
    @GetMapping("/outlines-statistic/")
    public String outlineStatistic(@RequestParam Map<String, String> params, Model model) {
        String year = params.getOrDefault("year", String.valueOf(LocalDate.now().getYear()));
        String period = params.getOrDefault("period", "MONTH");
        
        String yearActive = params.getOrDefault("yearActive", String.valueOf(LocalDate.now().getYear()));
        String periodActive = params.getOrDefault("periodActive", "MONTH");
        
        System.out.println("PARAMSSSS: " + params.get("facultyActive"));
        model.addAttribute("outlineReport", this.outlineReportService.getOutlineCompletionStatistics("", params.get("facultyActive")));
        model.addAttribute("outlineStats", this.statisticService.outlineSaleStatistic(params));
        return "statistic";
    }
}
