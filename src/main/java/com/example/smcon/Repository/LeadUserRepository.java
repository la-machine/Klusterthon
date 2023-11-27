package com.example.smcon.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.smcon.Model.LeadUsers;
import com.example.smcon.Model.Role;

public interface LeadUserRepository extends JpaRepository<LeadUsers, Long>{
    LeadUsers findByEmail(String email);
    List<LeadUsers> findByRole(Role role);
}
