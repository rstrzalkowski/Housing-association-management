package pl.lodz.p.it.ssbd2023.ssbd05.mow.cdi.endpoint;

import static pl.lodz.p.it.ssbd2023.ssbd05.shared.Roles.MANAGER;
import static pl.lodz.p.it.ssbd2023.ssbd05.shared.Roles.OWNER;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pl.lodz.p.it.ssbd2023.ssbd05.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2023.ssbd05.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd05.mow.ejb.managers.ReportManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd05.utils.converters.ReportDtoConverter;
import pl.lodz.p.it.ssbd2023.ssbd05.utils.rollback.RollbackUtils;

import java.time.Month;
import java.time.Year;

@RequestScoped
@Path("/reports")
@DenyAll
@Interceptors(LoggerInterceptor.class)
public class ReportEndpoint {

    @Inject
    private ReportManagerLocal reportManager;

    @Inject
    private RollbackUtils rollbackUtils;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({MANAGER, OWNER})
    public Response getReportDetails(@PathParam("id") Long id) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/community")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(MANAGER)
    public Response getAllCommunityReports() throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/community/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(MANAGER)
    public Response getCommunityReportByYear(@PathParam("year") Long year) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/place/{id}/years")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({OWNER, MANAGER})
    public Response getReportYearsByPlaceId(@PathParam("id") Long id) throws AppBaseException {
        return Response.ok(reportManager.getReportYearsByPlaceId(id)).build();
    }

    @GET
    @Path("/place/{id}/report/year")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(MANAGER)
    public Response getYearlyReportsForPlace(@PathParam("id") Long id, @QueryParam("year") Integer year)
        throws AppBaseException {
        return rollbackUtils.rollBackTXBasicWithOkStatus(
            () -> ReportDtoConverter.createPlaceReportYearDtoFromListOfReportForecastYear(
                reportManager.getAllReportsDataByPlaceAndYear(id, Year.of(year))),
            reportManager).build();
    }

    @GET
    @Path("/me/place/{id}/report/year")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(OWNER)
    public Response getOwnYearlyReportsForPlace(@PathParam("id") Long id, @QueryParam("year") Integer year)
        throws AppBaseException {
        return rollbackUtils.rollBackTXBasicWithOkStatus(
            () -> ReportDtoConverter.createPlaceReportYearDtoFromListOfReportForecastYear(
                reportManager.getAllOwnReportsDataByPlaceAndYear(
                    id,
                    Year.of(year),
                    securityContext.getUserPrincipal().getName())),
            reportManager).build();
    }

    @GET
    @Path("/place/{id}/report/month")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(MANAGER)
    public Response getMonthlyReportsForPlace(
        @PathParam("id") Long id,
        @QueryParam("year") Integer year,
        @QueryParam("month") Integer month) throws AppBaseException {
        return rollbackUtils.rollBackTXBasicWithOkStatus(
            () -> ReportDtoConverter.createPlaceReportMonthDtoFromListOfForecast(
                reportManager.getAllReportsDataByPlaceAndYearAndMonth(
                    id,
                    Year.of(year),
                    Month.of(month))),
            reportManager
        ).build();
    }

    @GET
    @Path("/me/place/{id}/report/month")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(OWNER)
    public Response getOwnMonthlyReportsForPlace(
        @PathParam("id") Long id,
        @QueryParam("year") Integer year,
        @QueryParam("month") Integer month) throws AppBaseException {
        return rollbackUtils.rollBackTXBasicWithOkStatus(
            () -> ReportDtoConverter.createPlaceReportMonthDtoFromListOfForecast(
                reportManager.getAllOwnReportsDataByPlaceAndYearAndMonth(
                    id,
                    Year.of(year),
                    Month.of(month),
                    securityContext.getUserPrincipal().getName())),
            reportManager
        ).build();
    }

}
