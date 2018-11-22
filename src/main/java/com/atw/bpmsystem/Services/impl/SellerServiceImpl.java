package com.atw.bpmsystem.Services.impl;

import com.atw.bpmsystem.Entities.Seller;
import com.atw.bpmsystem.Repositories.SellerRepository;
import com.atw.bpmsystem.Services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@EnableTransactionManagement
@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerRepository sellerRepository;
    /**添加销售商
     * 1.传入参数，界面上的所有销售商字段（参考Seller）
     * 2.完成销售商的添加操作
     * @param
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> addSeller(Seller seller) {
        Map<String, Object> modelMap=new HashMap<>();
        if(seller.getSellerName()!=null){
        Seller seller1=sellerRepository.findBySellerName(seller.getSellerName());
          if(seller1==null||StringUtils.isEmpty(seller1)){
              sellerRepository.save(seller);
              modelMap.put("success",true);
              modelMap.put("Msg","销售商"+seller.getSellerName()+"添加成功");
              modelMap.put("Seller",seller);
          }else {
              modelMap.put("success",false);
              modelMap.put("Msg","此销售商已经存在");
          }
        }
          else {
            modelMap.put("success",false);
            modelMap.put("Msg","输入销售商为空，请输入销售商！");
        }
        return modelMap;
    }
    /**删除销售商
     * 1.传入参数idList
     * 2.完成销售商的删除操作
     * @param idList
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> deleteSeller(List<Map<String, Integer>> idList) {
        Map<String, Object> modelMap=new HashMap<>();
        if(!idList.isEmpty()){
        for (Map<String,Integer> id:idList) {
            Seller seller= sellerRepository.getOne(id.get("id"));
            if(sellerRepository.findBySellerName(seller.getSellerName())!=null||!(StringUtils.isEmpty(sellerRepository.findBySellerName(seller.getSellerName())))){
                sellerRepository.delete(seller);
                modelMap.put("success",true);
                modelMap.put("Msg","删除"+seller.getSellerName()+"成功");
            }
            else {
                modelMap.put("success",false);
                modelMap.put("Msg","用户名"+seller.getSellerName()+"为空，删除失败");
            }
        }
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","输入id为空，请输入id");
        }
        return modelMap;
    }
    /**修改销售商
     * 1.传入参数，sellerId以及界面上的所有销售商字段（参考Seller）
     * 2.完成销售商的修改操作
     * @param seller
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> modifySeller(Seller seller) {
        Map<String, Object> modelMap=new HashMap<>();
           if(!StringUtils.isEmpty(seller.getSellerId())||seller.getSellerId()!=null){
               sellerRepository.save(seller);
               modelMap.put("success",true);
               modelMap.put("Msg","销售商"+seller.getSellerName()+"修改成功");
               modelMap.put("Seller",seller);
           }else {
               modelMap.put("success",false);
               modelMap.put("Msg","销售商为空，请输入销售商！");
           }
        return modelMap;
    }

    /**查询所有销售商
     * 1.传入参数为空
     * 2.完成查询所有销售商的操作
     * @return
     */
    @Override
    public Map<String, Object> findAllSeller() {
        Map<String, Object> modelMap=new HashMap<>();
         List<Seller> sellers=sellerRepository.findAll();
         if(!sellers.isEmpty()){
             modelMap.put("success",true);
             modelMap.put("Msg","销售商获取成功");
             modelMap.put("Sellers",sellers);
         }else {
             modelMap.put("success",false);
             modelMap.put("Msg","没有销售商！");
         }
        return modelMap;
    }
}
