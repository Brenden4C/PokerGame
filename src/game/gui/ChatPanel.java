package game.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextField chatInput;
    private JButton sendButton;
    private DefaultListModel<String> playerListModel;
    private JList<String> playerList;
    private ChatListener chatListener;

    public ChatPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 300)); // Adjust size as needed

        // === Player List Panel ===
        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        playerList.setBorder(BorderFactory.createTitledBorder("Players"));
        add(new JScrollPane(playerList), BorderLayout.NORTH);

        // === Chat Display Area ===
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBorder(BorderFactory.createTitledBorder("Chat"));
        add(chatScrollPane, BorderLayout.CENTER);

        // === Chat Input Panel ===
        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInput = new JTextField();
        sendButton = new JButton("Send");

        chatInputPanel.add(chatInput, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST);
        add(chatInputPanel, BorderLayout.SOUTH);

        // === Send Button Action ===
        sendButton.addActionListener(e -> sendMessage());
        chatInput.addActionListener(e -> sendMessage());
    }

    // === Method to Add Chat Messages ===
    public void addChatMessage(String message) {
        chatArea.append(message + "\n");
    }

    // === Method to Send Messages to Server ===
    private void sendMessage() {
        String message = chatInput.getText().trim();
        if (!message.isEmpty()) {
            //addChatMessage("You: " + message);
            if (chatListener != null) {
                chatListener.onChatMessageSent(message); // Send to server
            }
            chatInput.setText("");
        }
    }

    // === Update Player List (With Active Player Indicator) ===
    public void updatePlayerList(List<String> players, String activePlayer) {
        playerListModel.clear();
        for (String player : players) {
            if (player.equals(activePlayer)) {
                playerListModel.addElement(player + " *"); // Active player
            } else {
                playerListModel.addElement(player);
            }
        }
    }

    // === Interface for Handling Server Communication ===
    public void setChatListener(ChatListener listener) {
        this.chatListener = listener;
    }

    public interface ChatListener {
        void onChatMessageSent(String message);
    }
}
