package pl.lodz.p.it.ssbd2023.ssbd05.mok.cdi.endpoint;

import static pl.lodz.p.it.ssbd2023.ssbd05.utils.converters.AccountDtoConverter.createManagerAccessLevelFromDto;
import static pl.lodz.p.it.ssbd2023.ssbd05.utils.converters.AccountDtoConverter.createOwnerAccessLevelFromDto;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import pl.lodz.p.it.ssbd2023.ssbd05.entities.mok.AdminData;
import pl.lodz.p.it.ssbd2023.ssbd05.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2023.ssbd05.mok.cdi.endpoint.dto.request.AddManagerAccessLevelDto;
import pl.lodz.p.it.ssbd2023.ssbd05.mok.cdi.endpoint.dto.request.AddOwnerAccessLevelDto;
import pl.lodz.p.it.ssbd2023.ssbd05.mok.ejb.managers.AccountManagerLocal;

@RequestScoped
@Path("/accounts/{id}/access-levels")
public class AccessLevelEndpoint {

    @Inject
    private AccountManagerLocal accountManager;

    @PUT
    @RolesAllowed({"ADMIN"})
    @Path("/administrator")
    public void grantAdminAccessLevel(@PathParam("id") Long id) throws AppBaseException {
        accountManager.grantAccessLevel(id, new AdminData());
    }

    @PUT
    @RolesAllowed({"ADMIN"})
    @Path("/manager")
    public void grantManagerAccessLevel(@PathParam("id") Long id, @Valid AddManagerAccessLevelDto dto)
        throws AppBaseException {
        accountManager.grantAccessLevel(id, createManagerAccessLevelFromDto(dto));
    }

    @PUT
    @RolesAllowed({"MANAGER"})
    @Path("/owner")
    public void grantOwnerAccessLevel(@PathParam("id") Long id, @Valid AddOwnerAccessLevelDto dto)
        throws AppBaseException {
        accountManager.grantAccessLevel(id, createOwnerAccessLevelFromDto(dto));
    }
}
