package www.yema.cn.service.parse.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import www.yema.cn.service.parse.IProductIdParse;
import www.yema.cn.utils.HttpClientUtil;

@Service(value="mmfadProductIdParse")
public class MmfadProductIdParse implements IProductIdParse {

    @Override
    public String getProductId(String outShare) {
        String httpUrl = "http://quan.mmfad.com/";                                          
        Map<String, String> maps = new HashMap<String, String>();                                            
        maps.put("b2c", "0");                                             
        maps.put("channel", "1");        
        maps.put("coupon", "1");
        maps.put("p", "1");   
        maps.put("sort", "0");   
        maps.put("k", outShare);        
        String body=HttpClientUtil.getInstance().sendHttpPost(httpUrl, maps).toString();
        //System.out.println(body);
        if(body.indexOf("302:")>-1) {
            int startIndex=body.lastIndexOf("/")+1;
            int endIndex=body.indexOf(".");
            String productId=body.substring(startIndex,endIndex);
            return productId;
        }else {
            return null;
        }       
    }

}
