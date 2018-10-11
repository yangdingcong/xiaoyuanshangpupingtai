package com.onion.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onion.o2o.dto.ImageHolder;
import com.onion.o2o.dto.ShopExecution;
import com.onion.o2o.entity.Area;
import com.onion.o2o.entity.PersonInfo;
import com.onion.o2o.entity.Shop;
import com.onion.o2o.entity.ShopCategory;
import com.onion.o2o.enums.ShopStateEnum;
import com.onion.o2o.service.AreaService;
import com.onion.o2o.service.ShopCategoryService;
import com.onion.o2o.service.ShopService;
import com.onion.o2o.util.CodeUtil;
import com.onion.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagermentController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value="/getshopmanagementinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String,Object>();
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		//如果shopId没有
		if(shopId<=0) {
			//就从session中去获取
			Object currentShopObj=request.getSession().getAttribute("currentShop");
			//如果还没有，就进行重定向
			if(currentShopObj==null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			}else {
				Shop currentShop=(Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		}else {
			Shop currentShop=new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
		}
		return modelMap;
	}
	
	@RequestMapping(value="/getshoplist",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopList(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String,Object>();
		//由于不实现用户登录，这里假设它登录了，设置用户Id为1
		PersonInfo user=new PersonInfo();
		user.setUserId(1L);
		user.setName("北极的大黑脸");
		request.getSession().setAttribute("user", user);
		//通过session去获取用户的信息
		user=(PersonInfo) request.getSession().getAttribute("user");
	
		try {
			Shop shopCondition=new Shop();
			shopCondition.setOwner(user);
			ShopExecution se=shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg:", e.getMessage());
		}
		return modelMap;
	}
	
	
	
	@RequestMapping(value="/getshopbyid",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopById(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String,Object>();
		Long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId>-1) {
			try {
				Shop shop=shopService.getByShopId(shopId);
				List<Area> areaList=areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success",true);
			}catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg:", e.getMessage());
			}
			
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg:", "empty shopId");
		}
		return modelMap;
	}
	
	
	@RequestMapping(value="/getshopinitinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopInitInfo()
	{
		Map<String,Object> modelMap=new HashMap<String,Object>();
		List<ShopCategory> shopCategoryList=new ArrayList<ShopCategory>();
		List<Area> areaList=new ArrayList<Area>();
		try {
			//获取全部
			shopCategoryList=shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList=areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success",true);
		}catch(Exception e) 
		{
			modelMap.put("success", false);
			modelMap.put("errMsg:", e.getMessage());
			return modelMap;
		}
		return modelMap;
	}
	
	@RequestMapping(value="/registershop",method= {RequestMethod.POST})
	@ResponseBody
	private Map<String,Object> registerShop(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String,Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//1.接受并转化相应的参数，包括店铺信息以及图片信息
		String shopStr=HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper=new ObjectMapper();
		Shop shop =null;
		try {
			//转换
			shop=mapper.readValue(shopStr, Shop.class);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg:", e.getMessage());
			return modelMap;
		}
		//spring 自带的文件解析器
		CommonsMultipartFile shopImg=null;
		CommonsMultipartResolver commonsMultipartResolver=
				new CommonsMultipartResolver(request.getSession().getServletContext());//从本次会话中去获取上传的内容
		if(commonsMultipartResolver.isMultipart(request)) //判断request里是否有上传的文件流
		{
			//将request转换成MultipartHttpServletRequest,这样才能提取相应的文件流
			MultipartHttpServletRequest multipartHttpServletRequest= (MultipartHttpServletRequest) request;
			shopImg=(CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg:", "上传图片不能为空");
			return modelMap;
		}
		//2 注册店铺
		if(shop!=null&&shopImg!=null) 
		{
			/*
			 * 首先要假定前端传入的信息是不可靠的
			 * 所以前端传入的信息越少越好
			 * 比如shop 中的ownerId 我们可以通过session去获取
			 * */
			//通过session，去取出用户的数据，这里的user是通过用户登录时获取到的
			PersonInfo owner=(PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			
			
			ShopExecution se;
			try {
				ImageHolder thumbnail=new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
				se = shopService.addShop(shop,thumbnail);
				if(se.getState()==ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					//一旦店铺创建成功就将其保存到session中
					//表示该用户可以操作的店铺列表
					@SuppressWarnings("unchecked")
					List<Shop> shopList=(List<Shop>) request.getSession().getAttribute("shoplist");
					if(shopList==null||shopList.size()==0) {
						shopList=new ArrayList<Shop>();
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
					
				}else 
				{
					modelMap.put("success", false);
					modelMap.put("errMsg:", se.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg:", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg:", e.getMessage());
			}
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg:", "请输入店铺信息");
			return modelMap;
		}
		// 3 返回结果
		
	}
	
	@RequestMapping(value="/modifyshop",method= {RequestMethod.POST})
	@ResponseBody
	private Map<String,Object> modifyShop(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String,Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//1.接受并转化相应的参数，包括店铺信息以及图片信息
		String shopStr=HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper=new ObjectMapper();
		Shop shop =null;
		try {
			//转换
			shop=mapper.readValue(shopStr, Shop.class);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg:", e.getMessage());
			return modelMap;
		}
		//spring 自带的文件解析器
		CommonsMultipartFile shopImg=null;
		CommonsMultipartResolver commonsMultipartResolver=
				new CommonsMultipartResolver(request.getSession().getServletContext());//从本次会话中去获取上传的内容
		if(commonsMultipartResolver.isMultipart(request)) //判断request里是否有上传的文件流
		{
			//将request转换成MultipartHttpServletRequest,这样才能提取相应的文件流
			MultipartHttpServletRequest multipartHttpServletRequest= (MultipartHttpServletRequest) request;
			shopImg=(CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		//2 修改店铺
		if(shop!=null&&shop.getShopId()!=null) 
		{
			/*
			 * 首先要假定前端传入的信息是不可靠的
			 * 所以前端传入的信息越少越好
			 * 比如shop 中的ownerId 我们可以通过session去获取
			 * */
//			PersonInfo owner=new PersonInfo();
			//SESSION
			//在web开发中，服务器为每个用户浏览器创建一个会话对象
			//一个浏览器独占一个会话对象
			//在需要保存用户数据时，服务器程序可以把用户信息写道session中
			//用户使用浏览器访问服务器时，服务器程序可以从用户的session中取出该用户的数据为用户服务
			//服务器创建session后，会把session的id号一cookie的形式会写给客户端
			//这样客户端的浏览器不关，再去访问浏览器的时候，都会带着session id去
			//服务器发现客户端的浏览器带着sessionID就会使用内容中与之对应的内容服务。/
			
//			owner.setUserId(1L);
//			shop.setOwner(owner);
			
			ShopExecution se;
			try {
				if(shopImg==null) {
					se=shopService.modifyShop(shop, null);
				}else {
					ImageHolder thumbnail=new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
					se = shopService.modifyShop(shop,thumbnail);
				}
				if(se.getState()==ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else 
				{
					modelMap.put("success", false);
					modelMap.put("errMsg:", se.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg:", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg:", e.getMessage());
			}
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg:", "请输入店铺ID");
			return modelMap;
		}
		// 3 返回结果
		
	}
}
