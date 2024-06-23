/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.comwe.services;

import com.comwe.pojo.DTOs.CommentDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kitj3
 */
public interface CommentService {
    List<CommentDTO> getComments(Map<String, String> params, String outlineId);
    boolean addComment(Map<String, String> params);
}
