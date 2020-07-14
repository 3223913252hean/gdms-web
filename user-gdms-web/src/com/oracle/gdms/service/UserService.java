package com.oracle.gdms.service;

import com.oracle.gdms.entity.UserMode;

public interface UserService {
	
	/**
	 * 新用戶注册业务
	 * @param user
	 * @return
	 */
	int add(UserMode  user);

	UserMode login(UserMode user);

	/**
	 * 检查手机号码是否被占用
	 * @param mobile
	 * @return
	 */
	boolean hasMobile(String mobile);
}
