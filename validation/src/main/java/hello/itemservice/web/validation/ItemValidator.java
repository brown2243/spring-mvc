package hello.itemservice.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import hello.itemservice.domain.item.Item;

@Component
public class ItemValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Item.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors bindingResult) {
    Item item = (Item) target;
    ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName",
        "required");
    //
    if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
      bindingResult.rejectValue("price", "range", new Object[] { 1000, 1000000 },
          null);
    }
    if (item.getQuantity() == null || item.getQuantity() > 10000) {
      bindingResult.rejectValue("quantity", "max", new Object[] { 9999 }, null);
    }
    // 특정 필드 예외가 아닌 전체 예외
    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10000) {
        bindingResult.reject("totalPriceMin", new Object[] { 10000, resultPrice }, null);
      }
    }
  }
}
