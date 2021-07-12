package com.delivery.delivery.controller;

import com.delivery.delivery.Status;
import com.delivery.delivery.dto.DeliveryDTO;
import com.delivery.delivery.model.Courier;
import com.delivery.delivery.model.Delivery;
import com.delivery.delivery.service.CourierService;
import com.delivery.delivery.service.DeliveryService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DeliveryRestController {

    DeliveryService deliveryService;
    CourierService courierService;

    public DeliveryRestController(DeliveryService deliveryService, CourierService courierService) {
        this.deliveryService = deliveryService;
        this.courierService = courierService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Void> createDelivery(@RequestBody DeliveryDTO deliveryDTO) {

        Delivery delivery = deliveryDTO.fromDelivery();
        deliveryService.add(delivery);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Void> editDelivery(@RequestBody DeliveryDTO deliveryDTO, @PathVariable long id) {

        Delivery deliveryUpdate = deliveryDTO.fromDelivery();
        Delivery deliveryOld = deliveryService.getById(id);

        if (deliveryOld == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        deliveryUpdate.setId(id);
        deliveryUpdate.setStatus(deliveryOld.getStatus());
        deliveryUpdate.setCourier(deliveryOld.getCourier());

        deliveryService.add(deliveryUpdate);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST, value = "/delivery/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> getDelivery(@PathVariable long id) {

        Delivery delivery = deliveryService.getById(id);

        if (delivery == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(delivery);

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/canceled/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Void> canceledDelivery(@PathVariable long id) {

        Delivery delivery = deliveryService.getById(id);

        if (delivery == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        delivery.setStatus(Status.CANCELED);

        if (delivery.getCourier() != null) {
            delivery.getCourier().setDelivery(null);
            delivery.getCourier().setStatus(Status.EXPECTED);
            delivery.setCourier(null);
        }

        deliveryService.add(delivery);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/deliveries", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<Delivery>> getAllDeliveries() {

        ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryService.getAll();

        if (deliveries == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(deliveries, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/deliveries/active", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<Delivery>> getActiveDeliveries() {

        ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryService.getActiveDelivery();

        if (deliveries == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(deliveries, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delivery/{id_delivery}/courier/add", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Void> addCourier(@PathVariable long id_delivery) {

        Delivery delivery = deliveryService.getById(id_delivery);
        Courier courier = courierService.getExpectedCourier();

        if (delivery == null || courier == null || delivery.getStatus() == Status.CANCELED) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        delivery.setStatus(Status.ACTIVE);
        delivery.setCourier(courier);
        courier.setDelivery(delivery);
        courier.setStatus(Status.ACTIVE);

        deliveryService.add(delivery);
        courierService.add(courier);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delivery/{id_delivery}/courier/add/{id_courier}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Void> addCourier(@PathVariable long id_delivery, @PathVariable long id_courier) {

        Delivery delivery = deliveryService.getById(id_delivery);
        Courier courier = courierService.getById(id_courier);

        if (delivery == null || courier == null || delivery.getStatus() == Status.CANCELED || courier.getStatus() == Status.ACTIVE) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        delivery.setStatus(Status.ACTIVE);
        delivery.setCourier(courier);
        courier.setDelivery(delivery);
        courier.setStatus(Status.ACTIVE);

        deliveryService.add(delivery);
        courierService.add(courier);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/courier/add", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Void> addCourierAll(@RequestBody List<Courier> courierList) {

        ArrayList<Courier> couriers = (ArrayList<Courier>) courierList;

        if (couriers == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        couriers.stream().forEach(x -> courierService.add(x));

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
