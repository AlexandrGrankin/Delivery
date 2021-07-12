package com.delivery.delivery.dto;

import com.delivery.delivery.Status;
import com.delivery.delivery.model.Courier;
import com.delivery.delivery.model.Delivery;
import com.delivery.delivery.model.Product;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeliveryDTO {

    private Long id;
    public String address;
    public String date;
    public List<Product> products = new ArrayList<>();
    private Status status;
    private Courier courier;

    @SneakyThrows
    public Delivery fromDelivery(){

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        Delivery delivery = new Delivery();

        delivery.setAddress(address);
        delivery.setStatus(Status.EXPECTED);
//        delivery.setDate(format.parse(date));
        delivery.setDate(LocalDateTime.parse(date, format));

        products.stream().forEach(x -> x.setDelivery(delivery));
        delivery.setProducts(products);

        return delivery;
    }

    @Override
    public String toString() {
        return "DeliveryDTO{" +
                "address='" + address + '\'' +
                ", products=" + products +
                '}';
    }
}
