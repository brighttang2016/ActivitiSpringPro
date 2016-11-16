/**
 * 用户管理测试2016-08-31
 */
package com.sys.service.impl;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.springframework.stereotype.Service;

@Service("class5_1")
public class Class5_1 {
	@Resource
	public IdentityService  identityService;
	public void testUser(){
		System.out.println("class5_1->testUser");
		User user = identityService.newUser("tangliang");
		user.setId("2008");
		user.setFirstName("唐");
		user.setLastName("亮");
		user.setPassword("200810405234");
		identityService.deleteUser("2008");
		identityService.saveUser(user);
		
		User userInDb = identityService.createUserQuery().userId("2008").singleResult();
		System.out.println("userInDb.getFirstName():"+userInDb.getFirstName());
	}
}
