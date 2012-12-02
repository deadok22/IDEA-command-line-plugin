package ru.spbau.cmd_line_plugin.user_commands;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Sergey A. Savenko
 */
public class CommandsLoader implements ApplicationComponent {

    private static final Logger LOG = Logger.getInstance(CommandsLoader.class);

    private static final String COMMANDS_DOC_NAME = "commands.xml";
    private static final String COMMAND_ELEMENT_NAME = "Command";
    private static final String COMMAND_CLASS_ELEMENT_NAME = "Class";

    private ArrayList<UserCommand> userCommands;

    public CommandsLoader() {
    }

    public void initComponent() {
        userCommands = new ArrayList<UserCommand>();
        loadCommands();
    }

    public void disposeComponent() {}

    @NotNull
    public String getComponentName() {
        return "CommandsLoader";
    }

    public List<UserCommand> getAllCommands() {
        return userCommands;
    }

    private void loadCommands() {
        Document commandsDoc;
        try {
            commandsDoc = getCommandsDocument();
        } catch (Exception e) {
            LOG.error("Error loading " + COMMANDS_DOC_NAME, e);
            return;
        }
        NodeList commandElements = commandsDoc.getElementsByTagName(COMMAND_ELEMENT_NAME);
        for (int i = 0; i != commandElements.getLength(); ++i) {
            Node cmdNode = commandElements.item(i);
            if (Node.ELEMENT_NODE != cmdNode.getNodeType()) {
                continue;
            }
            NodeList cmdNodeContent = cmdNode.getChildNodes();
            for (int j = 0; j != cmdNodeContent.getLength(); ++j) {
                Node n = cmdNodeContent.item(j);
                if (Node.ELEMENT_NODE == n.getNodeType() && COMMAND_CLASS_ELEMENT_NAME.equals(n.getNodeName())) {
                    Node child = n.getFirstChild();
                    if (null == child || Node.TEXT_NODE != child.getNodeType()) {
                        break;
                    }
                    String className = child.getNodeValue().trim();
                    instantiateAndAddCommand(className);
                    break;
                }
            }
        }
    }

    private void instantiateAndAddCommand(String className) {
        try {
            Class commandClass = Class.forName(className);
            UserCommand command = (UserCommand)commandClass.newInstance();
            userCommands.add(command);
        } catch (Exception e) {
            LOG.error("Error loading command: " + className, e);
        }
    }

    private Document getCommandsDocument() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        URL commandsXmlUrl = getClass().getResource(COMMANDS_DOC_NAME);
        return builder.parse(commandsXmlUrl.toString());
    }

}
