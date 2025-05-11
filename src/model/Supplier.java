package model;

/**
 * Classe que representa um fornecedor no sistema de gerenciamento.
 * Contém informações básicas sobre o fornecedor e métodos para manipulação.
 */
public class Supplier {
    private int supplierId;      // ID único do fornecedor
    private String name;        // Nome do fornecedor
    private String contactInfo; // Informações de contato

    /**
     * Construtor completo para criar um fornecedor.
     * @param supplierId ID único do fornecedor
     * @param name Nome do fornecedor
     * @param contactInfo Informações de contato (telefone, email, etc.)
     */
    public Supplier(int supplierId, String name, String contactInfo) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    /**
     * Retorna o ID do fornecedor.
     * @return ID do fornecedor
     */
    public int getSupplierId() {
        return supplierId;
    }

    /**
     * Define o ID do fornecedor.
     * @param supplierId Novo ID do fornecedor
     */
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * Retorna o nome do fornecedor.
     * @return Nome do fornecedor
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do fornecedor com validação básica.
     * @param name Novo nome do fornecedor
     * @throws IllegalArgumentException Se o nome for nulo ou vazio
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do fornecedor não pode ser vazio.");
        }
        this.name = name;
    }

    /**
     * Retorna as informações de contato.
     * @return Informações de contato
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Define as informações de contato.
     * @param contactInfo Novas informações de contato
     * @throws IllegalArgumentException Se as informações forem nulas
     */
    public void setContactInfo(String contactInfo) {
        if (contactInfo == null) {
            throw new IllegalArgumentException("As informações de contato não podem ser nulas.");
        }
        this.contactInfo = contactInfo;
    }

    /**
     * Representação textual do fornecedor (usado em ComboBoxes).
     * @return Nome do fornecedor
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Método adicional para exibição completa das informações.
     * @return String formatada com todos os dados do fornecedor
     */
    public String toDetailedString() {
        return String.format("Fornecedor [ID=%d, Nome=%s, Contato=%s]", 
                           supplierId, name, contactInfo);
    }

    /**
     * Verifica se dois fornecedores são iguais comparando seus IDs.
     * @param obj Objeto a ser comparado
     * @return true se os IDs forem iguais
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Supplier supplier = (Supplier) obj;
        return supplierId == supplier.supplierId;
    }

    /**
     * Gera código hash baseado no ID do fornecedor.
     * @return Código hash
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(supplierId);
    }
}