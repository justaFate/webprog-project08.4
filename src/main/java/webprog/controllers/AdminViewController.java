package webprog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminViewController {

    @GetMapping("/")
    public String index() {
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories")
    public String categoriesPage() {
        return "categories/ajax";
    }

    @GetMapping("/admin/products")
    public String productsPage() {
        return "products/ajax";
    }
}

