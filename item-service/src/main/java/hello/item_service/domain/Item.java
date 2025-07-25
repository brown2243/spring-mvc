package hello.item_service.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

// @Getter
// @Setter
@NoArgsConstructor
@Data
public class Item {
  private Long id;
  private String itemName;
  private Integer price;
  private Integer quantity;

  public Item(String itemName, Integer price, Integer quantity) {
    this.itemName = itemName;
    this.price = price;
    this.quantity = quantity;

  }
}