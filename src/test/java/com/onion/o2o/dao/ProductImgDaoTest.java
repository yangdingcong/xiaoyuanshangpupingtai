package com.onion.o2o.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.onion.o2o.BaseTest;
import com.onion.o2o.entity.ProductImg;

public class ProductImgDaoTest extends BaseTest {
	@Autowired
	private ProductImgDao productImgDao;
	
	@Test
	@Ignore
	public void testBatchInsertProductImg() throws Exception {
		ProductImg productImg1=new ProductImg();
		productImg1.setImgAddr("图片1");
		productImg1.setImgDesc("");
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(1L);
		ProductImg productImg2=new ProductImg();
		productImg2.setImgAddr("图片2");
		productImg2.setImgDesc("测试");
		productImg2.setPriority(2);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(1L);
		List<ProductImg> productImgList =new ArrayList<ProductImg>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);
		
		int effectedNum=productImgDao.batchInsertProductImg(productImgList);
		System.out.println("添加的数量为："+effectedNum);
		
	}
	@Test 
	public void testDeleteProductImgByProductId() throws Exception{
		long productId=1L;
		int eff=productImgDao.deleteProductImgByProductId(productId);
		System.out.println("删除的数量为："+eff);
	}

}
