package com.delivery.delivery.service;

import com.delivery.delivery.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierService {

    Courier getById(long id);

    Courier add(Courier courier);

    Courier getExpectedCourier();

}
