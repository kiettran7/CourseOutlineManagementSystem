/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.comwe.services.impl;

import com.comwe.pojo.Lecturer;
import com.comwe.repositories.LecturerRepositoryQuery;
import com.comwe.services.LecturerServiceQuery;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lahon
 */
@Service
public class LecturerServiceQueryImpl implements LecturerServiceQuery {

    @Autowired
    private LecturerRepositoryQuery lecturerRepoQuery;
    
    @Override
    public List<Object> getLecturers(Map<String, String> params) {
        return this.lecturerRepoQuery.getLecturers(params);
    }

    @Override
    public Lecturer getLecturerByUserId(int userId) {
        return this.lecturerRepoQuery.getLecturerByUserId(userId);
    }
    
}
