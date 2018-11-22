package com.atw.bpmsystem.Controllers;

import com.atw.bpmsystem.Repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@EnableAutoConfiguration
@CrossOrigin(allowCredentials="true",maxAge = 36000)
@RestController
public class TypeController {
    @Autowired
    private TypeRepository typeRepository;
    /**查询所有类型
     * 1.传入参数为空
     * 2.完成查询所有类型的操作
     * @return
     */
    @RequestMapping(value = "/api/listType", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findAllType() {
        Map<String,Object>modelMap=new HashMap<>();
        modelMap.put("listType",typeRepository.findAll());
        return modelMap;
    }

}
