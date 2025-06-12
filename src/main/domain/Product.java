package main.domain;

import java.math.BigDecimal;

import main.annotations.KeyType;
import main.annotations.ProjectColumn;
import main.annotations.ProjectTable;
import main.dao.Persistent;

@ProjectTable("TB_PRODUCTS")
public class Product implements Persistent {
    @ProjectColumn(dbName = "id", setJavaName = "setId")
    private Long id;

    @KeyType("getCode")
    @ProjectColumn(dbName = "code", setJavaName = "setCode")
    private String code;

    @ProjectColumn(dbName = "name", setJavaName = "setName")
    private String name;

    @ProjectColumn(dbName = "description", setJavaName = "setDescription")
    private String description;

    @ProjectColumn(dbName = "price", setJavaName = "setPrice")
    private BigDecimal price;

    @ProjectColumn(dbName = "category", setJavaName = "setCategory")
    private String category;

    @Override
    public String toString() {
        return "Product [id=" + id + ", code=" + code + ", name=" + name + ", description=" + description + ", price="
                + price + ", category=" + category + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
