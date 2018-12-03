package www.yema.cn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taobao.api.ApiException;

import www.yema.cn.service.Alipay;
import www.yema.cn.utils.UnicodeUtil;

@RestController
public class Taobao {
    
    @Autowired
    private Alipay alipay;
    
    @RequestMapping(value = "/tk/{id}/api",method = RequestMethod.POST)
    public String login(@PathVariable(name = "id") String id, @RequestParam(name = "name") String name){
        System.out.println("id="+id);
        System.out.println("name="+name);
        return "login登录界面000";
    }
    
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public String alimama(String content) throws ApiException{
    	
    	String context=UnicodeUtil.unicodetoString(content);
    	
    	System.out.println("======================");
    	System.out.println(content);
    	
    	String share=alipay.getShare(context);
    	
        return "{\"rs\":1,\"tip\":\""+share+"\",\"end\":0}";
    }
    
    
    


}
