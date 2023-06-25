package com.example.AloneShip.service;


import com.example.AloneShip.mapper.AccountMapper;
import entitys.auth.AccountInfo;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Resource
    private AccountMapper accountMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountInfo info = accountMapper.accountInfo(username);
        if(info==null) throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(info.getUsername())
                .password(info.getPassword())
                .roles(info.getRole())
                .build();
    }
}
