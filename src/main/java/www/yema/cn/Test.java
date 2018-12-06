package www.yema.cn;

import www.yema.cn.utils.UnicodeUtil;

public class Test {
	
	public static void main(String[] args) {
		
		String a=UnicodeUtil.stringtoUnicode("￥SPBQblxQXHC￥");
		System.out.println(a);
		
		/*String httpUrl = "http://quan.mmfad.com/";;                                             
		Map<String, String> maps = new HashMap<String, String>();                                            
		maps.put("b2c", "0");                                             
		maps.put("channel", "1");        
		maps.put("coupon", "1");
		maps.put("p", "1");   
		maps.put("sort", "0");   
		maps.put("k", "￥gvTyblx9az2￥");        
		String body=HttpClientUtil.getInstance().sendHttpPost(httpUrl, maps);     
		System.out.println(body);*/
		
	

	}

}
