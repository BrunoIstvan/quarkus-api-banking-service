package service;

import domain.BankBranches;
import domain.entity.BankBranch;
import domain.entity.Address;
import domain.http.RegistrationStatus;
import exceptions.InactiveBankBranchException;
import exceptions.BankBranchNotFoundException;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import repository.BankBranchRepository;
import repository.AddressRepository;
import service.http.RegistrationStatusHttpService;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class BankBranchService {

    @RestClient
    private RegistrationStatusHttpService registrationStatusHttpService;

    private final BankBranchRepository bankBranchRepository;
    private final AddressRepository addressRepository;
    private final MeterRegistry meterRegistry;

    public BankBranchService(BankBranchRepository bankBranchRepository, AddressRepository addressRepository,
                             MeterRegistry meterRegistry) {
        this.bankBranchRepository = bankBranchRepository;
        this.addressRepository = addressRepository;
        this.meterRegistry = meterRegistry;
    }

    public void register(BankBranch bankBranch) {

        Timer timer = this.meterRegistry.timer("cadastrar_agencia_timer");
        timer.record(() -> {
            var agenciaHttp = registrationStatusHttpService.buscarPorCnpj(bankBranch.getCnpj());
            if(Objects.isNull(agenciaHttp)) {
                Log.error("Register - Agência não encontrada pelo cnpj: " + bankBranch.getCnpj());
                meterRegistry.counter("agencia_adiciona_nao_encontrada_counter").increment();
                throw new BankBranchNotFoundException();
            }

            if(agenciaHttp.getRegistrationStatus().equals(RegistrationStatus.ATIVO)) {
                bankBranchRepository.persist(bankBranch);
                Log.error("Register - Agência com cnpj " + bankBranch.getCnpj() + " ativo e cadastrado com sucesso");
                meterRegistry.counter("agencia_adiciona_counter").increment();
            } else {
                Log.error("Register - Agência com cnpj " + bankBranch.getCnpj() + " está inativo");
                meterRegistry.counter("agencia_adiciona_inativa_counter").increment();
                throw new InactiveBankBranchException();
            }
        });

    }

    public BankBranch getById(int id) {
        AtomicReference<BankBranch> agencia = new AtomicReference<>();
        Timer timer = this.meterRegistry.timer("buscar_por_id_agencia_timer");
        timer.record(() -> {
            Long longId = ((Integer) id).longValue();
            agencia.set(getAgenciaById(longId));
            Log.info("Agência recuperada com sucesso: " + longId);
            meterRegistry.counter("agencia_obter_por_id_counter").increment();
        });
        return agencia.get();
    }

    public void deleteById(int id) {
        Long longId = ((Integer) id).longValue();
        var excluida = this.bankBranchRepository.deleteById(longId);
        if(excluida) {
            Log.info("Agência excluída com sucesso: " + longId);
            meterRegistry.counter("agencia_excluir_por_id_counter").increment();
        } else {
            Log.info("Agência não encontrada: " + longId);
            meterRegistry.counter("agencia_excluir_por_id_nao_encontrada_counter").increment();
        }
    }

    public void update(BankBranch bankBranch) {

        var affectedRows = this.bankBranchRepository.update(
                "name = :nome, " +
                        "companyName = :razao_social, " +
                        "cnpj = :cnpj " +
                        "where id = :id",
                Parameters.with("nome", bankBranch.getName())
                        .and("razao_social", bankBranch.getCompanyName())
                        .and("cnpj", bankBranch.getCnpj())
                        .and("id", bankBranch.getId()));

        if(affectedRows > 0) {
            Log.info("Agência atua lizada com sucesso: " + bankBranch.getId());
            meterRegistry.counter("agencia_atualizar_por_id_counter").increment();
        } else {
            meterRegistry.counter("agencia_atualizar_por_id_nao_encontrada_counter").increment();
        }

    }

    public void updateAddress(int idAgencia, Address address) {
        Long longId = ((Integer) idAgencia).longValue();
        var agencia = getAgenciaById(longId);
        var affectedRows = this.addressRepository.update(
                "street = :logradouro, " +
                        "addressType = :tipo_endereco, " +
                        "complement = :complemento, " +
                        "number = :numero, " +
                        "neighborhood = :bairro " +
                        "where id = :id",
                Parameters.with("logradouro", address.getStreet())
                        .and("tipo_endereco", address.getAddressType())
                        .and("complemento", address.getComplement())
                        .and("bairro", address.getNeighborhood())
                        .and("numero", address.getNumber())
                        .and("id", agencia.getEndereco().getId()));

        if(affectedRows > 0) {
            Log.info("Endereço de agência atualizado com sucesso - AgenciaID: " + longId + " - EnderecoId: " + address.getId());
            meterRegistry.counter("endereco_agencia_atualizar_por_id_counter").increment();
        } else {
            meterRegistry.counter("endereco_agencia_atualizar_por_id_nao_encontrada_counter").increment();
        }

    }

    public BankBranches listAll() {
        var agencias = this.bankBranchRepository.listAll();
        Log.info("Agências listadas com sucesso");
        meterRegistry.counter("agencias_listar_counter").increment();
        return new BankBranches(agencias);
    }

    private BankBranch getAgenciaById(Long longId) {
        var agencia = this.bankBranchRepository.findById(longId);
        if (Objects.isNull(agencia)) {
            Log.error("Agência não encontrada pelo id: " + longId);
            meterRegistry.counter("agencia_obter_por_id_nao_encontrada_counter").increment();
            throw new BankBranchNotFoundException();
        }
        Log.info("Agência encontrada com sucesso: " + longId);
        return agencia;
    }

}
