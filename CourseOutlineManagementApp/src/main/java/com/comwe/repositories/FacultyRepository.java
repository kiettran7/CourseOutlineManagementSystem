/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.comwe.repositories;

import com.comwe.pojo.Faculty;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kitj3
 */
public interface FacultyRepository {
    List<Faculty> getFaculties(Map<String, String> params);
    Faculty getFacultyById(int id);
}
