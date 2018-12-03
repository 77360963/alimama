package www.yema.cn.service.parse.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import www.yema.cn.service.parse.IProductIdParse;
import www.yema.cn.utils.HttpClientUtil;

@Service(value="chaozhiProductIdParse")
public class ChaozhiProductIdParse implements IProductIdParse {

    @Override
    public String getProductId(String outShare) {
        String httpUrl = "http://tool.chaozhi.hk/#/tklParse";                                          
        Map<String, String> maps = new HashMap<String, String>(); 
        maps.put("tkl", outShare);        
        String body=HttpClientUtil.getInstance().sendHttpPost(httpUrl, maps).toString();
        System.out.println(body);
        return null;
    }
    
    public static void main(String[] args) {
        ChaozhiProductIdParse a=new ChaozhiProductIdParse();
        a.getProductId("￥MXWeblB3xS0￥");
    }

}
