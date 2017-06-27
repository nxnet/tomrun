package io.nxnet.tomrun.agent;

import java.util.Collection;
import java.util.Observer;

import io.nxnet.tomrun.parser.StaxTestProjectReader;

public interface TomAgent
{
    TomExecutionResult execute(TomExecutionRequest request) throws TomExecutionException;

    void setReader(StaxTestProjectReader reader);

    StaxTestProjectReader getReader();

    void setObservers(Collection<Observer> observers);

    Collection<Observer> getObservers();

    boolean addObserver(Observer observer);

    boolean removeObserver(Observer observer);

}
