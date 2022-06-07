package com.mytoporazka.mtsklepspring.controllers.admin;

import com.mytoporazka.lib.domain.Product;
import com.mytoporazka.mtsklepspring.components.DatabaseRepository;
import com.mytoporazka.mtsklepspring.model.ProductForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AddModifyProductController {

    private final DatabaseRepository repository;
    public AddModifyProductController(DatabaseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/app/admin/addModifyProduct")
    public String addProduct(Model model) {
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("modify", false);

        return "app/admin/addModifyProduct";
    }

    @PostMapping("/app/admin/addModifyProduct")
    public String addProductPost(Model model, @ModelAttribute ProductForm productForm) {
        var id = repository.product
                .getAllByPredicate(p -> true)
                .stream()
                .map(p -> p.productID)
                .reduce(0, Integer::max) + 1;

        var product = new Product(
                productForm.getName(),
                id,
                productForm.getDescription(),
                productForm.getPrice(),
                productForm.getManufacturer(),
                false);

        repository.product.add(product);
        repository.saveToDb();

        model.addAttribute("modify", true);
        model.addAttribute("message", "Dodano produkt!");
        return "redirect:/app/admin/addModifyProduct/" + id;
    }

    @GetMapping("/app/admin/addModifyProduct/{id}")
    public String modifyProduct(Model model, @PathVariable("id") int id) {
        var productForm = new ProductForm();

        var product = repository.product.getFirstByPredicate(p -> p.productID == id);
        if(product != null) {
            productForm.setName(product.name);
            productForm.setDescription(product.description);
            productForm.setManufacturer(product.manufacturer);
            productForm.setPrice(product.price);
        }
        model.addAttribute("modify", true);
        model.addAttribute("productForm", productForm);
        return "app/admin/addModifyProduct";
    }

    @PostMapping("/app/admin/addModifyProduct/{id}")
    public String modifyProductPost(Model model, @ModelAttribute ProductForm productForm, @PathVariable("id") int id) {
        var newId = repository.product
                .getAllByPredicate(p -> true)
                .stream()
                .map(p -> p.productID)
                .reduce(0, Integer::max) + 1;

        var product = new Product(
                productForm.getName(),
                newId,
                productForm.getDescription(),
                productForm.getPrice(),
                productForm.getManufacturer(),
                false);

        repository.product.modifyByPredicate(p -> p.productID == id, p -> p.deleted = true);
        repository.product.add(product);

        repository.saveToDb();

        model.addAttribute("modify", true);
        model.addAttribute("message", "Zmieniono produkt!");
        return "redirect:/app/admin/addModifyProduct/" + newId;
    }
}
