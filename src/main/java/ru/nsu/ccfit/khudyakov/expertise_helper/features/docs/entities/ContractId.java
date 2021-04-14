package ru.nsu.ccfit.khudyakov.expertise_helper.features.docs.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Embeddable
public class ContractId implements Serializable {

    private UUID projectId;

    private UUID expertId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractId that = (ContractId) o;
        return Objects.equals(projectId, that.projectId) &&
                Objects.equals(expertId, that.expertId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, expertId);
    }


}
