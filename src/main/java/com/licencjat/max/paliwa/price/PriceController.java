package com.licencjat.max.paliwa.price;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prices")
public class PriceController {

    private PriceManager priceManager;

    public PriceController(PriceManager priceManager) {
        this.priceManager = priceManager;
    }

    @GetMapping("/all")
    public String getAllPrices(Model model) {
        Iterable<Price> prices = priceManager.findByOrderByDatetimeDesc();
        model.addAttribute("prices", prices);
        return "prices";
    }
}
