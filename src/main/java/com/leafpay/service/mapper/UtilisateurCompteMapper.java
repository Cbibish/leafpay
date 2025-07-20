package com.leafpay.service.mapper;

import com.leafpay.domain.Compte;
import com.leafpay.domain.Utilisateur;
import com.leafpay.domain.UtilisateurCompte;
import com.leafpay.service.dto.CompteDTO;
import com.leafpay.service.dto.UtilisateurCompteDTO;
import com.leafpay.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UtilisateurCompte} and its DTO {@link UtilisateurCompteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilisateurCompteMapper extends EntityMapper<UtilisateurCompteDTO, UtilisateurCompte> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    @Mapping(target = "compte", source = "compte", qualifiedByName = "compteId")
    UtilisateurCompteDTO toDto(UtilisateurCompte s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);

    @Named("compteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompteDTO toDtoCompteId(Compte compte);
}
