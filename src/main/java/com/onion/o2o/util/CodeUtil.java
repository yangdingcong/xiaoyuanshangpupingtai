package com.onion.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
	public static boolean checkVerifyCode(HttpServletRequest request) {
		//获得图片的验证码的正确值
		String verifyCodeExpected=(String) request.getSession()
				.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		//用户输入的值
		String verifyCodeActual=HttpServletRequestUtil.getString(request,"verifyCodeActual");
		//判断是否为空或者不相等
		if(verifyCodeActual==null||!verifyCodeActual.equals(verifyCodeExpected)) {
			return false;
		}
		return true;
	}
}
