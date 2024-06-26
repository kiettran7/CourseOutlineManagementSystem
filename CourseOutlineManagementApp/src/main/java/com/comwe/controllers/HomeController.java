/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.comwe.controllers;

import com.comwe.pojo.Subject;
import com.comwe.services.AcademicYearService;
import com.comwe.services.FacultyService;
import com.comwe.services.LecturerServiceQuery;
import com.comwe.services.OutlineReportService;
import com.comwe.services.OutlineService;
import com.comwe.services.SubjectService;
import com.comwe.services.UserService;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author lahon
 */
@Controller
@PropertySource("classpath:configs.properties")
public class HomeController {

    @Autowired
    private OutlineService outlineService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private Environment env;

    @Autowired
    private FacultyService facultyService;
    
    @Autowired
    private LecturerServiceQuery lecturerServiceQuery;

    @Autowired
    private SubjectService subjectService;
    
//    @Autowired
//    private OutlineReportService outlineReportService;

    private static final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    @RequestMapping("/")
    public String index(@RequestParam Map<String, String> params, Model model) {
        boolean needsRedirect = false;

        // Kiểm tra và thiết lập pageOutline nếu chúng không tồn tại
        if (!params.containsKey("pageOutline")) {
            params.put("pageOutline", "1");
            needsRedirect = true;
        }

        int totalPageOutline = (int) Math.ceil((double) this.outlineService.getOutlines(params).size() / Integer.parseInt(this.env.getProperty("pageSize")));
        if (!params.containsKey("page")) {
            params.put("page", params.get("pageOutline"));
        }
        model.addAttribute("pageSizeOutline", totalPageOutline);
        model.addAttribute("outlines", this.outlineService.getOutlines(params));
        model.addAttribute("faculties", this.facultyService.getFaculties(params));
        model.addAttribute("lecturers", this.lecturerServiceQuery.getLecturers(params));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object username = null;

        if (authentication != null && authentication.isAuthenticated() && !authenticationTrustResolver.isAnonymous(authentication)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            }

            model.addAttribute("username", username);
        }

        return "index";
    }

    @GetMapping("/outlines/{outlineId}/")
    public String outlineDetail(@PathVariable(value = "outlineId") int outlineId, Model model) {
        model.addAttribute("outline", this.outlineService.getOutlineById(outlineId).get(0));
        return "outlineDetail";
    }

    @GetMapping("/users-manager/")
    public String lecturerManagement(@RequestParam Map<String, String> params, Model model, RedirectAttributes redirectAttributes) {
        boolean needsRedirect = false;

        // Kiểm tra và thiết lập pageLecturer và pageStudent nếu chúng không tồn tại
        if (!params.containsKey("pageLecturer")) {
            params.put("pageLecturer", "1");
            needsRedirect = true;
        }
        if (!params.containsKey("pageStudent")) {
            params.put("pageStudent", "1");
            needsRedirect = true;
        }

        // Nếu cần chuyển hướng, thêm các tham số vào RedirectAttributes và chuyển hướng
        if (needsRedirect) {
            redirectAttributes.addAllAttributes(params);
            return "redirect:/users-manager/";
        }
        
        // filter của lecturer
        params.put("nameUser", params.get("nameOfLecturer"));
        params.put("emailuser", params.get("emailOfLecturer"));
        params.put("isActive", params.get("isActiveLecturer"));

        params.put("role", "ROLE_LECTURER");
        int totalPageLecturer = (int) Math.ceil((double) this.userService.getNonAdminUsers(params).size() / Integer.parseInt(this.env.getProperty("pageSizeUser")));
        params.put("page", params.get("pageLecturer"));
        model.addAttribute("pageSizeLecturer", totalPageLecturer);
        model.addAttribute("lecturers", this.userService.getNonAdminUsers(params));

        // filter của student
        params.replace("nameUser", params.get("nameOfStudent"));
        params.replace("emailuser", params.get("emailOfStudent"));
        params.replace("isActive", params.get("isActiveStudent"));
 
        params.replace("role", "ROLE_STUDENT");
        params.replace("page", null);
        int totalPageStudent = (int) Math.ceil((double) this.userService.getNonAdminUsers(params).size() / Integer.parseInt(this.env.getProperty("pageSizeUser")));
        params.replace("page", params.get("pageStudent"));
        model.addAttribute("pageSizeStudent", totalPageStudent);
        model.addAttribute("students", this.userService.getNonAdminUsers(params));
        return "user";
    }

    public void sendMail(String to, String subject, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setFrom("locla2405@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(mimeMessage);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email");
        }
    }

    @PostMapping("/users-manager/{userId}/")
    public String sendEmailForUser(@PathVariable(value = "userId") int userId,
            @RequestParam Map<String, String> params) {
        String to = params.get("to");
        String subject = params.get("subject");
        String content = params.get("content");

        if ((to != null && subject != null && content != null)
                && (!to.isEmpty() && !subject.isEmpty() && !content.isEmpty())) {
            sendMail(to, subject, content);
            return "redirect:/users-manager/";
        }

        return "redirect:/users-manager/{userId}/";
    }

    @GetMapping("/users-manager/{userId}/")
    public String userDetails(@PathVariable(value = "userId") int userId,
            Model model, Map<String, String> params) {
        model.addAttribute("user", this.userService.getUserById(userId));
        return "userDetails";
    }

    @Autowired
    private AcademicYearService academicYearService;

    @GetMapping("/outline-management/")
    public String outlineManage(@RequestParam Map<String, String> params, Model model) {

        model.addAttribute("faculties", this.facultyService.getFaculties(params));
        model.addAttribute("subjects", this.subjectService.getSubjects(params));

        String subjectId = params.get("subjectId");
        if (subjectId != null && !subjectId.isEmpty()) {
            return "forward:/outline-management/" + subjectId + "/";
        }
        return "outlineManage";
    }

    @RequestMapping(path = "/outline-management/{subjectId}/", method = {
        RequestMethod.GET,
        RequestMethod.POST
    })
    public String subjectDetails(@RequestParam Map<String, String> params,
            Model model, @PathVariable(value = "subjectId") int subjectId, HttpServletRequest request) {
        if (HttpMethod.POST.matches(request.getMethod())) {
            int lecturerId = Integer.parseInt(params.get("lecturerId"));
            int academicYear1 = Integer.parseInt(params.get("academicYearId1"));
            int academicYear2 = -1;
            try {
                academicYear2 = Integer.parseInt(params.get("academicYearId2"));
            } catch (Exception ex) {
                System.err.println(ex);
            }

            if (this.outlineService.checkOutlineExist(subjectId, academicYear1, academicYear2) == true) {
                this.outlineService.addOutline(lecturerId, subjectId, academicYear1, academicYear2);
                return "redirect:/outline-management/";
            } else {
                System.out.println("That baiiii");
            }

        }

        model.addAttribute("academicYears", this.academicYearService.getAcademicYears(params));
        params.put("role", "ROLE_LECTURER");
        model.addAttribute("lecturers", this.userService.getNonAdminUsers(params));

        return "subjectDetails";
    }

    @GetMapping("/generate-pdf/")
    public String generatePDF() {
        return "generatePDF";
    }
    
//    @GetMapping("/statistical/")
//    public String statistical(@RequestParam Map<String, String> params, Model model) {
//        model.addAttribute("outlineReport", this.outlineReportService.getOutlineCompletionStatistics());
//        
//        return "statistical";
//    }

    @GetMapping("/subjects-management/")
    public String subjectsManagement(Model model, @RequestParam Map<String, String> params, HttpServletRequest request) {
        params.keySet().forEach(k -> System.out.println(k + " la: " + params.get(k)));

        String[] faculties = request.getParameterValues("faculties");
        System.out.println("ten mon hoc la: " + request.getParameterValues("subjectName"));
        if (faculties != null) {
            this.subjectService.addSubject(params.get("subjectName"), faculties);
            Arrays.stream(faculties).forEach(id -> System.out.println(id));
        }
        model.addAttribute("faculties", this.facultyService.getFaculties(params));

        return "subject";
    }

    
}
