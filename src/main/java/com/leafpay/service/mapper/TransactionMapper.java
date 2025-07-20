package com.leafpay.service.mapper;

import com.leafpay.domain.Compte;
import com.leafpay.domain.Transaction;
import com.leafpay.service.dto.CompteDTO;
import com.leafpay.service.dto.TransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaction} and its DTO {@link TransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {
    @Mapping(target = "compteSource", source = "compteSource", qualifiedByName = "compteId")
    @Mapping(target = "compteDestination", source = "compteDestination", qualifiedByName = "compteId")
    TransactionDTO toDto(Transaction s);

    @Named("compteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompteDTO toDtoCompteId(Compte compte);
}
