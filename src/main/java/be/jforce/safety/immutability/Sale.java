package be.jforce.safety.immutability;

import java.util.List;

public class Sale {
    private List<Detail> details;
    private String location;
    private String cashier;
    private String customer;

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

    public static class Detail {
        private String item;
        private int quantity;

        private Detail(String item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public static Detail create(String item, int quantity) {
            return new Detail(item,quantity);
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
}
