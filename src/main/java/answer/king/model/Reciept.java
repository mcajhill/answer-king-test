package answer.king.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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

	public BigDecimal getChange() {
        if (change == null) {
            change = payment.subtract(order.calculateTotalOrderCost());
        }

		return change;
	}
}
