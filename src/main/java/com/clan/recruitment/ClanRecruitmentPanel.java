package com.clan.recruitment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import net.runelite.client.ui.PluginPanel;

public class ClanRecruitmentPanel extends PluginPanel
{
	private final ClanRecruitmentPlugin plugin;
	private final ClanRecruitmentConfig config;

	private final JCheckBox enabledCheckBox = new JCheckBox("Enable clan recruitment hide");
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

		enabledCheckBox.setSelected(config.enabled());
		enabledCheckBox.addActionListener(e -> plugin.setFeatureEnabled(enabledCheckBox.isSelected()));

		hiddenUsersArea.setEditable(false);
		hiddenUsersArea.setLineWrap(true);
		hiddenUsersArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(hiddenUsersArea);
		scrollPane.setPreferredSize(new Dimension(220, 220));
		scrollPane.setBorder(BorderFactory.createTitledBorder("Hidden usernames"));

		clearButton.addActionListener(e -> plugin.clearHiddenPlayers());

		JPanel top = new JPanel(new GridLayout(0, 1, 0, 6));
		top.add(enabledCheckBox);

		add(top, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(clearButton, BorderLayout.SOUTH);

		refresh(plugin.getHiddenPlayers());
	}

	public void refresh(Collection<String> names)
	{
		SwingUtilities.invokeLater(() ->
		{
			enabledCheckBox.setSelected(config.enabled());

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