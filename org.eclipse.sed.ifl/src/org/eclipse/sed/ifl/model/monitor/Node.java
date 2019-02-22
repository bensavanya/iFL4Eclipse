package org.eclipse.sed.ifl.model.monitor;

import java.time.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public abstract class Node {
	protected abstract String getLabel();
	
	private LocalDateTime creation = LocalDateTime.now();
	
	public LocalDateTime getCreation() {
		return creation;
	}
	
	private Map<String, Object> properties = new HashMap<>();
	
	public Map<String, Object> getProperties() {
		return Collections.unmodifiableMap(properties);
	}

	public Node(Map<String, Object> properties) {
		super();
		this.properties.putAll(properties);
	}
	
	public Vertex createNode(GraphTraversalSource g) {
		var t = g.addV(getLabel()).property("type", getType()).property("created", getCreation().toString());
		for (var property : getProperties().entrySet()) {
			t.property(property.getKey(), property.getValue().toString());
		}
		return t.next();
	}

	protected final String getType() {
		return this.getClass().getSimpleName();
	}
}
