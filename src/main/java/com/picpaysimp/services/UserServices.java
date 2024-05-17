package com.picpaysimp.services;


import com.picpaysimp.domain.user.User;
import com.picpaysimp.domain.user.UserType;
import com.picpaysimp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServices {
    @Autowired
    private UserRepository repository;

    public void validadeTransaction(User sender, BigDecimal amount) throws Exception {
        if(sender.getUserType() == UserType.MERCHANT ){
            throw new Exception("Usuário do tipo MERCHANT não pode fazer transações");
        }
        if(sender.getBalance().compareTo(amount) < 0){
            throw new Exception("Saldo insuficiente");

        }
    }

    public User findUserById(Long id) throws Exception {
        return repository.findUserById(id).orElseThrow(()-> new Exception("Usuário não encontrado"));
    }

    public void createUser(User user) throws Exception {
        repository.save(user);
    }
}
