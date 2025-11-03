package com.example.Spring_E_Commerce.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orderID", nullable = false)
    private String orderID;

    @Column(name = "productName", nullable = false)
    private String productName;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    public OrderDetail() {}

    public OrderDetail(String orderID, String productName, Integer qty) {
        this.orderID = orderID;
        this.productName = productName;
        this.qty = qty;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderID() { return orderID; }
    public void setOrderID(String orderID) { this.orderID = orderID; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
}


