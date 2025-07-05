package com.inventory.ims.controller;

import com.inventory.ims.model.Bill;
import com.inventory.ims.model.BillItem;
import com.inventory.ims.model.Product;
import com.inventory.ims.service.BillingService;
import com.inventory.ims.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BillUIController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BillingService billingService;

    @GetMapping("/bills/create")
    public String showBillingForm(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "billing";
    }

    @PostMapping("/bills/save")
    public String saveBill(
            @RequestParam String customerName,
            @RequestParam List<Integer> productIds,
            @RequestParam List<Integer> quantities,
            Model model
    ) {
        double total = 0.0;
        List<BillItem> billItems = new ArrayList<>();

        for (int i = 0; i < productIds.size(); i++) {
            Product product = productService.getProductById(productIds.get(i));
            int qty = quantities.get(i);

            // ✅ Check stock
            if (product.getQuantity() < qty) {
                model.addAttribute("error", "Not enough stock for: " + product.getName());
                model.addAttribute("products", productService.getAllProducts());
                return "billing";
            }

            // ✅ Reduce stock
            product.setQuantity(product.getQuantity() - qty);
            productService.saveProduct(product); // persist updated stock

            double price = product.getPrice() * qty;

            BillItem item = new BillItem();
            item.setProductId(product.getId());
            item.setQuantity(qty);
            item.setPrice(price);
            item.setBill(null); // will be set later
            billItems.add(item);

            total += price;
        }

        Bill bill = new Bill();
        bill.setCustomerName(customerName);
        bill.setUserId(1); // TODO: Replace with logged-in user's ID
        bill.setTotal(total);
        bill.setDate(LocalDateTime.now());

        for (BillItem item : billItems) {
            item.setBill(bill);
        }

        bill.setItems(billItems);
        Bill savedBill = billingService.createBill(bill);
        model.addAttribute("bill", savedBill);

        return "bill_details";
    }

    // ✅ View All Bills
    @GetMapping("/bills/all")
    public String viewAllBills(Model model) {
        List<Bill> bills = billingService.getAllBills();
        model.addAttribute("bills", bills);
        return "view_bills"; // Make sure this template exists
    }
}
