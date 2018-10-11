$(function () {
	getlist();
	function getlist(e) {
		$.ajax({
			url : "/o2o/shopadmin/getshoplist",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					//渲染
					handleList(data.shopList);
					handleUser(data.user);
				}
			}
		});
	}

	function handleUser(data) {
		//在id为user-name的地方显示我们从后台取出的名字
		$('#user-name').text(data.name);
	}
	//遍历出data对象，然后拼接出每一行
	function handleList(data) {
		var html = '';
		data.map(function (item, index) {
			html += '<div class="row row-shop"><div class="col-40">'
				+ item.shopName +'</div><div class="col-40">'
				+ shopStatus(item.enableStatus) 
				+'</div><div class="col-20">'
				+ goShop(item.enableStatus, item.shopId) 
				+'</div></div>';

		});
		//追加到shop-wrap这个控件中
		$('.shop-wrap').html(html);
	}
	//点击“进入”时,能够进入到店铺管理页面
	function goShop(status, id) {
		if (status != 0 && status != -1) {
			return '<a href="/o2o/shopadmin/shopmanagement?shopId='+ id +'">进入</a>';
		} else {
			return '';
		}
	}

	function shopStatus(status) {
		if (status == 0) {
			return '审核中';
		} else if (status == -1) {
			return '店铺非法';
		} else {
			return '审核通过';
		}
	}


	/*$('#log-out').click(function () {
		$.ajax({
			url : "/o2o/shop/logout",
			type : "post",
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					window.location.href = '/o2o/shop/ownerlogin';
				}
			},
			error : function(data, error) {
				alert(error);
			}
		});
	});*/

});
