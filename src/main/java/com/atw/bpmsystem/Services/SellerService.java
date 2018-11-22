package com.atw.bpmsystem.Services;

import com.atw.bpmsystem.Entities.Seller;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SellerService {

    /**添加销售商
     * 1.传入参数，界面上的所有销售商字段（参考Seller）
     * 2.完成销售商的添加操作
     * @param
     * @return
     */
    public Map<String, Object> addSeller(Seller seller);

    /**删除销售商
     * 1.传入参数idList
     * 2.完成销售商的删除操作
     * @param idList
     * @return
     */
    public Map<String, Object> deleteSeller(List<Map<String,Integer>> idList);

    /**修改销售商
     * 1.传入参数，sellerId以及界面上的所有销售商字段（参考Seller）
     * 2.完成销售商的修改操作
     * @param seller
     * @return
     */
    public Map<String, Object> modifySeller(Seller seller);

    /**查询所有销售商
     * 1.传入参数为空
     * 2.完成查询所有销售商的操作
     * @return
     */
    public Map<String, Object> findAllSeller();

}
