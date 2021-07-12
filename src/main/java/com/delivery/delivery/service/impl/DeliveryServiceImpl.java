package com.delivery.delivery.service.impl;

import com.delivery.delivery.Status;
import com.delivery.delivery.model.Delivery;
import com.delivery.delivery.repo.DeliveryRepository;
import com.delivery.delivery.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    
    DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }
    
    @Override
    public Delivery getById(long id) {
        Delivery delivery = deliveryRepository.getById(id);
        try{
            delivery.getAddress();
        }catch (EntityNotFoundException e){
            return null;
        }
        return delivery;
    }

    @Override
    public Delivery add(Delivery delivery) {
        return deliveryRepository.saveAndFlush(delivery);
    }

    @Override
    public List<Delivery> getAll() {
        return deliveryRepository.findAll();
    }

    @Override
    public List<Delivery> getActiveDelivery() {
        List<Delivery> deliveries = deliveryRepository.findAll().stream().filter(x -> x.getStatus() == Status.ACTIVE).collect(Collectors.toList());
        return deliveries;
    }
}
