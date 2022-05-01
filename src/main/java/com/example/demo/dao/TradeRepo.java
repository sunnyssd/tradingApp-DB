package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Trade;

public interface TradeRepo extends JpaRepository<Trade, String>{

}
