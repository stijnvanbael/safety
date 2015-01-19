package be.jforce.safety.solution;

import java.math.BigDecimal;
import java.util.*;

public class Sale {
    private List<Detail> details;
    private List<Payment> payments = new ArrayList<Payment>();
    private String location;
    private String cashier;
    private String customer;

    public List<Detail> details() {
        return Collections.unmodifiableList(details);
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
        List<Detail> newDetails = new ArrayList<Detail>(details);
        newDetails.add(detail);
        return Sale.newBuilder()
                .cashier(cashier)
                .customer(customer)
                .location(location)
                .details(newDetails)
                .build();
    }

    public BigDecimal total() {
        BigDecimal total = BigDecimal.ZERO;
        for (Detail detail : details) {
            total = total.add(detail.value());
        }
        return total;
    }

    public void pay(String account, long timestamp, BigDecimal amount) {
        if (!containsPayment(account, timestamp)) {
            payments.add(new Payment(account, timestamp, amount));
        }
    }

    private boolean containsPayment(String account, long timestamp) {
        for (Payment payment : payments) {
            if (payment.account().equals(account) && payment.timestamp() == timestamp) {
                return true;
            }
        }
        return false;
    }

    public BigDecimal balance() {
        return total().subtract(paid());
    }

    private BigDecimal paid() {
        BigDecimal paid = BigDecimal.ZERO;
        for (Payment payment : payments) {
            paid = paid.add(payment.amount());
        }
        return paid;
    }

    public static class Detail {
        private static final Map<String, Detail> CACHE = new HashMap<String, Detail>();
        private String item;
        private int quantity;
        private BigDecimal price;

        private Detail(String item, int quantity, BigDecimal price) {
            this.item = item;
            this.quantity = quantity;
            this.price = price;
        }

        public static Detail create(String item, int quantity, BigDecimal price) {
            String key = item + "/" + quantity + "/" + price;
            if (CACHE.containsKey(key)) {
                return CACHE.get(key);
            }
            Detail detail = new Detail(item, quantity, price);
            CACHE.put(key, detail);
            return detail;
        }

        public BigDecimal value() {
            return price.multiply(new BigDecimal(quantity));
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
            instance.details = new ArrayList<Detail>(details);
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
            Sale result = instance;
            instance = new Sale();
            return result;
        }
    }

    private class Payment {
        private final String account;
        private final long timestamp;
        private final BigDecimal amount;

        public Payment(String account, long timestamp, BigDecimal amount) {
            this.account = account;
            this.timestamp = timestamp;
            this.amount = amount;
        }

        public BigDecimal amount() {
            return amount;
        }

        public String account() {
            return account;
        }

        public long timestamp() {
            return timestamp;
        }
    }
}
