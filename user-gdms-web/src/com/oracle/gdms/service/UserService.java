package com.oracle.gdms.service;

import com.oracle.gdms.entity.UserMode;

public interface UserService {
	
	/**
	 * ���Ñ�ע��ҵ��
	 * @param user
	 * @return
	 */
	int add(UserMode  user);

	UserMode login(UserMode user);

	/**
	 * ����ֻ������Ƿ�ռ��
	 * @param mobile
	 * @return
	 */
	boolean hasMobile(String mobile);
}
