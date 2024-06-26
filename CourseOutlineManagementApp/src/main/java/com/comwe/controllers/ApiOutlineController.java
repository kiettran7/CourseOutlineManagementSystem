/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.comwe.controllers;

import com.comwe.pojo.DTOs.OutlineDTO;
import com.comwe.pojo.User;
import com.comwe.services.DocumentService;
import com.comwe.services.FileStorageService;
import com.comwe.services.OutlineService;
import com.comwe.services.PdfService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lahon
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiOutlineController {

    @Autowired
    private OutlineService outlineService;

    @GetMapping(path = "/getOutlines/", produces = {
        MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<List<OutlineDTO>> list(@RequestParam Map<String, String> params) {
        List<OutlineDTO> outlines = this.outlineService.getOutlines(params);
        return new ResponseEntity<>(outlines, HttpStatus.OK);
    }

    @PostMapping(path = "/add-outline/", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<Object> addOutline(@RequestParam Map<String, String> params) {
        System.out.println("PARAM DO LAAAAA: " + params.keySet());

        if (this.outlineService.updateOutline(params) == true) {
            System.out.println("thanh cong");
        } else {
            System.out.println("That bai");
        }

        return new ResponseEntity<>("abc", HttpStatus.OK);
    }

    @GetMapping(path = "/getOutlines/{outlineId}/", produces = {
        MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<Object> retrieve(@PathVariable(value = "outlineId") int id) {
        return new ResponseEntity<>(this.outlineService.getOutlineById(id), HttpStatus.OK);
    }

    @GetMapping("/accept-outline/{outlineId}/")
    @ResponseStatus(HttpStatus.OK)
    public void acceptOutline(@PathVariable(value = "outlineId") int id) {
        this.outlineService.accept(id);
    }

    @GetMapping("/get-outline-doc-url/{outlineId}/")
    @ResponseStatus(HttpStatus.OK)
    public String getOutlineDocumentUrl(@PathVariable(value = "outlineId") int id) {
        return this.outlineService.getDocumentUrl(id);
    }

    @GetMapping(path = "/get-downloaded-outline-document/{userId}/", produces = {
        MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<List<Object>> getDownloadedOutlineDocument(@PathVariable(value = "userId") int userId) {
        return new ResponseEntity<>(this.outlineService.getDownoadedOutlineDocument(userId), HttpStatus.OK);
    }

    @GetMapping(path = "/get-pre-subjects/{outlineId}/", produces = {
        MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<List<Object>> getPrerequisiteSubjects(@PathVariable(value = "outlineId") int outlineId) {
        return new ResponseEntity<>(this.outlineService.getPrerequisiteSubjects(outlineId), HttpStatus.OK);
    }

}
