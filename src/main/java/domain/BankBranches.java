package domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import domain.entity.BankBranch;

import java.util.List;

public record BankBranches(
        @JsonProperty("agencias")
        List<BankBranch> bankBranches
) {
}
