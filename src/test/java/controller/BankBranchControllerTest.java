package controller;

import domain.BankBranches;
import domain.entity.BankBranch;
import domain.entity.Address;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import service.BankBranchService;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static utils.MockerUtils.mockarAgencia;

@QuarkusTest
class BankBranchControllerTest {

    @InjectMock
    private BankBranchService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrar_success() {

        // Arrange
        doNothing().when(service).register(any(BankBranch.class));

        // Act
        var agencia = mockarAgencia();
        var response = given().body(agencia)
                .contentType(ContentType.JSON)
                .when().post("/agencias");

        // Assert
        response.then().statusCode(HttpStatus.SC_CREATED);
        verify(service, atLeastOnce()).register(any(BankBranch.class));

    }

    @Test
    void atualizar_sucess() {

        // Arrange
        doNothing().when(service).update(any(BankBranch.class));

        // Act
        var agencia = mockarAgencia();
        var response = given().body(agencia)
                .contentType(ContentType.JSON)
                .when().put("/agencias");

        // Assert
        response.then().statusCode(HttpStatus.SC_ACCEPTED);
        verify(service, atLeastOnce()).update(any(BankBranch.class));

    }

    @Test
    void atualizarEndereco_sucess() {

        // Arrange
        doNothing().when(service).updateAddress(anyInt(), any(Address.class));

        // Act
        var agencia = mockarAgencia();
        var response = given().body(agencia)
                .contentType(ContentType.JSON)
                .when().put("/agencias/1/endereco");

        // Assert
        response.then().statusCode(HttpStatus.SC_ACCEPTED);
        verify(service, atLeastOnce()).updateAddress(anyInt(), any(Address.class));

    }

    @Test
    void obterPorId_success() {

        // Arrange
        var agencia = mockarAgencia();
        doReturn(agencia).when(service).getById(anyInt());

        // Act
        var response = given().contentType(ContentType.JSON)
                .when().get("/agencias/1")
                .then().statusCode(HttpStatus.SC_OK);

        // Assert
        response
                .body("id", is(1))
                .body("nome", is("nome"))
                .body("razao_social", is("razaoSocial"));
        verify(service, atLeastOnce()).getById(anyInt());

    }

    @Test
    void listarAgencias_success() {

        // Arrange
        var agencias = new BankBranches(Arrays.asList(mockarAgencia(), mockarAgencia(), mockarAgencia()));
        doReturn(agencias).when(service).listAll();

        // Act
        var response = given().contentType(ContentType.JSON)
                .when().get("/agencias")
                .then().statusCode(HttpStatus.SC_OK);

        // Assert
        response
                .body("agencias[0].id", is(1))
                .body("agencias[0].nome", is("nome"))
                .body("agencias[0].razao_social", is("razaoSocial"));
        verify(service, atLeastOnce()).listAll();

    }

    @Test
    void excluirPorId_success() {

        // Arrange
        doNothing().when(service).deleteById(anyInt());

        // Act
        var response = given().contentType(ContentType.JSON)
                .when().delete("/agencias/1");

        // Assert
        response
                .then().statusCode(HttpStatus.SC_OK);
        verify(service, atLeastOnce()).deleteById(anyInt());

    }
}