package io.nxnet.tomrun.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Generated;

public class TestProject implements WrapperAwareElement
{
    private Map<String, String> configuration = new LinkedHashMap<String, String>();

    private Properties properties = Properties.builder().build();

    private String id;

    private String name;

    private String description;

    private Set<TestSuite> testSuites = new LinkedHashSet<TestSuite>();

    private Wrapper beforeFirst;

    private Wrapper afterLast;

    private Wrapper beforeEach;

    private Wrapper afterEach;

    private String factory;

    @Generated("SparkTools")
    private TestProject(Builder builder)
    {
        this.configuration = builder.configuration;
        this.properties = builder.properties;
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.testSuites = builder.testSuites;
        this.beforeFirst = builder.beforeFirst;
        this.afterLast = builder.afterLast;
        this.beforeEach = builder.beforeEach;
        this.afterEach = builder.afterEach;
        this.factory = builder.factory;
    }

    public Properties getProperties()
    {
        return properties;
    }

    @Deprecated // Use builder instead
    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }

    public Set<TestSuite> getTestSuites()
    {
        return testSuites;
    }

    @Deprecated // Use builder instead
    public void setTestSuites(Set<TestSuite> testSuites)
    {
        this.testSuites = testSuites;
    }

    @Deprecated // Use builder instead
    public boolean addTestSuite(TestSuite testSuite)
    {
        if (this.testSuites == null)
        {
            this.testSuites = new LinkedHashSet<TestSuite>();
        }
        return this.testSuites.add(testSuite);
    }

    @Deprecated // Use builder instead
    public boolean addProperty(Property property)
    {
        return this.properties.addProperty(property);
    }

    public Map<String, String> getConfiguration()
    {
        return configuration;
    }

    @Deprecated // Use builder instead
    public void setConfiguration(Map<String, String> configuration)
    {
        this.configuration = configuration;
    }

    @Deprecated // Use builder instead
    public void addConfiguration(String key, String value)
    {
        if (this.configuration == null)
        {
            this.configuration = new LinkedHashMap<String, String>();
        }
        this.configuration.put(key, value);
    }

    public Wrapper getBeforeFirst()
    {
        return beforeFirst;
    }

    @Deprecated // Use builder instead
    public void setBeforeFirst(Wrapper beforeFirst)
    {
        this.beforeFirst = beforeFirst;
    }

    public Wrapper getAfterLast()
    {
        return afterLast;
    }

    @Deprecated // Use builder instead
    public void setAfterLast(Wrapper afterLast)
    {
        this.afterLast = afterLast;
    }

    public Wrapper getBeforeEach()
    {
        return beforeEach;
    }

    @Deprecated // Use builder instead
    public void setBeforeEach(Wrapper beforeEach)
    {
        this.beforeEach = beforeEach;
    }

    public Wrapper getAfterEach()
    {
        return afterEach;
    }

    @Deprecated // Use builder instead
    public void setAfterEach(Wrapper afterEach)
    {
        this.afterEach = afterEach;
    }

    public String getFactory()
    {
        return factory;
    }

    @Deprecated // Use builder instead
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
        builder2.append("{\n   ").append("class: ").append(this.getClass().getName()).append("\n   ");
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
        if (configuration != null)
        {
            builder2.append("configuration: ").append(configuration.toString().replaceAll("(.*)", "   $1").trim()).append("\n   ");
        }
        if (properties != null)
        {
            builder2.append("properties: ").append(properties.toString().replaceAll("(.*)", "   $1").trim()).append("\n   ");
        }
        if (beforeFirst != null)
        {
            builder2.append("beforeFirst: ").append(beforeFirst).append("\n   ");
        }
        if (beforeEach != null)
        {
            builder2.append("beforeEach: ").append(beforeEach).append("\n   ");
        }
        if (testSuites != null)
        {
            builder2.append("testSuites: ").append(testSuites.toString().replaceAll("(.*)", "   $1").trim()).append("\n   ");
        }
        if (afterEach != null)
        {
            builder2.append("afterEach: ").append(afterEach).append("\n   ");
        }
        if (afterLast != null)
        {
            builder2.append("afterLast: ").append(afterLast);
        }
        builder2.append("\n}");
        return builder2.toString();
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getType()
    {
        return "P";
    }

    @Override
    public Collection<WrapperAwareElement> getChildren()
    {
        Collection<WrapperAwareElement> children = new ArrayList<WrapperAwareElement>();
        if (this.testSuites != null)
        {
            for (TestSuite child : this.testSuites)
            {
                children.add(child);
            }
        }
        return children;
    }

    public DynamicInclude getDynamicInclude()
    {
        return null;
    }

    @Override
    public WrapperAwareElement getParent()
    {
        return null;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Creates builder to build {@link TestProject}.
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link TestProject}.
     */
    @Generated("SparkTools")
    public static final class Builder
    {
        private Map<String, String> configuration;
        private Properties properties;
        private String id;
        private String name;
        private String description;
        private Set<TestSuite> testSuites;
        private Wrapper beforeFirst;
        private Wrapper afterLast;
        private Wrapper beforeEach;
        private Wrapper afterEach;
        private String factory;

        private Builder()
        {
        }

        public Builder withConfiguration(Map<String, String> configuration)
        {
            this.configuration = configuration;
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

        public Builder withTestSuites(Set<TestSuite> testSuites)
        {
            this.testSuites = testSuites;
            return this;
        }

        public Builder addTestSuite(TestSuite testSuite)
        {
            if (this.testSuites == null)
            {
                this.testSuites = new LinkedHashSet<TestSuite>();
            }
            if (this.testSuites.contains(testSuite))
            {
                throw new IllegalArgumentException(
                        "Suite with same id already added to project, can't add suite: " + testSuite);
            }
            this.testSuites.add(testSuite);
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

        public Builder withFactory(String factory)
        {
            this.factory = factory;
            return this;
        }

        public TestProject build()
        {
            return new TestProject(this);
        }
    }

}
