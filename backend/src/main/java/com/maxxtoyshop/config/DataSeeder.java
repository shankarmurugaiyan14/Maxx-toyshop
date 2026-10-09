package com.maxxtoyshop.config;
import com.maxxtoyshop.model.*; import com.maxxtoyshop.repository.*; import lombok.RequiredArgsConstructor; import org.springframework.boot.CommandLineRunner; import org.springframework.stereotype.Component; import java.math.*; import java.util.*;
@Component @RequiredArgsConstructor public class DataSeeder implements CommandLineRunner {
 private final CategoryRepository cats; private final ProductRepository products;
 public void run(String...args){
  if(products.count()>0)return;
  String[] names={"Educational","Remote Control","Dolls","Action Figures","Soft Toys","Board Games","Outdoor Toys","Pretend Play","Electronic Toys"};
  for(String n:names)cats.findByNameIgnoreCase(n).orElseGet(()->cats.save(new Category(null,n)));
  add("LEGO Set STAR WARS","Educational","799","assets/LEGO STAR WARS.jpg","Popular");
  add("HotWheels","Remote Control","299","assets/Hotwheels.jpg","New");
  add("Barbie","Dolls","599","assets/Barbie.jpg",null);
  add("Batman","Action Figures","999","assets/Batman.jpg","Hot");
  add("RC Helicopter","Educational","1199","assets/Rc helicopter.jpg",null);
  add("Teddy Bear","Soft Toys","399","assets/Teddybear.jpg",null);
  add("MONOPOLY","Board Games","699","assets/Monopoly.avif",null);
  add("Cricket Bat","Outdoor Toys","1099","assets/Cricket bat.jpg",null);
  add("Mini Kitchen Set","Pretend Play","849","assets/mini kitchen set.jpg",null);
  add("Robot Buddy","Electronic Toys","1899","assets/robot buddy.jpg","Featured");
 }
 void add(String n,String c,String price,String img,String tag){Product p=new Product();p.setName(n);p.setDescription("Fun toy from Maxx Toy Shop");p.setPrice(new BigDecimal(price));p.setStock(100);p.setImage(img);p.setTag(tag);p.setCategory(cats.findByNameIgnoreCase(c).orElseThrow());products.save(p);}
}