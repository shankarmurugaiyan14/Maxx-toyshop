package com.maxxtoyshop.controller;
import com.maxxtoyshop.model.Product; import com.maxxtoyshop.repository.ProductRepository; import lombok.RequiredArgsConstructor; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/products") @RequiredArgsConstructor public class ProductController {
 private final ProductRepository repo;
 @GetMapping public List<Product> all(@RequestParam(required=false)String category,@RequestParam(required=false)String search){
  if(search!=null&&!search.isBlank())return repo.findByNameContainingIgnoreCase(search);
  if(category!=null&&!category.isBlank()&&!category.equalsIgnoreCase("All"))return repo.findByCategory_NameIgnoreCase(category);
  return repo.findAll();
 }
 @GetMapping("/{id}") public Product one(@PathVariable Long id){return repo.findById(id).orElseThrow(()->new IllegalArgumentException("Product not found: "+id));}
 @PostMapping public Product create(@RequestBody Product p){return repo.save(p);}
 @PutMapping("/{id}") public Product update(@PathVariable Long id,@RequestBody Product x){Product p=one(id);p.setName(x.getName());p.setDescription(x.getDescription());p.setPrice(x.getPrice());p.setStock(x.getStock());p.setImage(x.getImage());p.setTag(x.getTag());p.setCategory(x.getCategory());return repo.save(p);}
 @DeleteMapping("/{id}") public void delete(@PathVariable Long id){repo.deleteById(id);}
}