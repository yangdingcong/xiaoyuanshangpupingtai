package com.onion.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onion.o2o.dao.ShopCategoryDao;
import com.onion.o2o.entity.ShopCategory;
import com.onion.o2o.service.ShopCategoryService;
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService{
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	@Override
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryConditon)
	{
		return shopCategoryDao.queryShopCategory(shopCategoryConditon);
	}
}
