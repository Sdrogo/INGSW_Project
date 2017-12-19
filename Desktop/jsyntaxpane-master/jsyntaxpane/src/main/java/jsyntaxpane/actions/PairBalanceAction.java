/*
 * Copyright 2010 Martin Zipperling martin.zipperling@consol.de
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License 
 *       at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.  
 */
package jsyntaxpane.actions;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.Token;

/**
 * This action skips the entered character when it determines it to 
 * be the closing of a pair when that same character already is at the 
 * caret position, to avoid duplication during typing of e.g. closing bracket 
 * which might already have been added automatically as e.g. result of 
 * PairAction.
 * 
 * If the character is determined to be the starting character of a pair, this
 * action behaves like {@link PairAction}
 */
public class PairBalanceAction extends DefaultSyntaxAction {

    private static PairAction pairAction = new PairAction(); 
    
    public PairBalanceAction() {
        super("PAIR_BALANCE"); 
    }
    
    @Override
    public void actionPerformed(JTextComponent target, SyntaxDocument sdoc,
        int dot, ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        boolean mustSkip = false;
        boolean isStartingCharacter = PAIRS.containsKey(actionCommand);
        boolean isClosingCharacter = PAIRS.containsValue(actionCommand);
        String currentS = "";
        try {
            currentS = target.getText(dot, 1);
        }
        catch (BadLocationException ignored) {
            // Ignored
        }
        Token current = sdoc.getTokenAt(dot); 
        Token pair = sdoc.getPairFor(current);
        
        if (actionCommand.equals(currentS)) {
            if (isStartingCharacter && isClosingCharacter) {
                // If the character can be both starting and closing character:
                // Determine if current token begins with the character
                if (current.getText(sdoc).toString().startsWith(actionCommand)) {
                    mustSkip = true;
                }
            }
            else if (isClosingCharacter && pair != null) {
                mustSkip = true;
            }
        }
        if (mustSkip) {
            target.setCaretPosition(dot+1);
        }
        else {
            if (isStartingCharacter) {
                pairAction.actionPerformed(target, sdoc, dot, e);
            }
            else {
                target.replaceSelection(actionCommand);
            }
        }
    }
    
    
    private static Map<String, String> PAIRS = new HashMap<String, String>(4);


    static {
        PAIRS.put("(", ")");
        PAIRS.put("[", "]");
        PAIRS.put("\"", "\"");
        PAIRS.put("'", "'");
    }
}
