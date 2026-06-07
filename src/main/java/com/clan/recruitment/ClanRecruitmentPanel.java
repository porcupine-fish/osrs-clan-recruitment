package com.clan.recruitment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class ClanRecruitmentPanel extends PluginPanel
{
	private final ClanRecruitmentPlugin plugin;
	private final ClanRecruitmentConfig config;

	private final JButton toggleButton = new JButton();
	private final JTextArea hiddenUsersArea = new JTextArea();
	private final JButton clearButton = new JButton("Clear all");

	@Inject
	public ClanRecruitmentPanel(ClanRecruitmentPlugin plugin, ClanRecruitmentConfig config)
	{
		super(false);

		this.plugin = plugin;
		this.config = config;

		setLayout(new BorderLayout(0, 8));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		toggleButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		toggleButton.setForeground(Color.WHITE);
		toggleButton.setFocusPainted(false);
		toggleButton.addActionListener(e -> plugin.setFeatureEnabled(!config.enabled()));
		updateToggleButtonText(config.enabled());

		hiddenUsersArea.setEditable(false);
		hiddenUsersArea.setLineWrap(true);
		hiddenUsersArea.setWrapStyleWord(true);

		hiddenUsersArea.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		hiddenUsersArea.setForeground(Color.WHITE);
		hiddenUsersArea.setCaretColor(Color.WHITE);
		hiddenUsersArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JScrollPane scrollPane = new JScrollPane(hiddenUsersArea);
		scrollPane.setPreferredSize(new Dimension(220, 220));
		scrollPane.setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(ColorScheme.MEDIUM_GRAY_COLOR),
				"Hidden usernames"
			)
		);

		scrollPane.getViewport().setBackground(ColorScheme.DARKER_GRAY_COLOR);
		scrollPane.setBackground(ColorScheme.DARK_GRAY_COLOR);

		clearButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		clearButton.setForeground(Color.WHITE);
		clearButton.setFocusPainted(false);
		clearButton.addActionListener(e -> plugin.clearHiddenPlayers());

		JPanel top = new JPanel(new GridLayout(0, 1, 0, 6));
		top.setBackground(ColorScheme.DARK_GRAY_COLOR);
		top.add(toggleButton);

		add(top, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(clearButton, BorderLayout.SOUTH);

		refresh(plugin.getHiddenPlayers());
	}

	private void updateToggleButtonText(boolean enabled)
	{
		toggleButton.setText(enabled ? "Disable hiding players" : "Enable hiding players");
	}

	public void refresh(Collection<String> names)
	{
		SwingUtilities.invokeLater(() ->
		{
			updateToggleButtonText(config.enabled());

			if (names.isEmpty())
			{
				hiddenUsersArea.setText("");
				return;
			}

			String text = names.stream()
				.sorted(String.CASE_INSENSITIVE_ORDER)
				.collect(Collectors.joining("\n"));

			hiddenUsersArea.setText(text);
			hiddenUsersArea.setCaretPosition(0);
		});
	}
}