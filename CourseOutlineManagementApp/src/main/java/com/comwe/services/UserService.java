/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.comwe.services;

import com.comwe.pojo.DTOs.UserDTO;
import com.comwe.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lahon
 */
public interface UserService extends UserDetailsService {
    User getUserByUsername(String username);
    boolean authUser(String username, String password);
    List<UserDTO> getNonAdminUsers(Map<String, String> params);
    void userApprove(int id);
    User getUserById(int id);
    User addUser(Map<String, String> params, MultipartFile avatar);
    User updateUser(Map<String, String> params, MultipartFile avatar);
    User getCurrentLoginUser();
}
