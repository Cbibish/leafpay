package com.leafpay.service.mapper;

import com.leafpay.domain.Log;
import com.leafpay.domain.Utilisateur;
import com.leafpay.service.dto.LogDTO;
import com.leafpay.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Log} and its DTO {@link LogDTO}.
 */
@Mapper(componentModel = "spring")
public interface LogMapper extends EntityMapper<LogDTO, Log> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    LogDTO toDto(Log s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
