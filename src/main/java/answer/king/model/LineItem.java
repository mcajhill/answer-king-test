package answer.king.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "T_LINEITEM")
public class LineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal price;

    private Integer quantity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal currentPrice) {
        this.price = currentPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineItem)) return false;

        LineItem lineItem = (LineItem) o;

        return !(getId() != null ? !getId().equals(lineItem.getId()) : lineItem.getId() != null) &&
            !(getPrice() != null ? !getPrice().equals(lineItem.getPrice()) : lineItem.getPrice() != null) &&
            !(getQuantity() != null ? !getQuantity().equals(lineItem.getQuantity()) : lineItem.getQuantity() != null) &&
            !(getItem() != null ? !getItem().equals(lineItem.getItem()) : lineItem.getItem() != null);
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        result = 31 * result + (getQuantity() != null ? getQuantity().hashCode() : 0);
        result = 31 * result + (getItem() != null ? getItem().hashCode() : 0);
        return result;
    }
}
