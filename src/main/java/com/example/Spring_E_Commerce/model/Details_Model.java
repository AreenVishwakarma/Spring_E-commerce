package com.example.Spring_E_Commerce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Updated Notes:
 * - Payment fields are kept transient and have NO validation annotations here.
 * - All validation for card/payment info must happen in CheckoutForm (DTO).
 * - This prevents JPA/Bean Validation from failing on persist.
 */
@Entity
@Table(name = "details")
public class Details_Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Contact / shipping fields (same as before)...
    @Column(name = "email")
    private String email;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "address")
    private String address;
    @Column(name = "apartment")
    private String apartment;
    @Column(name = "city")
    private String city;
    @Column(name = "pincode")
    private String pincode;
    @Column(name = "state")
    private String state;
    @Column(name = "country")
    private String country;

    // Payment fields — transient and WITHOUT validation annotations here
    @Transient
    private String cardNum;

    @Transient
    private String cardName;

    @Transient
    private String cvv;

    @Transient
    private String expiryDate;

    @Column(name = "orderID")
    private String orderID;

    @Column(name = "totalPrice")
    private Double totalPrice;

    @Column(name = "paymentStatus")
    private String paymentStatus = "PENDING";

    @Column(name = "paymentMethod")
    private String paymentMethod;

    @Column(name = "transactionId")
    private String transactionId;

    @Column(name = "orderDate", insertable = false, updatable = false)
    private LocalDateTime orderDate;

    // Default constructor, getters and setters (only key ones shown here)
    public Details_Model() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getApartment() { return apartment; }
    public void setApartment(String apartment) { this.apartment = apartment; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    // Transient payment getters/setters (kept for safety but no validation here)
    public String getCardNum() { return cardNum; }
    public void setCardNum(String cardNum) { this.cardNum = cardNum; }

    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getOrderID() { return orderID; }
    public void setOrderID(String orderID) { this.orderID = orderID; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
}