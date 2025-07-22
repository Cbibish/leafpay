package com.leafpay.service.mapper;

import com.leafpay.domain.Role;
import com.leafpay.domain.Utilisateur;
import com.leafpay.service.dto.RoleDTO;
import com.leafpay.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Utilisateur} and its DTO {@link UtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, Utilisateur> {
    @Mapping(source = "role", target = "role")
    UtilisateurDTO toDto(Utilisateur utilisateur);

    @Mapping(source = "role", target = "role")
    Utilisateur toEntity(UtilisateurDTO utilisateurDTO);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    RoleDTO toDtoRoleId(Role role);
}
