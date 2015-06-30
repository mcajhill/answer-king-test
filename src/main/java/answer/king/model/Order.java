package answer.king.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import javax.persistence.*;


@Entity
@Table(name = "T_ORDER")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean paid = false;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = { CascadeType.ALL, CascadeType.PERSIST })
    private List<Item> items;

    @JsonIgnore
    @OneToOne(mappedBy = "order", cascade = { CascadeType.ALL })
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

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

    public Reciept getReciept() {
        return reciept;
    }

    public void setReciept(Reciept reciept) {
        this.reciept = reciept;
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