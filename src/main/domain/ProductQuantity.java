package main.domain;

import java.math.BigDecimal;

import main.annotations.ProjectColumn;
import main.annotations.ProjectTable;

@ProjectTable("TB_PRODUCT_QUANTITY")
public class ProductQuantity {

    @ProjectColumn(dbName = "id", setJavaName = "setId")
    private Long id;

    @ProjectColumn(dbName = "id_product_fk", setJavaName = "setProduct")
    private Product product;

    @ProjectColumn(dbName = "quantity", setJavaName = "setQuantity")
    private Integer quantity;

    @ProjectColumn(dbName = "total_price", setJavaName = "setTotalPrice")
    private BigDecimal totalPrice;

    public ProductQuantity() {
        this.quantity = 0;
        this.totalPrice = BigDecimal.ZERO;
    }

    public void add(Integer num) {
        this.quantity += num;
        BigDecimal newValue = this.product.getPrice().multiply(BigDecimal.valueOf(this.quantity));
        this.totalPrice = newValue;
    }

    public void remove(Integer num) {
        Integer result = this.quantity - num;
        this.quantity = result >= 0 ? result : 0;
        BigDecimal newPrice = this.product.getPrice().multiply(BigDecimal.valueOf(this.quantity));
        this.totalPrice = this.totalPrice.subtract(newPrice);
    }

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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
