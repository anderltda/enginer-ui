package br.com.enginer.domain.ui.usercase.schema.field.behavior.validation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public class Sync {

	private List<String> functions;
	private Map<String, String> messages;

	public Sync(String[] functions, String[] messages) {
		this.functions = new ArrayList<>();
		this.messages = new LinkedHashMap<>();
		for (int i = 0; i < functions.length; i++) {
			if(!functions[i].isEmpty()) {
				this.functions.add(functions[i]);
				this.messages.put(functions[i], messages[i]);
			}
		}
	}

	public List<String> getFunctions() {
		return functions.size() > 0 ? functions : null;
	}

	public void setFunctions(List<String> functions) {
		this.functions = functions;
	}

	public Map<String, String> getMessages() {
		return messages;
	}

	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}
}
