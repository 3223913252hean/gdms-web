package com.oracle.gdms.dao;

import com.oracle.gdms.entity.UserMode;

public interface UserDao {

	public int add(UserMode user);

	public UserMode login(UserMode user);

	public int hasMobile(String mobile);
}
