package be.uantwerpen.labplanner.Model;


import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import be.uantwerpen.labplanner.common.model.stock.Unit;
import be.uantwerpen.labplanner.common.model.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.DoubleType;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class OwnProduct extends AbstractPersistable<Long> {
    private static String DEFAULT_PRODUCTNAME = "default_productname";


    @Column
    @NotBlank(
            message = "{invalid.notblank}"
    )
    private String name;

    @Column
    private URL url;

    @Column
    private String description;
    @Column(
            name = "unit_cost"
    )
    @PositiveOrZero(
            message = "{product.invalid.cost}"
    )
    private Double unitCost;
    @Column(
            name = "stock_level"
    )
    private Double stockLevel;
    @Column(
            name = "low_stock_level"
    )
    private Double lowStockLevel;
    @Column(
            name = "reserved_stock_level"
    )
    private Double reservedStockLevel;
    @Column
    private Unit units;
    @Column(
            name = "image_name"
    )
    private String imageName;
    @Column(
            name = "properties_url"
    )
    private String properties;
    @Column(
            name = "product_creator_id"
    )
    private Long productCreatorId;
    @Column(
            name = "last_updated_by_id"
    )
    private Long lastUpdatedById;
    @Column(
            name = "created_on"
    )
    @CreationTimestamp
    private LocalDateTime createDateTime;
    @Column(
            name = "updated_on"
    )
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "aPROD_TAG",
            joinColumns = {@JoinColumn(
                    name = "aPROD_ID",
                    referencedColumnName = "ID"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "aTAG_ID",
                    referencedColumnName = "ID"
            )}
    )
    private List<OwnTag> tags;

    @ElementCollection
    @CollectionTable(name = "stocklvl")
    private Map<String, Double> productStockHistory;

    public OwnProduct() {this(DEFAULT_PRODUCTNAME,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);}

    public OwnProduct(String name, String description, Double unitCost, Double stockLevel, Double lowStockLevel,
                      Double reservedStockLevel, Unit units, String imageUrl, String properties, Long productCreatorId,
                      Long lastUpdatedById, LocalDateTime createDateTime, LocalDateTime updateDateTime, List<OwnTag> tags,
                      URL url, Map<String, Double> productStockHistory) {
        this.name = name;
        this.description = description;
        this.unitCost = unitCost;
        this.stockLevel = stockLevel;
        this.lowStockLevel = lowStockLevel;
        this.reservedStockLevel = reservedStockLevel;
        this.units = units;
        this.imageName = imageName;
        this.properties = properties;
        this.productCreatorId = productCreatorId;
        this.lastUpdatedById = lastUpdatedById;
        this.createDateTime = createDateTime;
        this.updateDateTime = updateDateTime;
        this.tags = tags;
        this.url = url;
        this.productStockHistory=productStockHistory;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Double getUnitCost() {
        return this.unitCost;
    }

    public Double getStockLevel() {
        return this.stockLevel;
    }

    public Double getLowStockLevel() {
        return this.lowStockLevel;
    }

    public Double getReservedStockLevel() {
        return this.reservedStockLevel;
    }

    public Unit getUnits() {
        return this.units;
    }

    public String getProperties() {
        return this.properties;
    }

    public Long getProductCreatorId() {
        return this.productCreatorId;
    }

    public Long getLastUpdatedById() {
        return this.lastUpdatedById;
    }

    public LocalDateTime getCreateDateTime() {
        return this.createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return this.updateDateTime;
    }

    public List<OwnTag> getTags() {
        return this.tags;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public void setStockLevel(Double stockLevel) {
        this.stockLevel = stockLevel;
    }

    public void setLowStockLevel(Double lowStockLevel) {
        this.lowStockLevel = lowStockLevel;
    }

    public void setReservedStockLevel(Double reservedStockLevel) {
        this.reservedStockLevel = reservedStockLevel;
    }

    public void setUnits(Unit units) {
        this.units = units;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public void setProductCreatorId(Long productCreatorId) {
        this.productCreatorId = productCreatorId;
    }

    public void setLastUpdatedById(Long lastUpdatedById) {
        this.lastUpdatedById = lastUpdatedById;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public void setTags(List<OwnTag> tags) {
        this.tags = tags;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Map<String, Double> getProductStockHistory() {
        return productStockHistory;
    }

    public void setProductStockHistory(Map<String, Double> productStockHistory) {
        this.productStockHistory = productStockHistory;
    }
}

