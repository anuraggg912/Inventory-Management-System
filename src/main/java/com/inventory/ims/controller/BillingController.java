package com.inventory.ims.controller;

import com.inventory.ims.dto.BillRequestDTO;
import com.inventory.ims.dto.BillRequestDTO.BillItemDTO;
import com.inventory.ims.model.Bill;
import com.inventory.ims.model.BillItem;
import com.inventory.ims.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/api/bills")
public class BillingController {

    @Autowired
    private BillingService billingService;

    // ✅ Create bill with nested items using DTO
    @PostMapping("/create")
    public Bill createBill(@RequestBody BillRequestDTO dto) {
        Bill bill = new Bill();
        bill.setCustomerName(dto.getCustomerName());
        bill.setUserId(dto.getUserId());
        bill.setTotal(dto.getTotal());

        List<BillItem> itemList = new ArrayList<>();
        for (BillItemDTO itemDTO : dto.getItems()) {
            BillItem item = new BillItem();
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setBill(bill); // 🔁 Set reverse reference
            itemList.add(item);
        }

        bill.setItems(itemList);

        return billingService.createBill(bill);
    }

    @GetMapping("/{id}")
    public Bill getBill(@PathVariable int id) {
        return billingService.getBillById(id);
    }
}
