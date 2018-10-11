package com.onion.o2o.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.onion.o2o.BaseTest;
import com.onion.o2o.dto.ImageHolder;
import com.onion.o2o.dto.ShopExecution;
import com.onion.o2o.entity.Area;
import com.onion.o2o.entity.PersonInfo;
import com.onion.o2o.entity.Shop;
import com.onion.o2o.entity.ShopCategory;
import com.onion.o2o.enums.ShopStateEnum;
import com.onion.o2o.exception.ShopOperationException;

public class ShopServiceTest extends BaseTest{
	@Autowired
	private ShopService shopService;
	
	@Test
	public void testGetShopList() {
		Shop shopCondition=new Shop();
		ShopCategory sc=new ShopCategory();
		sc.setShopCategoryId(1L);
		shopCondition.setShopCategory(sc);
		ShopExecution se=shopService.getShopList(shopCondition, 2, 3);
		System.out.println("店铺列表数："+se.getShopList().size());
		System.out.println("店铺总数："+se.getCount());
		
	}
	@Test
	
	public void testModifyShop() throws ShopOperationException,FileNotFoundException{
		Shop shop=new Shop();
		shop.setShopId(1L);
		shop.setShopName("咖啡奶茶");
		File shopImg=new File("D:\\app1\\image\\upload\\item\\shop\\12\\test1.jpg");
		InputStream is=new FileInputStream(shopImg);
		ImageHolder thumbnail=new ImageHolder("test1.jpg", is);
		ShopExecution shopExecution=shopService.modifyShop(shop,thumbnail);
		System.out.println("新的图片地址为："+shopExecution.getShop().getShopImg());
		
	}
	
	@Test
	@Ignore
	public void testAddShop() throws FileNotFoundException 
	{
		Shop shop=new Shop();
		PersonInfo owner=new PersonInfo();
		Area area=new Area();
		ShopCategory shopCategory=new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(2);;
		shopCategory.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺5");
		shop.setShopAddr("广州");
		shop.setPhone("56789");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
//		File shogImg=new File("/test1.jpg");
		//InputStream is=new FileInputStream(shogImg);
		//ShopExecution se=shopService.addShop(shop, is,shogImg.getName());
		//assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
	}
}
