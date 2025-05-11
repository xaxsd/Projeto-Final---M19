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

/**
 * Aplicação JavaFX para autenticação e gerenciamento de produtos
 */
public class LoginScreen extends Application {

    private UserDAO userDAO = new UserDAO();

    /**
     * Método principal de inicialização da interface
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Login");

        // Layout da tela de login
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Componentes da interface
        Text scenetitle = new Text("Bem-vindo");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Usuário:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Senha:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Login");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        // Ação do botão de login
        btn.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwBox.getText();

            if (username.isEmpty() || password.isEmpty()) {
                actiontarget.setText("Por favor, preencha todos os campos!");
                return;
            }

            try {
                if (userDAO.authenticate(username, password)) {
                    actiontarget.setText("Login bem-sucedido!");
                    showProductManagement(primaryStage);
                } else {
                    actiontarget.setText("Credenciais inválidas!");
                }
            } catch (Exception ex) {
                actiontarget.setText("Erro de conexão com o banco de dados");
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(grid, 350, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Exibe a tela de gerenciamento de produtos
     */
    private void showProductManagement(Stage loginStage) {
        try {
            loginStage.close();
            
            Stage productStage = new Stage();
            ProductDAO productDAO = new ProductDAO();
            
            // Configuração da tabela de produtos
            TableView<Product> table = new TableView<>();
            
            // Colunas da tabela
            TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
            
            TableColumn<Product, String> nameColumn = new TableColumn<>("Nome");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            
            TableColumn<Product, String> descColumn = new TableColumn<>("Descrição");
            descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            
            TableColumn<Product, Double> priceColumn = new TableColumn<>("Preço");
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            
            TableColumn<Product, Integer> stockColumn = new TableColumn<>("Estoque");
            stockColumn.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
            
            TableColumn<Product, Integer> supplierColumn = new TableColumn<>("Fornecedor");
            supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
            
            table.getColumns().addAll(idColumn, nameColumn, descColumn, priceColumn, stockColumn, supplierColumn);
            
            // Carrega os produtos do banco de dados
            ObservableList<Product> products = FXCollections.observableArrayList(productDAO.getAllProducts());
            table.setItems(products);
            
            // Botões de controle
            Button addBtn = new Button("Adicionar");
            Button editBtn = new Button("Editar");
            Button deleteBtn = new Button("Remover");
            Button refreshBtn = new Button("Atualizar");
            
            // Configura ações dos botões
            addBtn.setOnAction(e -> showProductDialog(null, productDAO, products));
            
            editBtn.setOnAction(e -> {
                Product selected = table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showProductDialog(selected, productDAO, products);
                } else {
                    showAlert("Aviso", "Selecione um produto para editar");
                }
            });
            
            deleteBtn.setOnAction(e -> {
                Product selected = table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    if (productDAO.deleteProduct(selected.getProductId())) {
                        products.remove(selected);
                        showAlert("Sucesso", "Produto removido com sucesso");
                    } else {
                        showAlert("Erro", "Falha ao remover produto");
                    }
                } else {
                    showAlert("Aviso", "Selecione um produto para remover");
                }
            });
            
            refreshBtn.setOnAction(e -> products.setAll(productDAO.getAllProducts()));
            
            // Layout dos botões
            HBox buttonBox = new HBox(10, addBtn, editBtn, deleteBtn, refreshBtn);
            buttonBox.setAlignment(Pos.CENTER);
            
            // Layout principal
            VBox root = new VBox(20, table, buttonBox);
            root.setPadding(new Insets(20));
            
            Scene scene = new Scene(root, 800, 600);
            productStage.setTitle("Gerenciamento de Produtos");
            productStage.setScene(scene);
            productStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Falha ao abrir o módulo de produtos");
            loginStage.show();
        }
    }
    
    /**
     * Exibe diálogo para adicionar/editar produto
     */
    private void showProductDialog(Product product, ProductDAO productDAO, ObservableList<Product> products) {
        Stage dialog = new Stage();
        dialog.setTitle(product == null ? "Novo Produto" : "Editar Produto");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        // Campos do formulário
        TextField nameField = new TextField();
        TextArea descField = new TextArea();
        TextField priceField = new TextField();
        TextField stockField = new TextField();
        TextField supplierField = new TextField();
        
        // Preenche campos se for edição
        if (product != null) {
            nameField.setText(product.getName());
            descField.setText(product.getDescription());
            priceField.setText(String.valueOf(product.getPrice()));
            stockField.setText(String.valueOf(product.getQuantityInStock()));
            supplierField.setText(String.valueOf(product.getSupplierId()));
        }
        
        // Adiciona campos ao formulário
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descrição:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Preço:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Estoque:"), 0, 3);
        grid.add(stockField, 1, 3);
        grid.add(new Label("ID Fornecedor:"), 0, 4);
        grid.add(supplierField, 1, 4);
        
        // Botão de salvar
        Button saveBtn = new Button("Salvar");
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
                    showAlert("Sucesso", "Dados salvos com sucesso");
                } else {
                    showAlert("Erro", "Falha ao salvar dados");
                }
            } catch (NumberFormatException ex) {
                showAlert("Erro", "Formato numérico inválido");
            } catch (Exception ex) {
                showAlert("Erro", "Falha ao processar operação");
                ex.printStackTrace();
            }
        });
        
        grid.add(saveBtn, 1, 5);
        
        Scene scene = new Scene(grid);
        dialog.setScene(scene);
        dialog.show();
    }
    
    /**
     * Exibe mensagem de alerta
     */
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