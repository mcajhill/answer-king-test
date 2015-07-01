package answer.king.model;

import answer.king.throwables.exception.IncompleteOrderException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "T_RECIEPT")
public class Reciept {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private BigDecimal payment;

    private BigDecimal change;

    @JsonIgnore
    @OneToOne(mappedBy = "reciept")
	private Order order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

    public void calculateChange() throws IncompleteOrderException {
        if (change == null) {

            if (order == null)
                throw new IncompleteOrderException();

            List<Item> items = order.getItems();
            BigDecimal totalOrderPrice = BigDecimal.ZERO;
            for(Item item : items) {
                totalOrderPrice = totalOrderPrice.add(item.getPrice());
            }

            change = payment.subtract(totalOrderPrice);
        }
    }

	public BigDecimal getChange() throws IncompleteOrderException {
        calculateChange();
		return change;
	}
}
