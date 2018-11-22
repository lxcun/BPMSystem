package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Integer>{
    /**通过销售商名称查找销售商
     * @param sellerName
     * @return
     */
    public Seller findBySellerName(String sellerName);
}
