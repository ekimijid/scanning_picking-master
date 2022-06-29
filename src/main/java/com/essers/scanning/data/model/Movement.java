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
import java.util.Date;

import static com.essers.scanning.data.constants.Constants.MOVEMENT_STATUS_TODO;

@Table(name = "movement", schema = "public")
@Entity
public final class Movement {
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
    @JoinColumn(name = "movement_type_id", nullable = false)
    private MovementType movementType;

    @Column(name = "priority", nullable = false)
    private int priority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "picking_id", nullable = false)
    private Picking picking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "UOM", nullable = false)
    private String uom;

    @Column(name = "location_from", nullable = false)
    private String locationFrom;

    @Column(name = "location_to", nullable = false)
    private String locationTo;

    @Column(name = "in_progress_timestamp")
    private Date inProgressTimestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "in_progress_user_id")
    private User user;

    @Column(name = "status", nullable = false)
    private String status;

    public Movement(Long id, Company company, String wmsSite,
                    String wmsWarehouse, MovementType movementType, int priority, Picking picking, Product product,
                    int quantity, String uom, String locationFrom, String locationTo) {
        this.id = id;
        this.company = company;
        this.wmsSite = wmsSite;
        this.wmsWarehouse = wmsWarehouse;
        this.movementType = movementType;
        this.priority = priority;
        this.picking = picking;
        this.product = product;
        this.quantity = quantity;
        this.uom = uom;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.status = MOVEMENT_STATUS_TODO;
    }

    public Movement() {
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

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Picking getPicking() {
        return picking;
    }

    public void setPicking(Picking picking) {
        this.picking = picking;
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

    public String getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(String locationFrom) {
        this.locationFrom = locationFrom;
    }

    public String getLocationTo() {
        return locationTo;
    }

    public void setLocationTo(String locationTo) {
        this.locationTo = locationTo;
    }

    public Date getInProgressTimestamp() {
        return (Date) inProgressTimestamp.clone();
    }

    public void setInProgressTimestamp(Date inProgressTimestamp) {
        this.inProgressTimestamp = (Date) inProgressTimestamp.clone();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
