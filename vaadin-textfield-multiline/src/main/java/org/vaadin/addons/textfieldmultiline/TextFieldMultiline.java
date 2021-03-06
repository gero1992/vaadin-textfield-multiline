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
    private boolean resetButtonEnabled;

    // To process events from the client, we implement ServerRpc
    private TextFieldMultilineServerRpc rpc = new TextFieldMultilineServerRpc() {

        @Override
        public void sendEnteredValues(String[] entered) {
            setValue(new ArrayList<>(Arrays.asList(entered)));
        }
    };

    public TextFieldMultiline() {

        // To receive events from the client, we register ServerRpc
        registerRpc(rpc);

        setValue(new ArrayList<String>(), true);
    }

    // We must override getState() to cast the state to MyComponentState
    @Override
    protected TextFieldMultilineState getState() {
        return (TextFieldMultilineState) super.getState();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends List<String>> getType() {
        return (Class<? extends List<String>>) new ArrayList<String>().getClass();
    }

    /**
     * Gets the current input prompt.
     * 
     * @see #setInputPrompt(String)
     * @return the current input prompt, or null if not enabled
     */
    public String getInputPrompt() {
        return inputPrompt;
    }

    /**
     * Sets the input prompt - a textual prompt that is displayed when the select would otherwise be empty, to prompt the user for input.
     * 
     * @param inputPrompt the desired input prompt, or null to disable
     */
    public void setInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
        markAsDirty();
    }

    public boolean isResetButtonEnabled() {
        return this.resetButtonEnabled;
    }

    public void setResetButtonEnabled(boolean resetButtonEnabled) {
        this.resetButtonEnabled = resetButtonEnabled;
        markAsDirty();
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void setValue(List<String> newFieldValue, final boolean repaintIsNotNeeded, final boolean ignoreReadOnly) {

        // Null value is not supported by the component, so we always put empty list in case of null
        if (newFieldValue == null) {
            newFieldValue = new ArrayList<>();
        }

        super.setValue(newFieldValue, repaintIsNotNeeded, ignoreReadOnly);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        target.addAttribute(TextFieldMultilineConstants.ATTR_ENABLED, isEnabled());
        target.addAttribute(TextFieldMultilineConstants.ATTR_READ_ONLY, isReadOnly());

        if (inputPrompt != null) {
            target.addAttribute(TextFieldMultilineConstants.ATTR_INPUTPROMPT, inputPrompt);
        }

        if (resetButtonEnabled) {
            target.addAttribute(TextFieldMultilineConstants.ATTR_RESET_BUTTON_ENABLED, true);
        }

        target.startTag("options");

        if (getValue() != null) {
            // Paints the available selection options from the value
            for (String value : getValue()) {
                // Paints the option
                target.startTag("so");
                target.addAttribute("value", value);
                target.endTag("so");
            }
        }
        target.endTag("options");
    }
}
