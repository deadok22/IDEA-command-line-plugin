package ru.spbau.cmd_line_plugin.commands;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.spbau.cmd_line_plugin.api.Command;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Author: Sergey A. Savenko
 */
public class ReflectionCommandsLoader {
    private static final Logger LOG = Logger.getInstance(CommandsLoader.class);

    private static final String COMMANDS_DOC_NAME = "commands.xml";
    private static final String COMMAND_ELEMENT_NAME = "Command";
    private static final String COMMAND_CLASS_ELEMENT_NAME = "Class";

    public ReflectionCommandsLoader() {
    }

    public Set<Command> loadCommands() {
        return loadAndInstantiateCommands();
    }

    private Set<Command> loadAndInstantiateCommands() {
        Document commandsDoc;
        try {
            commandsDoc = getCommandsDocument();
        } catch (Exception e) {
            LOG.error("Error loading " + COMMANDS_DOC_NAME, e);
            return Collections.EMPTY_SET;
        }
        HashSet<Command> commands = new HashSet<Command>();
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
                    Command cmd = instantiateAndAddCommand(child.getNodeValue().trim());
                    if (null != cmd) {
                        commands.add(cmd);
                    }
                    break;
                }
            }
        }
        return commands;
    }

    private Command instantiateAndAddCommand(String className) {
        try {
            Class commandClass = Class.forName(className);
            Command command = (Command)commandClass.newInstance();
            return command;
        } catch (Exception e) {
            LOG.error("Error loading command: " + className, e);
        }
        return null;
    }

    private Document getCommandsDocument() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        URL commandsXmlUrl = getClass().getResource(COMMANDS_DOC_NAME);
        return builder.parse(commandsXmlUrl.toString());
    }

}
