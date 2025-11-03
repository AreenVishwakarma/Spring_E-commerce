package com.example.Spring_E_Commerce.controller;

import com.example.Spring_E_Commerce.config.Razorpay_Config;
import com.example.Spring_E_Commerce.dto.CheckoutForm;
import com.example.Spring_E_Commerce.model.Catalog_Model;
import com.example.Spring_E_Commerce.model.Details_Model;
import com.example.Spring_E_Commerce.model.OrderDetail;
import com.example.Spring_E_Commerce.repository.Catalog_Repository;
import com.example.Spring_E_Commerce.repository.Admin_Repository;
import com.example.Spring_E_Commerce.repository.Detailrepo;
import com.example.Spring_E_Commerce.repository.OrderDetailRepository;
import com.example.Spring_E_Commerce.service.Payment_Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.HashMap;

@Controller
public class Catalog_Controller {

    @Autowired
    private Catalog_Repository Catalogrepo;
    
    
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private Admin_Repository adminRepository;
    
     @Autowired
    private Payment_Service paymentService;

    @Autowired
    private Razorpay_Config razorpayConfig;
    
     @Autowired
    private Detailrepo detailrepo;
     
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Root URL mapping
    @GetMapping("/")
    public String home() {
        return "redirect:/catalog/list";
    }

//<------------------------Add Products-------------------->
    // Show Add Product Form
    @GetMapping("/catalog/form")
    public String showForm(Model model, HttpSession session) {
        Object authed = session != null ? session.getAttribute("ADMIN_AUTH") : null;
        if (!(authed instanceof Boolean) || !((Boolean) authed)) {
            if (session != null) session.setAttribute("INTENDED_URL", "/catalog/form");
            return "redirect:/catalog/admin/login";
        }
        model.addAttribute("product", new Catalog_Model());
        return "Add_Products";
    }

    // Admin login page
    @GetMapping("/catalog/admin/login")
    public String adminLoginPage() {
        return "Admin_Login";
    }

    // Admin login (basic email+password check)
    @PostMapping("/catalog/admin/login")
    public String adminLogin(@RequestParam("email") String email,
                             @RequestParam("password") String password,
                             Model model,
                             HttpSession session) {
        boolean ok = adminRepository.findByEmailAndPassword(email, password).isPresent();
        if (ok) {
            if (session != null) {
                session.setAttribute("ADMIN_AUTH", true);
                Object intended = session.getAttribute("INTENDED_URL");
                if (intended instanceof String) {
                    String url = (String) intended;
                    session.removeAttribute("INTENDED_URL");
                    return "redirect:" + url;
                }
            }
            return "redirect:/catalog/orders";
        }
        model.addAttribute("error", "Invalid credentials");
        return "Admin_Login";
    }
    
    // Admin logout
    @GetMapping("/catalog/admin/logout")
    public String adminLogout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/catalog/admin/login";
    }
    // Save Product
    @PostMapping("/catalog/save")
    public String saveForm(@Valid @ModelAttribute("product") Catalog_Model product,
                           BindingResult bindingResult,
                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                           Model model) {
        try {
            // Check for validation errors
            if (bindingResult.hasErrors()) {
                model.addAttribute("error", "Please correct the errors below and try again.");
                return "Add_Products";
            }
            
            // Check if unum already exists
            if (product.getUnum() != null && !product.getUnum().isEmpty()) {
                boolean unumExists = Catalogrepo.findByUnum(product.getUnum()).isPresent();
                if (unumExists) {
                    model.addAttribute("error", "A product with this unique number already exists.");
                    return "Add_Products";
                }
            }
            
            // Handle optional image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String contentType = imageFile.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    model.addAttribute("error", "Only image files are allowed.");
                    return "Add_Products";
                }
                String uploadsDir = "src/main/resources/static/uploads";
                Path uploadPath = Paths.get(uploadsDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String original = imageFile.getOriginalFilename();
                if (original == null) {
                    original = "image";
                }
                String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')) : "";
                String filename = UUID.randomUUID().toString().replace("-", "") + ext;
                Path filePath = uploadPath.resolve(filename);
                // replace if exists just in case
                Files.copy(imageFile.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                // Store web-accessible path
                product.setImage("/uploads/" + filename);
            }

            // Save to database
            Catalog_Model savedProduct = Catalogrepo.save(product);
            model.addAttribute("success", "Product saved successfully! Product ID: " + savedProduct.getId());
            model.addAttribute("product", new Catalog_Model()); // Reset form
            return "Add_Products";
        } catch (Exception e) {
            model.addAttribute("error", "Error saving product: " + e.getMessage());
            return "Add_Products";
        }
    }

    // List Products
    @GetMapping("/catalog/orders")
    public String listOrders(Model model, HttpSession session) {
        Object authed = session != null ? session.getAttribute("ADMIN_AUTH") : null;
        if (!(authed instanceof Boolean) || !((Boolean) authed)) {
            if (session != null) session.setAttribute("INTENDED_URL", "/catalog/orders");
            return "redirect:/catalog/admin/login";
        }
        model.addAttribute("orders", detailrepo.findAll());
        var allItems = orderDetailRepository.findAll();
        java.util.Map<String, java.util.List<OrderDetail>> itemsByOrder = new java.util.HashMap<>();
        for (OrderDetail it : allItems) {
            if (it == null || it.getOrderID() == null) continue;
            itemsByOrder.computeIfAbsent(it.getOrderID(), k -> new java.util.ArrayList<>()).add(it);
        }
        model.addAttribute("orderItemsByOrderId", itemsByOrder);
        return "Orders_Details";
    }

    // Restore products list view
    @GetMapping("/catalog/list")
    public String listProducts(Model model) {
        model.addAttribute("products", Catalogrepo.findAll());
        return "View_Products";
    }
//<------------------------End---------------------------->
    
//<------------------------Buy Product-------------------->
    
    // Show Add Details Form
    @GetMapping("/catalog/cart-form")
    public String showBuyForm(Model model) {
        model.addAttribute("product", new Details_Model());
        return "Buy_Products";
    }
    
    @GetMapping("/catalog/cart")
    public String viewCart(@RequestParam(value = "orderId", required = false) String orderId, Model model, HttpSession session) {
        if (orderId != null && !orderId.isBlank()) {
            var headerOpt = detailrepo.findFirstByOrderID(orderId);
            var items = orderDetailRepository.findByOrderID(orderId);
            headerOpt.ifPresent(header -> model.addAttribute("order", header));
            model.addAttribute("orderItems", items);
        }
        return "Cart_Products";
    }

    // Save Details
    @PostMapping("/catalog/cart-save")
    public String saveBuyForm(
            @Valid @ModelAttribute("product") CheckoutForm form,
            BindingResult bindingResult,
            Model model) {
        try {
            // Validate form fields (includes card fields)
            if (bindingResult.hasErrors()) {
                model.addAttribute("error", "Please correct the errors below and try again.");
                return "Buy_Products";
            }

            String cartPayload = form.getCartPayload();
            if (cartPayload == null || cartPayload.trim().isEmpty()) {
                model.addAttribute("error", "Your cart is empty. Add items before placing the order.");
                return "Buy_Products";
            }

            var items = objectMapper.readValue(cartPayload, new TypeReference<java.util.List<java.util.Map<String, Object>>>() {});
            if (items == null || items.isEmpty()) {
                model.addAttribute("error", "Your cart is empty. Add items before placing the order.");
                return "Buy_Products";
            }

            // Generate Order ID
            String orderId = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();

            // Calculate total
            double total = 0.0;
            for (Map<String, Object> item : items) {
                double price = 0.0;
                Object priceObj = item.get("price");
                if (priceObj instanceof Number) {
                    price = ((Number) priceObj).doubleValue();
                } else if (priceObj != null) {
                    try { price = Double.parseDouble(String.valueOf(priceObj)); } catch (NumberFormatException ignored) {}
                }

                int qty = 1;
                Object qtyObj = item.get("qty");
                if (qtyObj instanceof Number) {
                    qty = ((Number) qtyObj).intValue();
                } else if (qtyObj != null) {
                    try { qty = Integer.parseInt(String.valueOf(qtyObj)); } catch (NumberFormatException ignored) {}
                }
                total += price * (double) qty;
            }

            // Map to persisted Details_Model (do NOT copy payment fields)
            Details_Model orderHeader = new Details_Model();
            orderHeader.setEmail(form.getEmail());
            orderHeader.setFirstName(form.getFirstName());
            orderHeader.setLastName(form.getLastName());
            orderHeader.setAddress(form.getAddress());
            orderHeader.setApartment(form.getApartment());
            orderHeader.setCity(form.getCity());
            orderHeader.setPincode(form.getPincode());
            orderHeader.setState(form.getState());
            orderHeader.setCountry(form.getCountry());
            orderHeader.setOrderID(orderId);
            orderHeader.setTotalPrice(total);
            orderHeader.setPaymentStatus("PENDING");

            // Persist order header
            detailrepo.save(orderHeader);

            // Persist order items
            for (Map<String, Object> item : items) {
                String productName = String.valueOf(item.getOrDefault("name", ""));
                int qty = 1;
                Object qtyObj = item.get("qty");
                if (qtyObj instanceof Number) {
                    qty = ((Number) qtyObj).intValue();
                } else if (qtyObj != null) {
                    try { qty = Integer.parseInt(String.valueOf(qtyObj)); } catch (NumberFormatException ignored) {}
                }
                OrderDetail row = new OrderDetail(orderId, productName, qty);
                orderDetailRepository.save(row);
            }

            // Immediately clear sensitive values in the DTO (minimize time in memory)
            // Note: Card fields removed since we're using Razorpay

            // Create Razorpay order server-side
            int amountPaise = (int) Math.round(total * 100);
            Map<String, Object> razorpayOrder = paymentService.createRazorpayOrder(amountPaise, "INR", orderId);

            // Add attributes for Thymeleaf checkout page
            model.addAttribute("razorpayOrderId", String.valueOf(razorpayOrder.get("id")));
            model.addAttribute("amount", razorpayOrder.get("amount"));
            model.addAttribute("currency", razorpayOrder.get("currency"));
            model.addAttribute("key", razorpayConfig.getKey());
            model.addAttribute("orderId", orderId);
            model.addAttribute("email", orderHeader.getEmail());
            model.addAttribute("name", orderHeader.getFirstName() + " " + orderHeader.getLastName());
            model.addAttribute("storeName", "Your Store Name");

            return "razorpay_checkout";
        } catch (Exception e) {
            model.addAttribute("error", "Error saving order details or creating payment: " + e.getMessage());
            return "Buy_Products";
        }
    }

    // Payment success page
    @GetMapping("/catalog/order/success/{orderId}")
    public String paymentSuccess(@PathVariable String orderId, Model model) {
        var orderOpt = detailrepo.findFirstByOrderID(orderId);
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            model.addAttribute("orderId", orderId);
            return "Payment_Success";
        }
        return "redirect:/catalog/list";
    }

    // Payment failure page
    @GetMapping("/catalog/order/failure/{orderId}")
    public String paymentFailure(@PathVariable String orderId, Model model) {
        var orderOpt = detailrepo.findFirstByOrderID(orderId);
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            model.addAttribute("orderId", orderId);
            return "Payment_Failure";
        }
        return "redirect:/catalog/list";
    }
//<---------------------------End------------------------------>
}



