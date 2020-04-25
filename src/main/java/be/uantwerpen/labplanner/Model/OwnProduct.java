package be.uantwerpen.labplanner.Model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import be.uantwerpen.labplanner.common.model.stock.Unit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class OwnProduct extends AbstractPersistable<Long> {
    @Column
    @NotBlank(
            message = "{invalid.notblank}"
    )
    @Pattern(
            regexp = "[a-zA-Z0-9-_]+",
            message = "{product.invalid.name}"
    )
    private String name;
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
            name = "image_url"
    )
    private String imageUrl;
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

    public OwnProduct() {
    }

    public OwnProduct(String name, String description, Double unitCost, Double stockLevel, Double lowStockLevel, Double reservedStockLevel, Unit units, String imageUrl, String properties, Long productCreatorId, Long lastUpdatedById, LocalDateTime createDateTime, LocalDateTime updateDateTime, List<OwnTag> tags) {
        this.name = name;
        this.description = description;
        this.unitCost = unitCost;
        this.stockLevel = stockLevel;
        this.lowStockLevel = lowStockLevel;
        this.reservedStockLevel = reservedStockLevel;
        this.units = units;
        this.imageUrl = imageUrl;
        this.properties = properties;
        this.productCreatorId = productCreatorId;
        this.lastUpdatedById = lastUpdatedById;
        this.createDateTime = createDateTime;
        this.updateDateTime = updateDateTime;
        this.tags = tags;
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

    public String getImageUrl() {
        return this.imageUrl;
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

    public void setId(Long id) {
        super.setId(id);
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}

