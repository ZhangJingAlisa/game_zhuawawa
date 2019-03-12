package com.zhua.game.service.user.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhua.game.base.BaseService;
import com.zhua.game.entity.UserAddressEntity;
import com.zhua.game.service.user.mapper.UserAddressMapper;
import com.zhua.game.service.user.vo.UserAddressVO;

@Service
public class UserAddressService extends BaseService {

	@Autowired
	private UserAddressMapper userAddressMapper;
	
	@Transactional
	public UserAddressVO createUserAddressEntity(String userId,String address,String contactName,String contactPhone){
		UserAddressEntity entity =  userAddressMapper.getUserAddressEntityByUserId(userId);
		if(entity == null){
			entity = initUserAddressEntity(address, userId, contactName, contactPhone);
			userAddressMapper.saveUserAddressEntity(entity);
		}
		return buildUserAddressVO(entity);
	}
	
	@Transactional
	public UserAddressVO updateUserAddressVO(String userId,String address,String contactName,String contactPhone){
		UserAddressEntity entity =  userAddressMapper.getUserAddressEntityByUserId(userId);
		if(entity != null){
			entity.setAddress(address != null ? address : entity.getAddress() );
			entity.setContactName(contactName != null ? contactName : entity.getContactName());
			entity.setContactPhone(contactPhone != null ? contactPhone : entity.getContactPhone());
			entity.setUtime(new Date());
			userAddressMapper.updateUserAddressEntity(entity);
		}
		return buildUserAddressVO(entity);
	}
	
	public UserAddressVO getUserAddressByUserId(String userId){
		UserAddressEntity entity =  userAddressMapper.getUserAddressEntityByUserId(userId);
		return buildUserAddressVO(entity);
	}
	
	
	
	private UserAddressVO buildUserAddressVO(UserAddressEntity entity){
		UserAddressVO vo = new UserAddressVO();
		if(entity != null){
			vo.setAddress(entity.getAddress());
			vo.setContactName(entity.getContactName());
			vo.setContactPhone(entity.getContactPhone());
			vo.setId(entity.getId());
			vo.setUserId(entity.getUserId());
		}
		return vo;
	}
	
	private UserAddressEntity initUserAddressEntity(String address,String userId,String contactName,String contactPhone){
		UserAddressEntity entity = new UserAddressEntity();
		entity.setAddress(address);
		entity.setContactName(contactName);
		entity.setContactPhone(contactPhone);
		entity.setCtime(new Date());
		entity.setId(generatID());
		entity.setUserId(userId);
		entity.setUtime(new Date());
		return entity;
	}
}
