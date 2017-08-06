package com.apabi.center.dao;

import org.apache.ibatis.annotations.Param;

import com.apabi.center.entity.Client;

public interface ClientMapper {
	Client findClientByIdURI(@Param("clientId") String clientId, @Param("redirectURI") String redirectURI);

}
