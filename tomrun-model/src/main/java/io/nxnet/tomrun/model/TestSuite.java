package io.nxnet.tomrun.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Generated;

public class TestSuite implements WrapperAwareElement
{
    private TestProject testProject;

    private String id;

    private String name;

    private String description;

    private Set<TestCase> testCases = new LinkedHashSet<TestCase>();

    private Set<TestSuite> testSuites = new LinkedHashSet<TestSuite>();

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
    private TestSuite(Builder builder)
    {
        this.testProject = builder.testProject;
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.testCases = builder.testCases;
        this.testSuites = builder.testSuites;
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

    public TestProject getTestProject()
    {
        return testProject;
    }

    public void setTestProject(TestProject testProject)
    {
        this.testProject = testProject;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Set<TestCase> getTestCases()
    {
        return testCases;
    }

    public void setTestCases(Set<TestCase> testCases)
    {
        this.testCases = testCases;
    }

    public boolean addTestCase(TestCase testCase)
    {
        if (this.testCases == null)
        {
            this.testCases = new LinkedHashSet<TestCase>();
        }
        return this.testCases.add(testCase);
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }

    public void addProperty(Property property)
    {
        this.properties.addProperty(property);
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder2 = new StringBuilder();
        builder2.append("{\n   class: ").append(getClass().getName()).append("\n   ");
        if (id != null)
        {
            builder2.append("id: ").append(id).append("\n   ");
        }
        if (name != null)
        {
            builder2.append("name: ").append(name).append("\n   ");
        }
        if (description != null)
        {
            builder2.append("description: ").append(description).append("\n   ");
        }
        if (factory != null)
        {
            builder2.append("factory: ").append(factory).append("\n   ");
        }
        if (targetVersions != null)
        {
            builder2.append("targetVersions: ").append(targetVersions).append("\n   ");
        }
        if (targetRequirements != null)
        {
            builder2.append("targetRequirements: ").append(targetRequirements).append("\n   ");
        }
        if (properties != null)
        {
            builder2.append("properties: ").append(properties).append("\n   ");
        }
        if (testProject != null)
        {
            builder2.append("testProject: ").append(testProject).append("\n   ");
        }
        if (beforeFirst != null)
        {
            builder2.append("beforeFirst: ").append(beforeFirst).append("\n   ");
        }
        if (beforeEach != null)
        {
            builder2.append("beforeEach: ").append(beforeEach).append("\n   ");
        }
        if (testCases != null)
        {
            builder2.append("testCases: ").append(testCases).append("\n   ");
        }
        if (afterLast != null)
        {
            builder2.append("afterLast: ").append(afterLast).append("\n   ");
        }
        if (afterEach != null)
        {
            builder2.append("afterEach: ").append(afterEach).append("\n   ");
        }
        if (dynamicInclude != null)
        {
            builder2.append("dynamicInclude: ").append(dynamicInclude);
        }
        builder2.append("\n}");
        return builder2.toString();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        TestSuite other = (TestSuite) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String getType()
    {
        return "S";
    }

    @Override
    public Collection<WrapperAwareElement> getChildren()
    {
        Collection<WrapperAwareElement> children = new ArrayList<WrapperAwareElement>();
        if (this.testCases != null && !this.testCases.isEmpty())
        {
            for (TestCase child : this.testCases)
            {
                children.add(child);
            }
        }
        if (this.testSuites != null && !this.testSuites.isEmpty())
        {
            for (TestSuite child : this.testSuites)
            {
                children.add(child);
            }
        }
        return children;
    }

    @Override
    public WrapperAwareElement getParent()
    {
        return getTestProject();
    }

    /**
     * Creates builder to build {@link TestSuite}.
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link TestSuite}.
     */
    @Generated("SparkTools")
    public static final class Builder
    {
        private TestProject testProject;
        private String id;
        private String name;
        private String description;
        private Set<TestCase> testCases;
        private Set<TestSuite> testSuites;
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

        public Builder withTestProject(TestProject testProject)
        {
            this.testProject = testProject;
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

        public Builder withTestCases(Set<TestCase> testCases)
        {
            this.testCases = testCases;
            return this;
        }

        public Builder addTestCase(TestCase testCase)
        {
            if (this.testCases == null)
            {
                this.testCases = new LinkedHashSet<>();
            }
            this.testCases.add(testCase);
            return this;
        }

        public Builder withTestSuites(Set<TestSuite> testSuites)
        {
            this.testSuites = testSuites;
            return this;
        }

        public Builder addTestSuite(TestSuite testSuite)
        {
            if (this.testSuites == null)
            {
                this.testSuites = new LinkedHashSet<>();
            }
            this.testSuites.add(testSuite);
            return this;
        }

        public Builder withProperties(Properties properties)
        {
            this.properties = properties;
            return this;
        }

        public Builder addProperty(Property property)
        {
            if (this.properties == null)
            {
                this.properties = Properties.builder().build();
            }
            this.properties.addProperty(property);
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

        public TestSuite build()
        {
            return new TestSuite(this);
        }
    }

}
