package pl.lodz.p.it.ssbd2023.ssbd05.mok.cdi.endpoint;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pl.lodz.p.it.ssbd2023.ssbd05.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2023.ssbd05.exceptions.AppRollbackLimitExceededException;
import pl.lodz.p.it.ssbd2023.ssbd05.exceptions.conflict.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2023.ssbd05.exceptions.unauthorized.AuthenticationException;
import pl.lodz.p.it.ssbd2023.ssbd05.mok.cdi.endpoint.dto.request.LoginDto;
import pl.lodz.p.it.ssbd2023.ssbd05.mok.cdi.endpoint.dto.request.RefreshJwtDto;
import pl.lodz.p.it.ssbd2023.ssbd05.mok.cdi.endpoint.dto.response.JwtRefreshTokenDto;
import pl.lodz.p.it.ssbd2023.ssbd05.mok.ejb.managers.AuthManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd05.utils.Properties;
import pl.lodz.p.it.ssbd2023.ssbd05.utils.annotations.ValidUUID;

import java.util.UUID;


@Path("")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.NEVER)
public class AuthEndpoint {

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private AuthManagerLocal authManager;

    @Context
    private SecurityContext securityContext;

    @Inject
    private Properties properties;

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(@NotNull @Valid LoginDto dto) throws AppBaseException {
        String ip = httpServletRequest.getRemoteAddr();

        CredentialValidationResult credentialValidationResult =
            identityStoreHandler.validate(new UsernamePasswordCredential(dto.getLogin(), dto.getPassword()));

        int txLimit = properties.getTransactionRepeatLimit();
        boolean rollbackTX = false;
        if (credentialValidationResult.getStatus() != CredentialValidationResult.Status.VALID) {
            do {
                try {
                    authManager.registerUnsuccessfulLogin(dto.getLogin(), ip);
                    rollbackTX = authManager.isLastTransactionRollback();
                } catch (AppOptimisticLockException aole) {
                    rollbackTX = true;
                    if (txLimit < 2) {
                        throw aole;
                    }
                }
            } while (rollbackTX && --txLimit > 0);

            if (rollbackTX && txLimit == 0) {
                throw new AppRollbackLimitExceededException();
            }
            throw new AuthenticationException();

        } else {
            JwtRefreshTokenDto jwtRefreshTokenDto = null;
            do {
                try {
                    jwtRefreshTokenDto = authManager.registerSuccessfulLogin(dto.getLogin(), ip);
                    rollbackTX = authManager.isLastTransactionRollback();
                } catch (AppOptimisticLockException aole) {
                    rollbackTX = true;
                    if (txLimit < 2) {
                        throw aole;
                    }
                }
            } while (rollbackTX && --txLimit > 0);

            if (rollbackTX && txLimit == 0) {
                throw new AppRollbackLimitExceededException();
            }
            return Response.ok(jwtRefreshTokenDto).build();
        }
    }

    @POST
    @Path("refresh")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response refreshJwt(@NotNull @Valid RefreshJwtDto dto) throws AppBaseException {
        UUID token = UUID.fromString(dto.getRefreshToken());

        int txLimit = properties.getTransactionRepeatLimit();
        boolean rollbackTX = false;
        JwtRefreshTokenDto jwtRefreshTokenDto = null;
        do {
            try {
                jwtRefreshTokenDto = authManager.refreshJwt(token, dto.getLogin());
                rollbackTX = authManager.isLastTransactionRollback();
            } catch (AppOptimisticLockException aole) {
                rollbackTX = true;
                if (txLimit < 2) {
                    throw aole;
                }
            }
        } while (rollbackTX && --txLimit > 0);

        if (rollbackTX && txLimit == 0) {
            throw new AppRollbackLimitExceededException();
        }
        return Response.ok(jwtRefreshTokenDto).build();
    }

    @DELETE
    @Path("logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "OWNER"})
    public Response logout(@ValidUUID @QueryParam("token") String token)
        throws AppBaseException {

        int txLimit = properties.getTransactionRepeatLimit();
        boolean rollbackTX = false;
        do {
            try {
                authManager.logout(token, securityContext.getUserPrincipal().getName());
                rollbackTX = authManager.isLastTransactionRollback();
            } catch (AppOptimisticLockException aole) {
                rollbackTX = true;
                if (txLimit < 2) {
                    throw aole;
                }
            }
        } while (rollbackTX && --txLimit > 0);

        if (rollbackTX && txLimit == 0) {
            throw new AppRollbackLimitExceededException();
        }

        return Response.noContent().build();
    }
}
