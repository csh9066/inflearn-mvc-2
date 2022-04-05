package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.web.ItemValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    /* BindingResult는 @Modelattribute 다음에 무조건 와야 한다 */
    /*
    * */
//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 검증 로직
        if (isEmpty(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
        }

        if (isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 10000000) {
            bindingResult.addError(new FieldError(
                    "item",
                    "price",
                    "가격은 1,000원 ~ 1,000,000원 까지 허용합니다."));
        }

        if (isNull(item.getQuantity()) || item.getQuantity() < 1 || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError(
                    "item",
                    "quantity",
                    "수량은 최대 9,999 까지 허용합니다."));
        }


        if (nonNull(item.getPrice()) && nonNull(item.getQuantity())) {
            int totalPrice = item.getPrice() * item.getQuantity();

            if (totalPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다."));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            log.info("item={}", item);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 검증 로직
        if (isEmpty(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
        }

        if (isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 10000000) {
            bindingResult.addError(new FieldError(
                    "item",
                    "price",
                    item.getPrice(),
                    false,
                    null,
                    null,
                    "가격은 1,000원 ~ 1,000,000원 까지 허용합니다."));
        }

        if (isNull(item.getQuantity()) || item.getQuantity() < 1 || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError(
                    "item",
                    "quantity",
                    item.getQuantity(),
                    false,
                    null,
                    null,
                    "가격은 1,000원 ~ 1,000,000원 까지 허용합니다."));
        }


        if (nonNull(item.getPrice()) && nonNull(item.getQuantity())) {
            int totalPrice = item.getPrice() * item.getQuantity();

            if (totalPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다."));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            log.info("item={}", item);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());
        // 검증 로직
        if (isEmpty(item.getItemName())) {
            bindingResult.addError(new FieldError(
                    "item",
                    "itemName",
                    item.getItemName(),
                    false,
                    new String[]{"required.item.itemName"},
                    null,
                    null));
        }

        if (isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 10000000) {
            bindingResult.addError(new FieldError(
                    "item",
                    "price",
                    item.getPrice(),
                    false,
                    new String[]{"range.item.price"},
                    new Object[] {1000, 1000000},
                    null));
        }

        if (isNull(item.getQuantity()) || item.getQuantity() < 1 || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError(
                    "item",
                    "quantity",
                    item.getQuantity(),
                    false,
                    new String[]{"max.item.quantity"},
                    new Object[]{9999},
                    null));
        }


        if (nonNull(item.getPrice()) && nonNull(item.getQuantity())) {
            int totalPrice = item.getPrice() * item.getQuantity();

            if (totalPrice < 10000) {
                bindingResult.addError(new ObjectError(
                        "item",
                        new String[]{"totalPriceMin"},
                        new Object[]{10000, totalPrice}
                        ,null
                ));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            log.info("item={}", item);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, Errors errors, RedirectAttributes redirectAttributes) {
        // 검증 로직
        if (isEmpty(item.getItemName())) {
            errors.rejectValue("itemName", "required");
        }

        if(isNull(item.getPrice())) {
            errors.rejectValue("price", "required");
        }

        if (isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 10000000) {
            errors.rejectValue("price", "range", new Object[]{1000 ,1000000}, null);
        }

        if (isNull(item.getQuantity()) || item.getQuantity() < 1 || item.getQuantity() > 9999) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }


        if (nonNull(item.getPrice()) && nonNull(item.getQuantity())) {
            int totalPrice = item.getPrice() * item.getQuantity();

            if (totalPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{9999, totalPrice}, null);
            }
        }

        if (errors.hasErrors()) {
            log.info("errors={}", errors);
            log.info("item={}", item);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, Errors errors, RedirectAttributes redirectAttributes) {
        itemValidator.validate(item, errors);

        if (errors.hasErrors()) {
            log.info("errors={}", errors);
            log.info("item={}", item);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, Errors errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            log.info("errors={}", errors);
            log.info("item={}", item);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

