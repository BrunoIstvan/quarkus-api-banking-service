package repository;

import domain.entity.BankBranch;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BankBranchRepository implements PanacheRepository<BankBranch> {



}
