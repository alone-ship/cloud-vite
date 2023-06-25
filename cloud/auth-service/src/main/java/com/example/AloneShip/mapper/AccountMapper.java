package com.example.AloneShip.mapper;

import entitys.auth.AccountInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper {
    @Select("SELECT * FROM account WHERE username = #{username}")
    AccountInfo accountInfo(String username);
}
