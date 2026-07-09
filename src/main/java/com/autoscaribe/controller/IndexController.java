/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoscaribe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 *
 * @author Isaac
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index"; // Buscará la plantilla index en templates
    }
}