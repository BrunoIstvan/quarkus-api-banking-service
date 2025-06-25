package domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "endereco")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_endereco")
    @JsonProperty("tipo_endereco")
    private String addressType;

    @Column(name = "logradouro")
    @JsonProperty("logradouro")
    private String street;

    @Column(name = "complemento")
    @JsonProperty("complemento")
    private String complement;

    @Column(name = "bairro")
    @JsonProperty("bairro")
    private String neighborhood;

    @JsonProperty("numero")
    @Column(name = "numero")
    private String number;

    public Address() {
    }

    public Address(Integer id, String addressType, String street, String complement, String number, String neighborhood) {
        this.id = id;
        this.addressType = addressType;
        this.street = street;
        this.complement = complement;
        this.number = number;
        this.neighborhood = neighborhood;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }
}

