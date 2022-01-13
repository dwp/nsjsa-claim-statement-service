package uk.gov.dwp.jsa.statement.controller;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.thymeleaf.*;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Circumstances;
import uk.gov.dwp.jsa.statement.domain.Statement;
import uk.gov.dwp.jsa.statement.service.StatementService;
import uk.gov.dwp.jsa.statement.util.date.MonthYearI18NDateFormat;
import uk.gov.dwp.jsa.statement.util.date.SimpleDateFormatter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ClaimStatementControllerTest {

    private static final String TEMPLATE_NAME = "statement/statement";
    private static final String HTML_CONTENT = "HTML_CONTENT";
    private static Statement statement;
    private static final UUID CLAIMANT_ID = UUID.randomUUID();
    private static final String PADDED_TEMPLATE_NAME = "statement/padded-statement";
    private static final Circumstances CIRCUMSTANCES = new Circumstances();

    @Mock
    private StatementService statementService;
    @Mock
    private Model model;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private HttpServletRequest mockedHttpServletRequest;

    @Mock
    private HttpServletResponse mockedHttpServletResponse;

    @Mock
    private PdfRendererBuilder pdfRendererBuilder;

    private ClaimStatementController controller;
    private String response;

    @Before
    public void beforeEachTest() {
        initMocks(this);
    }

    @Test
    public void getsStatement() throws ExecutionException, InterruptedException {
        givenAController();
        wheIGetTheStatement();
        thenTheStatementIsReturned();
    }

    @Test
    public void getsStatementPDF() throws ExecutionException, InterruptedException, IOException {
        givenAController();
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);

        when(mockedHttpServletResponse.getOutputStream()).thenReturn(servletOutputStream);
        controller.getStatementPDF(mockedHttpServletRequest,
                mockedHttpServletResponse, CLAIMANT_ID.toString(), model);

        verify(pdfRendererBuilder, times(1)).withHtmlContent(eq(HTML_CONTENT), anyString());
        verify(pdfRendererBuilder, times(1)).run();
    }

    private void givenAController() throws ExecutionException, InterruptedException {
        CIRCUMSTANCES.setLocale(Locale.ENGLISH.getLanguage());
        CIRCUMSTANCES.setClaimStartDate(LocalDate.now());

        when(templateEngine.process(any(String.class), any())).thenReturn(HTML_CONTENT);
        statement =  new Statement(
                null, null, CIRCUMSTANCES, null);
        controller = new ClaimStatementController(statementService, templateEngine);
        when(statementService.getStatement(CLAIMANT_ID)).thenReturn(statement);

        ReflectionTestUtils.setField(controller, "pdfRendererBuilder", pdfRendererBuilder);
        when(model.asMap()).thenReturn(Collections.singletonMap("field", "value"));
    }

    private void wheIGetTheStatement() throws ExecutionException, InterruptedException {
        response = controller.getStatement(mockedHttpServletRequest, mockedHttpServletResponse,CLAIMANT_ID.toString(), model);
    }

    private void thenThePaddedStatementIsReturned() {
        verify(model).addAttribute("statement", statement);
        verify(model).addAttribute(eq("dateFormatter"), any(SimpleDateFormatter.class));
        verify(model).addAttribute(eq("dateMonthYearFormatter"), any(MonthYearI18NDateFormat.class));
        assertThat(response, is(PADDED_TEMPLATE_NAME));
    }

    private void thenTheStatementIsReturned() {
        verify(model).addAttribute("statement", statement);
        verify(model).addAttribute(eq("dateFormatter"), any(SimpleDateFormatter.class));
        verify(model).addAttribute(eq("dateMonthYearFormatter"), any(MonthYearI18NDateFormat.class));
        assertThat(response, is(TEMPLATE_NAME));
    }
}
