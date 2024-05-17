package com.picpaysimp.services;


import com.picpaysimp.domain.transaction.Transaction;
import com.picpaysimp.domain.user.User;
import com.picpaysimp.dtos.TransactionDTO;
import com.picpaysimp.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private UserServices userServices;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
        User sender = this.userServices.findUserById(transactionDTO.senderId());
        User receiver = this.userServices.findUserById(transactionDTO.receiverId());

        System.out.println(transactionDTO.senderId());
        System.out.println(transactionDTO.receiverId());

        this.userServices.validateTransaction(sender,transactionDTO.amount());


//        Mock de autorização não funciona
//        if(!authorizeTransaction(sender,transactionDTO.amount())){
//            throw new Exception("Transação não autorizada");
//        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.amount());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDTO.amount()));
        receiver.setBalance(receiver.getBalance().add(transactionDTO.amount()));

        repository.save(transaction);
        this.userServices.saveUser(sender);
        this.userServices.saveUser(receiver);

        notificationService.sendNotification(sender, transaction.toString());
        notificationService.sendNotification(receiver, transaction.toString());

        return transaction;
    }

    public List<Transaction> getAllTransactions(){
        return repository.findAll();
    }

    public boolean authorizeTransaction(User sender, BigDecimal amount){
        ResponseEntity<Map> responseAuth =  restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);
        return responseAuth.getStatusCode() == HttpStatus.OK;
    }
}
