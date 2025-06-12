package main.services;

import main.domain.Product;
import main.services.generics.IGenericService;

public interface IProductService extends IGenericService<Product, String> {
}
