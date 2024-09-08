package com.pt.authms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.authms.model.entity.Userms;



@Repository
public interface UsermsRepository extends JpaRepository<Userms, Long>{
	
	public Optional<Userms> findByUserName(String userName);

}
