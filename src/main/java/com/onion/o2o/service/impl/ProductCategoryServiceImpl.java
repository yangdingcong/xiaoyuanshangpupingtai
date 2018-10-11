package com.onion.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onion.o2o.dao.ProductCategoryDao;
import com.onion.o2o.dao.ProductDao;
import com.onion.o2o.dto.ProductCategoryExecution;
import com.onion.o2o.entity.ProductCategory;
import com.onion.o2o.enums.ProductCategoryStateEnum;
import com.onion.o2o.exception.ProductCategoryOperationException;
import com.onion.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	
	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired
	private ProductDao productDao;
	
	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
		
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(
			List<ProductCategory> productCategoryList) throws RuntimeException {
		//空值判断
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				int effectedNum = productCategoryDao
						.batchInsertProductCategory(productCategoryList);
				if (effectedNum <= 0) {
					throw new RuntimeException("店铺类别创建失败");
				} else {

					return new ProductCategoryExecution(
							ProductCategoryStateEnum.SUCCESS);
				}

			} catch (Exception e) {
				throw new RuntimeException("batchAddProductCategory error: "
						+ e.getMessage());
			}
		} else {
			return new ProductCategoryExecution(
					ProductCategoryStateEnum.INNER_ERROR);
		}

	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		//先接触tb_product里的商品与该categoryId的关联
		try {
			int eff=productDao.updateProductCategoryToNull(productCategoryId);
			if(eff<0) {
				throw new RuntimeException("商品类别更新失败");
			}
		}catch(Exception e) {
			throw new RuntimeException("deleteProductCategory err:"+e.getMessage());
		}
		try {
			int effectedNum=productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum<=0) {
				throw new ProductCategoryOperationException("商品类别删除失败");
			}else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		}catch(Exception e) {
			throw new ProductCategoryOperationException("deleteProductcategory error:"+e.getMessage());
		}
	}

}
