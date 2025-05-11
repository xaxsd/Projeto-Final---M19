package app;

import dao.ProductDAO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Product;
import model.Supplier;
import model.SupplierDAO;

import java.util.List;
import java.util.Optional;

public class ProductManagerApp extends Application {

    private ProductDAO productDAO;
    private SupplierDAO supplierDAO;
    private TableView<Product> productTable = new TableView<>();
    
    // Form fields
    private TextField nameField = new TextField();
    private TextField descField = new TextField();
    private TextField priceField = new TextField();
    private TextField quantityField = new TextField();
    private ComboBox<Supplier> supplierCombo = new ComboBox<>();

    @Override
    public void init() throws Exception {
        try {
            productDAO = new ProductDAO();
            supplierDAO = new SupplierDAO();
        } catch (Exception e) {
            showAlert("Database Error", "Failed to initialize database connection: " + e.getMessage());
            Platform.exit();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Управління продуктами");
        
        setupFormFields();
        setupProductTable();
        
        VBox formPanel = createFormPanel();
        BorderPane root = new BorderPane();
        root.setLeft(formPanel);
        root.setCenter(productTable);

        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }

    private void setupFormFields() {
        nameField.setPromptText("Назва");
        descField.setPromptText("Опис");
        priceField.setPromptText("Ціна");
        quantityField.setPromptText("Кількість");
        supplierCombo.setPromptText("Оберіть постачальника");
        
        refreshSuppliersCombo();
    }

    private VBox createFormPanel() {
        Button addButton = new Button("Додати");
        addButton.setOnAction(e -> handleAddProduct());
        
        Button refreshButton = new Button("Оновити список постачальників");
        refreshButton.setOnAction(e -> refreshSuppliersCombo());

        VBox formPanel = new VBox(10,
            new Label("Новий продукт:"),
            nameField,
            descField,
            priceField,
            quantityField,
            new Label("Постачальник:"),
            supplierCombo,
            refreshButton,
            addButton
        );
        formPanel.setPrefWidth(250);
        formPanel.setStyle("-fx-padding: 10;");
        return formPanel;
    }

    private void setupProductTable() {
        // Name column
        TableColumn<Product, String> nameCol = new TableColumn<>("Назва");
        nameCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getName()));

        // Price column
        TableColumn<Product, String> priceCol = new TableColumn<>("Ціна");
        priceCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("%.2f", data.getValue().getPrice())));

        // Quantity column
        TableColumn<Product, String> quantityCol = new TableColumn<>("Кількість");
        quantityCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.valueOf(data.getValue().getQuantityInStock())));

        // Supplier column
        TableColumn<Product, String> supplierCol = new TableColumn<>("Постачальник");
        supplierCol.setCellValueFactory(data -> {
            int supplierId = data.getValue().getSupplierId();
            String supplierName = supplierDAO.getSupplierNameById(supplierId);
            return new SimpleStringProperty(supplierName);
        });

        productTable.getColumns().add(nameCol);
        productTable.getColumns().add(priceCol);
        productTable.getColumns().add(quantityCol);
        productTable.getColumns().add(supplierCol);
        
        productTable.setRowFactory(this::createProductTableRow);
        
        refreshProductTable();
    }

    private TableRow<Product> createProductTableRow(TableView<Product> tv) {
        TableRow<Product> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !row.isEmpty()) {
                handleDeleteProduct(row.getItem());
            }
        });
        return row;
    }

    private void handleAddProduct() {
        try {
            if (!validateInput()) {
                return;
            }

            Product product = createProductFromInput();
            productDAO.insertProduct(product);
            
            refreshProductTable();
            clearFormFields();
            
            showAlert("Успіх", "Продукт успішно додано");
        } catch (NumberFormatException ex) {
            showAlert("Помилка", "Будь ласка, введіть коректні числові значення для ціни та кількості");
        } catch (Exception ex) {
            showAlert("Помилка", "Не вдалося додати продукт: " + ex.getMessage());
        }
    }

    private boolean validateInput() {
        if (nameField.getText().isEmpty() || 
            priceField.getText().isEmpty() || 
            quantityField.getText().isEmpty() || 
            supplierCombo.getValue() == null) {
            
            showAlert("Помилка", "Будь ласка, заповніть всі обов'язкові поля");
            return false;
        }
        return true;
    }

    private Product createProductFromInput() {
        return new Product(
            nameField.getText(),
            descField.getText(),
            Double.parseDouble(priceField.getText()),
            Integer.parseInt(quantityField.getText()),
            supplierCombo.getValue().getSupplierId()
        );
    }

    private void handleDeleteProduct(Product product) {
        if (confirmDelete("Видалити продукт", 
            "Ви впевнені, що хочете видалити продукт " + product.getName() + "?")) {
            
            productDAO.deleteProduct(product.getProductId());
            refreshProductTable();
        }
    }

    private void refreshSuppliersCombo() {
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        supplierCombo.getItems().setAll(suppliers);
    }

    private void refreshProductTable() {
        List<Product> products = productDAO.getAllProducts();
        productTable.getItems().setAll(products);
    }

    private void clearFormFields() {
        nameField.clear();
        descField.clear();
        priceField.clear();
        quantityField.clear();
        supplierCombo.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean confirmDelete(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static void main(String[] args) {
        launch(args);
    }
}