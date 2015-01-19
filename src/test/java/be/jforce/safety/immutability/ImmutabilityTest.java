package be.jforce.safety.immutability;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class ImmutabilityTest {

    private Sale sale;
    private List<Sale.Detail> details;
    private Sale.Builder builder;

    @Before
    public void before() {
        details = new ArrayList<Sale.Detail>();
        details.add(Sale.Detail.create("Minecraft Creeper Backpack", 2));
        details.add(Sale.Detail.create("Portal Test Subject Jacket", 4));
        details.add(Sale.Detail.create("Borg Cube Fridge", 1));
        builder = Sale.newBuilder();
        sale = builder
                .details(details)
                .location("Kassa 4")
                .cashier("Kimberly")
                .customer("Yvonne")
                .build();
    }

    @Test
    public void mutableField() {
        details.add(Sale.Detail.create("Chewbacca Rug", 3));
        assertThat(sale.details().size(), is(3));

        List<Sale.Detail> newDetails = sale.details();
        try {
            newDetails.add(Sale.Detail.create("Chewbacca Rug", 3));
        } catch (Exception e) {
            // May or may not fail
        }
        assertThat(sale.details().size(), is(3));
    }

    @Test
    public void builder() {
        builder.cashier("HAL 9000");
        assertThat(sale.cashier(), is("Kimberly"));
    }

    @Test
    public void flyweight() {
        Sale.Detail detail1 = Sale.Detail.create("Chewbacca Rug", 3);
        Sale.Detail detail2 = Sale.Detail.create("Chewbacca Rug", 3);
        assertThat(detail1, sameInstance(detail2));
    }

    @Test
    public void copyOnWrite() {
        Sale newSale = sale.add(Sale.Detail.create("Chewbacca Rug", 3));
        assertThat(sale.details().size(), is(3));
        assertThat(newSale.details().size(), is(4));
    }
}
