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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onion.o2o.dto.ImageHolder;
import com.onion.o2o.dto.ProductExecution;
import com.onion.o2o.entity.Product;
import com.onion.o2o.entity.ProductCategory;
import com.onion.o2o.entity.Shop;
import com.onion.o2o.enums.ProductStateEnum;
import com.onion.o2o.exception.ProductOperationException;
import com.onion.o2o.service.ProductCategoryService;
import com.onion.o2o.service.ProductService;
import com.onion.o2o.util.CodeUtil;
import com.onion.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
	@Autowired 
	ProductService productService;
	@Autowired
	ProductCategoryService productCategoryService;
	
	//支持上传商品详情图的最大数量
	private static final int IMAGEMAXCOUNT=6;
	
	@RequestMapping(value="/getproductlistbyshop",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getProductListByShop(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String,Object>();
		//获取前台传过来的页码
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取前台传过来的每页要求返回的商品数上限
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		//从当前session中获取店铺信息，主要是获取shopId
		Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
		//空值判断
		if((pageIndex>-1)&&(pageSize>-1)&&(currentShop!=null)&&(currentShop.getShopId()!=null)) {
			//获取换入的需要检索的条件，包括是否需要从某个商品泪奔以及模糊查找商品名去筛选某个店铺下的商品列表
			//筛选的条件可以进行排列组合
			long productCategoryId=HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName=HttpServletRequestUtil.getString(request, "productName");
			Product productCondition=compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
			ProductExecution pe=productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		
		return modelMap;
	}
	
	private Product compactProductCondition(long shopId, long productCategoryId, String productName) {
		Product productCondition=new Product();
		Shop shop=new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		//若有指定类别的要求则添加进去
		if(productCategoryId!=-1L) {
			ProductCategory productCategory=new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if(productName!=null) {
			productCondition.setProductName(productName);
		}
		return productCondition;
	}

	@RequestMapping(value="/addproduct",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> addProduct(HttpServletRequest request) throws ProductOperationException{
		Map<String,Object> modelMap=new HashMap<String,Object>();
		//验证码校验
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//接受前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
		ObjectMapper mapper=new ObjectMapper();
		Product product=null;
		String productStr=HttpServletRequestUtil.getString(request, "productStr");
		//MultipartHttpServletRequest multipartRequest=null;
		ImageHolder thumbnail=null;
		List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
		try {
			//若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
			if(multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, productImgList);
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片不能为空");
				return modelMap;
			}
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			//尝试获取前端传过来的表单String流并将其转换成product实体类
			product=mapper.readValue(productStr,Product.class);
		}catch(Exception e) {
			System.out.println(e.toString());
			System.out.println(e.getMessage());
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		//若product信息，缩略图，详情图列表为非空，则开始进行商品添加操作
		if(product!=null&&thumbnail!=null&&productImgList.size()>0) {
			try {
				Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				ProductExecution pe=productService.addProduct(product, thumbnail, productImgList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg",pe.getStateInfo());
				}
			}catch(ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}
		
		return modelMap;
	}

	private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws IOException {
		MultipartHttpServletRequest multipartRequest;
		multipartRequest=(MultipartHttpServletRequest) request;
		//取出缩略图并构建ImageHolder对象
		CommonsMultipartFile thumbnailFile=(CommonsMultipartFile) multipartRequest.getFile("thumbnail");
		if(thumbnailFile!=null) {
			thumbnail =new ImageHolder(thumbnailFile.getOriginalFilename(),
					thumbnailFile.getInputStream());
		}
		//取出详情图列表并构建List<ImageHolder>列表对象，最多支持六张图上传
		for(int i=0;i<IMAGEMAXCOUNT;i++) {
			CommonsMultipartFile productImgFile=(CommonsMultipartFile) multipartRequest.getFile("productImg"+i);
			if(productImgFile!=null) {
				//若取出的第i个详情图文件流不为空，则将其加入详情图列表
				ImageHolder productImg=new ImageHolder(productImgFile.getOriginalFilename(),
						productImgFile.getInputStream());
				productImgList.add(productImg);
			}else {
				//若取出的第i个详情图为空，退出循环
				break;
			}
		}
		return thumbnail;
	}
	
	@RequestMapping(value="/getproductbyid",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getProductById(@RequestParam Long productId) throws ProductOperationException{
		Map<String,Object> modelMap=new HashMap<String,Object>();
		//非空判断
		if(productId>-1) {
			//获取商品信息
			Product product=productService.getProductById(productId);
			//获取该店铺下的商品类别列表
			List<ProductCategory> productCategoryList=productCategoryService.getProductCategoryList(product.getShop().getShopId());
			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	}
	@RequestMapping(value="/modifyproduct",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> modifyProduct(HttpServletRequest request) throws ProductOperationException{
		Map<String,Object> modelMap=new HashMap<String,Object>();
		//是商品编辑时调用还是上下架操作时调用
		//若为前者则进行验证码判断，后者则跳过验证码判断
		boolean statusChange=HttpServletRequestUtil.getBoolean(request, "statusChange");
		//验证码校验
		if(!statusChange&&!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//接受前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
		ObjectMapper mapper=new ObjectMapper();
		Product product=null;
		String productStr=HttpServletRequestUtil.getString(request, "productStr");
		//MultipartHttpServletRequest multipartRequest=null;
		ImageHolder thumbnail=null;
		List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			//若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
			if(multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, productImgList);
			}
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			//尝试获取前端传过来的表单String流并将其转换成product实体类
			product=mapper.readValue(productStr,Product.class);
		}catch(Exception e) {
			System.out.println(e.toString());
			System.out.println(e.getMessage());
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		//更新，只要确保product不为空就行
		if(product!=null) {
			try {
				Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				ProductExecution pe=productService.modifyProduct(product, thumbnail, productImgList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg",pe.getStateInfo());
				}
			}catch(ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}
		
		return modelMap;
	}
}
