package com.blog.controller;


import com.blog.exception.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {


    @GetMapping("/")
    public String index() throws NotFoundException {
        return "index";
    }

    @GetMapping("/blog")
    public String blog() throws NotFoundException {
        return "blog";
    }


    @GetMapping("/tags")
    public String tags() throws NotFoundException {
        return "tags";
    }

    @GetMapping("/archives")
    public String archives() throws NotFoundException {
        return "archives";
    }

    @GetMapping("/types")
    public String types() throws NotFoundException {
        return "types";
    }

    @GetMapping("/about")
    public String about() throws NotFoundException {
        return "about";
    }


}
