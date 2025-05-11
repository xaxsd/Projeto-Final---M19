package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import util.DatabaseConnection;

public class ProductDAO {
    // SQL queries
    private static final String INSERT_SQL = 
        "INSERT INTO Products (name, description, price, quantity_in_stock, supplier_id) " +
        "VALUES (?, ?, ?, ?, ?)";
    
    private static final String SELECT_ALL_SQL = "SELECT * FROM Products";
    private static final String UPDATE_SQL = 
        "UPDATE Products SET name=?, description=?, price=?, quantity_in_stock=?, supplier_id=? " +
        "WHERE product_id=?";
    private static final String DELETE_SQL = "DELETE FROM Products WHERE product_id=?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM Products WHERE product_id=?";

    /**
     * Inserts a new product into the database
     * @param product the product to insert
     * @return true if successful, false otherwise
     */
    public boolean insertProduct(Product product) {
        if (!validateProduct(product)) {
            System.err.println("Invalid product data");
            return false;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }

            try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                setStatementParameters(stmt, product);
                
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    return false;
                }
                
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setProductId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Failed to insert product: " + product);
        }
        return false;
    }

    /**
     * Retrieves all products from the database
     * @return list of products
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            
            while (rs.next()) {
                products.add(createProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            handleSQLException(e, "Failed to retrieve products");
        }
        return products;
    }

    /**
     * Updates an existing product in the database
     * @param product the product with updated data
     * @return true if successful, false otherwise
     */
    public boolean updateProduct(Product product) {
        if (!validateProduct(product) || product.getProductId() <= 0) {
            System.err.println("Invalid product data for update");
            return false;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            setStatementParameters(stmt, product);
            stmt.setInt(6, product.getProductId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException(e, "Failed to update product ID: " + product.getProductId());
        }
        return false;
    }

    /**
     * Deletes a product from the database
     * @param productId the ID of the product to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteProduct(int productId) {
        if (productId <= 0) {
            System.err.println("Invalid product ID for deletion");
            return false;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException(e, "Failed to delete product ID: " + productId);
        }
        return false;
    }

    /**
     * Retrieves a product by its ID
     * @param productId the ID of the product
     * @return the product if found, null otherwise
     */
    public Product getProductById(int productId) {
        if (productId <= 0) {
            return null;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createProductFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Failed to retrieve product ID: " + productId);
        }
        return null;
    }

    // Helper methods
    private boolean validateProduct(Product product) {
        return product != null && 
               product.getName() != null && !product.getName().trim().isEmpty() &&
               product.getDescription() != null &&
               product.getPrice() > 0 &&
               product.getQuantityInStock() >= 0 &&
               product.getSupplierId() > 0;
    }

    private void setStatementParameters(PreparedStatement stmt, Product product) 
        throws SQLException {
        stmt.setString(1, product.getName());
        stmt.setString(2, product.getDescription());
        stmt.setDouble(3, product.getPrice());
        stmt.setInt(4, product.getQuantityInStock());
        stmt.setInt(5, product.getSupplierId());
    }

    private Product createProductFromResultSet(ResultSet rs) throws SQLException {
        return new Product(
            rs.getInt("product_id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getInt("quantity_in_stock"),
            rs.getInt("supplier_id")
        );
    }

    private void handleSQLException(SQLException e, String context) {
        System.err.println(context);
        System.err.println("SQL State: " + e.getSQLState());
        System.err.println("Error Code: " + e.getErrorCode());
        System.err.println("Message: " + e.getMessage());
        e.printStackTrace();
    }
}