package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.Model.Report;
import be.uantwerpen.labplanner.Service.ReportService;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Controller
public class ReportController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    @PreAuthorize("hasAuthority('Console Access')")
    @RequestMapping(value="/reports/list", method= RequestMethod.GET)
    public String showReportList(final ModelMap model){
        model.addAttribute("allReports", reportService.findAll());
        return "/Reports/report-list";
    }

    @PreAuthorize("hasAuthority('Console Access')")
    @RequestMapping(value="/reports/{id}/delete",method = RequestMethod.GET)
    public String deleteReport(@PathVariable Long id, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        reportService.deleteById(id);
        model.clear();
        model.addAttribute("success", ResourceBundle.getBundle("messages",current).getString("delete.success"));
        model.addAttribute("allReports", reportService.findAll());
        return "/Reports/report-list";

    }

    @RequestMapping(value="/reports/put", method= RequestMethod.GET)
    public String createReport(final ModelMap model){
        model.addAttribute("report", new Report());
        return "Reports/report-form";
    }

    @RequestMapping(value={"/reports", "/reports/{id}"},
            method= RequestMethod.POST)
    public String addReport(@Valid Report report, BindingResult result,
                            final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        User currentUser =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        report.setCreator(currentUser);

        if(report.getTitle().length() == 0){
            model.addAttribute("allReports", reportService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.title"));
            return "Reports/report-form";
        }

        if(report.getDescription().length() == 0){
            model.addAttribute("allReports", reportService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.description"));
            return "Reports/report-form";
        }


        if(result.hasErrors()){
            model.addAttribute("allReports", reportService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.general"));

            return "Reports/report-form";
        }
        reportService.save(report);
        return "redirect:/home";
    }

}
