package main.domain;

import main.annotations.KeyType;
import main.annotations.ProjectColumn;
import main.annotations.ProjectTable;
import main.dao.Persistent;

@ProjectTable("TB_STOCK")
public class Stock implements Persistent {
    @KeyType("getId")
    @ProjectColumn(dbName = "id", setJavaName = "setId")
    private Long id;
    @ProjectColumn(dbName = "id_product_fk", setJavaName = "setProduct")
    private Product product;
    @ProjectColumn(dbName = "quantity", setJavaName = "setQuantity")
    private Integer quantity;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
