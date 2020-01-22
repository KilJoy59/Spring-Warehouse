package main.entity;

import main.entity.enums.OperationType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @Column(name = "invoice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @OneToOne(cascade = CascadeType.ALL)
    private Product product;

    @Column(name = "date")
    private Date date;

    @Column(name = "amount")
    private long amount;

    public Invoice() {
    }

    public Invoice(int id, OperationType operationType, Product product, Date date, long amount) {
        this.id = id;
        this.operationType = operationType;
        this.product = product;
        this.date = date;
        this.amount = amount;
    }

    public Invoice(OperationType operationType, Product product, Date date, long amount) {
        this.operationType = operationType;
        this.product = product;
        this.date = date;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
