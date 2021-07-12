package com.delivery.delivery.service.impl;

import com.delivery.delivery.Status;
import com.delivery.delivery.model.Courier;
import com.delivery.delivery.repo.CourierRepository;
import com.delivery.delivery.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourierServiceImpl implements CourierService {

    CourierRepository courierRepository;

    @Autowired
    public CourierServiceImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Override
    public Courier getById(long id) {
        return courierRepository.getById(id);
    }

    @Override
    public Courier add(Courier courier) {
        return courierRepository.saveAndFlush(courier);
    }

    @Override
    public Courier getExpectedCourier() {
        Courier courier = courierRepository.findAll().stream().filter(x->x.getStatus() == Status.EXPECTED).findFirst().orElse(null);
        return courier;
    }
}
