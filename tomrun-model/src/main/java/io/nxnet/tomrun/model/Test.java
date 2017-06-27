package io.nxnet.tomrun.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Generated;

public class Test implements ActionAwareElement, WrapperAwareElement
{
    private TestCase testCase;

    private String id;

    private String name;

    private String description;

    private Set<AbstractAction> actions = new LinkedHashSet<AbstractAction>();

    private Properties properties = Properties.builder().build();

    private DynamicInclude dynamicInclude;

    private Wrapper beforeFirst;

    private Wrapper afterLast;

    private String targetVersions;

    private String targetRequirements;

    private String factory;

    @Generated("SparkTools")
    private Test(Builder builder)
    {
        this.testCase = builder.testCase;
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.actions = builder.actions;
        this.properties = builder.properties;
        this.dynamicInclude = builder.dynamicInclude;
        this.beforeFirst = builder.beforeFirst;
        this.afterLast = builder.afterLast;
        this.targetVersions = builder.targetVersions;
        this.targetRequirements = builder.targetRequirements;
        this.factory = builder.factory;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public TestCase getTestCase()
    {
        return testCase;
    }

    public void setTestCase(TestCase testSuite)
    {
        this.testCase = testSuite;
    }

    public Set<AbstractAction> getActions()
    {
        return actions;
    }

    public void setActions(Set<AbstractAction> tests)
    {
        this.actions = tests;
    }

    public boolean addAction(AbstractAction test)
    {
        if (this.actions == null)
        {
            this.actions = new LinkedHashSet<AbstractAction>();
        }
        return this.actions.add(test);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }

    public boolean addProperty(Property property)
    {
        return this.properties.addProperty(property);
    }

    public DynamicInclude getDynamicInclude()
    {
        return dynamicInclude;
    }

    public void setDynamicInclude(DynamicInclude dynamicInclude)
    {
        this.dynamicInclude = dynamicInclude;
    }

    public Wrapper getBeforeFirst()
    {
        return beforeFirst;
    }

    public void setBeforeFirst(Wrapper beforeFirst)
    {
        this.beforeFirst = beforeFirst;
    }

    public Wrapper getAfterLast()
    {
        return afterLast;
    }

    public void setAfterLast(Wrapper afterLast)
    {
        this.afterLast = afterLast;
    }

    @Override
    public Wrapper getBeforeEach()
    {
        return null;
    }

    @Override
    public void setBeforeEach(Wrapper beforeEach)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Wrapper getAfterEach()
    {
        return null;
    }

    @Override
    public void setAfterEach(Wrapper afterEach)
    {
        throw new UnsupportedOperationException();
    }

    public String getTargetVersions()
    {
        return targetVersions;
    }

    public void setTargetVersions(String targetVersions)
    {
        this.targetVersions = targetVersions;
    }

    public String getTargetRequirements()
    {
        return targetRequirements;
    }

    public void setTargetRequirements(String targetRequirements)
    {
        this.targetRequirements = targetRequirements;
    }

    public String getFactory()
    {
        return factory;
    }

    public void setFactory(String factory)
    {
        this.factory = factory;
    }

    @Override
    public String toString()
    {
        return "Test [testCaseId=" + (testCase != null ? testCase.getId() : null) + ", id=" + id + ", name=" + name
                + ", description=" + description + ", properties=" + properties + ", actions=" + actions + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((testCase == null) ? 0 : testCase.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Test other = (Test) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (testCase == null)
        {
            if (other.testCase != null)
                return false;
        }
        else if (!testCase.equals(other.testCase))
            return false;
        return true;
    }

    @Override
    public String getType()
    {
        return "T";
    }

    @Override
    public Collection<WrapperAwareElement> getChildren()
    {
        Collection<WrapperAwareElement> children = new ArrayList<WrapperAwareElement>();
        for (AbstractAction action : actions)
        {
            children.add(action);
        }
        return children;
    }

    @Override
    public WrapperAwareElement getParent()
    {
        return getTestCase();
    }

    /**
     * Creates builder to build {@link Test}.
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link Test}.
     */
    @Generated("SparkTools")
    public static final class Builder
    {
        private TestCase testCase;
        private String id;
        private String name;
        private String description;
        private Set<AbstractAction> actions;
        private Properties properties;
        private DynamicInclude dynamicInclude;
        private Wrapper beforeFirst;
        private Wrapper afterLast;
        private String targetVersions;
        private String targetRequirements;
        private String factory;

        private Builder()
        {
        }

        public Builder withTestCase(TestCase testCase)
        {
            this.testCase = testCase;
            return this;
        }

        public Builder withId(String id)
        {
            this.id = id;
            return this;
        }

        public Builder withName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description)
        {
            this.description = description;
            return this;
        }

        public Builder withActions(Set<AbstractAction> actions)
        {
            this.actions = actions;
            return this;
        }

        public Builder withProperties(Properties properties)
        {
            this.properties = properties;
            return this;
        }

        public Builder withDynamicInclude(DynamicInclude dynamicInclude)
        {
            this.dynamicInclude = dynamicInclude;
            return this;
        }

        public Builder withBeforeFirst(Wrapper beforeFirst)
        {
            this.beforeFirst = beforeFirst;
            return this;
        }

        public Builder withAfterLast(Wrapper afterLast)
        {
            this.afterLast = afterLast;
            return this;
        }

        public Builder withTargetVersions(String targetVersions)
        {
            this.targetVersions = targetVersions;
            return this;
        }

        public Builder withTargetRequirements(String targetRequirements)
        {
            this.targetRequirements = targetRequirements;
            return this;
        }

        public Builder withFactory(String factory)
        {
            this.factory = factory;
            return this;
        }

        public Test build()
        {
            return new Test(this);
        }
    }

}
