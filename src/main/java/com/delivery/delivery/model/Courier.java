package com.delivery.delivery.model;

import com.delivery.delivery.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Table(name = "COURIER")
public class Courier {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    String name;
    Status status;
    String numberPhone;

    @JsonBackReference
    @OneToOne(mappedBy = "courier")
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Override
    public String toString() {
        return "Courier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", numberPhone='" + numberPhone + '\'' +
                '}';
    }
}
