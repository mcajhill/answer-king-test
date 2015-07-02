package answer.king.model;

import answer.king.throwables.exception.IncompleteOrderException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

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
            change = payment.subtract(order.calculateTotalOrderCost());
        }
    }

	public BigDecimal getChange() throws IncompleteOrderException {
        calculateChange();
		return change;
	}
}
