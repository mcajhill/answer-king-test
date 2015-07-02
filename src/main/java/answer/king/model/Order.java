package answer.king.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;


@Entity
@Table(name = "T_ORDER")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean paid = false;

    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL, CascadeType.PERSIST })
    @JoinColumn(name = "ORDER_ID")
    private List<LineItem> items;

    @OneToOne(cascade = { CascadeType.ALL, CascadeType.PERSIST })
    @JoinColumn(name = "RECIEPT_ID")
    private Reciept reciept;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public List<LineItem> getItems() {
		return items;
	}

	public void setItems(List<LineItem> items) {
		this.items = items;
	}

    public Reciept getReciept() {
        return reciept;
    }

    public void setReciept(Reciept reciept) {
        this.reciept = reciept;
    }

    public LineItem findLineItem(Item item) {
        LineItem lineItem = null;
        boolean found = false;

        // check the order for a LineItem instance which is linked to an Item
        for (Iterator iter = getItems().iterator(); iter.hasNext() && !found;) {
            LineItem current = (LineItem) iter.next();

            if (current.getItem().equals(item)) {
                lineItem = current;
                found = true;
            }
        }

        return lineItem;
    }

    public BigDecimal calculateTotalOrderCost() {
        List<LineItem> items = getItems();
        BigDecimal totalOrderCost = BigDecimal.ZERO;

        for (LineItem lineItem : items) {
            BigDecimal qty = new BigDecimal(lineItem.getQuantity());
            BigDecimal totalLinePrice = lineItem.getPrice().multiply(qty);
            totalOrderCost = totalOrderCost.add(totalLinePrice);
        }

        return totalOrderCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        return !(getId() != null ? !getId().equals(order.getId()) : order.getId() != null) &&
            !(getPaid() != null ? !getPaid().equals(order.getPaid()) : order.getPaid() != null) &&
            !(getItems() != null ? !getItems().equals(order.getItems()) : order.getItems() != null);
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getPaid() != null ? getPaid().hashCode() : 0);
        result = 31 * result + (getItems() != null ? getItems().hashCode() : 0);
        return result;
    }
}