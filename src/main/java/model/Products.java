package model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Products{
    private List<Product> products;
    private String recommendation;
    
}