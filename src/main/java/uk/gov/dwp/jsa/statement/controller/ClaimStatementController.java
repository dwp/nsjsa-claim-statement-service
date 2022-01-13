package uk.gov.dwp.jsa.statement.controller;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import uk.gov.dwp.jsa.security.roles.AnyRole;
import uk.gov.dwp.jsa.statement.domain.Statement;
import uk.gov.dwp.jsa.statement.service.StatementService;
import uk.gov.dwp.jsa.statement.util.date.MonthYearI18NDateFormat;
import uk.gov.dwp.jsa.statement.util.date.SimpleDateFormatter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.UUID;
import java.util.Map;
import java.util.Base64;
import java.util.Locale;

@Controller
@RequestMapping("/nsjsa")
public class ClaimStatementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClaimStatementController.class);

    private final StatementService statementService;
    private final TemplateEngine templateEngine;
    private final PdfRendererBuilder pdfRendererBuilder = new PdfRendererBuilder();

    @Autowired
    private ServletContext context;

    @SuppressFBWarnings("SIC")
    @Autowired
    public ClaimStatementController(final StatementService statementService, final TemplateEngine templateEngine) {
        this.statementService = statementService;
        this.templateEngine = templateEngine;
    }

    @AnyRole
    @GetMapping("/{version}/claim-statement/claim/{id}")
    public String getStatement(final HttpServletRequest request, final HttpServletResponse response,
                                     final @PathVariable("id") String claimantId,
                                     final Model model) throws ExecutionException, InterruptedException {

        LOGGER.debug("Getting claim statement for claimant: {}", claimantId);
        populateModel(model, UUID.fromString(claimantId), request, response);
        model.addAttribute("isPdf", false);
        return "statement/statement";
    }

    @AnyRole
    @GetMapping(value = "/{version}/claim-statement/claimpdf/{id}")
    public void getStatementPDF(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final @PathVariable("id") String claimantId,
                                final Model model) throws ExecutionException, InterruptedException, IOException {
        LOGGER.debug("Getting claim statement for claimant: {}", claimantId);
        populateModel(model, UUID.fromString(claimantId), request, response);

        // Create Thymeleaf context for rendering HTML for PDF.
        WebContext ctx = new WebContext(request, response, context);
        for (Map.Entry<String, Object> stringObjectEntry : model.asMap().entrySet()) {
            ctx.setVariable(((Map.Entry) stringObjectEntry).getKey().toString(), stringObjectEntry.getValue());
        }
        ctx.setVariable("isPdf", true);
        String locale = (String) model.getAttribute("locale");
        ctx.setLocale(Locale.forLanguageTag(locale == null ? "en" : locale));

        String html = templateEngine.process("statement/statement-pdf", ctx)
                .replaceAll("/agent-ui", "static")
                .replaceAll("<br>", "<br/>");

        // Now render the html into a PDF
        pdfRendererBuilder.useFastMode();
        pdfRendererBuilder.withHtmlContent(html, getClass()
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toString());
        pdfRendererBuilder.toStream(Base64.getEncoder().wrap(response.getOutputStream()));
        pdfRendererBuilder.run();
    }

    private void populateModel(final Model model, final UUID claimantId, final HttpServletRequest request,
                               final HttpServletResponse response)
            throws ExecutionException, InterruptedException {
        final Statement statement = statementService.getStatement(claimantId);
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver != null) {
            localeResolver.setLocale(request, response,
                    Locale.forLanguageTag(statement.getCircumstances().getLocale()));
        }
        String formatedDate = DateTimeFormatter.ofPattern("d MMMM yyyy", statement.getLocale()).format(
                statement.getCircumstances().getClaimStartDate()
        );

        String welshMonthDate = new MonthYearI18NDateFormat(
                statement.getLocale()).format(statement.getCircumstances().getClaimStartDate()
        );

        String welshFormattedDate = formattedWelshDate(statement.getCircumstances().getClaimStartDate(),
                welshMonthDate);

        model.addAttribute("statement", statement);
        model.addAttribute("dateFormatter", new SimpleDateFormatter());
        model.addAttribute("dateMonthYearFormatter", new MonthYearI18NDateFormat(statement.getLocale()));
        model.addAttribute("formattedLabelDate", formatedDate);
        model.addAttribute("formattedWelshLabelDate", welshFormattedDate);
        model.addAttribute("locale", statement.getCircumstances().getLocale());
    }

    private String formattedWelshDate(final LocalDate claimStartDate, final String welshMonthYear) {
        int dayOfMonth = claimStartDate.getDayOfMonth();
        return dayOfMonth + " " + welshMonthYear;
    }
}
