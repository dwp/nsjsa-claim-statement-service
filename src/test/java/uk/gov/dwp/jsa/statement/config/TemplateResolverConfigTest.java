package uk.gov.dwp.jsa.statement.config;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class TemplateResolverConfigTest {

    public static final String CHARACTER_ENCODING = "UTF-8";
    private static final TemplateMode TEMPLATE_MODE = TemplateMode.HTML;
    public static final String SUFFIX = ".html";
    public static final String PREFIX = "classpath:/templates/";

    @Mock
    private ApplicationContext applicationContext;

    private TemplateResolverConfig config;
    private SpringResourceTemplateResolver resolver;
    private TemplateEngine templateEngine;

    @Before
    public void beforeEachTest() throws Exception {
        initMocks(this);
    }

    @Test
    public void createsTemplateResolver() {
        givenAConfig();
        whenISetApplicationContext();
        whenICreateTheTemplateResolver();
        thenTheTemplateResolverIsCreated();
    }

    @Test
    public void templateEngine() {
        givenAConfig();
        whenICreateTheTemplateEngine();
        thenTheTemplateEngineIsCreated();
    }

    @Test
    public void setsApplicationContext() {
        givenAConfig();
        whenISetApplicationContext();
        thenTheApplicationContextIsSet();
    }

    private void givenAConfig() {
        config = new TemplateResolverConfig();
    }

    private void whenICreateTheTemplateResolver() {
        resolver = config.templateResolver();
    }

    private void whenISetApplicationContext() {
        config.setApplicationContext(applicationContext);
    }

    private void whenICreateTheTemplateEngine() {
        templateEngine = config.templateEngine();
    }

    private void thenTheTemplateEngineIsCreated() {
        assertThat(templateEngine.getTemplateResolvers().size(), is(1));
    }

    private void thenTheApplicationContextIsSet() {
        final ApplicationContext actualApplicationContext = (ApplicationContext) ReflectionTestUtils.getField(config, "applicationContext");
        assertThat(actualApplicationContext, is(applicationContext));
    }

    private void thenTheTemplateResolverIsCreated() {
        assertThat(resolver.getPrefix(), is(PREFIX));
        assertThat(resolver.getSuffix(), is(SUFFIX));
        assertThat(resolver.getTemplateMode(), is(TEMPLATE_MODE));
        assertThat(resolver.getCharacterEncoding(), is(CHARACTER_ENCODING));
        final ApplicationContext actualApplicationContext = (ApplicationContext) ReflectionTestUtils.getField(resolver, "applicationContext");
        assertThat(actualApplicationContext, is(applicationContext));
    }

}
