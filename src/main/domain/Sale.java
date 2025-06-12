package main.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import main.annotations.KeyType;
import main.annotations.ProjectColumn;
import main.annotations.ProjectTable;
import main.dao.Persistent;


@ProjectTable("TB_SALES")
public class Sale implements Persistent{
    public enum Status {
        INICIADA, CONCLUIDA, CANCELADA;
        public static Status getByName(String value) {
            for (Status status : Status.values()) {
                if (status.name().equals(value)) {
                    return status;
                }
            }
            return null;
        }
    }

    @ProjectColumn(dbName = "id", setJavaName= "setId")
    private Long id;

    @KeyType("getCode")
    @ProjectColumn(dbName = "code", setJavaName = "setCode")
    private String code;

    @ProjectColumn(dbName = "id_client_fk", setJavaName = "setIdClientFk")
    private Client client;

    @ProjectColumn(dbName = "total_price", setJavaName = "setTotalPrice")
    private BigDecimal totalPrice;

    @ProjectColumn(dbName = "date", setJavaName = "setDate")
    private Instant date;

    @ProjectColumn(dbName = "status", setJavaName = "setStatus")
    private Status status;

    private Set<ProductQuantity> products;

    public Sale() {
        this.products = new HashSet<>();
    }

    private void validateStatus() {
		if (this.status == Status.CONCLUIDA) {
			throw new UnsupportedOperationException("IMPOSSÍVEL ALTERAR VENDA FINALIZADA");
		}
	}

    public void addProduct(Product product, Integer quantity) {
        validateStatus();
        Optional<ProductQuantity> op = products.stream().filter(f -> f.getProduct().getCode().equals(product.getCode())).findAny();
        if (op.isPresent()) {
            ProductQuantity productQuantity = op.get();
            productQuantity.add(quantity);
        } else {
            ProductQuantity productQuantity = new ProductQuantity();
            productQuantity.setProduct(product);
            productQuantity.add(quantity);
            products.add(productQuantity);
        }
        recalculateTotal();
    }

    public void removeProduct(Product product, Integer quantity) {
        validateStatus();
        Optional<ProductQuantity> op = products.stream().filter(f -> f.getProduct().getCode().equals(product.getCode())).findAny();    
        if (op.isPresent()) {
            ProductQuantity productQuantity = op.get();
            if (productQuantity.getQuantity() > quantity) {
                productQuantity.remove(quantity);
            } else {
                products.remove(op.get());
            }
            recalculateTotal();
        }
    }

    public void removeAllProducts() {
		validateStatus();
		products.clear();
		totalPrice = BigDecimal.ZERO;
	}

    public Integer getTotalQuantity() {
        Integer quantity = products.stream().reduce(0, (a, b) -> a + b.getQuantity(), Integer::sum);
        return quantity;
    }

    public void recalculateTotal() {
        validateStatus();
        BigDecimal total = BigDecimal.ZERO;
        for (ProductQuantity prod : this.products) {
            total = total.add(prod.getTotalPrice());
        }
        this.totalPrice = total;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<ProductQuantity> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductQuantity> products) {
        this.products = products;
    }

}
