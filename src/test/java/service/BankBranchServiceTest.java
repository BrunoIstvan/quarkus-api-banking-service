package service;

import domain.http.RegistrationStatus;
import exceptions.InactiveBankBranchException;
import exceptions.BankBranchNotFoundException;
import io.quarkus.panache.common.Parameters;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import repository.BankBranchRepository;
import repository.AddressRepository;
import service.http.RegistrationStatusHttpService;
import utils.MockerUtils;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class BankBranchServiceTest {

    @InjectMock
    @RestClient
    private RegistrationStatusHttpService registrationStatusHttpService;

    @InjectMock
    private BankBranchRepository bankBranchRepository;

    @InjectMock
    private AddressRepository addressRepository;

    @Inject
    private BankBranchService service;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

    }

    // region register

    @Test
    public void register_success() {

        // Arrange
        var agencia = MockerUtils.mockarAgencia();
        var agenciaHttp = MockerUtils.mockarAgenciaHttp(RegistrationStatus.ATIVO);
        doReturn(agenciaHttp).when(registrationStatusHttpService).buscarPorCnpj(anyString());

        // Act
        service.register(agencia);

        // Assert
        verify(bankBranchRepository, atLeastOnce()).persist(agencia);
        verify(registrationStatusHttpService, atLeastOnce()).buscarPorCnpj(anyString());
    }

    @Test
    public void register_should_throws_AgenciaNaoEncontradaException_when_buscarPorCnpj_returns_null() {

        // Arrange
        var agencia = MockerUtils.mockarAgencia();
        doReturn(null).when(registrationStatusHttpService).buscarPorCnpj(anyString());

        // Act
        assertThrows(BankBranchNotFoundException.class, () -> service.register(agencia));

        // Assert
        verify(bankBranchRepository, never()).persist(agencia);
        verify(registrationStatusHttpService, atLeastOnce()).buscarPorCnpj(anyString());
    }

    @Test
    public void register_should_throws_AgenciaInativaException_when_buscarPorCnpj_returns_agencia_inativa() {

        // Arrange
        var agencia = MockerUtils.mockarAgencia();
        var agenciaHttp = MockerUtils.mockarAgenciaHttp(RegistrationStatus.INATIVO);
        doReturn(agenciaHttp).when(registrationStatusHttpService).buscarPorCnpj(anyString());

        // Act
        assertThrows(InactiveBankBranchException.class, () -> service.register(agencia));

        // Assert
        verify(bankBranchRepository, never()).persist(agencia);
        verify(registrationStatusHttpService, atLeastOnce()).buscarPorCnpj(anyString());
    }

    // endregion

    // region getById

    @Test
    public void getById_success() {

        // Arrange
        var agencia = MockerUtils.mockarAgencia();
        doReturn(agencia).when(bankBranchRepository).findById(anyLong());

        // Act
        var result = service.getById(1);

        // Assert
        assertNotNull(result);
        assertEquals(result.getCnpj(), agencia.getCnpj());
        verify(bankBranchRepository, atLeastOnce()).findById(anyLong());

    }

    @Test
    public void getById_should_throws_AgenciaNaoEncontradaException_when_agencia_not_found_by_id() {

        // Arrange
        doReturn(null).when(bankBranchRepository).findById(anyLong());

        // Act & Assert
        assertThrows(BankBranchNotFoundException.class, () -> service.getById(1));
        verify(bankBranchRepository, atLeastOnce()).findById(anyLong());

    }

    // endregion

    // region deleteById

    @Test
    public void deleteById_success() {

        // Arrange
        doReturn(true).when(bankBranchRepository).deleteById(anyLong());

        // Act
        service.deleteById(1);

        // Assert
        verify(bankBranchRepository, atLeastOnce()).deleteById(anyLong());

    }

    // endregion

    // region update

    @Test
    public void update_success() {

        // Arrange
        doReturn(1).when(bankBranchRepository).update(anyString(), any(Parameters.class));

        // Act
        service.update(MockerUtils.mockarAgencia());

        // Assert
        verify(bankBranchRepository, atLeastOnce()).update(anyString(), any(Parameters.class));

    }

    // endregion

    // region updateAddress

    @Test
    public void updateAddress_success() {

        // Arrange
        var agencia = MockerUtils.mockarAgencia();
        doReturn(agencia).when(bankBranchRepository).findById(1L);
        doReturn(1).when(addressRepository).update(anyString(), any(Parameters.class));

        // Act
        service.updateAddress(agencia.getId(), MockerUtils.mockarEndereco());

        // Assert
        verify(bankBranchRepository, atLeastOnce()).findById(1L);
        verify(addressRepository, atLeastOnce()).update(anyString(), any(Parameters.class));

    }

    @Test
    public void updateAddress_should_throws_AgenciaNaoEncontradaException_when_agencia_not_found_by_id() {

        // Arrange
        doReturn(null).when(bankBranchRepository).findById(anyLong());

        // Act
        assertThrows(BankBranchNotFoundException.class, () ->
                service.updateAddress(1, MockerUtils.mockarEndereco()));

        // Assert
        verify(bankBranchRepository, atLeastOnce()).findById(1L);
        verify(addressRepository, never()).update(anyString(), any(Parameters.class));

    }

    // endregion

    // region listAll
    @Test
    public void listAll_success() {

        // Arrange
        var agencias = Arrays.asList(MockerUtils.mockarAgencia(), MockerUtils.mockarAgencia(), MockerUtils.mockarAgencia());
        doReturn(agencias).when(bankBranchRepository).listAll();

        // Act
        var result = service.listAll();

        // Assert
        assertNotNull(result);
        assertEquals(result.bankBranches().size(), agencias.size());
        verify(bankBranchRepository, atLeastOnce()).listAll();

    }


}