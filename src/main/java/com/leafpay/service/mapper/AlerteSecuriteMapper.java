package com.leafpay.service.mapper;

import com.leafpay.domain.AlerteSecurite;
import com.leafpay.domain.Utilisateur;
import com.leafpay.service.dto.AlerteSecuriteDTO;
import com.leafpay.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlerteSecurite} and its DTO {@link AlerteSecuriteDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlerteSecuriteMapper extends EntityMapper<AlerteSecuriteDTO, AlerteSecurite> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    AlerteSecuriteDTO toDto(AlerteSecurite s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
