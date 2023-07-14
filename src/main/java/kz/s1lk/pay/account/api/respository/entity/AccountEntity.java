package kz.s1lk.pay.account.api.respository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AccountEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String number;
    private String ownerName;
    private String password;
    private String ownerSecondName;
    private double balance;
    private boolean active;
}
