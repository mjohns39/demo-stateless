package com.demo.authserver.user.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;

@Entity
@Data
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    @Column(nullable = false, unique = true)
    private String username;
 
    private String password;
    
//    @Column
//    @ElementCollection(targetClass = String.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_privileges", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges;
    
}
