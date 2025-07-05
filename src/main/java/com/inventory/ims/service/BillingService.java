package com.inventory.ims.service;

import com.inventory.ims.model.Bill;
import com.inventory.ims.model.BillItem;
import com.inventory.ims.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingService {

    @Autowired
    private BillRepository billRepository;

    public Bill createBill(Bill bill) {
        if (bill.getItems() != null) {
            for (BillItem item : bill.getItems()) {
                item.setBill(bill); // set the reverse reference
            }
        }
        return billRepository.save(bill);
    }

    public Bill getBillById(int id) {
        return billRepository.findById(id).orElse(null);
    }

    // ✅ NEW: Get all bills for view
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }
}
