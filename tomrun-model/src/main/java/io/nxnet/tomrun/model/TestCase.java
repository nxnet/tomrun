package io.nxnet.tomrun.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Generated;

public class TestCase implements WrapperAwareElement
{
    private TestSuite testSuite;

    private String id;

    private String name;

    private String description;

    private Set<Test> tests = new LinkedHashSet<Test>();

    private Properties properties = Properties.builder().build();

    private DynamicInclude dynamicInclude;

    private Wrapper beforeFirst;

    private Wrapper afterLast;

    private Wrapper beforeEach;

    private Wrapper afterEach;

    private String targetVersions;

    private String targetRequirements;

    private String factory;

    @Generated("SparkTools")
    private TestCase(Builder builder)
    {
        this.testSuite = builder.testSuite;
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.tests = builder.tests;
        this.properties = builder.properties;
        this.dynamicInclude = builder.dynamicInclude;
        this.beforeFirst = builder.beforeFirst;
        this.afterLast = builder.afterLast;
        this.beforeEach = builder.beforeEach;
        this.afterEach = builder.afterEach;
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

    public TestSuite getTestSuite()
    {
        return testSuite;
    }

    public void setTestSuite(TestSuite testSuite)
    {
        this.testSuite = testSuite;
    }

    public Set<Test> getTests()
    {
        return tests;
    }

    public void setTests(Set<Test> tests)
    {
        this.tests = tests;
    }

    public boolean addTest(Test test)
    {
        if (this.tests == null)
        {
            this.tests = new LinkedHashSet<Test>();
        }
        return this.tests.add(test);
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

    public Wrapper getBeforeEach()
    {
        return beforeEach;
    }

    public void setBeforeEach(Wrapper beforeEach)
    {
        this.beforeEach = beforeEach;
    }

    public Wrapper getAfterEach()
    {
        return afterEach;
    }

    public void setAfterEach(Wrapper afterEach)
    {
        this.afterEach = afterEach;
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
        return "TestCase [testSuiteId=" + (testSuite != null ? testSuite.getId() : null) + ", id=" + id + ", name="
                + name + ", description=" + description + ", properties=" + properties + ", tests=" + tests + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((testSuite == null) ? 0 : testSuite.hashCode());
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
        TestCase other = (TestCase) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (testSuite == null)
        {
            if (other.testSuite != null)
                return false;
        }
        else if (!testSuite.equals(other.testSuite))
            return false;
        return true;
    }

    @Override
    public String getType()
    {
        return "C";
    }

    @Override
    public Collection<WrapperAwareElement> getChildren()
    {
        Collection<WrapperAwareElement> children = new ArrayList<WrapperAwareElement>();
        for (Test child : tests)
        {
            children.add(child);
        }
        return children;
    }

    @Override
    public WrapperAwareElement getParent()
    {
        return getTestSuite();
    }

    /**
     * Creates builder to build {@link TestCase}.
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link TestCase}.
     */
    @Generated("SparkTools")
    public static final class Builder
    {
        private TestSuite testSuite;
        private String id;
        private String name;
        private String description;
        private Set<Test> tests;
        private Properties properties;
        private DynamicInclude dynamicInclude;
        private Wrapper beforeFirst;
        private Wrapper afterLast;
        private Wrapper beforeEach;
        private Wrapper afterEach;
        private String targetVersions;
        private String targetRequirements;
        private String factory;

        private Builder()
        {
        }

        public Builder withTestSuite(TestSuite testSuite)
        {
            this.testSuite = testSuite;
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

        public Builder withTests(Set<Test> tests)
        {
            this.tests = tests;
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

        public Builder withBeforeEach(Wrapper beforeEach)
        {
            this.beforeEach = beforeEach;
            return this;
        }

        public Builder withAfterEach(Wrapper afterEach)
        {
            this.afterEach = afterEach;
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

        public TestCase build()
        {
            return new TestCase(this);
        }
    }

}
