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
public class userService implements UserDetailsService {
    @Resource
    private AccountMapper accountMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountInfo accountInfo = accountMapper.accountInfo(username);
        if(accountInfo == null){
            throw new UsernameNotFoundException("user not found");
        }
        return User
                .withUsername(accountInfo.getUsername())
                .password(accountInfo.getPassword())
                .roles(accountInfo.getRole())
                .build();
    }
}
