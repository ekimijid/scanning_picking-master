package com.essers.scanning.data.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static com.essers.scanning.data.constants.Constants.PICKING_STATUS_TODO;

@Table(name = "picking", schema = "public")
@Entity
public final class Picking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "wms_site", nullable = false)
    private String wmsSite;

    @Column(name = "wms_warehouse", nullable = false)
    private String wmsWarehouse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "UOM", nullable = false)
    private String uom;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "status", nullable = false)
    private String status;

    public Picking(Long id, Company company, String wmsWarehouse,
                   String wmsSite, Product product, int quantity,
                   String uom, String location) {
        this.id = id;
        this.company = company;
        this.wmsWarehouse = wmsWarehouse;
        this.wmsSite = wmsSite;
        this.product = product;
        this.quantity = quantity;
        this.uom = uom;
        this.location = location;
        this.status = PICKING_STATUS_TODO;
    }

    public Picking() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getWmsSite() {
        return wmsSite;
    }

    public void setWmsSite(String wmsSite) {
        this.wmsSite = wmsSite;
    }

    public String getWmsWarehouse() {
        return wmsWarehouse;
    }

    public void setWmsWarehouse(String wmsWarehouse) {
        this.wmsWarehouse = wmsWarehouse;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
