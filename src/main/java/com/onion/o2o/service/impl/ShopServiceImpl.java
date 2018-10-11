package com.onion.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.onion.o2o.dao.ShopDao;
import com.onion.o2o.dto.ImageHolder;
import com.onion.o2o.dto.ShopExecution;
import com.onion.o2o.entity.Shop;
import com.onion.o2o.enums.ShopStateEnum;
import com.onion.o2o.exception.ShopOperationException;
import com.onion.o2o.service.ShopService;
import com.onion.o2o.util.ImageUtil;
import com.onion.o2o.util.PageCalculator;
import com.onion.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	
	@Autowired
	private ShopDao shopDao;
	
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws RuntimeException {
		if(shop==null)   //店铺非空判断
		{
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			//给店铺赋初始值
			shop.setEnableStatus(0);  //审核中
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			int effectedNum=shopDao.insertShop(shop);
			if(effectedNum<=0) 
			{
				//使用runtime exception能够回滚，若其他异常可能事务会进行提交
				throw new RuntimeException("店铺创建失败"); 
			}else 
			{
				if(thumbnail.getImage()!=null) 
				{
					//存储图片
					try {
						addShopImg(shop,thumbnail);
					}catch(Exception e) {
						throw new RuntimeException("addShopImg err:"+e.getMessage()); 
					}
					effectedNum=shopDao.updateShop(shop);
					if(effectedNum<=0) 
					{
						//使用runtime exception能够回滚，若其他异常可能事务会进行提交
						throw new RuntimeException("更新图片地址失败"); 
					}
				}
			}
		}catch(Exception e) {
			throw new RuntimeException("addShop error:"+e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}

	private void addShopImg(Shop shop,ImageHolder thumbnail) {
		//获取shop图片目录的相对值路径
		String dest=PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr=ImageUtil.generateThumbnail(thumbnail, dest);
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail)
			throws ShopOperationException {
		if(shop==null||shop.getShopId()==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else {
			//1.判断是否需要处理图片
			try {
			if(thumbnail.getImage()!=null) {
				//删去shop之前的地址
				Shop tempShop=shopDao.queryByShopId(shop.getShopId());
				//将之前的地址删除
				if(tempShop.getShopImg()!=null&&thumbnail.getImageName()!=null&&!"".equals(thumbnail.getImageName())) {
					ImageUtil.deleteFileOrPate(tempShop.getShopImg());
					
				}
				addShopImg(shop, thumbnail);
			}
			//2.更新店铺信息,根据前端传入的信息来更新
			shop.setLastEditTime(new Date());
			int effectedNum=shopDao.updateShop(shop);
			//创建失败
			if(effectedNum<=0) {
				return new ShopExecution(ShopStateEnum.INNER_ERROR);
			}else {
				//创建成功
				shop=shopDao.queryByShopId(shop.getShopId());
				return new ShopExecution(ShopStateEnum.SUCCESS,shop);
			}
			}catch(Exception e) {
				throw new ShopOperationException("modifyShop error"+e.getMessage());
			}
		}
		
	}

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList=shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count=shopDao.queryShopCount(shopCondition);
		ShopExecution se=new ShopExecution();
		if(shopList!=null) {
			se.setShopList(shopList);
			se.setCount(count);
		}else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

}
