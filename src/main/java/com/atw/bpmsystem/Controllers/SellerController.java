package com.atw.bpmsystem.Controllers;

import com.atw.bpmsystem.Entities.Seller;
import com.atw.bpmsystem.Services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class SellerController {
    @Autowired
    private SellerService sellerService;

    /**添加销售商
     * 1.传入参数，界面上的所有销售商字段（参考Seller）
     * 2.完成销售商的添加操作
     * @param
     * @return
     */
    @RequestMapping(value = "/api/addSeller", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> addSeller(@RequestBody Seller seller) {
        return sellerService.addSeller(seller);
    }

    /**删除销售商
     * 1.传入参数idList
     * 2.完成销售商的删除操作
     * @param idList
     * @return
     */
    @PostMapping("/api/deleteSeller")
    @ResponseBody
    public Map<String, Object> deleteSeller( @RequestBody List<Map<String,Integer>> idList) {
        return sellerService.deleteSeller(idList);
    }

    /**修改销售商
     * 1.传入参数，sellerId以及界面上的所有销售商字段（参考Seller）
     * 2.完成销售商的修改操作
     * @param seller
     * @return
     */
    @RequestMapping(value = "/api/modifySeller", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> modifySeller(@RequestBody Seller seller) {
        return sellerService.modifySeller(seller);
    }

    /**查询所有销售商
     * 1.传入参数为空
     * 2.完成查询所有销售商的操作
     * @return
     */
    @RequestMapping(value = "/api/listSeller", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findAllSeller() {
        return sellerService.findAllSeller();
    }

}
