package domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "agencia")
public class BankBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("nome")
    @Column(name = "nome")
    private String name;

    @Column(name = "razao_social")
    @JsonProperty("razao_social")
    private String companyName;

    private String cnpj;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Address address;

    public BankBranch() {
    }

    public BankBranch(Integer id, String nome, String companyName, String cnpj, Address address) {
        this.id = id;
        this.name = nome;
        this.companyName = companyName;
        this.cnpj = cnpj;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Address getEndereco() {
        return address;
    }

    public void setEndereco(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "BankBranch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", companyName='" + companyName + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", address=" + address +
                '}';
    }
}
