/**
 * 
 */
$(function() {
	//获取此店铺下的商品列表的url
	var listUrl='/o2o/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=999';
	//商品下架url
	var deleteUrl ='/o2o/shopadmin/modifyproduct';

	//获取此店铺下的商品列表
	function getList() {
		//从后台获取此店铺的商品列表
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var productList = data.productList;
				var tempHtml = '';
				//遍历每条商品信息，拼接成一行显示，列信息包括：
				//商品名称，优先级，上架\下架，编辑按钮
				//预览（未实现）
				productList.map(function(item, index) {
					//默认为下架
					var textOp = "下架";
					var contraryStatus = 0;
					if (item.enableStatus == 0) {
						//若状态值为0，将下架替换成上架
						textOp = "上架";
						//点击上架后，将状态置为1，上架变成下架
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					//拼接每件商品的行信息
					tempHtml += '' + '<div class="row row-product">'
							+ '<div class="col-30">'
							+ item.productName
							+ '</div>'
							+ '<div class="col-20">'
							+ item.priority
							+ '</div>'
							+ '<div class="col-50">'
							+ '<a href="#" class="edit" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">编辑</a>'
							+ '<a href="#" class="delete" data-id="'
							+ item.productId
							+ '" data-status="'
							+ contraryStatus
							+ '">'
							+ textOp
							+ '</a>'
							+ '<a href="#" class="preview" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">预览</a>'
							+ '</div>'
							+ '</div>';
				});
				$('.product-wrap').html(tempHtml);
			}
		});
	}

	getList();

	function deleteItem(id, enableStatus) {
		var product = {};
		product.productId = id;
		product.enableStatus = enableStatus;
		$.confirm('确定么?', function() {
			$.ajax({
				url : deleteUrl,
				type : 'POST',
				data : {
					productStr : JSON.stringify(product),
					statusChange : true
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('操作成功！');
						getList();
					} else {
						$.toast('操作失败！');
					}
				}
			});
		});
	}

	$('.product-wrap')
			.on(
					'click',
					'a',
					function(e) {
						var target = $(e.currentTarget);
						//根据传入的id值进入不同的操作
						if (target.hasClass('edit')) {
							window.location.href = '/o2o/shopadmin/productoperation?productId='
									+ e.currentTarget.dataset.id;
						} else if (target.hasClass('delete')) {
							deleteItem(e.currentTarget.dataset.id,
									e.currentTarget.dataset.status);
						} else if (target.hasClass('preview')) {
							//未实现
							window.location.href = '/o2o/frontend/productdetail?productId='
									+ e.currentTarget.dataset.id;
						}
					});

	$('#new').click(function() {
		window.location.href = '/o2o/shopadmin/productoperation';
	});
});