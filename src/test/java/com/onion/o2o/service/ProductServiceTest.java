package com.onion.o2o.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.onion.o2o.BaseTest;
import com.onion.o2o.dto.ImageHolder;
import com.onion.o2o.dto.ProductExecution;
import com.onion.o2o.entity.Product;
import com.onion.o2o.entity.ProductCategory;
import com.onion.o2o.entity.Shop;
import com.onion.o2o.enums.ProductStateEnum;
import com.onion.o2o.exception.ProductOperationException;

public class ProductServiceTest extends BaseTest {

	@Autowired
	private ProductService productService;
	@Test
	@Ignore
	public void testaddProduct() throws ProductOperationException,FileNotFoundException{
		Product product =new Product();
		Shop shop=new Shop();
		shop.setShopId(16L);
		ProductCategory pc=new ProductCategory();
		pc.setProductCategoryId(11L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("测试商品1");
		product.setProductDesc("test1");
		product.setPriority(10);
		product.setCreateTime(new Date());
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
		//缩略图
		File thumbnailFile=new File("D:\\app1\\image\\upload\\item\\shop\\12\\test1.jpg");
		InputStream is=new FileInputStream(thumbnailFile);
		ImageHolder thumbnail=new ImageHolder(thumbnailFile.getName(), is);
		//详情图
		File productImg1=new File("D:\\app1\\image\\upload\\item\\shop\\12\\test1.jpg");
		InputStream is1=new FileInputStream(productImg1);
		File productImg2=new File("D:\\app1\\image\\upload\\item\\shop\\12\\test1.jpg");
		InputStream is2=new FileInputStream(productImg2);
		List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(),is1));
		productImgList.add(new ImageHolder(productImg2.getName(),is2));
		ProductExecution pe=productService.addProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(),pe.getState());
	}
	@Test
	public void testmodifyProduct() throws ProductOperationException,FileNotFoundException{
		Product product =new Product();
		Shop shop=new Shop();
		shop.setShopId(16L);
		ProductCategory pc=new ProductCategory();
		pc.setProductCategoryId(11L);
		product.setProductId(2L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("正式商品1");
		product.setProductDesc("正式的");
		//缩略图
		File thumbnailFile=new File("D:\\app1\\image\\upload\\item\\shop\\12\\test1.jpg");
		InputStream is=new FileInputStream(thumbnailFile);
		ImageHolder thumbnail=new ImageHolder(thumbnailFile.getName(), is);
		//详情图
		File productImg1=new File("D:\\app1\\image\\upload\\item\\shop\\12\\test1.jpg");
		InputStream is1=new FileInputStream(productImg1);
		File productImg2=new File("D:\\app1\\image\\upload\\item\\shop\\12\\test1.jpg");
		InputStream is2=new FileInputStream(productImg2);
		List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(),is1));
		productImgList.add(new ImageHolder(productImg2.getName(),is2));
		ProductExecution pe=productService.modifyProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(),pe.getState());
	}

}
