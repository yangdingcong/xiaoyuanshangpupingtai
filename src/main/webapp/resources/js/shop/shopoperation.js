/**
 * 功能1，获取后台商铺分类，区域等信息，填充到select的option中
 * 功能2，提交表单到后台，让后台去注册
 * 还可以去实现校验功能
 */

$(function(){
	var shopId=getQueryString('shopId');
	//传了Id是更新店铺，不传是注册店铺
	var isEdit=shopId?true:false;
	
	var initUrl="/o2o/shopadmin/getshopinitinfo";
	var registerShopUrl="/o2o/shopadmin/registershop";
	var shopInfoUrl="/o2o/shopadmin/getshopbyid?shopId="+shopId;
	
	var editShopUrl="/o2o/shopadmin/modifyshop";
	if(!isEdit){
		//注册时
		//我们希望js一被加载就执行这个函数，让它去后台找到需要的信息
		getShopInitInfo();
	}else{
		//修改时
		getShopInfo(shopId);
	}
	
	//第一个功能
	function getShopInitInfo(){
		//以json的形式返回,一个参数是访问的url,第二个是返回的方法
		$.getJSON(initUrl, function(data){
			if(data.success){
				var tempHtml='';
				var tempAreaHtml='';
				//以map的形式返回，再遍历
				data.shopCategoryList.map(function(item,index){
					tempHtml+='<option data-id="'+item.shopCategoryId+'">'
					+item.shopCategoryName+'</option>';
				});
				data.areaList.map(function(item,index){
					
					tempAreaHtml+='<option data-id="'+item.areaId+'">'
					+item.areaName+'</option>';
				});
				$('#shop-category').html(tempHtml);
				$('#area').html(tempAreaHtml);
			}
		});
		
	}
	$('#submit').click(function(){
		var shop={};
		if(isEdit){
			shop.shopId=shopId;
		}
		shop.shopName=$('#shop-name').val();
		shop.shopAddr=$('#shop-addr').val();
		shop.phone=$('#shop-phone').val();
		shop.shopDesc=$('#shop-desc').val();
		shop.shopCategory={
				shopCategoryId:$('#shop-category').find('option').not(function(){
					return !this.selected;
				}).data('id')
		};
		shop.area={
				areaId:$('#area').find('option').not(function(){
					return !this.selected;
				}).data('id')
		};
		var shopImg = $("#shop-img")[0].files[0];
		var formData=new FormData();
		formData.append('shopImg',shopImg);
		formData.append('shopStr',JSON.stringify(shop));
		var verifyCodeActual=$('#j_kaptcha').val();
		if(!verifyCodeActual){
			$.toast("请输入验证码");
			return;
		}
		formData.append('verifyCodeActual',verifyCodeActual);
		$.ajax({
			url:(isEdit?editShopUrl:registerShopUrl),
			type:'POST',
			data:formData,
			contentType:false,
			processData:false,
			cache:false,
			success:function(data){
				if(data.success){
					$.toast('提交成功!');
				}else{
					$.toast('提交失败!'+data.errMsg);
				}
				$('#kaptcha_img').click();
			}
		});
	})
	function getShopInfo(shopId) {
		$.getJSON(shopInfoUrl, function(data) {
			if (data.success) {
				var shop = data.shop;
				$('#shop-name').val(shop.shopName);
				$('#shop-addr').val(shop.shopAddr);
				$('#shop-phone').val(shop.phone);
				$('#shop-desc').val(shop.shopDesc);
				var shopCategory = '<option data-id="'
						+ shop.shopCategory.shopCategoryId + '" selected>'
						+ shop.shopCategory.shopCategoryName + '</option>';
				var tempAreaHtml = '';
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				$('#shop-category').html(shopCategory);
				//取出后，不能修改值
				$('#shop-category').attr('disabled','disabled');
				$('#area').html(tempAreaHtml);
				$("#area option[data-id='"+shop.area.areaId+"']").attr('selected',"selected");
			}
		});
	}
})
