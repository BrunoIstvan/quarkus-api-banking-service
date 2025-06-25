package domain.http;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankBranchHttp {

    @JsonProperty("nome")
    private String name;

    @JsonProperty("cnpj")
    private String document;

    @JsonProperty("razaoSocial")
    private String companyName;

    @JsonProperty("situacaoCadastral")
    private RegistrationStatus registrationStatus;

    public BankBranchHttp() {
    }

    public BankBranchHttp(String name, String document, String companyName, String registrationStatus) {
        this.name = name;
        this.document = document;
        this.companyName = companyName;
        this.registrationStatus = RegistrationStatus.valueOf(registrationStatus);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
}