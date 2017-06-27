package io.nxnet.tomrun.execution;

public class ExecutionNodeEvent
{
    private ExecutionNodeEventName eventName;

    public ExecutionNodeEventName getEventName()
    {
        return eventName;
    }

    public void setEventName(ExecutionNodeEventName eventName)
    {
        this.eventName = eventName;
    }

    public static class Builder
    {
        private ExecutionNodeEventName eventName;

        public Builder eventName(ExecutionNodeEventName eventName)
        {
            this.eventName = eventName;
            return this;
        }

        public ExecutionNodeEvent build()
        {
            return new ExecutionNodeEvent(this);
        }
    }

    private ExecutionNodeEvent(Builder builder)
    {
        this.eventName = builder.eventName;
    }
}
