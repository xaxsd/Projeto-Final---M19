package model;
public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private int quantityInStock;
    private int supplierId;

    public Product() {}

    public Product(String name, String description, double price, int quantityInStock, int supplierId) {
        setName(name);
        this.description = description;
        setPrice(price);
        setQuantityInStock(quantityInStock);
        this.supplierId = supplierId;
    }

    // Геттери і Сеттери з валідацією
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty() || name.length() > 100) {
            throw new IllegalArgumentException("Назва продукту обов’язкова і не повинна перевищувати 100 символів.");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Ціна не може бути від’ємною.");
        }
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        if (quantityInStock < 0) {
            throw new IllegalArgumentException("Кількість не може бути від’ємною.");
        }
        this.quantityInStock = quantityInStock;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }
}
