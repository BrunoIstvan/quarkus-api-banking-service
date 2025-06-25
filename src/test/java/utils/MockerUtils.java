package utils;

import domain.entity.BankBranch;
import domain.entity.Address;
import domain.http.BankBranchHttp;
import domain.http.RegistrationStatus;

public class MockerUtils {

    public static BankBranchHttp mockarAgenciaHttp(RegistrationStatus registrationStatus) {
        return new BankBranchHttp("nome", "cnpj", "razao", registrationStatus.name());
    }

    public static Address mockarEndereco() {
        return new Address(1, "rua", "endereco", "complemento", "100", "bairro");
    }

    public static BankBranch mockarAgencia() {
        var endereco = mockarEndereco();
        return new BankBranch(1, "nome", "razaoSocial", "cnpj", endereco);
    }

}
