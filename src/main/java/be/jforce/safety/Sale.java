package be.jforce.safety;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Sale {
    private List<Detail> details;
    private List<Payment> payments = new ArrayList<Payment>();
    private String location;
    private String cashier;
    private String customer;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal paid = BigDecimal.ZERO;

    public List<Detail> details() {
        return details;
    }

    public String location() {
        return location;
    }

    public String cashier() {
        return cashier;
    }

    public String customer() {
        return customer;
    }

    public Sale add(Detail detail) {
        details.add(detail);
        return this;
    }

    public BigDecimal total() {
        for (Detail detail : details) {
            addToTotal(detail);
        }
        return total;
    }

    private void addToTotal(Detail detail) {
        total = total.add(detail.price().multiply(new BigDecimal(detail.quantity())));
    }

    public void pay(String account, BigDecimal amount) {
        payments.add(new Payment(account, amount));
    }

    public BigDecimal balance() {
        return total().subtract(paid());
    }

    private BigDecimal paid() {
        for(Payment payment : payments) {
            addToPaid(payment);
        }
        return paid;
    }

    private void addToPaid(Payment payment) {
        paid = paid.add(payment.amount());
    }

    public static class Detail {
        private String item;
        private int quantity;
        private BigDecimal price;

        private Detail(String item, int quantity, BigDecimal price) {
            this.item = item;
            this.quantity = quantity;
            this.price = price;
        }

        public static Detail create(String item, int quantity, BigDecimal price) {
            return new Detail(item, quantity, price);
        }

        public BigDecimal price() {
            return price;
        }

        public int quantity() {
            return quantity;
        }
    }

    private Sale() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Sale instance = new Sale();

        public Builder details(List<Detail> details) {
            instance.details = details;
            return this;
        }

        public Builder location(String location) {
            instance.location = location;
            return this;
        }

        public Builder cashier(String cashier) {
            instance.cashier = cashier;
            return this;
        }

        public Builder customer(String customer) {
            instance.customer = customer;
            return this;
        }

        public Sale build() {
            return instance;
        }
    }

    private class Payment {
        private final String account;
        private final BigDecimal amount;

        public Payment(String account, BigDecimal amount) {
            this.account = account;
            this.amount = amount;
        }

        public String account() {
            return account;
        }

        public BigDecimal amount() {
            return amount;
        }
    }
}
