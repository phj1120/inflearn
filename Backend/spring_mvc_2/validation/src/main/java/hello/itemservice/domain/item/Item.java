package hello.itemservice.domain.item;

import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.Data;

@Data
public class Item {

    private Long id;

    private String itemName;

    private Integer price;

    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public ItemSaveForm convertItemSaveForm() {
        return new ItemSaveForm(itemName, price, quantity);
    }

    public ItemUpdateForm convertItemUpdateForm() {
        return new ItemUpdateForm(id, itemName, price, quantity);
    }
}
