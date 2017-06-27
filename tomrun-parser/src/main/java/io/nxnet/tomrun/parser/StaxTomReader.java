package io.nxnet.tomrun.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import io.nxnet.tomrun.model.TestProject;

public class StaxTomReader implements TomReader, XMLStreamReader {

	private XMLStreamReader reader;

	public Object getProperty(String name) throws IllegalArgumentException {
		return reader.getProperty(name);
	}

	public int next() throws XMLStreamException {
		throw new UnsupportedOperationException();
	}

	public void require(int type, String namespaceURI, String localName)
			throws XMLStreamException {
		reader.require(type, namespaceURI, localName);
	}

	public String getElementText() throws XMLStreamException {
		return reader.getElementText();
	}

	public int nextTag() throws XMLStreamException {
		throw new UnsupportedOperationException();
	}

	public boolean hasNext() throws XMLStreamException {
		return reader.hasNext();
	}

	public void close() throws XMLStreamException {
		reader.close();
	}

	public String getNamespaceURI(String prefix) {
		return reader.getNamespaceURI(prefix);
	}

	public boolean isStartElement() {
		return reader.isStartElement();
	}

	public boolean isEndElement() {
		return reader.isEndElement();
	}

	public boolean isCharacters() {
		return reader.isCharacters();
	}

	public boolean isWhiteSpace() {
		return reader.isWhiteSpace();
	}

	public String getAttributeValue(String namespaceURI, String localName) {
		return reader.getAttributeValue(namespaceURI, localName);
	}

	public int getAttributeCount() {
		return reader.getAttributeCount();
	}

	public QName getAttributeName(int index) {
		return reader.getAttributeName(index);
	}

	public String getAttributeNamespace(int index) {
		return reader.getAttributeNamespace(index);
	}

	public String getAttributeLocalName(int index) {
		return reader.getAttributeLocalName(index);
	}

	public String getAttributePrefix(int index) {
		return reader.getAttributePrefix(index);
	}

	public String getAttributeType(int index) {
		return reader.getAttributeType(index);
	}

	public String getAttributeValue(int index) {
		return reader.getAttributeValue(index);
	}

	public boolean isAttributeSpecified(int index) {
		return reader.isAttributeSpecified(index);
	}

	public int getNamespaceCount() {
		return reader.getNamespaceCount();
	}

	public String getNamespacePrefix(int index) {
		return reader.getNamespacePrefix(index);
	}

	public String getNamespaceURI(int index) {
		return reader.getNamespaceURI(index);
	}

	public NamespaceContext getNamespaceContext() {
		return reader.getNamespaceContext();
	}

	public int getEventType() {
		return reader.getEventType();
	}

	public String getText() {
		return reader.getText();
	}

	public char[] getTextCharacters() {
		return reader.getTextCharacters();
	}

	public int getTextCharacters(int sourceStart, char[] target,
			int targetStart, int length) throws XMLStreamException {
		return reader.getTextCharacters(sourceStart, target, targetStart,
				length);
	}

	public int getTextStart() {
		return reader.getTextStart();
	}

	public int getTextLength() {
		return reader.getTextLength();
	}

	public String getEncoding() {
		return reader.getEncoding();
	}

	public boolean hasText() {
		return reader.hasText();
	}

	public Location getLocation() {
		return reader.getLocation();
	}

	public QName getName() {
		return reader.getName();
	}

	public String getLocalName() {
		return reader.getLocalName();
	}

	public boolean hasName() {
		return reader.hasName();
	}

	public String getNamespaceURI() {
		return reader.getNamespaceURI();
	}

	public String getPrefix() {
		return reader.getPrefix();
	}

	public String getVersion() {
		return reader.getVersion();
	}

	public boolean isStandalone() {
		return reader.isStandalone();
	}

	public boolean standaloneSet() {
		return reader.standaloneSet();
	}

	public String getCharacterEncodingScheme() {
		return reader.getCharacterEncodingScheme();
	}

	public String getPITarget() {
		return reader.getPITarget();
	}

	public String getPIData() {
		return reader.getPIData();
	}

	@Override
	public TestProject read(InputStream tomInputStream)
			throws TomConfigurationError {

		try
		{
			this.reader = createReader(tomInputStream);
			while(this.reader.hasNext())
			{
				String parser = this.reader.getAttributeValue(null, "parser");
			}
			return null;
		}
        catch (XMLStreamException e)
        {
            throw new TomConfigurationError(e);
        }
		finally
		{
			try 
			{
				closeReader(reader);
			} 
			catch (IOException e) 
			{
				throw new TomConfigurationError("Error closing reader", e);
			}
		}
	}

    private XMLStreamReader createReader(InputStream in) throws TomConfigurationError
    {
        XMLStreamReader reader = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try
        {
            reader = factory.createXMLStreamReader(new BufferedInputStream(in));
        }
        catch (XMLStreamException e)
        {
            throw new TomConfigurationError(e);
        }
        return reader;
    }

    private void closeReader(XMLStreamReader reader) throws IOException
    {
        if (reader != null) 
        {
            try
            {
                reader.close();
            }
            catch (XMLStreamException e)
            {
                throw new IOException(e);
            }
        }
    }

}
