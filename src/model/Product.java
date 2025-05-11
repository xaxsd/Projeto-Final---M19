package model;

/**
 * Classe que representa um produto no sistema de gerenciamento.
 * Contém informações básicas sobre o produto e validações de negócio.
 */
public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private int quantityInStock;
    private int supplierId;

    /**
     * Construtor padrão que inicializa um produto com valores default.
     */
    public Product() {
        this.productId = 0;
        this.name = "";
        this.description = "";
        this.price = 0.0;
        this.quantityInStock = 0;
        this.supplierId = 0;
    }

    /**
     * Construtor completo para criação de um produto com todos os atributos.
     * @param productId ID único do produto
     * @param name Nome do produto
     * @param description Descrição detalhada
     * @param price Preço unitário
     * @param quantityInStock Quantidade em estoque
     * @param supplierId ID do fornecedor
     */
    public Product(int productId, String name, String description,
                 double price, int quantityInStock, int supplierId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.supplierId = supplierId;
    }

    // Métodos Getters e Setters com validações

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    /**
     * Define o nome do produto com validação.
     * @param name Nome do produto
     * @throws IllegalArgumentException Se o nome for inválido
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("O nome não pode exceder 100 caracteres.");
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

    /**
     * Define o preço do produto com validação.
     * @param price Preço do produto
     * @throws IllegalArgumentException Se o preço for negativo
     */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    /**
     * Define a quantidade em estoque com validação.
     * @param quantityInStock Quantidade em estoque
     * @throws IllegalArgumentException Se a quantidade for negativa
     */
    public void setQuantityInStock(int quantityInStock) {
        if (quantityInStock < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
        this.quantityInStock = quantityInStock;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * Representação textual do produto.
     * @return String formatada com informações do produto
     */
    @Override
    public String toString() {
        return String.format("Produto [ID=%d, Nome=%s, Preço=R$%.2f, Estoque=%d]", 
                           productId, name, price, quantityInStock);
    }
}