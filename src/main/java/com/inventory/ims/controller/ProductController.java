package com.inventory.ims.controller;

import com.inventory.ims.model.Product;
import com.inventory.ims.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 🟢 ADMIN: Show Add Product Form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "add_product";
    }

    // 🟢 ADMIN: Handle Add Product
    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "add_product";
        }
        productService.saveProduct(product);
        return "redirect:/products/view";
    }

    // 🟢 ADMIN: View with Edit/Delete
    @GetMapping("/view")
    public String viewAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "view_products";
    }

    // 🔵 USER: View Only
    @GetMapping("/user/view")
    public String viewProductsForUser(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "user_view_products";
    }

    // 🟢 ADMIN: Show Edit Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products/view";
        }
        model.addAttribute("product", product);
        return "edit_product";
    }

    // 🟢 ADMIN: Handle Edit
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable int id,
                                @ModelAttribute("product") @Valid Product updatedProduct,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "edit_product";
        }
        updatedProduct.setId(id);
        productService.saveProduct(updatedProduct);
        return "redirect:/products/view";
    }

    // 🟢 ADMIN: Delete
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return "redirect:/products/view";
    }
}
