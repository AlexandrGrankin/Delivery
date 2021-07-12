package com.delivery.delivery.service;

import com.delivery.delivery.model.Delivery;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryService {

    Delivery getById(long id);
    Delivery add(Delivery delivery);
    List<Delivery> getAll();

//    @Query("select * from delivery d where d.status = ACTIVE")
    List<Delivery> getActiveDelivery();

}
