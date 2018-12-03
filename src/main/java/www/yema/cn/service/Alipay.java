package www.yema.cn.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import www.yema.cn.pojo.conpon.ConponBean;
import www.yema.cn.pojo.conpon.Map_data;
import www.yema.cn.pojo.conpon.Result_list;
import www.yema.cn.pojo.generateShare.Data;
import www.yema.cn.pojo.generateShare.GenerateShare;
import www.yema.cn.pojo.product.ProductBean;
import www.yema.cn.pojo.product.Results;
import www.yema.cn.service.parse.IProductIdParse;
import www.yema.cn.utils.HttpClientUtil;

import com.alibaba.fastjson.JSON;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkItemInfoGetResponse;
import com.taobao.api.response.TbkTpwdCreateResponse;

@Service
public class Alipay {
    
    @Resource(name="mmfadProductIdParse")
    private IProductIdParse productIdParse;
	
	public static String url="http://gw.api.taobao.com/router/rest";
	public static String appkey="25245534";
	public static String secret="391965a3c847eec1018cfe941f0d8221";
	
	
	
	public  String getShare(String context) throws ApiException {		    
       //1.获取推荐商品id
		String productId= productIdParse.getProductId(context);
		if(StringUtils.isBlank(productId)) {			
			return "未找到相关信息";
		}		
		
		//2.  获取商品title
		String ProductTitle=getProductTitle(productId);
		
		//3. 获取优惠券
		String Conpon=getConpon(ProductTitle);
		
		//4.获取推广
		String generate=generateShare(Conpon);

        return generate;
		
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public static String getProductIdByOutShareUrl(String outShareContext) {
		int contextStartIndex=outShareContext.indexOf("￥");
		int contextEndIndex=outShareContext.lastIndexOf("￥")+1;
		String outShare=outShareContext.substring(contextStartIndex, contextEndIndex);
		//System.out.println(outShare);		
		String httpUrl = "http://quan.mmfad.com/";;                                             
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
	
	
	/**
	 *   获取商品title
	 * @return
	 * @throws ApiException
	 */
	public static String getProductTitle(String productId) throws ApiException{
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);		
		TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
		req.setNumIids(productId);
		TbkItemInfoGetResponse response = client.execute(req);
		String body=response.getBody();	
		//System.out.println(body);		
		ProductBean infoDo = JSON.parseObject(body, ProductBean.class);		
		Results results=infoDo.getTbk_item_info_get_response().getResults();
		String title=results.getN_tbk_item().get(0).getTitle();	
		//System.out.println(title);
		return title;
		
	}
	
	/**
	 * 获取优惠券
	 * @param productTitle
	 * @return
	 * @throws ApiException
	 */
	public static String getConpon(String productTitle) throws ApiException {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
		req.setQ(productTitle);
		req.setHasCoupon(true);
		req.setAdzoneId(60129500330L);
		TbkDgMaterialOptionalResponse response = client.execute(req);
		String body=response.getBody();		
		//System.out.println(body);
		ConponBean conponBean=JSON.parseObject(body, ConponBean.class);
		Result_list result_list=conponBean.getTbk_dg_material_optional_response().getResult_list();
		List<Map_data> list=result_list.getMap_data();
		String shareUrl="https:"+list.get(0).getCoupon_share_url();
		//System.out.println(shareUrl);
		return shareUrl;
	}
	
	
	public static String generateShare(String productHref) throws ApiException {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
		req.setText("吴晓丽分享的链接");
		req.setUrl(productHref);
		req.setLogo("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=511778976,2722131452&fm=173&app=49&f=JPEG?w=640&h=358&s=9A384E864C511BC028B5356703008076");
		TbkTpwdCreateResponse response = client.execute(req);
		String body=response.getBody();		
		//System.out.println(body);
		GenerateShare generateShare=JSON.parseObject(body, GenerateShare.class);
		Data data=generateShare.getTbk_tpwd_create_response().getData();
		String generateShareHref=data.getModel();
		//System.out.println(generateShareHref);
		return generateShareHref;
	}
	
	
	public static  void main(String[] args) throws ApiException {		
		
		String context="复制这条信息，￥63ATblD1JFg￥ ，打开【手机淘宝】即可查看\r\n" + "";
		
		//1.获取推荐商品id
		String productId=getProductIdByOutShareUrl(context); 		
		if(StringUtils.isBlank(productId)) {
			System.out.println("未找到商品");
			return;
		}		
		
		//2.  获取商品title
		String ProductTitle=getProductTitle(productId);
		
		//3. 获取优惠券
		String Conpon=getConpon(ProductTitle);
		
		//4.获取推广
		String generate=generateShare(Conpon);
		
		System.out.println(generate);

	}

}
