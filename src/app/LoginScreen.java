package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.UserDAO;
import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;
import javafx.scene.control.cell.PropertyValueFactory;

public class LoginScreen extends Application {

    private UserDAO userDAO = new UserDAO();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Система входу");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Ласкаво просимо");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Ім'я користувача:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Пароль:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Увійти");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwBox.getText();

            if (username.isEmpty() || password.isEmpty()) {
                actiontarget.setText("Будь ласка, введіть дані!");
                return;
            }

            try {
                if (userDAO.authenticate(username, password)) {
                    actiontarget.setText("Вхід успішний!");
                    showProductManagement(primaryStage);
                } else {
                    actiontarget.setText("Невірні облікові дані!");
                }
            } catch (Exception ex) {
                actiontarget.setText("Помилка підключення до БД");
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(grid, 350, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showProductManagement(Stage loginStage) {
        try {
            loginStage.close();
            
            Stage productStage = new Stage();
            ProductDAO productDAO = new ProductDAO();
            
            // Створюємо таблицю для відображення продуктів
            TableView<Product> table = new TableView<>();
            
            // Колонки таблиці
            TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
            
            TableColumn<Product, String> nameColumn = new TableColumn<>("Назва");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            
            TableColumn<Product, String> descColumn = new TableColumn<>("Опис");
            descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            
            TableColumn<Product, Double> priceColumn = new TableColumn<>("Ціна");
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            
            TableColumn<Product, Integer> stockColumn = new TableColumn<>("Кількість");
            stockColumn.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
            
            TableColumn<Product, Integer> supplierColumn = new TableColumn<>("Постачальник");
            supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
            
            table.getColumns().addAll(idColumn, nameColumn, descColumn, priceColumn, stockColumn, supplierColumn);
            
            // Оновлення даних у таблиці
            ObservableList<Product> products = FXCollections.observableArrayList(productDAO.getAllProducts());
            table.setItems(products);
            
            // Кнопки для керування
            Button addBtn = new Button("Додати");
            Button editBtn = new Button("Редагувати");
            Button deleteBtn = new Button("Видалити");
            Button refreshBtn = new Button("Оновити");
            
            // Обробники подій для кнопок
            addBtn.setOnAction(e -> {
                showProductDialog(null, productDAO, products);
            });
            
            editBtn.setOnAction(e -> {
                Product selected = table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showProductDialog(selected, productDAO, products);
                } else {
                    showAlert("Попередження", "Будь ласка, виберіть продукт для редагування");
                }
            });
            
            deleteBtn.setOnAction(e -> {
                Product selected = table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    if (productDAO.deleteProduct(selected.getProductId())) {
                        products.remove(selected);
                        showAlert("Успіх", "Продукт успішно видалено");
                    } else {
                        showAlert("Помилка", "Не вдалося видалити продукт");
                    }
                } else {
                    showAlert("Попередження", "Будь ласка, виберіть продукт для видалення");
                }
            });
            
            refreshBtn.setOnAction(e -> {
                products.setAll(productDAO.getAllProducts());
            });
            
            HBox buttonBox = new HBox(10, addBtn, editBtn, deleteBtn, refreshBtn);
            buttonBox.setAlignment(Pos.CENTER);
            
            VBox root = new VBox(20, table, buttonBox);
            root.setPadding(new Insets(20));
            
            Scene scene = new Scene(root, 800, 600);
            productStage.setTitle("Управління продуктами");
            productStage.setScene(scene);
            productStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Помилка", "Не вдалося відкрити модуль управління продуктами");
            loginStage.show();
        }
    }
    
    private void showProductDialog(Product product, ProductDAO productDAO, ObservableList<Product> products) {
        Stage dialog = new Stage();
        dialog.setTitle(product == null ? "Додати продукт" : "Редагувати продукт");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        // Поля форми
        TextField nameField = new TextField();
        TextArea descField = new TextArea();
        TextField priceField = new TextField();
        TextField stockField = new TextField();
        TextField supplierField = new TextField();
        
        // Якщо редагуємо існуючий продукт - заповнюємо поля
        if (product != null) {
            nameField.setText(product.getName());
            descField.setText(product.getDescription());
            priceField.setText(String.valueOf(product.getPrice()));
            stockField.setText(String.valueOf(product.getQuantityInStock()));
            supplierField.setText(String.valueOf(product.getSupplierId()));
        }
        
        // Додаємо поля на форму
        grid.add(new Label("Назва:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Опис:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Ціна:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Кількість:"), 0, 3);
        grid.add(stockField, 1, 3);
        grid.add(new Label("ID постачальника:"), 0, 4);
        grid.add(supplierField, 1, 4);
        
        Button saveBtn = new Button("Зберегти");
        saveBtn.setOnAction(e -> {
            try {
                Product p = product != null ? product : new Product();
                p.setName(nameField.getText());
                p.setDescription(descField.getText());
                p.setPrice(Double.parseDouble(priceField.getText()));
                p.setQuantityInStock(Integer.parseInt(stockField.getText()));
                p.setSupplierId(Integer.parseInt(supplierField.getText()));
                
                boolean success;
                if (product == null) {
                    success = productDAO.insertProduct(p);
                    if (success) products.add(p);
                } else {
                    success = productDAO.updateProduct(p);
                    if (success) products.set(products.indexOf(product), p);
                }
                
                if (success) {
                    dialog.close();
                    showAlert("Успіх", "Дані успішно збережено");
                } else {
                    showAlert("Помилка", "Не вдалося зберегти дані");
                }
            } catch (NumberFormatException ex) {
                showAlert("Помилка", "Невірний формат числових даних");
            } catch (Exception ex) {
                showAlert("Помилка", "Сталася помилка при збереженні");
                ex.printStackTrace();
            }
        });
        
        grid.add(saveBtn, 1, 5);
        
        Scene scene = new Scene(grid);
        dialog.setScene(scene);
        dialog.show();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}