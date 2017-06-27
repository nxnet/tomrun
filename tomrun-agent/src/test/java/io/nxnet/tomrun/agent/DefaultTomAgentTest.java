package io.nxnet.tomrun.agent;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import io.nxnet.tomrun.agent.DefaultTomExecutionRequest;
import io.nxnet.tomrun.agent.TomAgent;
import io.nxnet.tomrun.agent.TomExecutionResult;
import io.nxnet.tomrun.agent.ext.DummyPropertyDecorator;
import io.nxnet.tomrun.alias.AliasRegistry;
import io.nxnet.tomrun.alias.AliasRegistryFactory;
import io.nxnet.tomrun.context.Context;
import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeEvent;
import io.nxnet.tomrun.execution.ExecutionNodeType;
import io.nxnet.tomrun.property.def.DefaultPropertyFactory;
import io.nxnet.tomrun.resolver.def.DefaultValueResolverFactory;
import io.nxnet.tomrun.resolver.def.LiteralResolver;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/tomrun-application-context.xml")
public class DefaultTomAgentTest
{
    @Autowired
    private TomAgent tomAgent;
    
    @Before
    public void setUp() throws Exception
    {
        AliasRegistry aliasRegistry = AliasRegistryFactory.newInstance().getAliasRegistry();
        aliasRegistry.registerAlias(DefaultValueResolverFactory.DEFAULT_ALIAS_NAMESPACE, 
                LiteralResolver.class.getName(), "literal");
        aliasRegistry.registerAlias(DefaultPropertyFactory.DEFAULT_PROPERTY_DECORATOR_ALIAS_NAMESPACE,
                DummyPropertyDecorator.class.getName(), "property");
    }
    
    @Test
    public void execute() throws Exception
    {
        Observer testObserver = new Observer() {
            
            private int assertCounter = 0;
            
            @Override
            public void update(Observable o, Object arg)
            {
                try
                {
                    ExecutionNode executionNode = (ExecutionNode) o;
                    ExecutionNodeEvent event = (ExecutionNodeEvent) arg;
                    switch (event.getEventName())
                    {       
                        case EXEC_START:
                                    
                            Context context = executionNode.getContext();
                            switch(executionNode.getType())
                            {
                                case PROJECT:
                                    assertEquals("Unexpected property value", "http://localhost:8080/gtweb", context.getProperty("applicationUrl"));
                                    assertCounter++;
                                    assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("project_prop_1"));
                                    assertCounter++;
                                    assertEquals("Unexpected property value", "LiteralResolver2_project_prop_2_val", context.getProperty("project_prop_2"));
                                    assertCounter++;
                                    assertEquals("Unexpected property value", "LiteralResolver2_LiteralResolver2_project_prop_2_val_project_prop_3_val", context.getProperty("project_prop_3"));
                                    assertCounter++;
                                    assertEquals("Unexpected property value", "LiteralResolver2_localhost_val", context.getProperty("project_prop_4"));
                                    assertCounter++;
                                    break;
                                case SUITE:
                                    if ("1".equals(executionNode.getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 2, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.getProperty("s1_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.findProperty("project_prop_1"));
                                        assertCounter++;
                                        assertNull("Unexpected property value", context.getProperty("project_prop_1"));
                                        assertCounter++;
                                    }
                                    break;
                                case CASE:
                                    if ("1".equals(executionNode.getId()) 
                                            && ExecutionNodeType.SUITE.equals(executionNode.getParent().getType()) && "1".equals(executionNode.getParent().getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 3, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_prop_2"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_prop_3"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_prop_1_val", context.getProperty("s1_c1_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_c1_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.findProperty("s1_c1_prop_3"));
                                        assertCounter++;
                                    }
                                    break;
                                case TEST:
                                    if ("1".equals(executionNode.getId()) 
                                            && ExecutionNodeType.SUITE.equals(executionNode.getParent().getParent().getType()) && "1".equals(executionNode.getParent().getParent().getId()) 
                                            && ExecutionNodeType.CASE.equals(executionNode.getParent().getType()) && "1".equals(executionNode.getParent().getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 4, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_prop_2"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_prop_3"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_prop_4"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_t1_prop_1_val", context.getProperty("s1_c1_t1_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_c1_t1_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.findProperty("s1_c1_t1_prop_3"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_prop_1_val", context.findProperty("s1_c1_t1_prop_4"));
                                        assertCounter++;
                                    }
                                    break;
                                case WRAPPER:
                                    if ("BFS".equals(executionNode.getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 2, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("bfs_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("bfs_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "bfs_prop_1_val", context.getProperty("bfs_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("bfs_prop_2"));
                                        assertCounter++;
                                    }
                                    else if ("BES".equals(executionNode.getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 2, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("bes_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("bes_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "bes_prop_1_val", context.getProperty("bes_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("bes_prop_2"));
                                        assertCounter++;
                                    }
                                    else if ("AES".equals(executionNode.getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 2, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("aes_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("aes_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "aes_prop_1_val", context.getProperty("aes_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("aes_prop_2"));
                                        assertCounter++;
                                    }
                                    else if ("ALS".equals(executionNode.getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 2, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("als_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("als_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "als_prop_1_val", context.getProperty("als_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("als_prop_2"));
                                        assertCounter++;
                                    }
                                    else if ("BFC".equals(executionNode.getId()) 
                                            && ExecutionNodeType.SUITE.equals(executionNode.getParent().getType()) && "1".equals(executionNode.getParent().getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 3, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_bfc_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_bfc_prop_2"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_bfc_prop_3"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_bfc_prop_1_val", context.getProperty("s1_bfc_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_bfc_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.getProperty("s1_bfc_prop_3"));
                                        assertCounter++;
                                    }
                                    else if ("BEC".equals(executionNode.getId()) 
                                            && ExecutionNodeType.SUITE.equals(executionNode.getParent().getType()) && "1".equals(executionNode.getParent().getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 3, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_bec_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_bec_prop_2"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_bec_prop_3"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_bec_prop_1_val", context.getProperty("s1_bec_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_bec_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.getProperty("s1_bec_prop_3"));
                                        assertCounter++;
                                    }
                                    else if ("AEC".equals(executionNode.getId()) 
                                            && ExecutionNodeType.SUITE.equals(executionNode.getParent().getType()) && "1".equals(executionNode.getParent().getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 3, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_aec_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_aec_prop_2"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_aec_prop_3"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_aec_prop_1_val", context.getProperty("s1_aec_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_aec_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.getProperty("s1_aec_prop_3"));
                                        assertCounter++;
                                    }
                                    else if ("ALC".equals(executionNode.getId()) 
                                            && ExecutionNodeType.SUITE.equals(executionNode.getParent().getType()) && "1".equals(executionNode.getParent().getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 3, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_alc_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_alc_prop_2"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_alc_prop_3"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_alc_prop_1_val", context.getProperty("s1_alc_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_alc_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.getProperty("s1_alc_prop_3"));
                                        assertCounter++;
                                    }
                                    else if ("BFT".equals(executionNode.getId()) 
                                            && ExecutionNodeType.SUITE.equals(executionNode.getParent().getParent().getType()) && "1".equals(executionNode.getParent().getParent().getId()) 
                                            && ExecutionNodeType.CASE.equals(executionNode.getParent().getType()) && "1".equals(executionNode.getParent().getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 4, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_bft_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_bft_prop_2"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_bft_prop_3"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_bft_prop_4"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_bft_prop_1_val", context.getProperty("s1_c1_bft_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_c1_bft_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.getProperty("s1_c1_bft_prop_3"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_prop_1_val", context.getProperty("s1_c1_bft_prop_4"));
                                        assertCounter++;
                                    }
                                    else if ("BET".equals(executionNode.getId()) 
                                            && ExecutionNodeType.SUITE.equals(executionNode.getParent().getParent().getType()) && "1".equals(executionNode.getParent().getParent().getId()) 
                                            && ExecutionNodeType.CASE.equals(executionNode.getParent().getType()) && "1".equals(executionNode.getParent().getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 4, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_bet_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_bet_prop_2"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_bet_prop_3"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_bet_prop_4"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_bet_prop_1_val", context.getProperty("s1_c1_bet_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_c1_bet_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.getProperty("s1_c1_bet_prop_3"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_prop_1_val", context.getProperty("s1_c1_bet_prop_4"));
                                        assertCounter++;
                                    }
                                    else if ("AET".equals(executionNode.getId()) 
                                            && ExecutionNodeType.SUITE.equals(executionNode.getParent().getParent().getType()) && "1".equals(executionNode.getParent().getParent().getId()) 
                                            && ExecutionNodeType.CASE.equals(executionNode.getParent().getType()) && "1".equals(executionNode.getParent().getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 4, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_aet_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_aet_prop_2"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_aet_prop_3"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_aet_prop_4"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_aet_prop_1_val", context.getProperty("s1_c1_aet_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_c1_aet_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.getProperty("s1_c1_aet_prop_3"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_prop_1_val", context.getProperty("s1_c1_aet_prop_4"));
                                        assertCounter++;
                                    }
                                    else if ("ALT".equals(executionNode.getId()) 
                                            && ExecutionNodeType.SUITE.equals(executionNode.getParent().getParent().getType()) && "1".equals(executionNode.getParent().getParent().getId()) 
                                            && ExecutionNodeType.CASE.equals(executionNode.getParent().getType()) && "1".equals(executionNode.getParent().getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 4, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_alt_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_alt_prop_2"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_alt_prop_3"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_alt_prop_4"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_alt_prop_1_val", context.getProperty("s1_c1_alt_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_c1_alt_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.getProperty("s1_c1_alt_prop_3"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_prop_1_val", context.getProperty("s1_c1_alt_prop_4"));
                                        assertCounter++;
                                    }
                                    else if ("BFA".equals(executionNode.getId()) 
                                            && ExecutionNodeType.SUITE.equals(executionNode.getParent().getParent().getParent().getType()) && "1".equals(executionNode.getParent().getParent().getParent().getId()) 
                                            && ExecutionNodeType.CASE.equals(executionNode.getParent().getParent().getType()) && "1".equals(executionNode.getParent().getParent().getId())
                                            && ExecutionNodeType.TEST.equals(executionNode.getParent().getType()) && "1".equals(executionNode.getParent().getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 5, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_bfa_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_bfa_prop_2"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_bfa_prop_3"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_bfa_prop_4"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_bfa_prop_5"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_t1_bfa_prop_1_val", context.getProperty("s1_c1_t1_bfa_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_c1_t1_bfa_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.getProperty("s1_c1_t1_bfa_prop_3"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_prop_1_val", context.getProperty("s1_c1_t1_bfa_prop_4"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_t1_prop_1_val", context.getProperty("s1_c1_t1_bfa_prop_5"));
                                        assertCounter++;
                                    }
                                    else if ("ALA".equals(executionNode.getId()) 
                                            && ExecutionNodeType.SUITE.equals(executionNode.getParent().getParent().getParent().getType()) && "1".equals(executionNode.getParent().getParent().getParent().getId()) 
                                            && ExecutionNodeType.CASE.equals(executionNode.getParent().getParent().getType()) && "1".equals(executionNode.getParent().getParent().getId())
                                            && ExecutionNodeType.TEST.equals(executionNode.getParent().getType()) && "1".equals(executionNode.getParent().getId()))
                                    {
                                        assertEquals("Unexpected number of properties", 5, context.getPropertyNames().size());
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_ala_prop_1"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_ala_prop_2"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_ala_prop_3"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_ala_prop_4"));
                                        assertCounter++;
                                        assertTrue("Property missing", context.getPropertyNames().contains("s1_c1_t1_ala_prop_5"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_t1_ala_prop_1_val", context.getProperty("s1_c1_t1_ala_prop_1"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "project_prop_1_val", context.getProperty("s1_c1_t1_ala_prop_2"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_prop_1_val", context.getProperty("s1_c1_t1_ala_prop_3"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_prop_1_val", context.getProperty("s1_c1_t1_ala_prop_4"));
                                        assertCounter++;
                                        assertEquals("Unexpected property value", "s1_c1_t1_prop_1_val", context.getProperty("s1_c1_t1_ala_prop_5"));
                                        assertCounter++;
                                    }
                                    break;
                                default:
                                    break;
                            }
                            
                            break;
                            
                        case EXEC_END:
                            
                            if (ExecutionNodeType.PROJECT.equals(executionNode.getType()))
                            {
                                assertEquals("Some assert(s) wasn't checked", 176, assertCounter);
                            }
                            
                            break;
                    }     
                    
                }
                finally
                {
                    // do nothing
                }
            }
        };
        
        try
        {
        	// Add test specific observer
	        this.tomAgent.addObserver(testObserver);
	        
	        // Run agent
	        TomExecutionResult executionResult = this.tomAgent.execute(
	                new DefaultTomExecutionRequest.Builder()
	                    .tom("classpath:///tom.xml")
	                    .suite("[1|2]")
	                    .caze(".*")
	                    .test(".+")
	                    .action("1")
	                    .wrapper("\\w+")
	                .build());
	        
	        // Assert execution result
	        assertEquals("Unexpected number of suites ran", 2, executionResult.getSuitesRan());
	        assertEquals("Unexpected number of cases ran", 4, executionResult.getCasesRan());
	        assertEquals("Unexpected number of tests ran", 8, executionResult.getTestsRan());
	        assertEquals("Unexpected number of actions ran", 66, executionResult.getActionsRan());
        }
        finally
        {
        	// Remove test specific observer
        	this.tomAgent.removeObserver(testObserver);
        }
    }
    
    @Test
    public void executeCustomProjectFactory() throws Exception
    {
    	TomExecutionResult executionResult = this.tomAgent.execute(
                new DefaultTomExecutionRequest.Builder()
                    .tom("classpath:///io.nxnet.tomrun/agent/DefaultTomAgentTest/executeCustomProjectFactory/tom.xml")
                .build());
    }
}
