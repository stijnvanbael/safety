package be.jforce.safety;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IdempotenceTest {
    private Sale sale;

    @Before
    public void before() {
        List<Sale.Detail> details = new ArrayList<Sale.Detail>();
        details.add(Sale.Detail.create("Minecraft Creeper Backpack", 2, new BigDecimal("35.00")));
        details.add(Sale.Detail.create("Portal Test Subject Jacket", 4, new BigDecimal("59.95")));
        details.add(Sale.Detail.create("Borg Cube Fridge", 1, new BigDecimal("120.00")));
        Sale.Builder builder = Sale.newBuilder();
        sale = builder
                .details(details)
                .location("Kassa 4")
                .cashier("Kimberly")
                .customer("Yvonne")
                .build();
    }

    @Test
    public void sideEffectFreeFunctions() {
        assertThat(sale.total(), is(new BigDecimal("429.80")));
        assertThat(sale.total(), is(new BigDecimal("429.80")));
    }

    @Test
    public void repeatableMutations() {
        String account = "1234567890";
        BigDecimal amount = new BigDecimal("429.80");
        sale.pay(account, amount);
        sale.pay(account, amount);

        assertThat(sale.balance(), is(new BigDecimal("0.00")));
    }
}
