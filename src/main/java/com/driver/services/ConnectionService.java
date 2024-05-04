package com.driver.services;


import com.driver.model.User;

public interface ConnectionService{

    public User communicate(int senderId, int receiverId) throws Exception;
    public User disconnect(int userId) throws Exception;
    public User connect(int userId, String countryName) throws Exception;

}