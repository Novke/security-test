package com.sandbox.ProductSecurity.controller;

import com.sandbox.ProductSecurity.model.Coupon;
import com.sandbox.ProductSecurity.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couponapi")
@CrossOrigin
public class CouponRestController {

    @Autowired
    CouponRepository couponRepository;

    //NEW
    @PostMapping("/coupons")
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon create(@RequestBody Coupon coupon){
        return couponRepository.save(coupon);
    }

    //NEW
    @GetMapping("/coupons/{code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Coupon getcoupon(@PathVariable("code") String code){
        return couponRepository.findByCode(code);
    }
}
