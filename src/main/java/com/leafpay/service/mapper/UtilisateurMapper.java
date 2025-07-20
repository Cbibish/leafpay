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
    @Mapping(target = "idRole", source = "idRole", qualifiedByName = "roleId")
    UtilisateurDTO toDto(Utilisateur s);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoleDTO toDtoRoleId(Role role);
}
