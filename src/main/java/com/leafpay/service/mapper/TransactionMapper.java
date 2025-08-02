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

    // Use the mapping that includes iban here:
    @Mapping(target = "compteSource", source = "compteSource", qualifiedByName = "compteWithIban")
    @Mapping(target = "compteDestination", source = "compteDestination", qualifiedByName = "compteWithIban")
    TransactionDTO toDto(Transaction s);

    // Original minimal mapping (only id) – still useful elsewhere if needed
    @Named("compteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompteDTO toDtoCompteId(Compte compte);

    // NEW mapping that includes iban
    @Named("compteWithIban")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "iban", source = "iban")
    CompteDTO toDtoCompteWithIban(Compte compte);
}
