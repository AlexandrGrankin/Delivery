package com.delivery.delivery.dto;

public class ProductDTO {

    public String name;
    public double weight;
    public int count;

    @Override
    public String toString() {
        return "ProductDTO{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", count=" + count +
                '}';
    }

}
