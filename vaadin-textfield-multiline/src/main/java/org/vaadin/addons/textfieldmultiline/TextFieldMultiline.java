package org.vaadin.addons.textfieldmultiline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.vaadin.addons.textfieldmultiline.client.TextFieldMultilineConstants;
import org.vaadin.addons.textfieldmultiline.client.TextFieldMultilineServerRpc;
import org.vaadin.addons.textfieldmultiline.client.TextFieldMultilineState;

import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.LegacyComponent;

// This is the server-side UI component that provides public API 
// for MyComponent
public class TextFieldMultiline extends com.vaadin.ui.AbstractField<List<String>> implements LegacyComponent {

	private String inputPrompt = null;

	// To process events from the client, we implement ServerRpc
	private TextFieldMultilineServerRpc rpc = new TextFieldMultilineServerRpc() {

		@Override
		public void sendEnteredValues(String[] entered) {
            setValue(new ArrayList<>(Arrays.asList(entered)));
		}
	};

	public TextFieldMultiline() {

		// To receive events from the client, we register ServerRpc
		registerRpc(this.rpc);

		setValue(new ArrayList<String>(), true);
	}

	// We must override getState() to cast the state to MyComponentState
	@Override
	protected TextFieldMultilineState getState() {
		return (TextFieldMultilineState) super.getState();
	}

	/**
	 * Gets the current input prompt.
	 * 
	 * @see #setInputPrompt(String)
	 * @return the current input prompt, or null if not enabled
	 */
	public String getInputPrompt() {
		return this.inputPrompt;
	}

	/**
	 * Sets the input prompt - a textual prompt that is displayed when the
	 * select would otherwise be empty, to prompt the user for input.
	 * 
	 * @param inputPrompt
	 *            the desired input prompt, or null to disable
	 */
	public void setInputPrompt(String inputPrompt) {
		this.inputPrompt = inputPrompt;
		markAsDirty();
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		// TODO Auto-generated method stub

	}

    @Override
    protected boolean setValue(List<String> newFieldValue, final boolean userOriented) {

        // Null value is not supported by the component, so we always put empty list in case of null
        if (newFieldValue == null) {
            newFieldValue = new ArrayList<>();
        }

        return super.setValue(newFieldValue, userOriented);
    }

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		target.addAttribute(TextFieldMultilineConstants.ATTR_ENABLED, isEnabled());
		target.addAttribute(TextFieldMultilineConstants.ATTR_READ_ONLY, isReadOnly());

		if (this.inputPrompt != null) {
			target.addAttribute(TextFieldMultilineConstants.ATTR_INPUTPROMPT, this.inputPrompt);
		}

		target.startTag("options");

        if (getValue() != null) {
		    // Paints the available selection options from the value
            for (final String value : getValue()) {
                // Paints the option
                target.startTag("so");
                target.addAttribute("value", value);
                target.endTag("so");
		    }
        }
		target.endTag("options");
	}

	@Override
	public List<String> getValue() {
		return getState().value;
	}

	@Override
	protected void doSetValue(List<String> value) {
		getState().value = value;
	}
}
