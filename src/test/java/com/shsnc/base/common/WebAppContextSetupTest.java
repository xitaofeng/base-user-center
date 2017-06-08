

@Rollback(true)
@ContextConfiguration({"/beans.xml"})
public class WebAppContextSetupTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
}